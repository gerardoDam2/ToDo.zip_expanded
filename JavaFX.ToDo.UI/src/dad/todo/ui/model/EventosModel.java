package dad.todo.ui.model;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.time.LocalDate;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.stage.StageStyle;

import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Optional;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXPopup;

import dad.todo.services.ServiceException;
import dad.todo.services.ServiceFactory;
import dad.todo.services.items.EventoItem;
import dad.todo.services.items.LugarItem;
import dad.todo.ui.App;
import dad.todo.ui.eventos.EventosController;
import dad.todo.ui.utils.TimeUtils;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.LongProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;

public class EventosModel extends GridPane implements Initializable {

	private LongProperty eventoID;
	private StringProperty titulo;
	private ObjectProperty<LocalDate> fecha;
	private StringProperty descripcion;
	private ObjectProperty<LocalTime> horaInicio;
	private ObjectProperty<LocalTime> horaFin;
	private ObjectProperty<LugarModel> lugar;
	private BooleanProperty terminada;

	@FXML
	private Label labelTitulo;

	@FXML
	private Button mapButton;

	@FXML
	private Label horaInicioLabel;

	@FXML
	private Label horaFinLabel;

	@FXML
	private Button editButton;

	@FXML
	private Button eliminarButton;

	@FXML
	private CheckBox checkBoxTerminado;
	private EventosController eventosController;

	public EventosModel(EventosController controller) {
		this.eventosController = controller;
		eventoID = new SimpleLongProperty(this, "eventoID");
		titulo = new SimpleStringProperty(this, "titulo");
		fecha = new SimpleObjectProperty<>(this, "fecha");
		descripcion = new SimpleStringProperty(this, "descipcion");
		horaInicio = new SimpleObjectProperty<>(this, "horaInicio");
		horaFin = new SimpleObjectProperty<>(this, "horaFin");
		lugar = new SimpleObjectProperty<>(this, "lugar");
		terminada = new SimpleBooleanProperty(this, "terminada");

		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("EventosModelView.fxml"));
			loader.setRoot(this);
			loader.setController(this);
			loader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public EventosModel(EventosController controller, Boolean realizado) {
		this.eventosController = controller;
		eventoID = new SimpleLongProperty(this, "eventoID");
		titulo = new SimpleStringProperty(this, "titulo");
		fecha = new SimpleObjectProperty<>(this, "fecha");
		descripcion = new SimpleStringProperty(this, "descipcion");
		horaInicio = new SimpleObjectProperty<>(this, "horaInicio");
		horaFin = new SimpleObjectProperty<>(this, "horaFin");
		lugar = new SimpleObjectProperty<>(this, "lugar");
		terminada = new SimpleBooleanProperty(this, "terminada", realizado);

		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("EventosModelView.fxml"));
			loader.setRoot(this);
			loader.setController(this);
			loader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static EventosModel fromItem(EventoItem item, EventosController controller) {

		EventosModel evento = new EventosModel(controller, item.getRealizado());

		evento.setTitulo(item.getTitulo());

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(item.getFecha());
		LocalDate fechaAux = LocalDate.of(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1,
				calendar.get(Calendar.DAY_OF_MONTH));
		evento.setFecha(fechaAux);

		LocalTime horaIniAux = LocalTime.of(calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE));
		evento.setHoraInicio(horaIniAux);

