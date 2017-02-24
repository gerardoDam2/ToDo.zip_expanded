package dad.todo.ui.eventos;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXPopup;
import com.jfoenix.controls.JFXSnackbar.SnackbarEvent;

import dad.calendario.CalendarioController;
import dad.todo.services.ServiceException;
import dad.todo.services.ServiceFactory;
import dad.todo.services.items.EventoItem;
import dad.todo.services.jpa.utils.EmailUtil;
import dad.todo.ui.App;
import dad.todo.ui.ToDoController;
import dad.todo.ui.model.EventosModel;
import dad.todo.ui.report.detalleEvento;
import dad.todo.ui.utils.EmailAttachmentSender;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SetProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleSetProperty;
import javafx.collections.FXCollections;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SplitPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.view.JasperViewer;

public class EventosController implements Initializable {

	@FXML
	private VBox rightPanel;

	@FXML
	private BorderPane eventsPane;

	@FXML
	private ListView<EventosModel> eventosListView;
	// private JFXListView<EventosModel> eventosListView;

	@FXML
	private JFXDatePicker fechaEventosDatePicker;

	@FXML
	private BorderPane calendarContainer;
	private SplitPane view;

	private ListProperty<EventosModel> eventos;

	private CrearEditarEventosController editarCrearController;

	private CalendarioController calendarioController;

	private SetProperty<LocalDate> diasConEventos;

	public boolean editMode = false;

	public EventosController() {

		diasConEventos = new SimpleSetProperty<>(this, "diasConEventos", FXCollections.observableSet());

		eventos = new SimpleListProperty<>(this, "eventos", FXCollections.observableArrayList());
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("EventosView.fxml"));
			loader.setController(this);
			view = loader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		calendarioController = new CalendarioController();
		calendarContainer.setCenter(calendarioController);
		calendarioController.dayClickedEventProperty().addListener(e->{
			if (editMode) {
				changeViewToEventsList(fechaEventosDatePicker.getValue());
			}
		});

		calendarioController.specialDaysProperty().bind(diasConEventos);
		fechaEventosDatePicker.valueProperty().addListener((obs, oldValue, newValue) -> onFechaChange(newValue));
		// fechaEventosDatePicker.setValue(LocalDate.now());

		Bindings.bindBidirectional(calendarioController.selectedDayProperty(), fechaEventosDatePicker.valueProperty());
		eventosListView.itemsProperty().bind(eventos);

		BorderPane borderpane = new BorderPane();
		ImageView imagev = new ImageView(new Image(getClass().getResource("sinEventos.gif").toExternalForm()));
		borderpane.setBottom(imagev);
		borderpane.setAlignment(imagev, Pos.CENTER);
		eventosListView.setPlaceholder(borderpane);

		editarCrearController = new CrearEditarEventosController(this);

		initPopUp();

