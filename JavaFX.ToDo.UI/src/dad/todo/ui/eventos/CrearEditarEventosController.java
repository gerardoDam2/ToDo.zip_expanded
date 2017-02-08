package dad.todo.ui.eventos;

import java.awt.geom.Point2D;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ResourceBundle;

import org.controlsfx.control.textfield.AutoCompletionBinding;
import org.controlsfx.control.textfield.TextFields;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;

import dad.todo.services.ServiceException;
import dad.todo.services.ServiceFactory;
import dad.todo.services.items.EventoItem;
import dad.todo.services.items.LugarItem;
import dad.todo.ui.model.EventosModel;
import dad.todo.ui.utils.GoogleMapsComponent;
import dad.todo.ui.utils.MapV2;
import dad.todo.ui.utils.TimeUtils;
import impl.org.controlsfx.autocompletion.AutoCompletionTextFieldBinding;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleSetProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableSet;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import maps.java.Geocoding;

public class CrearEditarEventosController implements Initializable {

	@FXML
	private JFXTextField direccionTextField;

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

	private Geocoding ObjGeocod;
	
	private ObservableSet<String> listaDirecciones;

	private AutoCompletionBinding<String> bindeadormagico;

	private GoogleMapsComponent mapa;
	


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

	}

	@FXML
	void onCancelarAction(ActionEvent event) {
		clearForm();
		editMode = false;
		eventosController.changeViewToEventsList();
	}

	// TODO
	private void clearForm() {
		fechaDatePicker.setValue(null);
		tituloTextField.clear();
		descripcionTextArea.clear();
		horaInicioDatePicker.setTime(null);
		horaFinDatePicker.setTime(null);
		realizadaCheckBox.setSelected(false);
	}

	// TODO REFRESCAR LOS DATOS DESPUES DE ACTUALIZAR
	@FXML
	void onGuardarAction(ActionEvent event) {
		
		
		EventoItem eventoItem = new EventoItem();
		eventoItem.setDescripcion(descripcionTextArea.getText());
		long duracion = ChronoUnit.MINUTES.between(horaInicioDatePicker.getTime(), horaFinDatePicker.getTime());
		eventoItem.setDuracion(duracion);
		eventoItem.setFecha(TimeUtils.localDateToDate(fechaDatePicker.getValue(), horaInicioDatePicker.getTime()));
		// TODO imp lugar
		LugarItem lugar = new LugarItem();
		lugar.setDescripcion("todo");
		lugar.setLatitud(666d);
		lugar.setLongitud(666d);
		eventoItem.setLugar(lugar);
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
	direccionTextField.textProperty().addListener((obs,oldValue,newValue)->onDireccionTextFieldChange(newValue));
	bindeadormagico =TextFields.bindAutoCompletion(direccionTextField,listaDirecciones );
//	mapa =new GoogleMapsComponent();
	MapV2 mapa2 = new MapV2();
	gridContainer.add(mapa2, 0, 6);
	GridPane.setColumnSpan(mapa2, 2);
	
	}

	private void onDireccionTextFieldChange(String newValue) {
		if (newValue.trim().length()!=0 && newValue.charAt(newValue.length()-1)==' ') {
			
			
		
		
			try {
				 ObjGeocod= new Geocoding();
				 ObjGeocod.getCoordinates(newValue);
			} catch (UnsupportedEncodingException | MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			listaDirecciones.add(ObjGeocod.getAddressFound());
			System.out.println(listaDirecciones);
			
			if (bindeadormagico != null) {
				bindeadormagico.dispose();
			}
			
			 bindeadormagico =TextFields.bindAutoCompletion(tituloTextField,listaDirecciones );
			 bindeadormagico.setHideOnEscape(false);
			 
		}
		
		
	}

}
