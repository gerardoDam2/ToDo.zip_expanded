package dad.todo.ui;

import java.net.URL;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;

public class MenuController implements Initializable{
	 @FXML
	    private TextField nombreUserTextField;

	    @FXML
	    private TextField emailTextField;

	    @FXML
	    private JFXButton guardarPerfilButton;

	    @FXML
	    private TextField passActualTextField;

	    @FXML
	    private TextField nuevaPassTextField;

	    @FXML
	    private JFXButton cambiarPassButton;

	    @FXML
	    private ToggleGroup temaSelected;
	    
	    

    public MenuController() {
		
    	

	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
	}

}
