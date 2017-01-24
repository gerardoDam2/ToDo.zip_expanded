/**
 * 
 */
package dad.todo.ui;

import java.net.URL;
import java.util.ResourceBundle;

import dad.calendario.CalendarioController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

/**
 * @author Gerardo
 *
 */
public class App extends Application implements Initializable {
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub
		AnchorPane root = FXMLLoader.load(getClass().getResource("ToDoView.fxml"));
		root.getStylesheets().add(getClass().getResource("todoStyle.css").toExternalForm());
		
		((BorderPane)root.getChildren().get(0)).setCenter(new CalendarioController());
		Scene scene = new Scene(root) ;
		primaryStage.setScene(scene);
		primaryStage.show();
		
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {

	}



	public static void main(String[] args) {
		launch(args);
	}

}
