package dad.todo.ui.model;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Calendar;


import dad.todo.services.items.EventoItem;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.LongProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class EventosModel {

	private LongProperty eventoID;
	private StringProperty titulo;
	private ObjectProperty<LocalDate> fecha;
	private StringProperty descripcion;
	private ObjectProperty<LocalTime> horaInicio;
	private ObjectProperty<LocalTime> horaFin;
	private ObjectProperty<LugarModel> lugar;
	private BooleanProperty terminada;

	public EventosModel() {
		eventoID = new SimpleLongProperty(this, "eventoID");
		titulo = new SimpleStringProperty(this, "titulo");
		fecha = new SimpleObjectProperty<>(this, "fecha");
		descripcion = new SimpleStringProperty(this, "descipcion");
		horaInicio = new SimpleObjectProperty<>(this, "horaInicio");
		horaFin = new SimpleObjectProperty<>(this, "horaFin");
		lugar = new SimpleObjectProperty<>(this, "lugar");
		terminada = new SimpleBooleanProperty(this, "terminada");
	}
	
	
	public static EventosModel fromItem(EventoItem item) {

		EventosModel evento = new EventosModel();
		
		evento.setTitulo(item.getTitulo());
		
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(item.getFecha());
		LocalDate fechaAux = LocalDate.of(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH)+1,
				calendar.get(Calendar.DAY_OF_MONTH));
		evento.setFecha(fechaAux);
		
		LocalTime horaIniAux = LocalTime.of(calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE));
		evento.setHoraInicio(horaIniAux);
		
		calendar.add(Calendar.MINUTE, item.getDuracion().intValue());
		LocalTime horaFinAux = LocalTime.of(calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE));
		evento.setHoraFin(horaFinAux);

		evento.setDescripcion(item.getDescripcion());

		evento.setEventoID(item.getId());

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

}
