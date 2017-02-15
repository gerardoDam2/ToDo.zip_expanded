package dad.todo.ui.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.lynden.gmapsfx.GoogleMapView;
import com.lynden.gmapsfx.MapComponentInitializedListener;
import com.lynden.gmapsfx.javascript.event.UIEventType;
import com.lynden.gmapsfx.javascript.object.GoogleMap;
import com.lynden.gmapsfx.javascript.object.LatLong;
import com.lynden.gmapsfx.javascript.object.MapOptions;
import com.lynden.gmapsfx.javascript.object.MapTypeIdEnum;
import com.lynden.gmapsfx.javascript.object.Marker;
import com.lynden.gmapsfx.javascript.object.MarkerOptions;

import netscape.javascript.JSObject;
//TODO LIMPIAR ESTA MIERDA DE CODIGO
public abstract class GoogleMapsComponent extends GoogleMapView implements MapComponentInitializedListener {

	private GoogleMap map;
	private double latitud = 0;
	private double longitud =0;
	
	private boolean editable;
	private Marker marca;

	
	public GoogleMapsComponent() {
		this.editable = true;
	    this.addMapInializedListener(this);
	}
	
	public void mapInitialized() {
	    //Set the initial properties of the map.
	    MapOptions mapOptions = new MapOptions();
	    

	    mapOptions.center(new LatLong(latitud, longitud))
	            .mapType(MapTypeIdEnum.ROADMAP)
	            .overviewMapControl(false)
	            .panControl(false)
	            .rotateControl(false)
	            .scaleControl(false)
	            .streetViewControl(false)
	            .zoomControl(false)
	            .zoom(10);
	    
	    map = this.createMap(mapOptions);

	    //Add a marker to the map
	    MarkerOptions markerOptions = new MarkerOptions();

	    markerOptions.position( new LatLong(latitud, longitud) )
	                .visible(Boolean.TRUE)
	                .title("Sin dirección");

	     marca = new Marker( markerOptions );
	    
	    map.addUIEventHandler(UIEventType.click, (JSObject obj) -> {
	    	
	    	if (editable) {
		    	LatLong ll = new LatLong((JSObject) obj.getMember("latLng"));
	
		    	latitud = ll.getLatitude();
		    	longitud = ll.getLongitude();
		    	
		    	
		    	
		    	marca.setPosition(ll);
		    	onMarcaChange(getAdress());
		    	map.setZoom(map.getZoom()+1);
		    	map.setZoom(map.getZoom()-1);
		    	

	    	}
	    });
	    
	    map.addMarker(marca);
	}
	
	public abstract void onMarcaChange(String string);

	public void prueba (LatLong latLong ){
		marca.setPosition(latLong);
    	int currentZoom = map.getZoom();
    	map.setZoom( currentZoom - 1 );
    	map.setZoom( currentZoom );
    	map.setCenter(latLong);
    	map.setZoom(18);
	}
	
	public void setEditable(boolean editable) {
		this.editable = editable;
	}
	
	public boolean isEditable() {
		return editable;
	}
	
	public void setCoordenadas(double altitud, double longitud){
		this.latitud = altitud;
		this.longitud = longitud;		
	}
	

	public double [] getCoordenadas(){
		double [] coordenadas = {latitud, longitud};
		return coordenadas;
	}
	
	public String getAdress(){
		String calle = "";
    	String url= "http://maps.googleapis.com/maps/api/geocode/json?latlng="+latitud+","+longitud+"&sensor=true";
    	
	    try {
			JSONObject json = readJsonFromUrl(url);
			JSONArray params = json.getJSONArray("results");
			JSONObject param1 = params.getJSONObject(0);
			calle = param1.getString("formatted_address");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
		}
		return calle;
	}
	
	private static String readAll(Reader rd) throws IOException {
	    StringBuilder sb = new StringBuilder();
	    int cp;
	    while ((cp = rd.read()) != -1) {
	      sb.append((char) cp);
	    }
	    return sb.toString();
	  }

	 private static JSONObject readJsonFromUrl(String url) throws IOException, JSONException {
	    InputStream is = new URL(url).openStream();
	    try {
	      BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
	      String jsonText = readAll(rd);
	      JSONObject json = new JSONObject(jsonText);
	      return json;
	    } finally {
	      is.close();
	    }
	  }
	 
	public void renombrarMarca (String title){
		marca.setTitle(title);
	}
	
	

}
