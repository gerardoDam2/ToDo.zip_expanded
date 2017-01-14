package dad.todo.services;

import dad.todo.services.items.UsuarioItem;

public interface UsuariosService {
	
	// m�todos que podemos invocar sin estar logueados
	public boolean login(String username, String password) throws ServiceException;
	public void alta(UsuarioItem usuario) throws ServiceException;
	public void recuperarPassword(String email) throws ServiceException;
	
	// m�todos que podemos invocar s�lo estando logueados
	public void baja() throws ServiceException;
	public UsuarioItem getLogueado() throws ServiceException;
	public void cambiarPassword(String oldPassword, String newPassword) throws ServiceException;
	public void actualizar(UsuarioItem usuario) throws ServiceException;
	
}
