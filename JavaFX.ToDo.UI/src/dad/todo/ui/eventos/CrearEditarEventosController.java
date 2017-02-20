package dad.todo.ui.eventos;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ResourceBundle;

import org.controlsfx.control.textfield.AutoCompletionBinding;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXSnackbar.SnackbarEvent;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.validation.RequiredFieldValidator;
import com.jfoenix.validation.base.ValidatorBase;

import dad.todo.services.ServiceException;
import dad.todo.services.ServiceFactory;
import dad.todo.services.items.EventoItem;
import dad.todo.services.items.LugarItem;
import dad.todo.ui.ToDoController;
import dad.todo.ui.model.EventosModel;
import dad.todo.ui.utils.GoogleMapsComponent;
import dad.todo.ui.utils.MapV2;
import dad.todo.ui.utils.TimeUtils;
import javafx.application.Platform;
import javafx.beans.binding.When;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleSetProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableSet;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.media.AudioClip;

public class CrearEditarEventosController implements Initializable {

	@FXML
	private Label fechaErrorLabel;

	@FXML
	private Label horaFinErrorLabel;

	@FXML
	private JFXTextField tituloTextField;

	@FXML
	private JFXDatePicker horaInicioDatePicker;

	@FXML
	private JFXDatePicker horaFinDatePicker;

	@FXML
	private JFXTextArea descripcionTextArea;

	@FXML
	private JFXDatePicker fechaDatePicker;

	@FXML
	private JFXCheckBox realizadaCheckBox;

	@FXML
	private JFXButton guardarButton;

	@FXML
	private JFXButton cancelarButton;

	@FXML
	private GridPane gridContainer;

	private BooleanProperty horaFinValidator;

	private ScrollPane view;

	private EventosController eventosController;

	private boolean editMode;

	private EventosModel eventoEditado;

	private ObservableSet<String> listaDirecciones;

	private AutoCompletionBinding<String> bindeadormagico;

	private MapV2 mapaDemigrante;

	private BooleanProperty fechaValidator;

	private AudioClip okSound;

	private AudioClip errorSound;

