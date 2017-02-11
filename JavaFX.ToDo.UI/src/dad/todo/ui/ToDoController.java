package dad.todo.ui;

import java.io.IOException;
import java.net.URL;
import java.nio.BufferUnderflowException;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import com.jfoenix.controls.JFXDrawer;
import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.transitions.hamburger.HamburgerBackArrowBasicTransition;

import dad.todo.services.ServiceException;
import dad.todo.services.ServiceFactory;
import dad.todo.services.items.EventoItem;
import dad.todo.ui.eventos.EventosController;
import dad.todo.ui.gestor_propiedades.GestorDePropiedades;
import dad.todo.ui.model.UsuarioModel;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import jdk.internal.util.xml.PropertiesDefaultHandler;

public class ToDoController implements Initializable {
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
					GestorDePropiedades.getPropiedades().getProperty("style"));
			styleSelected.addListener((obs, oldV, newV) -> onStyleChange(newV));

			FXMLLoader loader = new FXMLLoader(getClass().getResource("ToDoView.fxml"));
			loader.setController(this);
			view = loader.load();

			Scene scene = new Scene(view);
			scene.getStylesheets().add(getClass().getResource("todoStyle.css").toExternalForm());

			stage = new Stage();

			stage.setX(Double.valueOf(GestorDePropiedades.getPropiedades().getProperty("stageX")));
			stage.setY(Double.valueOf(GestorDePropiedades.getPropiedades().getProperty("stageY")));
			stage.setWidth(Double.valueOf(GestorDePropiedades.getPropiedades().getProperty("stageWidth")));
			stage.setHeight(Double.valueOf(GestorDePropiedades.getPropiedades().getProperty("stageHeight")));
			
		

			stage.setOnCloseRequest(close -> guardarConfig());

			stage.setScene(scene);

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private void onStyleChange(String newV) {

		GestorDePropiedades.getPropiedades().put("style", newV);

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
		GestorDePropiedades.getPropiedades().setProperty("stageX", stage.getX() + "");
		GestorDePropiedades.getPropiedades().setProperty("stageY", stage.getY() + "");
		GestorDePropiedades.getPropiedades().setProperty("stageWidth", stage.getWidth() + "");
		GestorDePropiedades.getPropiedades().setProperty("stageHeight", stage.getHeight() + "");
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		transition = new HamburgerBackArrowBasicTransition(opcionesButton);
		transition.setRate(-1);
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("MenuView.fxml"));
			BorderPane MenuView = loader.load();
			drawer.setSidePane(MenuView);
			eventosController = new EventosController();
			contenidoPane.setCenter(eventosController.getView());

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
		stage.show();
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
	


}
