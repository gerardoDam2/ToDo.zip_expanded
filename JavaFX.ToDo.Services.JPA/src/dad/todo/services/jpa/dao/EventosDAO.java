package dad.todo.services.jpa.dao;


import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.Query;
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
			em.getTransaction().rollback();
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
			em.getTransaction().rollback();
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
			em.getTransaction().rollback();
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

	public List<EventoItem> getAll(UsuarioItem usuario) throws ServiceException {
		
		EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
		List<EventoItem> ListaItems=null;
		try {
			 Query query = em.createQuery("FROM Evento  where usuario_username= :username");
			 query.setParameter("username", usuario.getUsername());
			   List<Evento> entityList = (List<Evento>)query.getResultList();
			   
			   ListaItems = entityList.stream().map(evento->evento.toItem()).collect(Collectors.toList());
		} catch (Exception e) {
			throw new ServiceException("no se encontro el evento",e);
		}finally {
			em.close();
		}	
		return ListaItems;
	}

	public List<EventoItem> getByFecha(UsuarioItem usuario, Date fecha) throws ServiceException {
		EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
		List<EventoItem> ListaItems=null;
		try {
			 TypedQuery<Evento> query = em.createQuery("SELECT e FROM Evento e  where e.fecha BETWEEN :start AND :end  AND e.usuario.username=:username",Evento.class);
			 Calendar calendar=Calendar.getInstance();
			 calendar.setTime(fecha);
			 calendar.set(Calendar.HOUR_OF_DAY, 0);
			 calendar.set(Calendar.MINUTE, 0);
			 calendar.set(Calendar.SECOND, 0);
			 Date start = calendar.getTime();
			 calendar.set(Calendar.HOUR_OF_DAY, 23);
			 calendar.set(Calendar.MINUTE, 59);
			 calendar.set(Calendar.SECOND, 59);
			 Date end=calendar.getTime();
			 query.setParameter("start", start);
			 query.setParameter("end", end);
			 query.setParameter("username", usuario.getUsername());
			 List<Evento> entityList = (List<Evento>)query.getResultList();
			 ListaItems = entityList.stream().map(evento->evento.toItem()).collect(Collectors.toList());
			 
		} catch (Exception e) {
			throw new ServiceException("no se encontro el evento",e);
		}finally {
			em.close();
		}	
		return ListaItems;
	}
	
	
	

}
