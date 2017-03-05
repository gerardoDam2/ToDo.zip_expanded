package dad.todo.ui.loader;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.animation.RotateTransition;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

public class LoaderController implements Initializable{
	


    @FXML
    private AnchorPane oPane;
    
    @FXML
    private Pane view;

	private Stage loaderStage;

	private RotateTransition rt;

	private Stage primarySage;
    
    
    public LoaderController(Stage primaryStage) {
    	this.primarySage= primaryStage;
    	try {
    	FXMLLoader loader = new FXMLLoader(getClass().getResource("LoaderView.fxml"));
    	loader.setController(this);
			loader.load();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		
		 rt = new RotateTransition(Duration.seconds(2), oPane);
	     rt.setByAngle(360);
	     rt.setCycleCount(Timeline.INDEFINITE);
	     rt.setInterpolator(Interpolator.EASE_BOTH);
	     rt.play();
	     
	     
	 
		 Scene loaderScene = new Scene(view);
		 loaderScene.setFill(null);
		 loaderStage = new Stage();
		 loaderStage.initStyle(StageStyle.TRANSPARENT);
		 loaderStage.setScene(loaderScene);
		 loaderStage.initOwner(primarySage);
		 loaderStage.setAlwaysOnTop(true);
		 loaderStage.show();
		//
	}
	
	public Pane getView() {
		return view;
	}
	public void close() {
		rt.stop();
		FadeTransition ft = new FadeTransition(Duration.seconds(2),view);
		ft.setToValue(0);
		ft.setOnFinished(f->loaderStage.close());
		ft.play();
	}
}
