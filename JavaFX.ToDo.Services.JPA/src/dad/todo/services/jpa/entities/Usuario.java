package dad.todo.services.jpa.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import dad.todo.services.items.UsuarioItem;

@Entity
@Table(name = "usuarios")
public class Usuario {

	@Id
	private String username;

	private String password;

	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@PrimaryKeyJoinColumn
	private Perfil perfil;
	
	@OneToMany(cascade=CascadeType.ALL ,fetch=FetchType.EAGER,mappedBy="usuario", orphanRemoval = true)
	private List<Evento> eventos = new ArrayList<>();

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Perfil getPerfil() {
		return perfil;
	}

	public void setPerfil(Perfil perfil) {
		this.perfil = perfil;
	}
	
	

	public List<Evento> getEventos() {
		return eventos;
	}

	public void setEventos(List<Evento> eventos) {
		this.eventos = eventos;
	}
	
   

	public static Usuario fromItem(UsuarioItem usuario) {
		Usuario entity = new Usuario();
		entity.setUsername(usuario.getUsername());
//		entity.setPassword(usuario.getPassword());
		entity.setPerfil(new Perfil());
		entity.getPerfil().setEmail(usuario.getEmail());
		entity.getPerfil().setNombre(usuario.getNombre());
		entity.getPerfil().setUsuario(entity);
		return entity;
	}

	public UsuarioItem toItem() {
		UsuarioItem item = new UsuarioItem();
		item.setUsername(getUsername());
		item.setPassword(null);
		item.setNombre(getPerfil().getNombre());
		item.setEmail(getPerfil().getEmail());
		return item;
	}

}
