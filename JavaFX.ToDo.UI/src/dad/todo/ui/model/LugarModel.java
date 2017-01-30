package dad.todo.ui.model;

import dad.todo.services.items.LugarItem;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class LugarModel {

	private StringProperty descripccion;
	private DoubleProperty latitud;
	private DoubleProperty longitud;
	
	public LugarModel() {
	descripccion= new SimpleStringProperty(this,"descripcion");
	latitud= new SimpleDoubleProperty(this,"latitud");
	longitud= new SimpleDoubleProperty(this,"longitud");
	}
	

	public static LugarModel fromItem(LugarItem item) {
		LugarModel lugar = new LugarModel();
		lugar.setDescripccion(item.getDescripcion());
		lugar.setLatitud(item.getLatitud());
		lugar.setLongitud(item.getLongitud());
		return lugar;
	}

	public StringProperty descripccionProperty() {
		return this.descripccion;
	}

	public String getDescripccion() {
		return this.descripccionProperty().get();
	}

	public void setDescripccion(final String descripccion) {
		this.descripccionProperty().set(descripccion);
	}

	public DoubleProperty latitudProperty() {
		return this.latitud;
	}

	public double getLatitud() {
		return this.latitudProperty().get();
	}

	public void setLatitud(final double latitud) {
		this.latitudProperty().set(latitud);
	}

	public DoubleProperty longitudProperty() {
		return this.longitud;
	}

	public double getLongitud() {
		return this.longitudProperty().get();
	}

	public void setLongitud(final double longitud) {
		this.longitudProperty().set(longitud);
	}

}
