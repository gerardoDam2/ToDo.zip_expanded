package dad.todo.ui;

import java.net.URL;
import java.util.ResourceBundle;

import org.controlsfx.validation.Severity;
import org.controlsfx.validation.ValidationSupport;
import org.controlsfx.validation.Validator;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXColorPicker;
import com.jfoenix.controls.JFXSnackbar.SnackbarEvent;
import com.jfoenix.controls.JFXTextField;
import com.sun.org.apache.xerces.internal.util.ParserConfigurationSettings;
import com.sun.xml.internal.bind.v2.TODO;

import dad.todo.services.ServiceException;
import dad.todo.services.ServiceFactory;
import dad.todo.services.items.UsuarioItem;
import dad.todo.ui.gestor_propiedades.GestorDePropiedades;
import dad.todo.ui.gestor_propiedades.TodoStyleModel;
import dad.todo.ui.model.UsuarioModel;
import dad.todo.ui.utils.ValidatorUtil;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.AudioClip;
import javafx.stage.Screen;
import javafx.stage.Window;

public class MenuController implements Initializable {
	
    @FXML
    private TitledPane seguridadPane;
	
	@FXML
     TextField crearEventoKey;

    @FXML
    TextField editarEventoKey;

    @FXML
    TextField borrarEventoKey;

    @FXML
    TextField abrirUbicacionKey;
	
    @FXML
    private JFXColorPicker base1Picker;

    @FXML
    private JFXColorPicker base2Picker;

    @FXML
    private JFXColorPicker text1Picker;

    @FXML
    private ComboBox<TodoStyleModel> stylesComboBox;

    @FXML
    private JFXColorPicker texto2Picker;

    @FXML
    private JFXColorPicker fondoPicker;
    
    @FXML
    private JFXTextField newStyleTextField;

    @FXML
    private Button addNewStyleButton;
	@FXML
	private TextField nombreUserTextField;

	@FXML
	private TextField emailTextField;

	@FXML
	private JFXButton guardarPerfilButton;

    @FXML
    private PasswordField passActualPassField;

    @FXML
    private PasswordField nuevaPassPassField;

	@FXML
	private JFXButton cambiarPassButton;

	
	@FXML
    private JFXButton nombreText;


	private UsuarioModel usuario;
	 @FXML
	    private VBox boxtest;
	
    @FXML
    private TitledPane perfilTitlePane;
    
    ToDoController tc;

