package dad.todo.ui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXDecorator;
import com.jfoenix.controls.JFXDrawer;
import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.controls.JFXSnackbar;
import com.jfoenix.transitions.hamburger.HamburgerBackArrowBasicTransition;

import dad.todo.services.ServiceException;
import dad.todo.services.ServiceFactory;
import dad.todo.ui.eventos.EventosController;
import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.media.AudioClip;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

public class ToDoController implements Initializable {
	
	
    @FXML
    private AnchorPane rootView;
    
	@FXML
	private JFXDrawer drawer;

	@FXML
	private JFXHamburger opcionesButton;

	private HamburgerBackArrowBasicTransition transition;

	@FXML
	private BorderPane contenidoPane;

	private AnchorPane view;

	private Stage stage;

	private static SimpleStringProperty styleSelected;
	

	private EventosController eventosController;

	private MenuController menuController;

	public static JFXSnackbar notificator;
	
	private StringProperty crearChar;
	private StringProperty editarChar;
	private StringProperty borrarChar;
	private StringProperty ubicacionChar;

	private AudioClip wellcomeSound;

	private JFXDecorator decorator;

	private Scene scene;

	public static AudioClip okSound;

	@FXML
	void onOpcionesMousePressed(MouseEvent event) {

		transition.setRate(transition.getRate() * -1);
		transition.play();

		if (drawer.isShown()) {
			drawer.close();
		} else
			drawer.open();
	}

