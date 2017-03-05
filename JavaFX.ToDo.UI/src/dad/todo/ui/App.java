/**
 * 
 */
package dad.todo.ui;



import com.jfoenix.controls.JFXDecorator;

import dad.todo.services.jpa.utils.JPAUtil;
import dad.todo.ui.gestor_propiedades.GestorDePropiedades;
import dad.todo.ui.login.LoginController;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
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