		eventosListView.setStyle("-fx-background-insets: 0 ;");
	}

	private void initPopUp() {

		ContextMenu contextMenu = new ContextMenu();
		// eventosListView.setContextMenu(contextMenu);
		MenuItem button1 = new MenuItem("Generar reporte detallado");
		button1.setOnAction(e -> {
			onReporteDetalladoAction();
		});

		MenuItem button2 = new MenuItem("Generar reporte general");
		button2.setOnAction(e -> {
			onReportGeneralAction();
		});

		button1.getStyleClass().add("menuButton");
		button2.getStyleClass().add("menuButton");

		contextMenu.getItems().addAll(button1, button2);


//		contextMenu.setOnShown(jfoenixbug->contextMenu.setStyle(
//				".menu-item { -fx-background-color: -fx-base1; } .menu-item:hover { -fx-background-color: -fx-base2; } .menu-item:pressed { -fx-background-color: #004C65; } .context-menu { -fx-background-color: transparent; }"));
	
		eventosListView.setCellFactory(lv -> {

			ListCell<EventosModel> cell = new ListCell<EventosModel>() {

				@Override
				protected void updateItem(EventosModel item, boolean empty) {
					super.updateItem(item, empty);
					setGraphic(null);
					setContextMenu(null);
					if (!empty && item != null) {
						super.updateItem(item, empty);
						setGraphic(item);
						setContextMenu(contextMenu);
					}
				}
			};
			return cell;
		});

	}

	private void onReportGeneralAction() {
		Task<Void> jasperTask = new Task<Void>() {

			@Override
			protected Void call() throws Exception {
				Map<String, Object> parametros = new HashMap<>();
				String nombre = "";
				nombre = ServiceFactory.getUsuariosService().getLogueado().getNombre();
				parametros.put("nombreUsuario", nombre);
				parametros.put("LOGO", this.getClass().getResource("../images/jasperLogo.png"));
				InputStream is = getClass().getResourceAsStream("../report/general.jasper");
				JasperPrint jasperPrint = JasperFillManager.fillReport(is, parametros,
						new JRBeanCollectionDataSource(detalleEvento.getTodosLosEventos()));
				is.close();
				JasperViewer.viewReport(jasperPrint, false);
				return null;
			}
		};
		new Thread(jasperTask).start();
	}

	private void onReporteDetalladoAction() {

		detalleEvento eventoSelect = detalleEvento
				.fromEventoModel(eventosListView.getSelectionModel().getSelectedItem());

		Task<Void> jasperTask = new Task<Void>() {

			@Override
			protected Void call() throws Exception {
				Map<String, Object> parametros = new HashMap<>();
				String nombre = "";
				nombre = ServiceFactory.getUsuariosService().getLogueado().getNombre();
				parametros.put("nombreUsuario", nombre);
				parametros.put("LOGO", this.getClass().getResource("../images/jasperLogo.png"));

				InputStream is;
				if (eventosListView.getSelectionModel().getSelectedItem().getLugar() != null)
					is = getClass().getResourceAsStream("../report/detalle.jasper");
				else
					is = getClass().getResourceAsStream("../report/detalleSinLugar.jasper");

				List<detalleEvento> eventosReport = new ArrayList<>();
				eventosReport.add(eventoSelect);

				JasperPrint jasperPrint = JasperFillManager.fillReport(is, parametros,
						new JRBeanCollectionDataSource(eventosReport));

				// JasperExportManager.exportReportToHtmlFile(jasperPrint,"./informe.html");

				is.close();
				JasperViewer.viewReport(jasperPrint, false);

				return null;
			}
		};

		new Thread(jasperTask).start();
	}

	private void editarEvento(EventosModel evento) {
		editarCrearController.initEdit(evento);
		changeViewToEditEvent();

	}

	private void onFechaChange(LocalDate newValue) {

		List<EventoItem> eventosItem = new ArrayList<>();
		if (editMode) {
			editarCrearController.onCancelarAction(null);
		}
		Task<List<EventoItem>> eventoysByFechaTask = new Task<List<EventoItem>>() {
			@Override
			protected List<EventoItem> call() throws ServiceException {
				Date fecha = Date.from(newValue.atStartOfDay(ZoneId.systemDefault()).toInstant());
				List<EventoItem> eventosItem = ServiceFactory.getEventosService().buscarEventosPorFecha(fecha);
				return eventosItem;
			}
		};

		Thread hilo = new Thread(eventoysByFechaTask);
		hilo.setName("eventosByFechaTask");
		eventoysByFechaTask.setOnSucceeded(s -> {
			eventos.setAll(eventoysByFechaTask.getValue().stream().map(e -> EventosModel.fromItem(e, this))
					.collect(Collectors.toList()));

			System.out.println(eventos);
		});

		hilo.start();
	}

	@FXML
	private Button addEventoButton;

	@FXML
	public void onAddEventoButtonAction(ActionEvent event) {

		editarCrearController.initCreateEvent(fechaEventosDatePicker.getValue());
		changeViewToEditEvent();
	}

	public SplitPane getView() {
		return view;
	}

	public void changeViewToEventsList(LocalDate f) {
		editMode = false;
		if (f == null) {
			onFechaChange(fechaEventosDatePicker.getValue());
		} else {
			onFechaChange(f);
		}

		rightPanel.getChildren().remove(0);
		rightPanel.getChildren().add(eventsPane);
		updateDiasConEventosList();
	}

	public void onEditEventAction(EventosModel evento) {
		if (!editMode) {

			editarCrearController.initEdit(evento);
			changeViewToEditEvent();
		}
	}

	public void changeViewToEditEvent() {
		editMode = true;
		rightPanel.getChildren().remove(0);
		rightPanel.getChildren().add(editarCrearController.getView());
	}

	private void updateDiasConEventosList() {

		Task<Void> updateDiasConEventosTask = new Task<Void>() {
			@Override
			protected Void call() throws Exception {
				diasConEventos.clear();
				diasConEventos.addAll(ServiceFactory.getEventosService().getEventos().stream()
						.map(EventosController::itemToLocalDate).collect(Collectors.toList()));
				return null;
			}
		};
		Thread hilo = new Thread(updateDiasConEventosTask);
		hilo.setName("getAllDatesWithEvents");
		hilo.start();
	}

	private static LocalDate itemToLocalDate(EventoItem eventoItem) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(eventoItem.getFecha());
		LocalDate fechaAux = LocalDate.of(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1,
				calendar.get(Calendar.DAY_OF_MONTH));
		return fechaAux;
	}

	// public void mostrarMenu(MouseEvent event, EventosModel eventosModel) {
	// if (menuPopUp.isVisible()) {
	// menuPopUp.close();
	// }
	// Bounds boundsInScene =
	// eventosModel.localToScene(eventosModel.getBoundsInLocal());
	// menuPopUp.show(JFXPopup.PopupVPosition.TOP, JFXPopup.PopupHPosition.LEFT,
	// 400,
	// 400);
	//
	// }

	// public void CerrarMenu() {
	// if (menuPopUp.isVisible()) {
	// menuPopUp.close();
	// }
	// }

	public void load() {
		updateDiasConEventosList();
		fechaEventosDatePicker.setValue(LocalDate.now());
	}

	public ListView<EventosModel> getEventosListView() {
		return eventosListView;
	}

	public void clear() {
		editarCrearController.clearForm();
		editMode = false;
		rightPanel.getChildren().remove(0);
		rightPanel.getChildren().add(eventsPane);
	}

	public CrearEditarEventosController getEditarCrearController() {
		return editarCrearController;
	}
}
