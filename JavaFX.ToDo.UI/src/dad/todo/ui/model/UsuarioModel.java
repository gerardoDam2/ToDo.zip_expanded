package dad.todo.ui.model;


import dad.todo.services.ServiceException;
import dad.todo.services.ServiceFactory;
import dad.todo.services.UsuariosService;
import dad.todo.services.items.UsuarioItem;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class UsuarioModel {
	
	
	
	private StringProperty username;
	private StringProperty email;
	private StringProperty nombre;

	
	public UsuarioModel() {
		username = new SimpleStringProperty(this,"username");
		email = new SimpleStringProperty(this,"email");
		nombre= new SimpleStringProperty(this,"nombre");
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
	
	

	public void updateInfo() {
		UsuariosService uServ = ServiceFactory.getUsuariosService();
		
	try {
		UsuarioItem user = uServ.getLogueado();
		username.set(user.getUsername());
		nombre.set(user.getNombre());
		email.set(user.getEmail());
	} catch (ServiceException e) {
		e.printStackTrace();
	}
		
	}
	

	
	

}
