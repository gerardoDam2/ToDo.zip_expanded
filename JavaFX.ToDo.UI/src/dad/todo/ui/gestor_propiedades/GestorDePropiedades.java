package dad.todo.ui.gestor_propiedades;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;
import java.util.stream.Stream;

import org.apache.commons.collections.set.SynchronizedSet;

import com.sun.glass.ui.GestureSupport;
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
import net.sf.jasperreports.export.parameters.ParametersHtmlExporterOutput;

public class GestorDePropiedades {
	
	Properties propiedades;
	HashMap<String, String> defaultProperties;
	private String pathFile;
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
	private String directorioTodo;
	private boolean flag;
	
	public GestorDePropiedades() {
		
		consumidoresDeStyle=new ArrayList<>();
		hojasEstilo= new SimpleListProperty<>(this,"hojasEstilo",FXCollections.observableArrayList());
		currentStyle = new SimpleObjectProperty<>(this,"currentStyle");
		currentStyle.addListener(e->onCurrentStyleChange());
		
		Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
		directorioTodo=System.getProperty("user.home")+File.separator+"ToDo"+File.separator;
		
		//TODO
		System.err.println("dir todo " +directorioTodo);
		pathFile=directorioTodo+"ToDoConfig.properties";
		System.out.println("properti dir " +pathFile);
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
				FileInputStream input = new FileInputStream(pathFile);
				propiedades.load(input); // si el fichero existe cargo sus valores
			} catch (IOException e) {
				propiedades.putAll(defaultProperties);
			}
			
			System.out.println(propiedades.getProperty("style"));
			
		 cargarHojasDeEstilo();
	
		 hojasEstilo.stream().filter(p->p.getFileName().equals(propiedades.getProperty("style"))).forEach(p->currentStyle.set(p));
		 System.out.println("EL ESTILO ACTUAL ES");
		 System.out.println(currentStyle.get().getNombre());
		 System.out.println(hojasEstilo.size());
		 //TODO TESTEANDO
		 
