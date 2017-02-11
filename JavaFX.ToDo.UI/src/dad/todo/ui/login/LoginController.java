package dad.todo.ui.login;

import java.io.IOException;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;

import dad.todo.services.ServiceException;
import dad.todo.services.ServiceFactory;
import dad.todo.services.items.UsuarioItem;
import dad.todo.ui.ToDoController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Hyperlink;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import com.jfoenix.controls.JFXTabPane;

import javafx.fxml.FXMLLoader;

public class LoginController {

	private JFXTabPane view;

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



	public LoginController() {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("LoginView.fxml"));
			loader.setController(this);
			view = loader.load();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@FXML
	void onLoginButtonAction(ActionEvent event) {
		//TODO CAMBIAR A FALSE AL TERMINAR PRUEBAS
		boolean validate = false;
		try {
			validate = ServiceFactory.getUsuariosService().login(usernameTextField.getText(),
					passwordTextField.getText());
		} catch (ServiceException e) {
			e.printStackTrace();
		}

		if (validate) {
			((Stage) view.getScene().getWindow()).close();
			new ToDoController().show();
		} else {
			//TODO
			System.err.println("LoginController: credenciales incorrectas ");
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
}
