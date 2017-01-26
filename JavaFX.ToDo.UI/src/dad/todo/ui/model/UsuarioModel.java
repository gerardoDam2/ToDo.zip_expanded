package dad.todo.ui.model;

import javafx.beans.property.ListProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;

public class UsuarioModel {
	
	private StringProperty username;
	private StringProperty email;
	private StringProperty nombre;
	private ListProperty<EventosModel> eventos;
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
	

	
	

}
