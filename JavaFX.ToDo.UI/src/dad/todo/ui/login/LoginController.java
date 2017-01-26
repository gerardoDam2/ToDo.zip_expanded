package dad.todo.ui.login;

import java.io.IOException;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;

import dad.todo.ui.ToDoController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Hyperlink;
import javafx.stage.Stage;

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
    private JFXTextField passwordRegister1TextField;

    @FXML
    private JFXTextField passwordRegister2TextField;

    @FXML
    private JFXButton registerButton;

    private ToDoController toDo;

	public LoginController(ToDoController toDo) {
		this.toDo=toDo;
		try {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("LoginView.fxml"));
		loader.setController(this);
		view= loader.load();
		
		
		
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	  @FXML
	    void onLoginButtonAction(ActionEvent event) {
		  ((Stage)view.getScene().getWindow()).close();
		  toDo.show();
	    }

	    @FXML
	    void onRecuperarPassAction(ActionEvent event) {

	    }

	    @FXML
	    void onRegisterButtonAction(ActionEvent event) {

	    }
	
	public JFXTabPane getView() {
		return view;
	}
}
