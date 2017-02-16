package dad.todo.ui.gestor_propiedades;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class TodoStyleModel {

	String fileName;
	StringProperty nombre;
	StringProperty base1;
	StringProperty base2;
	StringProperty texto1;
	StringProperty texto2;
	StringProperty fondo;

	public TodoStyleModel() {
		nombre = new SimpleStringProperty(this, "nombre");
		base1 = new SimpleStringProperty(this, "base1");
		base2 = new SimpleStringProperty(this, "base2");
		texto1 = new SimpleStringProperty(this, "texto1");
		texto2 = new SimpleStringProperty(this, "texto2");
		fondo = new SimpleStringProperty(this, "fondo");
	}

	public StringProperty nombreProperty() {
		return this.nombre;
	}
	

	public String getNombre() {
		return this.nombreProperty().get();
	}
	

	public void setNombre(final String nombre) {
		this.nombreProperty().set(nombre);
	}
	

	public StringProperty base1Property() {
		return this.base1;
	}
	

	public String getBase1() {
		return this.base1Property().get();
	}
	

	public void setBase1(final String base1) {
		this.base1Property().set(base1);
	}
	

	public StringProperty base2Property() {
		return this.base2;
	}
	

	public String getBase2() {
		return this.base2Property().get();
	}
	

	public void setBase2(final String base2) {
		this.base2Property().set(base2);
	}
	

	public StringProperty texto1Property() {
		return this.texto1;
	}
	

	public String getTexto1() {
		return this.texto1Property().get();
	}
	

	public void setTexto1(final String texto1) {
		this.texto1Property().set(texto1);
	}
	

	public StringProperty texto2Property() {
		return this.texto2;
	}
	

	public String getTexto2() {
		return this.texto2Property().get();
	}
	

	public void setTexto2(final String texto2) {
		this.texto2Property().set(texto2);
	}
	

	public StringProperty fondoProperty() {
		return this.fondo;
	}
	

	public String getFondo() {
		return this.fondoProperty().get();
	}
	

	public void setFondo(final String fondo) {
		this.fondoProperty().set(fondo);
	}
	
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
}
