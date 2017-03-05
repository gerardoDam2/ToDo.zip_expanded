/**
 * 
 */
package dad.todo.ui;



import com.jfoenix.controls.JFXDecorator;

import dad.todo.services.jpa.utils.JPAUtil;
import dad.todo.ui.gestor_propiedades.GestorDePropiedades;
import dad.todo.ui.loader.LoaderController;
import dad.todo.ui.login.LoginController;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * @author Gerardo
 *
 */
public class App extends Application  {


	static LoginController login;
	public static GestorDePropiedades gestorDePropiedades;

	@Override
	public void start(Stage primaryStage) throws Exception {
		
		LoaderController l = new LoaderController(primaryStage);
		
		Thread.setDefaultUncaughtExceptionHandler(App::showError);
		
		
		Task<Void> initTask = new Task<Void>() {
			@Override
			protected Void call() throws Exception {
				// TODO Auto-generated method stub
				JPAUtil.initEntityManagerFactory("todo");
				gestorDePropiedades= new GestorDePropiedades();
				return null;
			}
		};
		
		initTask.setOnSucceeded(s->{
			login= new LoginController();
			JFXDecorator decorator = new JFXDecorator(primaryStage, login.getView());
			App.gestorDePropiedades.ocupatedelCssPorMi(decorator);
			decorator.setCustomMaximize(false);
		
			HBox d=(HBox)decorator.getChildren().get(0);
			d.getChildren().remove(0);
			d.getChildren().remove(0);
			d.getChildren().remove(0);
			Scene scene = new Scene(decorator,500,430);
			primaryStage.initStyle(StageStyle.UNDECORATED);
			primaryStage.setResizable(false);
			primaryStage.setScene(scene);
			l.close();
		});
		
		new Thread(initTask).start();

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
		// LauncherImpl.launchApplication(App.class, LoaderController.class, args);
	}
	
	@Override
	public void stop() throws Exception {
		JPAUtil.closeEntityManagerFactory();
		gestorDePropiedades.guardarPropiedades();
		super.stop();
	}

}
