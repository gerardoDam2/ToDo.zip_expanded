package dad.todo.ui.eventos;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ResourceBundle;

import org.controlsfx.control.textfield.AutoCompletionBinding;
import org.controlsfx.validation.Severity;
import org.controlsfx.validation.ValidationSupport;
import org.controlsfx.validation.Validator;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.validation.RequiredFieldValidator;
import com.sun.javafx.scene.control.behavior.DatePickerBehavior;
import com.sun.javafx.scene.control.skin.DatePickerSkin;

import dad.todo.services.ServiceException;
import dad.todo.services.ServiceFactory;
import dad.todo.services.items.EventoItem;
import dad.todo.services.items.LugarItem;
import dad.todo.ui.model.EventosModel;
import dad.todo.ui.utils.MapV2;
import dad.todo.ui.utils.TimeUtils;
import dad.todo.ui.utils.ValidatorUtil;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.binding.When;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleSetProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableSet;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.HorizontalDirection;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;

public class CrearEditarEventosController implements Initializable {
	
	@FXML
    private Label errorLabel;

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

	private BorderPane view;

	private EventosController eventosController;

	private boolean editMode;

	private EventosModel eventoEditado;

	private ObservableSet<String> listaDirecciones;

	private AutoCompletionBinding<String> bindeadormagico;

	private MapV2 mapaDemigrante;

	public CrearEditarEventosController(EventosController eventosController) {
		this.horaFinValidator=new SimpleBooleanProperty(this,"horaFinValidator");
		this.eventosController = eventosController;
		listaDirecciones = new SimpleSetProperty<>(this, "listaDirecciones", FXCollections.observableSet());
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("CrearEditarEvento.fxml"));
			loader.setController(this);
			view = loader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public BorderPane getView() {
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
			mapaDemigrante.setLugar(evento.getLugar().getDescripccion(), evento.getLugar().getLatitud(),
					evento.getLugar().getLongitud());

		// ValidationSupport validationEvento = new ValidationSupport();
		// validationEvento.registerValidator(tituloTextField, false,
		// Validator.createRegexValidator("formato incorrecto",
		// ValidatorUtil.EMAIL_PATTERN, Severity.ERROR));
		// validationEvento.registerValidator(horaInicioDatePicker, false,
		// Validator.createRegexValidator("formato incorrecto",
		// ValidatorUtil.EMAIL_PATTERN, Severity.ERROR));
		// validationEvento.registerValidator(horaFinDatePicker, false,
		// Validator.createRegexValidator("formato incorrecto",
		// ValidatorUtil.EMAIL_PATTERN, Severity.ERROR));
		// validationEvento.registerValidator(descripcionTextArea, false,
		// Validator.createRegexValidator("formato incorrecto",
		// ValidatorUtil.EMAIL_PATTERN, Severity.ERROR));

	}

	// TODO
	@FXML
	void onCancelarAction(ActionEvent event) {
		clearForm();
		editMode = false;
		eventosController.changeViewToEventsList(fechaDatePicker.getValue());
	}

	// TODO
	private void clearForm() {
		fechaDatePicker.setValue(null);
		tituloTextField.clear();
		descripcionTextArea.clear();
		realizadaCheckBox.setSelected(false);
		mapaDemigrante.clear();
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
			try {
				ServiceFactory.getEventosService().actualizarEvento(eventoItem);
			} catch (ServiceException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			try {
				ServiceFactory.getEventosService().crearEvento(eventoItem);
			} catch (ServiceException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		onCancelarAction(null);
	}

	// TODO
	public void initCreateEvent(LocalDate localDate) {
		fechaDatePicker.setValue(localDate);
		horaInicioDatePicker.setTime(LocalTime.now());
		horaFinDatePicker.setTime(LocalTime.now().plusHours(1));
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		mapaDemigrante = new MapV2();
		gridContainer.add(mapaDemigrante, 0, 6);
		GridPane.setColumnSpan(mapaDemigrante, 2);
		horaInicioDatePicker.setValue(LocalDate.now());
		horaInicioDatePicker.setTime(LocalTime.now());
		horaFinDatePicker.setValue(LocalDate.now());
		horaFinDatePicker.setTime(LocalTime.now().plusHours(1));
		errorLabel.visibleProperty().bind(horaFinValidator);
		horaFinDatePicker.timeProperty().addListener(e->onDatePickersChange());
		horaInicioDatePicker.timeProperty().addListener(e->onDatePickersChange());
		errorLabel.setTooltip(new Tooltip("La hora de finalizacion no puede ser inferior a la hora de inicio"));
		


	}

	private void onDatePickersChange() {
		if (horaInicioDatePicker.getTime().isAfter(horaFinDatePicker.getTime())) {
			horaFinValidator.set(true);
		}else{
			horaFinValidator.set(false);

		}
			
		
	}

}