	public CrearEditarEventosController(EventosController eventosController) {
		this.horaFinValidator = new SimpleBooleanProperty(this, "horaFinValidator");
		this.fechaValidator = new SimpleBooleanProperty(this, "fechaValidator");
		this.eventosController = eventosController;
		listaDirecciones = new SimpleSetProperty<>(this, "listaDirecciones", FXCollections.observableSet());
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("CrearEditarEvento.fxml"));
			loader.setController(this);
			view = loader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}

		okSound = new AudioClip(getClass().getResource("../sonidos/ok.wav").toExternalForm());
		errorSound = new AudioClip(getClass().getResource("../sonidos/error.wav").toExternalForm());
	}

	public ScrollPane getView() {
		return view;
	}

	// TODO
	public void initEdit(EventosModel evento) {
		editMode = true;
		eventoEditado = evento;
		fechaDatePicker.setValue(evento.getFecha());
		tituloTextField.setText(evento.getTitulo());
		horaInicioDatePicker.setTime(evento.getHoraInicio());
		horaFinDatePicker.setTime(evento.getHoraFin());
		descripcionTextArea.setText(evento.getDescripcion());
		realizadaCheckBox.setSelected(evento.isTerminada());
		if (evento.getLugar() != null)
			try {
				mapaDemigrante.setLugar(evento.getLugar().getDescripccion(), evento.getLugar().getLatitud(),evento.getLugar().getLongitud());
			} catch (Exception e) {
				System.out.println("mapa demigrante esta fallando  en CrearEditarEventos:initEdit ");
			}

	}

	// TODO
	@FXML
	void onCancelarAction(ActionEvent event) {
		clearForm();
		editMode = false;
		eventosController.changeViewToEventsList(fechaDatePicker.getValue());
	}

	// TODO
	public void clearForm() {
		fechaDatePicker.setValue(null);
		tituloTextField.clear();
		descripcionTextArea.clear();
		realizadaCheckBox.setSelected(false);
		
		try {
			mapaDemigrante.clear();
		} catch (Exception e) {
			System.out.println("mapa demigrante esta fallando  en CrearEditarEventos:clearForm ");
			mapaDemigrante=new MapV2();
			
		}
		horaInicioDatePicker.setPromptText("Hora de inicio");
		horaFinDatePicker.setPromptText("Hora de fin");
	}

	// TODO limpiar comprobacion de direccion para borrarla
	@FXML
	void onGuardarAction(ActionEvent event) {

		EventoItem eventoItem = new EventoItem();
		eventoItem.setDescripcion(descripcionTextArea.getText());
		long duracion = ChronoUnit.MINUTES.between(horaInicioDatePicker.getTime(), horaFinDatePicker.getTime());
		eventoItem.setDuracion(duracion);
		eventoItem.setFecha(TimeUtils.localDateToDate(fechaDatePicker.getValue(), horaInicioDatePicker.getTime()));

		if (mapaDemigrante.getLugarSearchTextField().getText().trim().length() != 0
				&& !mapaDemigrante.getDireccion().equals("sin dirección")) {
			LugarItem lugar = new LugarItem();
			lugar.setDescripcion(mapaDemigrante.getDireccion());
			lugar.setLatitud(mapaDemigrante.getLatitud());
			lugar.setLongitud(mapaDemigrante.getLongitud());
			eventoItem.setLugar(lugar);
		} else {
			eventoItem.setLugar(null);
		}

		eventoItem.setRealizado(realizadaCheckBox.isSelected());
		eventoItem.setTitulo(tituloTextField.getText());

		if (editMode) {
			eventoItem.setId(eventoEditado.getEventoID());
			Task<Void> guardarTask = new Task<Void>() {

				@Override
				protected Void call() {
					try {
						ServiceFactory.getEventosService().actualizarEvento(eventoItem);
						ToDoController.notificator.enqueue(new SnackbarEvent("Evento Guardado "));
						okSound.play();

					} catch (ServiceException e) {
						ToDoController.notificator
								.enqueue(new SnackbarEvent("Error! No hemos podido guardar el evento"));
						errorSound.play();
						e.printStackTrace();
					}

					return null;
				}
			};

			new Thread(guardarTask).start();
			onCancelarAction(null);

		} else {
			Task<Void> crearTask = new Task<Void>() {

				@Override
				protected Void call() {
					try {
						ServiceFactory.getEventosService().crearEvento(eventoItem);
						ToDoController.notificator.enqueue(new SnackbarEvent("Evento Creado"));
						okSound.play();

					} catch (ServiceException e) {
						ToDoController.notificator.enqueue(new SnackbarEvent("Error! No hemos podido crear el evento"));
						errorSound.play();
						e.printStackTrace();
					}

					return null;
				}
			};
			crearTask.setOnSucceeded(f->onCancelarAction(null));
			new Thread(crearTask).start();

		}
	}

	// TODO
	public void initCreateEvent(LocalDate localDate) {
		fechaDatePicker.setValue(localDate);
		horaInicioDatePicker.setTime(LocalTime.now());
		horaFinDatePicker.setTime(LocalTime.now().plusHours(1));

	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		Platform.runLater(()->{
			mapaDemigrante = new MapV2();
			gridContainer.add(mapaDemigrante, 0, 6);
			GridPane.setColumnSpan(mapaDemigrante, 2);
			
		});
	
		horaInicioDatePicker.setValue(LocalDate.now());
		horaInicioDatePicker.setTime(LocalTime.now());
		horaFinDatePicker.setValue(LocalDate.now());
		horaFinDatePicker.setTime(LocalTime.now().plusHours(1));

		// validar hora
		horaFinErrorLabel.visibleProperty().bind(horaFinValidator);
		horaFinDatePicker.timeProperty().addListener(e -> onDatePickersChange());
		horaInicioDatePicker.timeProperty().addListener(e -> onDatePickersChange());
		horaFinErrorLabel.setTooltip(new Tooltip("La hora de finalizacion no puede ser inferior a la hora de inicio"));

		// validar fecha
		fechaErrorLabel.setTooltip(new Tooltip("Debe introducir una fecha"));
		fechaErrorLabel.visibleProperty().bind(fechaDatePicker.valueProperty().isNull());

		// titulo
		// tituloTextField.setStyle("-fx-label-float:true;");
		// tituloTextField.setPromptText("Password");
		// RequiredFieldValidator tituloValidator = new
		// RequiredFieldValidator();
		// tituloValidator.setMessage("Password Can't be empty");
		// tituloTextField.getValidators().add(tituloValidator);
		// tituloTextField.focusedProperty().addListener((o,oldVal,newVal)->{
		// if(!newVal) tituloTextField.validate();
		// });

		RequiredFieldValidator tituloValidator = new RequiredFieldValidator();
		tituloValidator.setMessage("Debe introducir un titulo");
		tituloTextField.getValidators().add(tituloValidator);
		tituloTextField.focusedProperty().addListener((o, oldVal, newVal) -> {
			if (!newVal)
				tituloTextField.validate();
		});

		RequiredFieldValidator descripcionValidator = new RequiredFieldValidator();
		descripcionValidator.setMessage("Debe introducir un descripción");
		descripcionTextArea.getValidators().add(descripcionValidator);
		descripcionTextArea.focusedProperty().addListener((o, oldVal, newVal) -> {
			if (!newVal)
				descripcionTextArea.validate();
		});
		tituloValidator.setIcon(new ImageView(
				new Image(getClass().getResourceAsStream("../images/Cancel-48.png"), 24, 24, false, false)));
		descripcionValidator.setIcon(new ImageView(
				new Image(getClass().getResourceAsStream("../images/Cancel-48.png"), 24, 24, false, false)));

		
	}

	public boolean prueba() {
		return tituloTextField.getText().equals("hola");
	}

	private void onDatePickersChange() {
		if (horaInicioDatePicker.getTime().isAfter(horaFinDatePicker.getTime())) {
			horaFinValidator.set(true);
		} else {
			horaFinValidator.set(false);
		}

	}

	public EventosModel getEventoEditado() {
		return eventoEditado;
	}
}
