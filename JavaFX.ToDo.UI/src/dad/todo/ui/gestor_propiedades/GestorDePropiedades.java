package dad.todo.ui.gestor_propiedades;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.paint.Color;
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
		 hojasEstilo.stream().filter(p->p.getFileName().equals(propiedades.getProperty("style"))).forEach(p->currentStyle.set(p));
		 System.out.println(hojasEstilo.size());
		System.out.println("Tema actual------------------------" );
		System.out.println(currentStyle.get());
		
		
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
					System.out.println("archivo >"+file.getName());
					Scanner sc = new Scanner(file,"UTF8");
					ArrayList<String> valores = new ArrayList<>();
					sc.nextLine();
					for (int i = 0; i < 6; i++) {
						String cadena = sc.nextLine();
						cadena=cadena.substring(cadena.indexOf(":")+1,cadena.indexOf(";"));
						System.out.println("cadena "+cadena);
						valores.add(cadena);
					}
					sc.close();
					style.setNombre(valores.get(0));
					style.setBase1(Color.valueOf(valores.get(1)));
					style.setBase2(Color.valueOf(valores.get(2)));
					style.setTexto1(Color.valueOf(valores.get(3)));
					style.setTexto2(Color.valueOf(valores.get(4)));
					style.setFondo(Color.valueOf(valores.get(5)));
					System.out.println("----"+style.getBase1().hashCode());
					hojasEstilo.add(style);
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		}

	public  Properties getPropiedades() {
		return propiedades;
	}
	
	public void guardarPropiedades() {
		
		try {
			guardarCss();
			propiedades.put("style", currentStyle.get().getFileName());
			output = new FileOutputStream(pathFile);
			propiedades.store(output, "Borrar este fichero en caso de querer restaurar a la configuración por defecto");
		} catch ( IOException | URISyntaxException e) {
			e.printStackTrace();
		}


	}

	private void guardarCss() throws URISyntaxException, IOException {
//		Task<Void> gurdarTask = new Task<Void>() {
//			@Override
//			protected Void call() throws Exception {
//				return null;
//			}
//		};
//		new Thread(gurdarTask).start();
		TodoStyleModel cssObject = currentStyle.get();
		Path pathBody = Paths.get(getClass().getResource("cssbody").toURI());
		List<String> cssBody = Files.readAllLines(pathBody);
		System.out.println("CSS BODY -----------");
		cssBody.forEach(f-> {System.out.println(f);});
		System.out.println("imprimiendo objeto css");
		System.out.println(currentStyle.get().toString());
		cssBody.add(0,currentStyle.get().toString());
		Path pathDestino = Paths.get(getClass().getResource(currentStyle.get().getFileName()).toURI());
		
		Files.write(pathDestino,cssBody,Charset.forName("UTF-8"));
	}
	
	

	public  void ocupatedelCssPorMi(Parent p){
		consumidoresDeStyle.add(p);
		p.getStylesheets().clear();
		System.out.println(currentStyle.get().getFileName());
		p.getStylesheets().add(this.getClass().getResource(currentStyle.get().getFileName()).toExternalForm());
	}

	public ObjectProperty<TodoStyleModel> currentStyleProperty() {
		return this.currentStyle;
	}
	

	public TodoStyleModel getCurrentStyle() {
		return this.currentStyleProperty().get();
	}
	

	public void setCurrentStyle(final TodoStyleModel currentStyle) {
		this.currentStyleProperty().set(currentStyle);
	}

	public ListProperty<TodoStyleModel> hojasEstiloProperty() {
		return this.hojasEstilo;
	}
	

	public ObservableList<TodoStyleModel> getHojasEstilo() {
	return this.hojasEstiloProperty().get();
	}
	

	public  void setHojasEstilo(ObservableList<TodoStyleModel> hojasEstilo) {
	this.hojasEstiloProperty().set(hojasEstilo);
	}
	
	
	
	

	


	
	
}
