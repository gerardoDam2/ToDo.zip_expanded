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
import java.nio.file.OpenOption;
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

	//TODO TESTEANDO
	private ObjectProperty<Color> base1;
	private ObjectProperty<Color> base2;
	private ObjectProperty<Color> texto1;
	private ObjectProperty<Color> texto2;
	private ObjectProperty<Color> fondo;
	
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
		defaultProperties.put("crear", "n");
		defaultProperties.put("editar", "e");
		defaultProperties.put("borrar", "d");
		defaultProperties.put("ubicacion", "m");
	
		
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
		 
		 //TODO TESTEANDO
		 
		 base1=new SimpleObjectProperty<>(this,"base1",currentStyle.get().base1.get());
		 base2=new SimpleObjectProperty<>(this,"base2",currentStyle.get().base2.get());
		 texto1=new SimpleObjectProperty<>(this,"texto1",currentStyle.get().texto1.get());
		 texto2=new SimpleObjectProperty<>(this,"texto2",currentStyle.get().texto2.get());
		 fondo=new SimpleObjectProperty<>(this,"fondo",currentStyle.get().fondo.get());
		 
		 
		 base1.addListener((a,b,c)->onAnyColorChange(c));
		 base2.addListener((a,b,c)->onAnyColorChange(c));
		 texto1.addListener((a,b,c)->onAnyColorChange(c));
		 texto2.addListener((a,b,c)->onAnyColorChange(c));
		 fondo.addListener((a,b,c)->onAnyColorChange(c));
		 
	}
	
	private void onAnyColorChange(Color c) {
	
		currentStyle.get().setBase1(base1.get());
		currentStyle.get().setBase2(base2.get());
		currentStyle.get().setTexto1(texto1.get());
		currentStyle.get().setTexto2(texto2.get());
		currentStyle.get().setFondo(fondo.get());
		onCurrentStyleChange();
	}

	public void onCurrentStyleChange() {
		try {
			
			base1.set(currentStyle.get().getBase1());
			base2.set(currentStyle.get().getBase2());
			texto1.set(currentStyle.get().getTexto1());
			texto2.set(currentStyle.get().getTexto2());
			fondo.set(currentStyle.get().getFondo());
			
			
			consumidoresDeStyle.forEach(a->{
				a.getStylesheets().clear();
			});
			guardarCss();
			consumidoresDeStyle.forEach(a->{
				a.getStylesheets().add(this.getClass().getResource(currentStyle.get().getFileName()).toExternalForm());
			});
		} catch (URISyntaxException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
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

	public void guardarCss() throws URISyntaxException, IOException {
//		Task<Void> gurdarTask = new Task<Void>() {
//			@Override
//			protected Void call() throws Exception {
//				return null;
//			}
//		};
//		new Thread(gurdarTask).start();
		System.out.println("guardando css");
		TodoStyleModel cssObject = currentStyle.get();
		Path pathBody = Paths.get(getClass().getResource("cssbody").toURI());
		List<String> cssBody = Files.readAllLines(pathBody);
		cssBody.add(0,currentStyle.get().toCssHeader());
		Path pathDestino = Paths.get(getClass().getResource(currentStyle.get().getFileName()).toURI());
		
		Files.write(pathDestino,cssBody,Charset.forName("UTF-8"));
	}
	
	

	public  void ocupatedelCssPorMi(Parent p){
		consumidoresDeStyle.add(p);
	
		System.out.println(currentStyle.get().getFileName());
		p.getStylesheets().setAll(this.getClass().getResource(currentStyle.get().getFileName()).toExternalForm());
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

	public ObjectProperty<Color> base1Property() {
		return this.base1;
	}
	

	public Color getBase1() {
		return this.base1Property().get();
	}
	

	public void setBase1(final Color base1) {
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
	
	
	
	
	

	


	
	
}