	public MenuController() {
		usuario = new UsuarioModel();
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {


		Bindings.bindBidirectional(nombreUserTextField.textProperty(), usuario.nombreProperty());
		Bindings.bindBidirectional(emailTextField.textProperty(), usuario.emailProperty());
		
	    ValidationSupport validationPerfil = new ValidationSupport();
	    ValidationSupport validationPassword = new ValidationSupport();

        validationPerfil.registerValidator(emailTextField, false, Validator.createRegexValidator("formato incorrecto", ValidatorUtil.EMAIL_PATTERN, Severity.ERROR));
        validationPerfil.registerValidator(nombreUserTextField, false,Validator.createEmptyValidator("El campo no puede estar vacío"));
        guardarPerfilButton.disableProperty().bind(validationPerfil.invalidProperty());
        
        validationPassword.registerValidator(nuevaPassPassField, false,Validator.createEmptyValidator("Debe introducir una contraseña"));
        validationPassword.registerValidator(passActualPassField, false,Validator.createEmptyValidator("Debe introducir una contraseña"));
        cambiarPassButton.disableProperty().bind(validationPassword.invalidProperty());

        base1Picker.valueProperty().bindBidirectional(App.gestorDePropiedades.base1Property());
        base2Picker.valueProperty().bindBidirectional(App.gestorDePropiedades.base2Property());
        text1Picker.valueProperty().bindBidirectional(App.gestorDePropiedades.texto1Property());
        texto2Picker.valueProperty().bindBidirectional(App.gestorDePropiedades.texto2Property());
        fondoPicker.valueProperty().bindBidirectional(App.gestorDePropiedades.fondoProperty());

        
//        stylesComboBox.itemsProperty().bindBidirectional(App.gestorDePropiedades.hojasEstiloProperty());
        stylesComboBox.itemsProperty().bind(App.gestorDePropiedades.hojasEstiloProperty());
        stylesComboBox.getSelectionModel().select(App.gestorDePropiedades.currentStyleProperty().get());
        stylesComboBox.getSelectionModel().selectedItemProperty().addListener((obs,oldv,newV)->onSelectedStyleChange(newV));
        
        
        //
        crearEventoKey.textProperty().addListener((a,b,c)->onCrearChartChange(c));
        editarEventoKey.textProperty().addListener((a,b,c)->onEditarChartChange(c));
        borrarEventoKey.textProperty().addListener((a,b,c)->onBorrarChartChange(c));
        abrirUbicacionKey.textProperty().addListener((a,b,c)->onUbicacionChartChange(c));
        

	}
	
	



	private void onBorrarChartChange(String c) {
		// TODO Auto-generated method stub
		String x=c.substring(c.length()-1);
		if ((crearEventoKey.getText()+editarEventoKey.getText()+abrirUbicacionKey.getText()).contains(x)) {
			borrarEventoKey.setText(" ");
		}
		 if (c.length() > 1) {
             borrarEventoKey.setText(x);
         }
	}

	private void onEditarChartChange(String c) {
		// TODO Auto-generated method stub
		String x=c.substring(c.length()-1);
		if ((crearEventoKey.getText()+abrirUbicacionKey.getText()+borrarEventoKey.getText()).contains(x)) {
			editarEventoKey.setText(" ");
		}
		 if (c.length() > 1) {
            editarEventoKey.setText(x);
         }
	}

	private void onCrearChartChange(String c) {
		// TODO Auto-generated method stub
		String x=c.substring(c.length()-1);
		if ((abrirUbicacionKey.getText()+editarEventoKey.getText()+borrarEventoKey.getText()).contains(x)) {
			crearEventoKey.setText(" ");
		}
		 if (c.length() > 1) {
            crearEventoKey.setText(x);
         }
	}

	private void onUbicacionChartChange(String c) {
		String x=c.substring(c.length()-1);
		if ((crearEventoKey.getText()+editarEventoKey.getText()+borrarEventoKey.getText()).contains(x)) {
			abrirUbicacionKey.setText(" ");
		}
		 if (c.length() > 1) {
             abrirUbicacionKey.setText(x);
         }
	}

	private void onSelectedStyleChange(TodoStyleModel newV) {
		App.gestorDePropiedades.cambiarEstilo(newV);
		base1Picker.setValue(newV.getBase1());
		base2Picker.setValue(newV.getBase2());
		text1Picker.setValue(newV.getTexto1());
		texto2Picker.setValue(newV.getTexto2());
		fondoPicker.setValue(newV.getFondo());
		
	
	}

	

	private void updatePerfilFields() {
		nombreText.setText(usuario.getNombre());
		nombreUserTextField.setText(usuario.getNombre());
		emailTextField.setText(usuario.getEmail());
	}

	@FXML
	void onCambiarPassAction(ActionEvent event) {
		try {
			ServiceFactory.getUsuariosService().cambiarPassword(passActualPassField.getText(), nuevaPassPassField.getText());
			ToDoController.notificator.enqueue(new SnackbarEvent("contraseña cambiada"));
			ToDoController.okSound.play();
			nuevaPassPassField.clear();
			passActualPassField.clear();
			
		} catch (ServiceException e) {
			ToDoController.notificator.enqueue(new SnackbarEvent("la contraseña es incorrecta"));
			ToDoController.okSound.play();
		}
	}

	@FXML
	void onGuardarPerfilAction(ActionEvent event) {
	 try {
		UsuarioItem usuarioL = ServiceFactory.getUsuariosService().getLogueado();
		usuarioL.setEmail(usuario.getEmail());
		usuarioL.setNombre(usuario.getNombre());
		ServiceFactory.getUsuariosService().actualizar(usuarioL);
		updatePerfilFields();
	} catch (ServiceException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
	}

	public void load() {
		usuario.updateInfo();
		updatePerfilFields();
	}
	
    @FXML
    void onAddNewStyleAction(ActionEvent event) {
    	App.gestorDePropiedades.crearNuevoEstilo(newStyleTextField.getText());
    
//    	stylesComboBox.getSelectionModel().selectLast();
    	
    }
    
    @FXML
    void onEliminarUsuarioAction(ActionEvent event) {
    		try {
    			System.out.println("dando de baja");
				ServiceFactory.getUsuariosService().baja();
				darDeBaja();
				
			} catch (ServiceException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    }
    
    
    
    public  void darDeBaja(){
		   Label label = new Label("I Want You Back");
		   label.setStyle("-fx-font-size:50; -fx-text-fill:white");
		   Button cerrar = new Button("X");
		   cerrar.setOnAction(e->Platform.exit());
		   cerrar.setPadding(new Insets(40));
		   cerrar.setStyle("-fx-background-color:transparent;-fx-text-fill:white;-fx-font-size:40");
		   
		   VBox v = new VBox(label,cerrar);
		   v.setAlignment(Pos.CENTER);
		   v.setStyle("-fx-background-color:black");
		   tc.getScene().setRoot(v);
		   Window win = tc.getScene().getWindow();
		   
		   Screen screen = Screen.getPrimary();
		   Rectangle2D bounds = screen.getVisualBounds();

		   win.setX(bounds.getMinX());
		   win.setY(bounds.getMinY());
		   win.setWidth(bounds.getWidth());
		   win.setHeight(bounds.getHeight());
		   
		   new AudioClip(getClass().getResource("./sonidos/bb.wav").toExternalForm()).play();

	   }
    
    public void setTc(ToDoController tc) {
		this.tc = tc;
	}
}
