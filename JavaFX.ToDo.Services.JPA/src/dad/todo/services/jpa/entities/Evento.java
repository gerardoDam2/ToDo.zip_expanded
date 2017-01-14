package dad.todo.services.jpa.entities;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import dad.todo.services.items.EventoItem;

@Entity
@Table(name = "eventos")
public class Evento {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String titulo;

	private Date fecha;

	private String descripcion;

	private Long duracion; // en minutos

	private Boolean realizado;

	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@PrimaryKeyJoinColumn	
	private Lugar lugar;
	
	@ManyToOne
	private Usuario usuario;

	
	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

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

	public Lugar getLugar() {
		return lugar;
	}

	public void setLugar(Lugar lugar) {
		this.lugar = lugar;
	}
	
	public EventoItem toItem() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public static Evento fromItem(EventoItem item) {
		Evento evento = new Evento();
		evento.setDescripcion(item.getDescripcion());
		evento.setDuracion(item.getDuracion());
		evento.setFecha(item.getFecha());
		evento.setId(item.getId());
		Lugar lugar= Lugar.fromItem(item.getLugar());
		lugar.setEvento(evento);
		evento.setLugar(lugar);
		evento.setRealizado(item.getRealizado());
		evento.setTitulo(item.getTitulo());
		return evento;
	}

}
