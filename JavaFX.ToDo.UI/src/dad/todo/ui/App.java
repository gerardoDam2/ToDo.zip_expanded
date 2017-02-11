/**
 * 
 */
package dad.todo.ui;

import java.net.URL;
import java.util.ResourceBundle;

import dad.calendario.CalendarioController;
import dad.todo.services.ServiceFactory;
import dad.todo.services.items.UsuarioItem;
import dad.todo.services.jpa.utils.JPAUtil;
import dad.todo.ui.gestor_propiedades.GestorDePropiedades;
import dad.todo.ui.login.LoginController;
import dad.todo.ui.model.UsuarioModel;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Screen;
import javafx.stage.Stage;

/**
 * @author Gerardo
 *
 */
public class App extends Application  {


	private ToDoController todoController;
	private GestorDePropiedades propiedades;

	@Override
	public void start(Stage primaryStage) throws Exception {
		JPAUtil.initEntityManagerFactory("todo");
		propiedades= new GestorDePropiedades();
		LoginController login= new LoginController();
		Scene loginScene= new Scene(login.getView());
		primaryStage.setScene(loginScene);
		primaryStage.show();

	

	}

	


	public static void main(String[] args) {
		launch(args);
	
	}
	
	@Override
	public void stop() throws Exception {
		JPAUtil.closeEntityManagerFactory();
		propiedades.guardarPropiedades();
		super.stop();
	}

}
