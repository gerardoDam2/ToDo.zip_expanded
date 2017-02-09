package dad.todo.ui.utils;

import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ResourceBundle;

import org.controlsfx.control.textfield.TextFields;

import com.lynden.gmapsfx.javascript.object.LatLong;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import jdk.nashorn.internal.codegen.ObjectClassGenerator;
import maps.java.Geocoding;

//TODO LIMPIAR ESTA MIERDA DE CODIGO
public class MapV2 extends StackPane implements Initializable {

	@FXML
	private BorderPane mapContainer;

	@FXML
	private TextField lugarSearchTextField;

	private GoogleMapsComponent mapa;

	private Geocoding ObjGeocod;

	private String direccionActual;

	public MapV2() {
		
		FXMLLoader loader = new FXMLLoader(getClass().getResource("MapV2View.fxml"));
		ObjGeocod= new Geocoding();
		loader.setController(this);
		loader.setRoot(this);
		try {
			loader.load();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		mapa = new GoogleMapsComponent() {
			
			@Override
			public void onMarcaChange(String string) {
				// TODO Auto-generated method stub
				lugarSearchTextField.setText(string);
				mapa.renombrarMarca(string);
				
				
			}
		};
		mapContainer.setCenter(mapa);

		lugarSearchTextField.textProperty().addListener((obs, oValue, nValue) -> onLugarSearch(nValue));

	}

	public void onLugarSearch(String nValue) {
		System.out.println("intentando cambiar");
	}

	@FXML
	    void onSearchKeyPressed(ActionEvent event)  {
		String newValue = lugarSearchTextField.getText();
		if (newValue.trim().length()!=0) {
			try {
				Point2D.Double resultadoCD = ObjGeocod.getCoordinates(newValue);
				
				LatLong ll = new LatLong(resultadoCD.getX(),resultadoCD.getY());
			
				System.out.println(ll);
				 mapa.prueba(ll);
				 direccionActual = ObjGeocod.getAddressFound();
				 mapa.renombrarMarca(direccionActual);
			} catch (UnsupportedEncodingException | MalformedURLException e) {
				e.printStackTrace();
			}
			 
		}
	
		 
	    }

}
