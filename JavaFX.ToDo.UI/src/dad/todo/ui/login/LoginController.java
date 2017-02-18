package dad.todo.ui.login;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import org.controlsfx.validation.Severity;
import org.controlsfx.validation.ValidationSupport;
import org.controlsfx.validation.Validator;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;

import dad.todo.services.ServiceException;
import dad.todo.services.ServiceFactory;
import dad.todo.services.items.UsuarioItem;
import dad.todo.ui.App;
import dad.todo.ui.ToDoController;
import dad.todo.ui.utils.ValidatorUtil;
import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import com.jfoenix.controls.JFXTabPane;

import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;

public class LoginController implements Initializable {

	private JFXTabPane view;
	
    @FXML
    private Tab iniciarSesionTab;

    @FXML
    private Tab registrarTab;
	
    @FXML
    private Label loginErrorLabel;

	@FXML
	private JFXTextField usernameTextField;

	@FXML
	private JFXPasswordField passwordTextField;

	@FXML
	private JFXButton loginButton;

	@FXML
	private Hyperlink recuperarPassLink;

	@FXML
	private JFXTextField usernameRegisterTextField;

	@FXML
	private JFXTextField nombreRegisterTextField;

	@FXML
	private JFXTextField emailRegisterTextField;

	@FXML
	private JFXPasswordField passwordRegister1TextField;

	@FXML
	private JFXPasswordField passwordRegister2TextField;

	@FXML
	private JFXButton registerButton;

	private FadeTransition tr;

	private ToDoController toDoController;



	public LoginController()  {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("LoginView.fxml"));
			loader.setController(this);
			view = loader.load();
	        Platform.runLater(()->chekAutoLogin());
	        toDoController= new ToDoController();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@FXML
	void onLoginButtonAction(ActionEvent event) {
		boolean validate = false;
		try {
			validate = ServiceFactory.getUsuariosService().login(usernameTextField.getText(),
			passwordTextField.getText());
		} catch (ServiceException e) {
			e.printStackTrace();
		}

		if (validate) {
			usernameTextField.clear();
			passwordTextField.clear();
			((Stage) view.getScene().getWindow()).hide();
			loadTodoController();
		} else {
			System.err.println("LoginController: credenciales incorrectas ");
				tr.playFromStart();
		    
		    
			
		}

	}

	@FXML
	void onRecuperarPassAction(ActionEvent event) {
		Scene scene = new Scene (new RecuperarPassController().getView());
		Stage stage = new Stage();
		stage.setScene(scene);
		stage.initStyle(StageStyle.TRANSPARENT);
		stage.initModality(Modality.APPLICATION_MODAL);
		stage.initOwner(view.getScene().getWindow());
		stage.show();
	}

	@FXML
	void onRegisterButtonAction(ActionEvent event) {
		
		if (checkRegister()) {
			UsuarioItem usuario = new UsuarioItem();
			usuario.setUsername(usernameRegisterTextField.getText());
			usuario.setNombre(nombreRegisterTextField.getText());
			usuario.setPassword(passwordRegister1TextField.getText());
			usuario.setEmail(emailRegisterTextField.getText());
			try {
				ServiceFactory.getUsuariosService().alta(usuario);
				Notificator.success("Su cuenta ha sido creada correctamente");
				usernameTextField.setText(usernameRegisterTextField.getText());
				clearRegisterFields();
				view.getSelectionModel().select(0);
				passwordTextField.requestFocus();
			} catch (ServiceException e) {
				e.printStackTrace();
			}
		
			
		}else {
			//TODO
			System.err.println("LoginController: las contraseñas no coinciden ");
		}
		
	}

	private void clearRegisterFields() {
		usernameRegisterTextField.clear();
		nombreRegisterTextField.clear();
		passwordRegister1TextField.clear();
		passwordRegister2TextField.clear();
		emailRegisterTextField.clear();
	}

	//TODO
	private boolean checkRegister() {
		return passwordRegister1TextField.getText().equals(passwordRegister2TextField.getText());
	}

	public JFXTabPane getView() {
		return view;
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		loginErrorLabel.setOpacity(0);
		tr= new FadeTransition();
	    tr.setDuration(Duration.seconds(1.5));
	    tr.setFromValue(0);
	    tr.setToValue(1);
	    tr.setAutoReverse(true);
	    tr.setCycleCount(2);
	    tr.setNode(loginErrorLabel);
	    
	    ValidationSupport registrarUsuarioValidator = new ValidationSupport();

	    
        registrarUsuarioValidator.registerValidator(nombreRegisterTextField, false,Validator.createEmptyValidator("El campo no puede estar vacío"));
        registrarUsuarioValidator.registerValidator(usernameRegisterTextField, false,Validator.createEmptyValidator("El campo no puede estar vacío"));
        
        registrarUsuarioValidator.registerValidator(emailRegisterTextField, false, Validator.createRegexValidator("formato incorrecto", ValidatorUtil.EMAIL_PATTERN, Severity.ERROR));

        registrarUsuarioValidator.registerValidator(passwordRegister2TextField, false,
        		Validator.createPredicateValidator(p->passwordRegister2TextField.getText().equals(passwordRegister1TextField.getText()), "Las contraseñas deben ser iguales"));
        registrarUsuarioValidator.registerValidator(passwordRegister1TextField, false,Validator.createEmptyValidator("Debe introducir una contraseña"));
        
        
        registerButton.disableProperty().bind(registrarUsuarioValidator.invalidProperty());
        
        
        
	}

	private void chekAutoLogin() {
	String userLogin=	App.gestorDePropiedades.getPropiedades().getProperty("user","no user");
	if (userLogin.equals("no user")) {
		show();
	}else{
		String pass=App.gestorDePropiedades.getPropiedades().getProperty("pass");
		
		try {
			boolean respuesta = ServiceFactory.getUsuariosService().login(userLogin, pass);
			if (respuesta) {
				((Stage) view.getScene().getWindow()).hide();
				loadTodoController();
				
			}
		} catch (ServiceException e) {
			e.printStackTrace();
		}
	}
		
	}
	
	public void show() {
		((Stage) view.getScene().getWindow()).show();
	}
	
	public void loadTodoController(){
		toDoController.show();
	}
}
