/**
 * 
 */
package dad.todo.ui;

import java.io.File;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ResourceBundle;

import org.scenicview.ScenicView;
import org.scenicview.view.ScenegraphTreeView;

import com.jfoenix.controls.JFXDecorator;

import dad.calendario.CalendarioController;
import dad.todo.services.ServiceFactory;
import dad.todo.services.items.UsuarioItem;
import dad.todo.services.jpa.utils.JPAUtil;
import dad.todo.ui.gestor_propiedades.GestorDePropiedades;
import dad.todo.ui.login.LoginController;
import dad.todo.ui.model.UsuarioModel;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * @author Gerardo
 *
 */
public class App extends Application  {


	private ToDoController todoController;
	static LoginController login;
	public static GestorDePropiedades gestorDePropiedades;

	@Override
	public void start(Stage primaryStage) throws Exception {
		
		JPAUtil.initEntityManagerFactory("todo");
		
        Thread.setDefaultUncaughtExceptionHandler(App::showError);
		
		gestorDePropiedades= new GestorDePropiedades();
		 login= new LoginController();
		
		JFXDecorator decorator = new JFXDecorator(primaryStage, login.getView());
		decorator.setCustomMaximize(false);
		App.gestorDePropiedades.ocupatedelCssPorMi(decorator);
		
	   
	
		HBox d=(HBox)decorator.getChildren().get(0);
		d.getChildren().remove(0);
		d.getChildren().remove(0);
		d.getChildren().remove(0);
		Scene scene = new Scene(decorator,500,430);
		primaryStage.initStyle(StageStyle.UNDECORATED);
		primaryStage.setResizable(false);
		
//		ScenicView.show(scene);
		primaryStage.setScene(scene);

	

	}

	 private static void showError(Thread t, Throwable e) {
	        if (Platform.isFxApplicationThread()) {
	        	System.err.println(e.getLocalizedMessage());
	        } else {
	            System.err.println("error en hilo"+t);
	        }
	    }


	public static void main(String[] args) {
		launch(args);
	
	}
	
	@Override
	public void stop() throws Exception {
		JPAUtil.closeEntityManagerFactory();
		gestorDePropiedades.guardarPropiedades();
		super.stop();
	}

}
