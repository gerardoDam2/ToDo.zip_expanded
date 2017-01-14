package dad.todo.services.items;

import java.util.Date;

public class EventoItem {
	private Long id;
	private String titulo;
	private Date fecha;
	private String descripcion;
	private Long duracion; // en minutos
	private Boolean realizado;
	private LugarItem lugar;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public Long getDuracion() {
		return duracion;
	}

	public void setDuracion(Long duracion) {
		this.duracion = duracion;
	}

	public Boolean getRealizado() {
		return realizado;
	}

	public void setRealizado(Boolean realizado) {
		this.realizado = realizado;
	}

	public LugarItem getLugar() {
		return lugar;
	}

	public void setLugar(LugarItem lugar) {
		this.lugar = lugar;
	}

}
