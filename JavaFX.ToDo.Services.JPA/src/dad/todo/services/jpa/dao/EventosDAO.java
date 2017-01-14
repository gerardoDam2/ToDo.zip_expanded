package dad.todo.services.jpa.dao;

import java.util.function.Predicate;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import dad.todo.services.ServiceException;
import dad.todo.services.items.EventoItem;
import dad.todo.services.items.UsuarioItem;
import dad.todo.services.jpa.entities.Evento;
import dad.todo.services.jpa.entities.Lugar;
import dad.todo.services.jpa.entities.Usuario;
import dad.todo.services.jpa.utils.JPAUtil;

public class EventosDAO {

	public  void crear(UsuarioItem usuario, Evento evento) throws ServiceException {
		EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
		try {
			em.getTransaction().begin();
				//TODO comprobar sin con hacer un fromItem es suficiente
				Usuario usuarioEntiy = em.find(Usuario.class, usuario.getUsername());
				evento.setUsuario(usuarioEntiy);
				usuarioEntiy.getEventos().add(evento);
				em.persist(usuarioEntiy);
			em.getTransaction().commit();
		} catch (Exception e) {
			throw new ServiceException("error al crear evento",e);
		}finally {
			em.close();
		}	
			
	}

	public void deleteById(UsuarioItem usuario, Long id) throws ServiceException {

		EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
		try {
			em.getTransaction().begin();
				Usuario usuarioEntity = em.find(Usuario.class, usuario.getUsername());
		        Predicate<Evento> eventoPredicate = p-> p.getId()==id;
		        usuarioEntity.getEventos().removeIf(eventoPredicate);
				em.persist(usuarioEntity);
			em.getTransaction().commit();
		} catch (Exception e) {
			throw new ServiceException("error al borrar evento",e);
		}finally {
			em.close();
		}	
		
	}

	public void updateEvento(UsuarioItem usuario, Evento evento)throws ServiceException {
		
		EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
		try {
			em.getTransaction().begin();
				Usuario usuarioEntiy = em.find(Usuario.class, usuario.getUsername());
				usuarioEntiy.getEventos().stream()
				.filter(eventoEntity-> eventoEntity.getId()==evento.getId())
				.findAny()
				.ifPresent(eventoEntity-> {
					eventoEntity.setDescripcion(evento.getDescripcion());
					eventoEntity.setDuracion(evento.getDuracion());
					eventoEntity.setFecha(evento.getFecha());
					eventoEntity.setId(evento.getId());
					Lugar lugar=eventoEntity.getLugar();
					lugar.setDescripcion(evento.getLugar().getDescripcion());
					lugar.setLatitud(evento.getLugar().getLatitud());
					lugar.setLongitud(evento.getLugar().getLongitud());
					eventoEntity.setRealizado(evento.getRealizado());
					eventoEntity.setTitulo(evento.getTitulo());
				});
				em.persist(usuarioEntiy);
			em.getTransaction().commit();
		} catch (Exception e) {
			throw new ServiceException("error al actualizar evento",e);
		}finally {
			em.close();
		}	
		
	}

	public EventoItem findById(UsuarioItem usuario, Long id) throws ServiceException {
		EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
		EventoItem eventoItem=null;
		try {
				 TypedQuery<Evento> query = em.createQuery(
					        "SELECT e FROM Evento e WHERE e.id = :id AND usuario_username = :username ", Evento.class);
				 			query.setParameter("id", id);
				 			query.setParameter("username", usuario.getUsername());
				 Evento eventoEntity = query.getSingleResult();
				 eventoItem=eventoEntity.toItem();
		} catch (Exception e) {
			throw new ServiceException("no se encontro el evento",e);
		}finally {
			em.close();
		}	
		return eventoItem;
		
	}
	
	

}
