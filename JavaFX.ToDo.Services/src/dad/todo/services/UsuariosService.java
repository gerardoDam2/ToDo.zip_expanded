package dad.todo.services;

import dad.todo.services.items.UsuarioItem;

public interface UsuariosService {
	
	// métodos que podemos invocar sin estar logueados
	public boolean login(String username, String password) throws ServiceException;
	public void alta(UsuarioItem usuario) throws ServiceException;
	public void recuperarPassword(String email) throws ServiceException;
	
	// métodos que podemos invocar sólo estando logueados
	public void baja() throws ServiceException;
	public UsuarioItem getLogueado() throws ServiceException;
	public void cambiarPassword(String oldPassword, String newPassword) throws ServiceException;
	public void actualizar(UsuarioItem usuario) throws ServiceException;
	
}
