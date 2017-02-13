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
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import com.sun.javafx.scene.control.behavior.DatePickerBehavior;

import dad.todo.services.ServiceException;
import dad.todo.services.ServiceFactory;
import dad.todo.services.items.EventoItem;
import dad.todo.services.items.LugarItem;
import dad.todo.ui.model.EventosModel;
import dad.todo.ui.utils.MapV2;
import dad.todo.ui.utils.TimeUtils;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.SimpleSetProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableSet;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;

public class CrearEditarEventosController implements Initializable {

	

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

	private BorderPane view;

	private EventosController eventosController;

	private boolean editMode;

	private EventosModel eventoEditado;

	
	private ObservableSet<String> listaDirecciones;

	private AutoCompletionBinding<String> bindeadormagico;

	private MapV2 mapaDemigrante;

	


	public CrearEditarEventosController(EventosController eventosController) {
		this.eventosController = eventosController;
		listaDirecciones= new SimpleSetProperty<>(this,"listaDirecciones",FXCollections.observableSet());
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
		if (evento.getLugar()!=null) 
		mapaDemigrante.setLugar(evento.getLugar().getDescripccion(),evento.getLugar().getLatitud(),evento.getLugar().getLongitud());
		
	}

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
		horaInicioDatePicker.setTime(LocalTime.now());
		horaFinDatePicker.setTime(LocalTime.now().plusHours(1));
		realizadaCheckBox.setSelected(false);
		mapaDemigrante.clear();
	}

	// TODO limpiar comprobacion de direccion para borrarla
	@FXML
	void onGuardarAction(ActionEvent event) {
		
		
		EventoItem eventoItem = new EventoItem();
		eventoItem.setDescripcion(descripcionTextArea.getText());
		long duracion = ChronoUnit.MINUTES.between(horaInicioDatePicker.getTime(), horaFinDatePicker.getTime());
		eventoItem.setDuracion(duracion);
		eventoItem.setFecha(TimeUtils.localDateToDate(fechaDatePicker.getValue(), horaInicioDatePicker.getTime()));
		// TODO imp lugar
		if(mapaDemigrante.getLugarSearchTextField().getText().trim().length()!=0 && !mapaDemigrante.getDireccion().equals("sin dirección")){
		LugarItem lugar = new LugarItem();
		lugar.setDescripcion(mapaDemigrante.getDireccion());
		lugar.setLatitud(mapaDemigrante.getLatitud());
		lugar.setLongitud(mapaDemigrante.getLongitud());
		eventoItem.setLugar(lugar);
		}else {
			eventoItem.setLugar(null);
			System.out.println("seteando lugar a null");
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
	
	
	}



}
