package dad.todo.ui.model;

import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import dad.todo.services.EventosService;
import dad.todo.services.ServiceException;
import dad.todo.services.ServiceFactory;
import dad.todo.services.UsuariosService;
import dad.todo.services.items.EventoItem;
import dad.todo.services.items.UsuarioItem;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class UsuarioModel {
	
	
	
	private StringProperty username;
	private StringProperty email;
	private StringProperty nombre;
	private ListProperty<EventosModel> eventos;
	
	public UsuarioModel() {
		username = new SimpleStringProperty(this,"username");
		email = new SimpleStringProperty(this,"email");
		nombre= new SimpleStringProperty(this,"nombre");
		eventos= new SimpleListProperty<>(this,"eventos",FXCollections.observableArrayList());
	}
	
	public StringProperty usernameProperty() {
		return this.username;
	}
	
	public String getUsername() {
		return this.usernameProperty().get();
	}
	
	public void setUsername(final String username) {
		this.usernameProperty().set(username);
	}
	
	public StringProperty emailProperty() {
		return this.email;
	}
	
	public String getEmail() {
		return this.emailProperty().get();
	}
	
	public void setEmail(final String email) {
		this.emailProperty().set(email);
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
	
	public ListProperty<EventosModel> eventosProperty() {
		return this.eventos;
	}
	
	public ObservableList<EventosModel> getEventos() {
	return this.eventosProperty().get();
	}

	public void updateInfo() {
		
		UsuariosService uServ = ServiceFactory.getUsuariosService();
		EventosService eServ = ServiceFactory.getEventosService();
		
	try {
		UsuarioItem user = uServ.getLogueado();
		username.set(user.getUsername());
		nombre.set(user.getNombre());
		email.set(user.getEmail());
		
		eventos.clear();
		List<EventoItem> eventosItem = eServ.getEventos();
		//TODO
//		eventos.addAll(eventosItem.stream().map(EventosModel::fromItem).collect(Collectors.toList()));
		
	} catch (ServiceException e) {
		e.printStackTrace();
	}
		
	}
	

	
	

}