	public ToDoController() {
		try {
			
			
			
			styleSelected = new SimpleStringProperty(this, "styleSelected",
					App.gestorDePropiedades.getPropiedades().getProperty("style"));
			styleSelected.addListener((obs, oldV, newV) -> onStyleChange(newV));

			FXMLLoader loader = new FXMLLoader(getClass().getResource("ToDoView.fxml"));
			loader.setController(this);
			view = loader.load();

			

			stage = new Stage();
			stage.initStyle(StageStyle.UNDECORATED);
			decorator = new JFXDecorator(stage, view);
			decorator.setCustomMaximize(false);
			scene = new Scene(decorator);
			
			HBox d=(HBox)decorator.getChildren().get(0);
			d.getChildren().remove(0);
			d.getChildren().remove(0);
			d.getChildren().remove(0);
			d.getChildren().get(0).setFocusTraversable(false);
			Button LogoutButton = new Button();
			LogoutButton.setFocusTraversable(false);
			LogoutButton.setOnAction(oa->desloguear());
			LogoutButton.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("./images/logout-24.png"))));
			LogoutButton.getStyleClass().add("windowbutton");
			HBox leftContentUndecorator = new HBox(LogoutButton);
			leftContentUndecorator.setLayoutX(0);
			d.getChildren().add(0,leftContentUndecorator);
			HBox.setHgrow(leftContentUndecorator, Priority.ALWAYS);
			
			stage.setX(Double.valueOf(App.gestorDePropiedades.getPropiedades().getProperty("stageX")));
			stage.setY(Double.valueOf(App.gestorDePropiedades.getPropiedades().getProperty("stageY")));
			stage.setWidth(Double.valueOf(App.gestorDePropiedades.getPropiedades().getProperty("stageWidth")));
			stage.setHeight(Double.valueOf(App.gestorDePropiedades.getPropiedades().getProperty("stageHeight")));
			
			stage.getIcons().add(new Image(getClass().getResourceAsStream("./images/TodoList-64.png")));

			
			decorator.setOnCloseButtonAction( ()-> {
				guardarConfig();
				Platform.exit();
			});
			stage.setOnCloseRequest(close -> guardarConfig());

			App.gestorDePropiedades.ocupatedelCssPorMi(scene.getRoot());
			stage.setScene(scene);
			
			//atajos de teclado
			crearChar= new SimpleStringProperty(this,"crearChar",App.gestorDePropiedades.getPropiedades().getProperty("crear"));
			editarChar= new SimpleStringProperty(this,"editarChar",App.gestorDePropiedades.getPropiedades().getProperty("editar"));
			borrarChar= new SimpleStringProperty(this,"borrarChar",App.gestorDePropiedades.getPropiedades().getProperty("borrar"));
			ubicacionChar= new SimpleStringProperty(this,"ubicacionChar",App.gestorDePropiedades.getPropiedades().getProperty("ubicacion"));
			
			wellcomeSound = new AudioClip(getClass().getResource("./sonidos/intro.wav").toExternalForm());
			okSound = new AudioClip(getClass().getResource("./sonidos/ok.wav").toExternalForm());



		} catch (IOException e) {
			e.printStackTrace();
		}

	}


	private void desloguear() {
		try {
			App.gestorDePropiedades.getPropiedades().remove("user");
			App.gestorDePropiedades.getPropiedades().remove("pass");
			drawer.close();
			stage.close();
			ServiceFactory.getUsuariosService().logout();
			eventosController.clear();
			App.login.show();
		} catch (ServiceException e) {
		}
	}

	private void onStyleChange(String newV) {

		App.gestorDePropiedades.getPropiedades().put("style", newV);

		switch (newV) {
		case "light":
			break;
		case "dark":
			break;
		default:
			break;
		}

		
	}

	private void guardarConfig() {
		System.out.println("guardando el estado del stage");
		App.gestorDePropiedades.getPropiedades().setProperty("stageX", stage.getX() + "");
		App.gestorDePropiedades.getPropiedades().setProperty("stageY", stage.getY() + "");
		App.gestorDePropiedades.getPropiedades().setProperty("stageWidth", stage.getWidth() + "");
		App.gestorDePropiedades.getPropiedades().setProperty("stageHeight", stage.getHeight() + "");
		App.gestorDePropiedades.getPropiedades().setProperty("crear",crearChar.get() );
		App.gestorDePropiedades.getPropiedades().setProperty("editar", editarChar.get());
		App.gestorDePropiedades.getPropiedades().setProperty("borrar", borrarChar.get());
		App.gestorDePropiedades.getPropiedades().setProperty("ubicacion", ubicacionChar.get());
		
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		transition = new HamburgerBackArrowBasicTransition(opcionesButton);
		transition.setRate(-1);
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("MenuView.fxml"));
			BorderPane MenuView = loader.load();
			menuController =(MenuController)loader.getController();
			menuController.setTc(this);
			
			drawer.setSidePane(MenuView);
			eventosController = new EventosController();
			contenidoPane.setCenter(eventosController.getView());
			notificator = new JFXSnackbar();
			notificator.registerSnackbarContainer(rootView);
			AnchorPane.getTopAnchor(notificator);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public BorderPane getContenidoPane() {
		return contenidoPane;
	}

	public AnchorPane getView() {
		return view;
	}

	public void show() {
		eventosController.load();
		menuController.load();
		stage.show();
		wellcomeSound.play();
		
		try {
			notificator.show("Bienvenido "+ServiceFactory.getUsuariosService().getLogueado().getNombre(), 4000);
		} catch (ServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void bind(){
		Bindings.bindBidirectional(menuController.crearEventoKey.textProperty(), crearChar);
		Bindings.bindBidirectional(menuController.editarEventoKey.textProperty(), editarChar);
		Bindings.bindBidirectional(menuController.borrarEventoKey.textProperty(), borrarChar);
		Bindings.bindBidirectional(menuController.abrirUbicacionKey.textProperty(), ubicacionChar);
	}
	public static SimpleStringProperty styleSelectedProperty() {
		return styleSelected;
	}

	public String getStyleSelected() {
		return styleSelectedProperty().get();
	}

	public void setStyleSelected(final String styleSelected) {
		styleSelectedProperty().set(styleSelected);
	}
	

	   @FXML
	    void onAnyKeyPressed(KeyEvent event) {
		   if (event.isControlDown() && event.getCode()!= KeyCode.CONTROL) {
				String key =event.getText();
				
				if (crearChar.get().equalsIgnoreCase(key)) {
					eventosController.onAddEventoButtonAction(null);
				}else if (editarChar.get().equalsIgnoreCase(key)) {
					if (eventosController.getEventosListView().getSelectionModel().selectedItemProperty().get()!=null) {
						eventosController.onEditEventAction(eventosController.getEventosListView().getSelectionModel().selectedItemProperty().get());
					}
				}else if (borrarChar.get().equalsIgnoreCase(key)) {
				if (eventosController.editMode) {
					eventosController.getEventosListView().getSelectionModel().clearSelection();
					if(eventosController.getEditarCrearController().getEventoEditado()!=null)
					eventosController.getEditarCrearController().getEventoEditado().onEliminarButtonAction(null);
					eventosController.changeViewToEventsList(null);
				}else if (eventosController.getEventosListView().getSelectionModel().selectedItemProperty().get()!=null){
					eventosController.getEventosListView().getSelectionModel().selectedItemProperty().get().onEliminarButtonAction(null);
				}

				}else if (ubicacionChar.get().equalsIgnoreCase(key)) {
					if (eventosController.getEventosListView().getSelectionModel().selectedItemProperty().get()!=null) {
						System.out.println("no hay evento para cargar el jodido mapa");
						eventosController.getEventosListView().getSelectionModel().selectedItemProperty().get().onMapButtonAction(null);
					}
				}
				
			}
	    }
	   
	  public Scene getScene() {
		return scene;
	}
}
