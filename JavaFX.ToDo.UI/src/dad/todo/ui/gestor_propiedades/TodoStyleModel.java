package dad.todo.ui.gestor_propiedades;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.paint.Color;

public class TodoStyleModel {

	String fileName;
	StringProperty nombre;
	ObjectProperty<Color> base1;
	ObjectProperty<Color> base2;
	ObjectProperty<Color> texto1;
	ObjectProperty<Color> texto2;
	ObjectProperty<Color> fondo;

	public TodoStyleModel() {
		nombre = new SimpleStringProperty(this, "nombre");
		base1 = new SimpleObjectProperty<>(this, "base1");
		base2 = new SimpleObjectProperty<>(this, "base2");
		texto1 = new SimpleObjectProperty<>(this, "texto1");
		texto2 = new SimpleObjectProperty<>(this, "texto2");
		fondo = new SimpleObjectProperty<>(this, "fondo");
		
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
	

	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public ObjectProperty<Color> base1Property() {
		return this.base1;
	}
	

	public Color getBase1() {
		return this.base1Property().get();
	}
	

	public void setBase1( Color base1) {
		this.base1Property().set(base1);
	}
	

	public ObjectProperty<Color> base2Property() {
		return this.base2;
	}
	

	public Color getBase2() {
		return this.base2Property().get();
	}
	

	public void setBase2(final Color base2) {
		this.base2Property().set(base2);
	}
	

	public ObjectProperty<Color> texto1Property() {
		return this.texto1;
	}
	

	public Color getTexto1() {
		return this.texto1Property().get();
	}
	

	public void setTexto1(final Color texto1) {
		this.texto1Property().set(texto1);
	}
	

	public ObjectProperty<Color> texto2Property() {
		return this.texto2;
	}
	

	public Color getTexto2() {
		return this.texto2Property().get();
	}
	

	public void setTexto2(final Color texto2) {
		this.texto2Property().set(texto2);
	}
	

	public ObjectProperty<Color> fondoProperty() {
		return this.fondo;
	}
	

	public Color getFondo() {
		return this.fondoProperty().get();
	}
	

	public void setFondo(final Color fondo) {
		this.fondoProperty().set(fondo);
	}
	
	
	@Override
	public String toString() {
		
		return getNombre();
		
	}
	
 public String 	toCssHeader(){
	 String cssHeader ="*{\n"+
				"/*:"+nombre.get()+";*/\n"+
				"-fx-base1:"+toRGBCode(getBase1())+";\n"+
				"-fx-base2:"+toRGBCode(getBase2())+";\n"+
				"-fx-color-texto:"+toRGBCode(getTexto1())+";\n"+
				"-fx-color-texto-headers:"+toRGBCode(getTexto2())+";\n"+
				"-fx-color-fondo:"+toRGBCode(getFondo())+";\n"+
				"}\n";
			return cssHeader;
	 
 }
	
	    public  String toRGBCode( Color color )
	    {
	        return String.format( "#%02X%02X%02X",
	            (int)( color.getRed() * 255 ),
	            (int)( color.getGreen() * 255 ),
	            (int)( color.getBlue() * 255 ) );
	    }
	    
	    
	
}