		calendar.add(Calendar.MINUTE, item.getDuracion().intValue());
		LocalTime horaFinAux = LocalTime.of(calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE));
		evento.setHoraFin(horaFinAux);

		evento.setDescripcion(item.getDescripcion());

		evento.setEventoID(item.getId());

		if (item.getLugar() != null)
			evento.setLugar(LugarModel.fromItem(item.getLugar()));

		evento.setTerminada(item.getRealizado());

		return evento;
	}

	public StringProperty tituloProperty() {
		return this.titulo;
	}

	public String getTitulo() {
		return this.tituloProperty().get();
	}

	public void setTitulo(final String titulo) {
		this.tituloProperty().set(titulo);
	}

	public ObjectProperty<LocalDate> fechaProperty() {
		return this.fecha;
	}

	public LocalDate getFecha() {
		return this.fechaProperty().get();
	}

	public void setFecha(final LocalDate fecha) {
		this.fechaProperty().set(fecha);
	}

	public StringProperty descripcionProperty() {
		return this.descripcion;
	}

	public String getDescripcion() {
		return this.descripcionProperty().get();
	}

	public void setDescripcion(final String descripcion) {
		this.descripcionProperty().set(descripcion);
	}

	public ObjectProperty<LocalTime> horaInicioProperty() {
		return this.horaInicio;
	}

	public LocalTime getHoraInicio() {
		return this.horaInicioProperty().get();
	}

	public void setHoraInicio(final LocalTime horaInicio) {
		this.horaInicioProperty().set(horaInicio);
	}

	public ObjectProperty<LocalTime> horaFinProperty() {
		return this.horaFin;
	}

	public LocalTime getHoraFin() {
		return this.horaFinProperty().get();
	}

	public void setHoraFin(final LocalTime horaFin) {
		this.horaFinProperty().set(horaFin);
	}

	public ObjectProperty<LugarModel> lugarProperty() {
		return this.lugar;
	}

	public LugarModel getLugar() {
		return this.lugarProperty().get();
	}

	public void setLugar(final LugarModel lugar) {
		this.lugarProperty().set(lugar);
	}

	public LongProperty eventoIDProperty() {
		return this.eventoID;
	}

	public long getEventoID() {
		return this.eventoIDProperty().get();
	}

	public void setEventoID(final long eventoID) {
		this.eventoIDProperty().set(eventoID);
	}

	public BooleanProperty terminadaProperty() {
		return this.terminada;
	}

	public boolean isTerminada() {
		return this.terminadaProperty().get();
	}

	public void setTerminada(final boolean terminada) {
		this.terminadaProperty().set(terminada);
	}

	@FXML
	void onEditButtonAction(ActionEvent event) {
		eventosController.onEditEventAction(this);
	}

	@FXML
	public void onEliminarButtonAction(ActionEvent event) {
		try {
			Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.initStyle(StageStyle.UNDECORATED);
			
			alert.initOwner(this.getScene().getWindow());
			alert.setTitle("Eliminar evento");
			alert.setHeaderText("¿Esta seguro de borrar este evento?");
			alert.setContentText("Una vez borrado el evento no podrá recuperarlo "
					+ "\n en su lugar usted podría marcar este evento como finalizado.");
			
			alert.getDialogPane().getStylesheets().add(
					   getClass().getResource("../gestor_propiedades/"+App.gestorDePropiedades.currentStyleProperty().getValue().getFileName()).toExternalForm());
			alert.getDialogPane().getStyleClass().add("myDialog");

			Optional<ButtonType> result = alert.showAndWait();
			if (result.get() == ButtonType.OK){
				ServiceFactory.getEventosService().eliminarEvento(this.getEventoID());
				eventosController.changeViewToEventsList(null);
			    // ... user chose OK
			}
		} catch (ServiceException e) {
			e.printStackTrace();
		}
	}

	@FXML
	public void onMapButtonAction(ActionEvent event) {
		if (getLugar() != null) {

			// TODO
			System.out.println("mostrar mapa ");
			try {

				Desktop.getDesktop().browse(new URI("http://maps.google.com/maps?q=loc:" + this.getLugar().getLatitud()
						+ "," + this.getLugar().getLongitud()));

			} catch (IOException | URISyntaxException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		checkBoxTerminado.setSelected(isTerminada());
		checkBoxTerminado.selectedProperty().addListener((obs, oldValue, newValue) -> onTerminadaChange(newValue));
		labelTitulo.textProperty().bind(tituloProperty());
		horaInicioLabel.textProperty().bind(horaInicioProperty().asString());
		horaFinLabel.textProperty().bind(horaFinProperty().asString());

		this.setStyle("-fx-background-color:green");
		mapButton.disableProperty().bind(lugarProperty().isNull());

	}

	private void onTerminadaChange(Boolean newValue) {
		// TODO
		EventoItem eventoItem = new EventoItem();
		eventoItem.setDescripcion(getDescripcion());
		long duracion = ChronoUnit.MINUTES.between(horaInicio.get(), horaFin.get());
		eventoItem.setDuracion(duracion);
		eventoItem.setFecha(TimeUtils.localDateToDate(getFecha(), getHoraInicio()));
		eventoItem.setId(getEventoID());
		eventoItem.setRealizado(newValue);
		eventoItem.setTitulo(getTitulo());
		if (getLugar() != null) {
			LugarItem lugarItem = new LugarItem();
			lugarItem.setDescripcion(getLugar().getDescripccion());
			lugarItem.setLatitud(getLugar().getLatitud());
			lugarItem.setLongitud(getLugar().getLongitud());
			eventoItem.setLugar(lugarItem);
		}

		try {
			ServiceFactory.getEventosService().actualizarEvento(eventoItem);
		} catch (ServiceException e) {
			e.printStackTrace();
		}

	}

	@FXML
	void onEventClicked(MouseEvent event) {
		if (event.getButton() == MouseButton.SECONDARY) {
			System.out.println("eventoClicked");
			eventosController.mostrarMenu(event, this);
		} else {
			eventosController.CerrarMenu();
		}
	}
}
