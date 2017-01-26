package dad.todo.ui.model;

import java.time.LocalDate;
import java.time.LocalTime;

import javafx.beans.property.LongProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.StringProperty;

public class EventosModel {
	
	private LongProperty id;
	private StringProperty titulo;
	private ObjectProperty<LocalDate> fecha;
	private StringProperty descripcion;
	private ObjectProperty<LocalTime> horaInicio;
	private ObjectProperty<LocalTime> horaFin;
	private ObjectProperty<LugarModel> lugar;
	public LongProperty idProperty() {
		return this.id;
	}
	
	public long getId() {
		return this.idProperty().get();
	}
	
	public void setId(final long id) {
		this.idProperty().set(id);
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
	
	
	
}