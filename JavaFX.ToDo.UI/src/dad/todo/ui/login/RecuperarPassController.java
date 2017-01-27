package dad.todo.ui.login;

import java.io.IOException;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;

import dad.todo.services.ServiceException;
import dad.todo.services.ServiceFactory;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class RecuperarPassController {
	
	   @FXML
	    private JFXTextField emailTextField;

	    @FXML
	    private JFXButton cancelarButton;

	    @FXML
	    private JFXButton enviarButton;

		private AnchorPane view;
	    
	    
	    public RecuperarPassController() {

	    try {
	    	FXMLLoader loader = new FXMLLoader(getClass().getResource("recuperarPassView.fxml"));
	    	loader.setController(this);
	    	view=loader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}
	    }

	    @FXML
	    void onCancelarButtonAction(ActionEvent event) {
	    	((Stage)view.getScene().getWindow()).close();
	    }

	    @FXML
	    void onEnviarButtonAction(ActionEvent event) {
	    	try {
				ServiceFactory.getUsuariosService().recuperarPassword(emailTextField.getText());
			} catch (ServiceException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    	onCancelarButtonAction(null);
	    }

	    public AnchorPane getView() {
			return view;
		}

}
