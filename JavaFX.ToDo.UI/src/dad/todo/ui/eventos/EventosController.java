package dad.todo.ui.eventos;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import dad.calendario.CalendarioController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;

public class EventosController implements Initializable {

	@FXML
	private BorderPane calendarContainer;
	private SplitPane view;
	
	public EventosController() {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("EventosView.fxml"));
			loader.setController(this);
			view=loader.load();
			view.getStylesheets().add(getClass().getResource("../todoStyle.css").toExternalForm());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		calendarContainer.setCenter(new CalendarioController());
	
	}

	public SplitPane getView() {
		return view;
	}
}
