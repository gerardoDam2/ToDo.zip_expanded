package dad.todo.services.jpa.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import dad.todo.services.items.LugarItem;

@Entity
@Table(name = "lugares")
public class Lugar implements Serializable {
	private static final long serialVersionUID = 2758694861858617188L;

	@Id
	@OneToOne(fetch = FetchType.LAZY)
	private Evento evento;

	@Column(nullable = false)
	private Double longitud;

	@Column(nullable = false)
	private Double latitud;

	@Column(nullable = false)
	private String descripcion;

	public Double getLongitud() {
		return longitud;
	}

	public void setLongitud(Double longitud) {
		this.longitud = longitud;
	}

	public Double getLatitud() {
		return latitud;
	}

	public void setLatitud(Double latitud) {
		this.latitud = latitud;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Lugar other = (Lugar) obj;
		if (evento == null) {
			if (other.evento != null)
				return false;
		} else if (!evento.equals(other.evento))
			return false;
		return true;
	}
	
	public static Lugar fromItem(LugarItem item) {
		Lugar lugar = new Lugar();
		lugar.setDescripcion(item.getDescripcion());
		lugar.setLatitud(item.getLatitud());
		lugar.setLongitud(item.getLongitud());
		return lugar;
	}
	
	public void setEvento(Evento evento) {
		this.evento = evento;
	}
	public  LugarItem toItem() {
		//TODO
		return null;
	}

}