		 base1=new SimpleObjectProperty<>(this,"base1",currentStyle.get().base1.getValue());
		 base2=new SimpleObjectProperty<>(this,"base2",currentStyle.get().base2.getValue());
		 texto1=new SimpleObjectProperty<>(this,"texto1",currentStyle.get().texto1.getValue());
		 texto2=new SimpleObjectProperty<>(this,"texto2",currentStyle.get().texto2.getValue());
		 fondo=new SimpleObjectProperty<>(this,"fondo",currentStyle.get().fondo.getValue());
		 
		 
		 base1.addListener((a,b,c)->onAnyColorChange(c));
		 base2.addListener((a,b,c)->onAnyColorChange(c));
		 texto1.addListener((a,b,c)->onAnyColorChange(c));
		 texto2.addListener((a,b,c)->onAnyColorChange(c));
		 fondo.addListener((a,b,c)->onAnyColorChange(c));
		 
	}
	
	private void onAnyColorChange(Color c) {
		System.out.println("un color ha cambiado");
		currentStyle.get().setBase1(base1.getValue());
		currentStyle.get().setBase2(base2.getValue());
		currentStyle.get().setTexto1(texto1.getValue());
		currentStyle.get().setTexto2(texto2.getValue());
		currentStyle.get().setFondo(fondo.getValue());
		onCurrentStyleChange();
	}

	public void onCurrentStyleChange() {
		try {
			
//			base1.set(currentStyle.get().getBase1());
//			base2.set(currentStyle.get().getBase2());
//			texto1.set(currentStyle.get().getTexto1());
//			texto2.set(currentStyle.get().getTexto2());
//			fondo.set(currentStyle.get().getFondo());
			
//			base1.setValue(currentStyle.get().getBase1());
//			base2.setValue(currentStyle.get().getBase2());
//			texto1.setValue(currentStyle.get().getTexto1());
//			texto2.setValue(currentStyle.get().getTexto2());
//			fondo.setValue(currentStyle.get().getFondo());	
			
			
			
			//TODO se puede quitar y usar setall
			consumidoresDeStyle.forEach(a->{
				a.getStylesheets().clear();
			});
			
			guardarCss();// es el que hace que se refresque el css
			
			
			//una vez guardado les agrego el estilo
			consumidoresDeStyle.forEach(a->{
				a.getStylesheets().addAll("file:///"+currentStyle.get().getPath().toString().replace("\\", "/"));
			});
			
		
		} catch (URISyntaxException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	private void cargarHojasDeEstilo() {
			Stream<Path> ficheros = null;
			try {
				ficheros = Files.list(Paths.get(directorioTodo));
				
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			
			ficheros.filter(f->f.getFileName().toString().endsWith(".css")).forEach(f->{
				TodoStyleModel style = new TodoStyleModel();
				style.setFileName(f.getFileName().toString());
				style.setPath(f);
				
			
				//TODO USAR READER

				System.out.println("----------------------------------------------------"+f.getFileName());
				try {
					List<String> hojaCss;
					hojaCss = Files.readAllLines(f);
				int i =0;
				
				String parametro= hojaCss.get(++i);
				style.setNombre(parametro.substring(parametro.indexOf(":")+1,parametro.indexOf(";")));
				
				parametro= hojaCss.get(++i);
				style.setBase1(Color.valueOf(parametro.substring(parametro.indexOf(":")+1,parametro.indexOf(";"))));
				
				parametro= hojaCss.get(++i);
				style.setBase2(Color.valueOf(parametro.substring(parametro.indexOf(":")+1,parametro.indexOf(";"))));
				
				parametro= hojaCss.get(++i);
				style.setTexto1(Color.valueOf(parametro.substring(parametro.indexOf(":")+1,parametro.indexOf(";"))));
				
				parametro= hojaCss.get(++i);
				style.setTexto2(Color.valueOf(parametro.substring(parametro.indexOf(":")+1,parametro.indexOf(";"))));
				
				parametro= hojaCss.get(++i);
				style.setFondo(Color.valueOf(parametro.substring(parametro.indexOf(":")+1,parametro.indexOf(";"))));
				
				
				
				
				//TODO PRUEBA
				System.err.println("comprobando que se crea bien el objecto");
				System.out.println(style.toCssHeader());
				
				hojasEstilo.add(style);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			});
			
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

		TodoStyleModel cssObject = currentStyle.get();
		Path pathBody = Paths.get(getClass().getResource("cssbody").toURI());
		List<String> cssBody = Files.readAllLines(pathBody);
		cssBody.add(0,currentStyle.get().toCssHeader());
		Files.write(currentStyle.get().getPath(),cssBody,Charset.forName("UTF-8"));
	}
	
	

	public  void ocupatedelCssPorMi(Parent p){
		consumidoresDeStyle.add(p);
		p.getStylesheets().setAll("file:///"+currentStyle.get().getPath().toString().replace("\\", "/"));
	}
	
	
public void crearNuevoEstilo(String text)  {
		
		try {
			TodoStyleModel styleT = new TodoStyleModel();
			styleT.setFileName(text + ".css");
			styleT.setNombre(text);
			styleT.setBase1(base1.getValue());
			styleT.setBase2(base2.getValue());
			styleT.setTexto1(texto1.getValue());
			styleT.setTexto2(texto2.getValue());
			styleT.setFondo(fondo.getValue());
			System.out.println("cabecera del nuevo style");
			System.out.println(styleT.toCssHeader());
			styleT.setPath(Paths.get(directorioTodo+styleT.getFileName()));
			
			
			Path pathBody = Paths.get(getClass().getResource("cssbody").toURI());
			
			List<String> cssBody = Files.readAllLines(pathBody);
			cssBody.add(0, styleT.toCssHeader());
		
			
			BufferedWriter bw = Files.newBufferedWriter(styleT.getPath(), Charset.forName("UTF-8"));
			cssBody.forEach(a->{
					try {
						bw.write(a);
						bw.newLine();
					} catch (IOException e) {
						e.printStackTrace();
					}
			});
			bw.flush();
			bw.close();
//			cargarHojasDeEstilo();
			hojasEstilo.add(styleT);
			currentStyle.set(styleT);
			
		} catch (Exception   e) {
		}

		
		
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

	public void cambiarEstilo(TodoStyleModel style) {
		
		List<String> hojaCss = null;
		try {
			hojaCss = Files.readAllLines(style.getPath());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	int i =0;
	
	String parametro= hojaCss.get(++i);
	style.setNombre(parametro.substring(parametro.indexOf(":")+1,parametro.indexOf(";")));
	
	parametro= hojaCss.get(++i);
	style.setBase1(Color.valueOf(parametro.substring(parametro.indexOf(":")+1,parametro.indexOf(";"))));
	
	parametro= hojaCss.get(++i);
	style.setBase2(Color.valueOf(parametro.substring(parametro.indexOf(":")+1,parametro.indexOf(";"))));
	
	parametro= hojaCss.get(++i);
	style.setTexto1(Color.valueOf(parametro.substring(parametro.indexOf(":")+1,parametro.indexOf(";"))));
	
	parametro= hojaCss.get(++i);
	style.setTexto2(Color.valueOf(parametro.substring(parametro.indexOf(":")+1,parametro.indexOf(";"))));
	
	parametro= hojaCss.get(++i);
	style.setFondo(Color.valueOf(parametro.substring(parametro.indexOf(":")+1,parametro.indexOf(";"))));
	currentStyle.set(style);
	
	
	System.out.println("cambiando al nuevo style" );
	System.out.println(style.getPath());
	currentStyle.set(style);
	
//	flag=false;
//	
//	base1.setValue(style.getBase1());
//	base2.setValue(style.getBase2());
//	texto1.setValue(style.getTexto1());
//	texto2.setValue(style.getTexto2());
//	fondo.setValue(style.getFondo());	
//		
//	flag=true;
	
	}

	
	
	
	
	
	

	


	
	
}
