package dad.todo.ui.gestor_propiedades;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;
import java.util.Scanner;

import com.sun.net.httpserver.Filter;

import javafx.beans.property.ListProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.stage.Screen;

public class GestorDePropiedades {
	
	Properties propiedades;
	HashMap<String, String> defaultProperties;
	private String pathFile;
	private FileInputStream input;
	private FileOutputStream output;
	private  ObjectProperty<TodoStyleModel> currentStyle;
	
	
	private  ArrayList<Parent>consumidoresDeStyle;
	
	private ListProperty<TodoStyleModel> hojasEstilo;
	
	
	public GestorDePropiedades() {
		consumidoresDeStyle=new ArrayList<>();
		hojasEstilo= new SimpleListProperty<>(this,"hojasEstilo",FXCollections.observableArrayList());
		currentStyle = new SimpleObjectProperty<>(this,"currentStyle");
		currentStyle.addListener(e->onCurrentStyleChange());
		
		Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
		pathFile=System.getProperty("user.home")+"/ToDoConfig.properties";
		
		defaultProperties=new HashMap<>();
		
		defaultProperties.put("stageWidth", primaryScreenBounds.getWidth()+"");
		defaultProperties.put("stageHeight",""+primaryScreenBounds.getHeight());
		defaultProperties.put("stageX", ""+primaryScreenBounds.getMinX());
		defaultProperties.put("stageY", ""+primaryScreenBounds.getMinY());
		defaultProperties.put("style", "light");
		defaultProperties.put("CalendarPosition", "left");
	
		
		propiedades = new Properties();
		
		
			try {
				input = new FileInputStream(pathFile);
				propiedades.load(input); // si el fichero existe cargo sus valores
			} catch (IOException e) {
				defaultProperties.forEach( (k,v)-> {
					propiedades.put(k,v);
				}); // si no existe cargo los valores por defecto
			}
			
		 cargarHojasDeEstilo();
		 
		
			
	}
	
	private void onCurrentStyleChange() {
		consumidoresDeStyle.forEach(a->{
			a.getStylesheets().clear();
			a.getStylesheets().add(this.getClass().getResource(currentStyle.get().getFileName()).toExternalForm());
		});
	}

	private void cargarHojasDeEstilo() {
		try {
			File f = new File(getClass().getResource(".").toURI());
			File[] ficheros = f.listFiles();
			for (File file : ficheros) {
				if (file.getName().endsWith("css")) {
					TodoStyleModel style = new TodoStyleModel();
					style.setFileName(file.getName());
					Scanner sc = new Scanner(file);
					ArrayList<String> valores = new ArrayList<>();
					sc.nextLine();
					for (int i = 0; i < 6; i++) {
						String cadena = sc.nextLine();
						cadena=cadena.substring(cadena.indexOf(":")+1,cadena.indexOf(";"));
						valores.add(cadena);
					}
					style.setNombre(valores.get(0));
					style.setBase1(valores.get(1));
					style.setBase2(valores.get(2));
					style.setTexto1(valores.get(3));
					style.setTexto2(valores.get(4));
					style.setFondo(valores.get(5));
					hojasEstilo.add(style);
				}
			}
			hojasEstilo.stream().filter(p->p.getFileName().equals(propiedades.getProperty("style"))).forEach(p->currentStyle.set(p));
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public  Properties getPropiedades() {
		return propiedades;
	}
	
	public void guardarPropiedades() {
		
		try {
			propiedades.put("style", currentStyle.get().getFileName());
			output = new FileOutputStream(pathFile);
			propiedades.store(output, "Borrar este fichero en caso de querer restaurar a la configuración por defecto");
		} catch ( IOException e) {
			e.printStackTrace();
		}


	}

	
	

	public  void ocupatedelCssPorMi(Parent p){
		consumidoresDeStyle.add(p);
		p.getStylesheets().clear();
		System.out.println(currentStyle.get().getFileName());
		p.getStylesheets().add(this.getClass().getResource(currentStyle.get().getFileName()).toExternalForm());
	}
	
	

	


	
	
}
