package dad.todo.ui;

import java.io.IOException;
import java.net.URL;
import java.nio.BufferUnderflowException;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXDrawer;
import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.transitions.hamburger.HamburgerBackArrowBasicTransition;

import dad.todo.ui.eventos.EventosController;
import dad.todo.ui.model.UsuarioModel;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class ToDoController implements Initializable {
	@FXML
	private JFXDrawer drawer;

	@FXML
	private JFXHamburger opcionesButton;

	private HamburgerBackArrowBasicTransition transition;

	@FXML
	private BorderPane contenidoPane;

	private AnchorPane view;

	private Stage stage;


	@FXML
	void onOpcionesMousePressed(MouseEvent event) {

		transition.setRate(transition.getRate() * -1);
		transition.play();

		if (drawer.isShown()) {
			drawer.close();
		} else
			drawer.open();
	}

	public ToDoController() {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("ToDoView.fxml"));
			loader.setController(this);
			view = loader.load();
			
			Scene scene = new Scene (view);
			scene.getStylesheets().add(getClass().getResource("todoStyle.css").toExternalForm());
			
			stage = new Stage();
			
			Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
			stage.setX(primaryScreenBounds.getMinX());
			stage.setY(primaryScreenBounds.getMinY());
			stage.setWidth(primaryScreenBounds.getWidth());
			stage.setHeight(primaryScreenBounds.getHeight());
			stage.setScene(scene);
			
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		transition = new HamburgerBackArrowBasicTransition(opcionesButton);
		transition.setRate(-1);
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("MenuView.fxml"));
			BorderPane MenuView = loader.load();
			drawer.setSidePane(MenuView);
			EventosController eventosController = new EventosController();
			contenidoPane.setCenter(eventosController.getView());

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public BorderPane getContenidoPane() {
		return contenidoPane;
	}
	
	public AnchorPane getView() {
		return view;
	}
	
	public void show() {
		stage.show();
	}
}
