package dad.todo.ui.model;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXCheckBox;

import dad.todo.services.items.EventoItem;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.LongProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

public class EventosModel  extends GridPane implements Initializable{
	
	private LongProperty eventoID;
	private StringProperty titulo;
	private ObjectProperty<LocalDate> fecha;
	private StringProperty descripcion;
	private ObjectProperty<LocalTime> horaInicio;
	private ObjectProperty<LocalTime> horaFin;
	private ObjectProperty<LugarModel> lugar;
	private BooleanProperty terminada;
	
    @FXML
    private JFXCheckBox finalizadaCheckBox;

    @FXML
    private Label tituloLabel;

    @FXML
    private Label horaInicioLabel;

    @FXML
    private Label horaFinLabel;
	
	public EventosModel() {
		// TODO Auto-generated constructor stub
		titulo= new SimpleStringProperty(this,"titulo");
		terminada= new SimpleBooleanProperty(this,"terminada",false);
		System.out.println("crea");

		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("EventoItemView.fxml"));
			loader.setController(this);
			loader.setRoot(this);
			loader.load();
			
			Platform.runLater(()-> {
				
				
			});
	
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		System.out.println("ini");
		finalizadaCheckBox.selectedProperty().bind(terminada);
		tituloLabel.textProperty().bind(titulo);
	
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
	
	//TODO
	public static EventosModel fromItem(EventoItem item){
		return null;
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




	
	
	
}
