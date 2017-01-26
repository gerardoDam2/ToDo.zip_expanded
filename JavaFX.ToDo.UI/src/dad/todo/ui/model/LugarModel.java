package dad.todo.ui.model;

import javafx.beans.property.LongProperty;
import javafx.beans.property.StringProperty;

public class LugarModel {

	private StringProperty descripccion;
	private LongProperty latitud;
	private LongProperty longitud;
	public StringProperty descripccionProperty() {
		return this.descripccion;
	}
	
	public String getDescripccion() {
		return this.descripccionProperty().get();
	}
	
	public void setDescripccion(final String descripccion) {
		this.descripccionProperty().set(descripccion);
	}
	
	public LongProperty latitudProperty() {
		return this.latitud;
	}
	
	public long getLatitud() {
		return this.latitudProperty().get();
	}
	
	public void setLatitud(final long latitud) {
		this.latitudProperty().set(latitud);
	}
	
	public LongProperty longitudProperty() {
		return this.longitud;
	}
	
	public long getLongitud() {
		return this.longitudProperty().get();
	}
	
	public void setLongitud(final long longitud) {
		this.longitudProperty().set(longitud);
	}
	
	
	
}
