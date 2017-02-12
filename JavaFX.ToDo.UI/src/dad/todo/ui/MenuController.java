package dad.todo.ui;

import java.net.URL;
import java.util.ResourceBundle;

import org.controlsfx.validation.Severity;
import org.controlsfx.validation.ValidationResult;
import org.controlsfx.validation.ValidationSupport;
import org.controlsfx.validation.Validator;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXRippler;

import dad.todo.services.ServiceException;
import dad.todo.services.ServiceFactory;
import dad.todo.services.items.UsuarioItem;
import dad.todo.ui.gestor_propiedades.GestorDePropiedades;
import dad.todo.ui.model.UsuarioModel;
import dad.todo.ui.utils.ValidatorUtil;
import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Control;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class MenuController implements Initializable {
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

       
       
       
       switch (GestorDePropiedades.getPropiedades().getProperty("style")) {
	case "light":
		lightThemeRadioButton.setSelected(true);

		break;
	case "dark":
		darkThemeRadioButton.setSelected(true);
		break;
	default:
		break;
	}
       
       temaSelected.selectedToggleProperty().addListener((obs,oldV,newV)->onThemeChange(newV));

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
