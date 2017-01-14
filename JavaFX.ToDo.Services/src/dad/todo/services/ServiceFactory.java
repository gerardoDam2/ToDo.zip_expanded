package dad.todo.services;

import java.util.ResourceBundle;

public class ServiceFactory {
	private static UsuariosService usuariosService;
	private static EventosService eventosService;
	
	private static final ResourceBundle BUNDLE = ResourceBundle.getBundle("META-INF/services"); 
	
	private static Object instanceService(String propertyName) {
		try {
			Class<?> clazz = Class.forName(BUNDLE.getString(propertyName));
		    return clazz.newInstance();
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
		    throw new RuntimeException("Error al instanciar servicio", e);
		}
	}
	
	public static UsuariosService getUsuariosService() {
		if (usuariosService == null) {
			usuariosService = (UsuariosService) instanceService("service.usuarios");
		}
		return usuariosService;
	}
	
	public static EventosService getEventosService() {
		if (eventosService == null) {
			eventosService = (EventosService) instanceService("service.eventos");
		}
		return eventosService;
	}

}
