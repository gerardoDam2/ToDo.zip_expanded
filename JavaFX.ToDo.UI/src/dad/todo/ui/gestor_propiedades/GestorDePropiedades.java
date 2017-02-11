package dad.todo.ui.gestor_propiedades;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Properties;

import javafx.geometry.Rectangle2D;
import javafx.stage.Screen;

public class GestorDePropiedades {
	
	static Properties propiedades;
	HashMap<String, String> defaultProperties;
	private String pathFile;
	private FileInputStream input;
	private FileOutputStream output;
	
	
	public GestorDePropiedades() {
		
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
		
	}
	
	public static Properties getPropiedades() {
		return propiedades;
	}
	
	public void guardarPropiedades() {
		
		try {
			output = new FileOutputStream(pathFile);
			propiedades.store(output, "Borrar este fichero en caso de querer restaurar a la configuración por defecto");
		} catch ( IOException e) {
			e.printStackTrace();
		}


	}

}
