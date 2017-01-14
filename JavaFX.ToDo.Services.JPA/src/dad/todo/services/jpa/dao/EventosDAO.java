package dad.todo.services.jpa.dao;

import java.util.function.Predicate;

import javax.persistence.EntityManager;

import dad.todo.services.ServiceException;
import dad.todo.services.items.EventoItem;
import dad.todo.services.items.UsuarioItem;
import dad.todo.services.jpa.entities.Evento;
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

}
