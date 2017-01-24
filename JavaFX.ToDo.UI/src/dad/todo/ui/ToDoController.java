package dad.todo.ui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXDrawer;
import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.transitions.hamburger.HamburgerBackArrowBasicTransition;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;

public class ToDoController implements Initializable{
	@FXML
    private JFXDrawer drawer;

    @FXML
    private JFXHamburger opcionesButton;

	private HamburgerBackArrowBasicTransition transition;
      

    @FXML
    void onOpcionesMousePressed(MouseEvent event) {
    	  
      
    	
       
        transition.setRate(transition.getRate()*-1);
        transition.play();
        
        if(drawer.isShown())
        {
            drawer.close();
        }else
            drawer.open();
    }

    public ToDoController() {
		// TODO Auto-generated constructor stub
		

	}



	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		transition = new HamburgerBackArrowBasicTransition(opcionesButton);
		transition.setRate(-1);
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("MenuView.fxml"));
			BorderPane view =loader.load();
			drawer.setSidePane(view);
			opcionesButton.getChildren().forEach(f->f.getStyle());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
