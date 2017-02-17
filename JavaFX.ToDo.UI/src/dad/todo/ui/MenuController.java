package dad.todo.ui;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ResourceBundle;

import org.controlsfx.validation.Severity;
import org.controlsfx.validation.ValidationResult;
import org.controlsfx.validation.ValidationSupport;
import org.controlsfx.validation.Validator;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXColorPicker;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXRippler;
import com.jfoenix.controls.JFXTextField;

import dad.todo.services.ServiceException;
import dad.todo.services.ServiceFactory;
import dad.todo.services.items.UsuarioItem;
import dad.todo.ui.gestor_propiedades.GestorDePropiedades;
import dad.todo.ui.gestor_propiedades.TodoStyleModel;
import dad.todo.ui.model.UsuarioModel;
import dad.todo.ui.utils.ValidatorUtil;
import javafx.beans.Observable;
import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

public class MenuController implements Initializable {
	
    @FXML
    private JFXColorPicker base1Picker;

    @FXML
    private JFXColorPicker base2Picker;

    @FXML
    private JFXColorPicker text1Picker;

    @FXML
    private JFXComboBox<TodoStyleModel> stylesComboBox;

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
	private ToggleGroup temaSelected;
	
	@FXML
    private JFXButton nombreText;

	@FXML
	private RadioButton darkThemeRadioButton;

	@FXML
	private RadioButton lightThemeRadioButton;

	private UsuarioModel usuario;
	 @FXML
	    private VBox boxtest;
	
    @FXML
    private TitledPane perfilTitlePane;

	public MenuController() {
		usuario = new UsuarioModel();
		

	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		updatePerfilFields();

		Bindings.bindBidirectional(nombreUserTextField.textProperty(), usuario.nombreProperty());
		Bindings.bindBidirectional(emailTextField.textProperty(), usuario.emailProperty());
		
	    ValidationSupport validationPerfil = new ValidationSupport();
	    ValidationSupport validationPassword = new ValidationSupport();

        validationPerfil.registerValidator(emailTextField, false, Validator.createRegexValidator("formato incorrecto", ValidatorUtil.EMAIL_PATTERN, Severity.ERROR));
        validationPerfil.registerValidator(nombreUserTextField, false,Validator.createEmptyValidator("El campo no puede estar vacío"));
        guardarPerfilButton.disableProperty().bind(validationPerfil.invalidProperty());
        
        validationPassword.registerValidator(nuevaPassPassField, false,
        		Validator.createPredicateValidator(p->nuevaPassPassField.getText().equals(passActualPassField.getText()), "Las contraseñas deben ser iguales"));
        validationPassword.registerValidator(passActualPassField, false,Validator.createEmptyValidator("Debe introducir una contraseña"));
        cambiarPassButton.disableProperty().bind(validationPassword.invalidProperty());

        base1Picker.valueProperty().bindBidirectional(App.gestorDePropiedades.base1Property());
        base2Picker.valueProperty().bindBidirectional(App.gestorDePropiedades.base2Property());
        text1Picker.valueProperty().bindBidirectional(App.gestorDePropiedades.texto1Property());
        texto2Picker.valueProperty().bindBidirectional(App.gestorDePropiedades.texto2Property());
        fondoPicker.valueProperty().bindBidirectional(App.gestorDePropiedades.fondoProperty());
        
        stylesComboBox.itemsProperty().bindBidirectional(App.gestorDePropiedades.hojasEstiloProperty());
        stylesComboBox.getSelectionModel().select(App.gestorDePropiedades.currentStyleProperty().get());
        stylesComboBox.getSelectionModel().selectedItemProperty().addListener((obs,oldv,newV)->onSelectedStyleChange(newV));
        
//        base1Picker.valueProperty().addListener(b->onBase1change(b));
//        base2Picker.valueProperty().addListener(b->onBase1change(b));
//        text1Picker.valueProperty().addListener(b->onBase1change(b));
//        texto2Picker.valueProperty().addListener(b->onBase1change(b));
//        fondoPicker.valueProperty().addListener(b->onBase1change(b));
        
       
       //TODO DASDASDASDSADASDSADADAS
       switch ("light") {
	case "light":
		lightThemeRadioButton.setSelected(true);

		break;
	case "dark":
		darkThemeRadioButton.setSelected(true);
		break;
	default:
		break;
	}
       

	}
	
	

	private void onBase1change(Observable b) {
			App.gestorDePropiedades.getCurrentStyle().setBase1(base1Picker.getValue());
			App.gestorDePropiedades.getCurrentStyle().setBase2(base2Picker.getValue());
			App.gestorDePropiedades.getCurrentStyle().setTexto1(text1Picker.getValue());
			App.gestorDePropiedades.getCurrentStyle().setTexto2(texto2Picker.getValue());
			App.gestorDePropiedades.getCurrentStyle().setFondo(fondoPicker.getValue());
			App.gestorDePropiedades.onCurrentStyleChange();
	}

	private void onSelectedStyleChange(TodoStyleModel newV) {
		App.gestorDePropiedades.currentStyleProperty().set(newV);
	}

	private void onThemeChange(Toggle newV) {
		if (newV==lightThemeRadioButton) {
			ToDoController.styleSelectedProperty().set("light");
		}else {
			ToDoController.styleSelectedProperty().set("dark");
		}
	}

	private void updatePerfilFields() {
		nombreText.setText(usuario.getNombre());
	}

	@FXML
	void onCambiarPassAction(ActionEvent event) {

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

}
