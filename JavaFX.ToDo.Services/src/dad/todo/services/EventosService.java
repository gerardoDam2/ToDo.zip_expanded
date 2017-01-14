package dad.todo.services;

import java.util.Date;
import java.util.List;

import dad.todo.services.items.EventoItem;

public interface EventosService {
	
	// todos los métodos deben comprobar que hay usuario logueado
	// a través del servicio UsuariosService
	
	public void crearEvento(EventoItem evento) throws ServiceException;
	public void eliminarEvento(Long id) throws ServiceException;
	public void actualizarEvento(EventoItem evento) throws ServiceException;
	public EventoItem getEvento(Long id) throws ServiceException;
	public List<EventoItem> getEventos() throws ServiceException;
	public List<EventoItem> buscarEventosPorFecha(Date fecha) throws ServiceException;

}
