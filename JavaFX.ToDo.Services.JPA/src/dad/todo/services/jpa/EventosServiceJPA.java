package dad.todo.services.jpa;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;

import dad.todo.services.EventosService;
import dad.todo.services.ServiceException;
import dad.todo.services.ServiceFactory;
import dad.todo.services.items.EventoItem;
import dad.todo.services.items.UsuarioItem;
import dad.todo.services.jpa.dao.EventosDAO;
import dad.todo.services.jpa.entities.Evento;
import dad.todo.services.jpa.entities.Lugar;
import dad.todo.services.jpa.utils.JPAUtil;

public class EventosServiceJPA implements EventosService {
	
	
	private EventosDAO eventosDAO;

	public EventosServiceJPA() {
	 eventosDAO = new EventosDAO();
	}

	@Override
	public void crearEvento(EventoItem evento) throws ServiceException {
		UsuarioItem usuario = ServiceFactory.getUsuariosService().getLogueado();
		Evento eventoEntity = Evento.fromItem(evento);
		eventosDAO.crear(usuario,eventoEntity);
		
	}

	@Override
	public void eliminarEvento(Long id) throws ServiceException {
		UsuarioItem usuario = ServiceFactory.getUsuariosService().getLogueado();
		eventosDAO.deleteById(usuario,id);
	}

//	@Override
//	public void actualizarEvento(EventoItem evento) throws ServiceException {
//		UsuarioItem usuario = ServiceFactory.getUsuariosService().getLogueado();
//		eventosDAO.updateEvento(usuario,Evento.fromItem(evento));
//	}
	@Override
	public void actualizarEvento(EventoItem evento) throws ServiceException {
		
			EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();		
		
		try {
			Evento entity = em.find(Evento.class, evento.getId());
			boolean persist = entity.getLugar() == null;
			entity.setDescripcion(evento.getDescripcion());
			entity.setDuracion(evento.getDuracion());
			entity.setFecha(evento.getFecha());
			entity.setRealizado(evento.getRealizado());
			entity.setTitulo(evento.getTitulo());
			Lugar lugarEntity = Lugar.fromItem(evento.getLugar());
			if(evento.getLugar() != null)
				lugarEntity.setEvento(entity);
			entity.setLugar(lugarEntity);
			
			em.getTransaction().begin();
			
			if (persist) 
				em.persist(entity);
			else
				em.merge(entity);
			
			em.getTransaction().commit();
			
		} catch (Exception e) {
			em.getTransaction().rollback();
			throw new ServiceException("Error  actualizarEvento", e);
		} finally {
		}
		em.close();
		
	}

	@Override
	public EventoItem getEvento(Long id) throws ServiceException {
		UsuarioItem usuario = ServiceFactory.getUsuariosService().getLogueado();
		return eventosDAO.findById(usuario,id);
	}

	@Override
	public List<EventoItem> getEventos() throws ServiceException {
		UsuarioItem usuario = ServiceFactory.getUsuariosService().getLogueado();
		return eventosDAO.getAll(usuario);
	}

	@Override
	public List<EventoItem> buscarEventosPorFecha(Date fecha) throws ServiceException {
		//TODO preguntar a fran el critério 
		UsuarioItem usuario = ServiceFactory.getUsuariosService().getLogueado();
		return eventosDAO.getByFecha(usuario,fecha);
	}

}
