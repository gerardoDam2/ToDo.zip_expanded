package dad.todo.services.jpa.dao;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import dad.todo.services.ServiceException;
import dad.todo.services.items.UsuarioItem;
import dad.todo.services.jpa.entities.Evento;
import dad.todo.services.jpa.entities.Perfil;
import dad.todo.services.jpa.entities.Usuario;
import dad.todo.services.jpa.utils.JPAUtil;

// DAO = Data Access Object
public class UsuarioDAO {
	
	//TODO comprobar el password del usuario en cada operación
	
	public Usuario findByUsername(String username) throws ServiceException {
		Usuario usuario = null;
		EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
		try {
			usuario = em.find(Usuario.class, username);
		} catch (PersistenceException e) {
			throw new ServiceException("No se pudo recuperar el usuario", e);
		} finally {
			em.close();
		}
		return usuario;
	}

	public void create(Usuario usuario) throws ServiceException {
		EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
		try {
			em.getTransaction().begin();
				em.persist(usuario);
			em.getTransaction().commit();
		} catch (PersistenceException e) {
			em.getTransaction().rollback();
			throw new ServiceException("No se pudo dar de alta al usuario", e);
		} finally {
			em.close();
		}		
	}

	public void delete(Usuario logueado) throws ServiceException {
		EntityManager em= JPAUtil.getEntityManagerFactory().createEntityManager();
		
		try {
			em.getTransaction().begin();
				Usuario entity = em.find(Usuario.class, logueado.getUsername());
				em.remove(entity);
			em.getTransaction().commit();
		} catch (Exception e) {
			em.getTransaction().rollback();
			throw new ServiceException("No se pudo eliminar el usuario", e);
		}finally {
			em.close();
		}
	}

	public void updatePassword(Usuario logueado, String newPassword) throws ServiceException {
		EntityManager em= JPAUtil.getEntityManagerFactory().createEntityManager();
		
		try {
			em.getTransaction().begin();
				logueado.setPassword(newPassword);
				em.merge(logueado);
			em.getTransaction().commit();
		} catch (Exception e) {
			em.getTransaction().rollback();
			throw new ServiceException("Error al cambiar la contraseña", e);
		}finally {
			em.close();
		}

	}

	public void updatePerfil(UsuarioItem usuario) throws ServiceException {
		EntityManager em= JPAUtil.getEntityManagerFactory().createEntityManager();
		
		try {
			em.getTransaction().begin();
				Perfil entity = em.find(Perfil.class, usuario.getUsername());
				entity.setNombre(usuario.getNombre());
				entity.setEmail(usuario.getEmail());
				em.persist(entity);
			em.getTransaction().commit();
		} catch (Exception e) {
			em.getTransaction().rollback();
			throw new ServiceException("Error al actualizar perfil", e);
		}finally {
			em.close();
		}

	}

	public  Usuario findByEmail(String email) throws ServiceException {
		Usuario user;
		EntityManager em= JPAUtil.getEntityManagerFactory().createEntityManager();
		try {
			TypedQuery<Usuario>  query= em.createQuery("from Usuario u where u.perfil.email = :correo ", Usuario.class);
			query.setParameter("correo", email);
			user = query.getSingleResult();
		}
		catch ( NoResultException e) {
			return null;
		}
		catch (Exception e) {
			throw new ServiceException("Error al buscar usuario por email", e);
		}
		 return user;
	}

	public void setPassword(Usuario user, String newPass) throws ServiceException {
		EntityManager em= JPAUtil.getEntityManagerFactory().createEntityManager();
		try {
			em.getTransaction().begin();
				user.setPassword(newPass);
				em.merge(user);
			em.getTransaction().commit();
		} catch (Exception e) {
			em.getTransaction().rollback();
			throw new ServiceException("Error al establecer el nuevo password", e);
		}
	}
	
	

}
