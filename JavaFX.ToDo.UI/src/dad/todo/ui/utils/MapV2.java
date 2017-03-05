package dad.todo.ui.utils;

import java.awt.geom.Point2D;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ResourceBundle;


import com.lynden.gmapsfx.javascript.object.LatLong;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import maps.java.Geocoding;

//TODO LIMPIAR ESTA MIERDA DE CODIGO
public  class MapV2 extends StackPane implements Initializable {

	@FXML
	private BorderPane mapContainer;

	@FXML
	private TextField lugarSearchTextField;

	private GoogleMapsComponent mapa;

	private Geocoding ObjGeocod;

	private StringProperty direccion;
	private DoubleProperty latitud;
	private DoubleProperty longitud;

	public MapV2() {

		direccion = new SimpleStringProperty(this, "direccion","sin dirección");
		latitud = new SimpleDoubleProperty(this, "latitud");
		longitud = new SimpleDoubleProperty(this, "longitud");

		FXMLLoader loader = new FXMLLoader(getClass().getResource("MapV2View.fxml"));
		ObjGeocod = new Geocoding();
		loader.setController(this);
		loader.setRoot(this);
		try {
			loader.load();
		} catch (IOException e) {

		}
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		mapa = new GoogleMapsComponent() {

			@Override
			public void onMarcaChange(String dirParam) {
				if (dirParam.trim().length()==0) {
					return;
				}
				// TODO Auto-generated method stub
				lugarSearchTextField.setText(dirParam);
				mapa.renombrarMarca(dirParam);
				direccion.set(dirParam);
				Point2D.Double resultadoCD;
				try {
					resultadoCD = ObjGeocod.getCoordinates(dirParam);
					LatLong ll = new LatLong(resultadoCD.getX(), resultadoCD.getY());
					latitud.set(ll.getLatitude());
					longitud.set(ll.getLongitude());

				} catch (UnsupportedEncodingException e) {
				} catch (MalformedURLException e) {
				}finally {
				}

				
			}
		};
		lugarSearchTextField.setText("sin dirección");

		mapContainer.setFocusTraversable(false);
		mapContainer.setCenter(mapa);
//		mapa.prueba(new LatLong(0,0));
	}

	@FXML
	void onSearchKeyPressed(ActionEvent event) {
		String newValue = lugarSearchTextField.getText();
		if (newValue.trim().length() != 0) {
			try {
				Point2D.Double resultadoCD = ObjGeocod.getCoordinates(newValue);

				LatLong ll = new LatLong(resultadoCD.getX(), resultadoCD.getY());
				latitud.set(ll.getLatitude());
				longitud.set(ll.getLongitude());
				mapa.prueba(ll);
				direccion.set(ObjGeocod.getAddressFound());
				mapa.renombrarMarca(direccion.get());
			} catch (Exception e) {
			}

		}
	}

	public StringProperty direccionProperty() {
		return this.direccion;
	}

	public String getDireccion() {
		return this.direccionProperty().get();
	}

	public void setDireccion(final String direccion) {
		this.direccionProperty().set(direccion);
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
	
	public void clear() {
		
	
		try {
			direccion.set("sin dirección");
			latitud.set(0);
			longitud.set(0);
			mapa.prueba(new LatLong(0,0));
			lugarSearchTextField.textProperty().set(direccion.get());
			mapa.renombrarMarca(direccion.get());
		} catch (Exception e) {

		}
	}

	public void setLugar(String descripccion, double latitud2, double longitud2) {
		direccion.set(descripccion);
		LatLong ll = new LatLong(latitud2,longitud2);
		mapa.prueba(ll);
		latitud.set(ll.getLatitude());
		longitud.set(ll.getLongitude());
		mapa.renombrarMarca(direccion.get());
		lugarSearchTextField.setText(direccion.get());
	}
	
	public TextField getLugarSearchTextField() {
		return lugarSearchTextField;
	}

	public GoogleMapsComponent getMapa() {
		return mapa;
	}
	
	public void setMapa(GoogleMapsComponent mapa) {
		this.mapa = mapa;
	}
	
	
}
