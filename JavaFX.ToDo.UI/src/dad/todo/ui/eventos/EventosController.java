package dad.todo.ui.eventos;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import com.jfoenix.controls.JFXDatePicker;

import dad.calendario.CalendarioController;
import dad.todo.services.ServiceException;
import dad.todo.services.ServiceFactory;
import dad.todo.services.items.EventoItem;
import dad.todo.ui.model.EventosModel;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.layout.BorderPane;
import javafx.util.converter.LocalTimeStringConverter;

public class EventosController implements Initializable {

	@FXML
	private TableView<EventosModel> eventosTable;

	@FXML
	private TableColumn<EventosModel, Boolean> estadoColumn;

	@FXML
	private TableColumn<EventosModel, String> tituloColumn;

	@FXML
	private TableColumn<EventosModel, LocalTime> inicioColumn;

	@FXML
	private TableColumn<EventosModel, LocalTime> finColumn;

	@FXML
	private JFXDatePicker fechaEventosDatePicker;

	@FXML
	private BorderPane calendarContainer;
	private SplitPane view;

	private ListProperty<EventosModel> eventos;

	public EventosController() {
		eventos= new SimpleListProperty<>(this,"eventos",FXCollections.observableArrayList());
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("EventosView.fxml"));
			loader.setController(this);
			view = loader.load();
			view.getStylesheets().add(getClass().getResource("../todoStyle.css").toExternalForm());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		calendarContainer.setCenter(new CalendarioController());
		estadoColumn.setCellFactory(c -> new CheckBoxTableCell<>());
		
		estadoColumn.setCellValueFactory(v->v.getValue().terminadaProperty());
		tituloColumn.setCellValueFactory(v->v.getValue().tituloProperty());
		inicioColumn.setCellValueFactory(v->v.getValue().horaInicioProperty());
		finColumn.setCellValueFactory(v->v.getValue().horaFinProperty());

		fechaEventosDatePicker.valueProperty().addListener((obs, oldValue, newValue) -> onFechaChange(newValue));

		
		eventosTable.itemsProperty().bind(eventos);

	}

	private void onFechaChange(LocalDate newValue) {
		
				Date fecha = Date.from(newValue.atStartOfDay(ZoneId.systemDefault()).toInstant());

		eventos.clear();
		try {
		List<EventoItem> eventosItem = ServiceFactory.getEventosService().buscarEventosPorFecha(fecha);
		eventosTable.getItems().clear();
		eventos.addAll(eventosItem.stream().map(EventosModel::fromItem).collect(Collectors.toList()));
		} catch (ServiceException e) {
			e.printStackTrace();
		}

	}

	public SplitPane getView() {
		return view;
	}
}
