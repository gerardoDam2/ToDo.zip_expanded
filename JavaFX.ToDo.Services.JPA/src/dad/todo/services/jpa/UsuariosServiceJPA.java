package dad.todo.services.jpa;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.UUID;

import javax.mail.MessagingException;

import dad.todo.services.ServiceException;
import dad.todo.services.UsuariosService;
import dad.todo.services.items.UsuarioItem;
import dad.todo.services.jpa.dao.UsuarioDAO;
import dad.todo.services.jpa.entities.Usuario;
import dad.todo.services.jpa.utils.EmailUtil;

public class UsuariosServiceJPA implements UsuariosService {
	
	private UsuarioDAO usuarioDao; 
	private Usuario logueado;
	
	public UsuariosServiceJPA() {
		usuarioDao = new UsuarioDAO();
		logueado = null;
	}

	@Override
	public boolean login(String username, String password) throws ServiceException {
		// recuperar el usuario por el "username"
		Usuario usuario = usuarioDao.findByUsername(username);
		if (usuario == null) {
			return false;
		}
		
		// encriptar el password del parámetro
		password = encryptPassword(password);
		
		// comprobar si el password encriptado coincide con el del usuario
		if (password.equals(usuario.getPassword())) {
			logueado = usuario;
			return true;
		}
		
		return false;
	}

	@Override
	public void alta(UsuarioItem usuario) throws ServiceException {
		// TODO validar el usuarioItem (ver si sus campos tienen valores válidos)
		Usuario entity = Usuario.fromItem(usuario);
		entity.setPassword(encryptPassword(usuario.getPassword()));
		usuarioDao.create(entity);
	}

	@Override
	public void recuperarPassword(String email) throws ServiceException {
		Usuario user=usuarioDao.findByEmail(email);
		if (user!=null) {
			String newPass = UUID.randomUUID().toString();
			usuarioDao.setPassword(user,encryptPassword(newPass));
			try {
				EmailUtil.sendEmail(email, "ToDo recovery password ", newPass);
			} catch (MessagingException e) {
				throw new ServiceException("error al enviar el correo",e);
			}
		}
	}

	@Override
	public void baja() throws ServiceException {
		try {
			usuarioDao.delete(logueado);
			logueado=null;
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public UsuarioItem getLogueado() throws ServiceException {
		if (logueado == null) return null;
		return logueado.toItem();
	}

	@Override
	public void cambiarPassword(String oldPassword, String newPassword) throws ServiceException {
		
		if (logueado==null) 
			throw new ServiceException("debe estar logueado para cambiar la contraseña");
		else if (!logueado.getPassword().equals(encryptPassword(oldPassword)))
			// TODO posible vulnerabilidad
			throw new ServiceException("la contraseña actual es erronea");
		else
		usuarioDao.updatePassword(logueado,encryptPassword(newPassword));
	}

	@Override
	public void actualizar(UsuarioItem usuario) throws ServiceException {
		//TODO comprobar que el usuario logueado y el item son iguales?
		usuarioDao.updatePerfil(usuario);
	}
	
	private String encryptPassword(String password) {
		try {
			MessageDigest digest = MessageDigest.getInstance("SHA-256");
			byte[] hash = Base64.getEncoder().encode(digest.digest(password.getBytes(StandardCharsets.UTF_8)));
			return new String(hash);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			throw new RuntimeException("Error encriptando contraseña", e);
		}
	}

}
