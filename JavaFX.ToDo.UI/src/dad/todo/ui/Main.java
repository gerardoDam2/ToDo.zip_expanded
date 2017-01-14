package dad.todo.ui;

import dad.todo.services.ServiceException;
import dad.todo.services.ServiceFactory;
import dad.todo.services.items.EventoItem;
import dad.todo.services.items.LugarItem;
import dad.todo.services.items.UsuarioItem;
import dad.todo.services.jpa.entities.Usuario;
import dad.todo.services.jpa.utils.JPAUtil;
import java.util.Date;

public class Main {

	public static void main(String[] args) throws ServiceException {
		JPAUtil.initEntityManagerFactory("todo");
		
		//TODO ALTA
//		UsuarioItem usuario = new UsuarioItem();
//		usuario.setUsername("fran");
//		usuario.setPassword("1234");
//		usuario.setNombre("Fran Vargas");
//		usuario.setEmail("fran.profe.icm@gmail.com");
//		ServiceFactory.getUsuariosService().alta(usuario);
		
		//TODO LOGUEO
		boolean ok = ServiceFactory.getUsuariosService().login("fran", "1234");
		System.out.println("Usuario válido: " + ok);
		UsuarioItem logueado = ServiceFactory.getUsuariosService().getLogueado();
		System.out.println(logueado.getNombre());
		
		//TODO Baja
//		ServiceFactory.getUsuariosService().baja();
		
//		TODO CREAR EVENTO
//		LugarItem lugar= new LugarItem();
//		lugar.setDescripcion("la cuesta");
//		lugar.setLatitud(20.0);
//		lugar.setLongitud(30.0);
//		EventoItem evento = new EventoItem();
//		evento.setDescripcion("evento test");
//		evento.setDuracion(20L);
//		evento.setFecha(new Date());
//		evento.setLugar(lugar);
//		evento.setRealizado(false);
//		evento.setTitulo("prueba");
//		ServiceFactory.getEventosService().crearEvento(evento);
		
		//TODO BORRAR EVENTO
//		ServiceFactory.getEventosService().eliminarEvento(8L);
		
		//TODO UPDATE EVENTO
//		LugarItem lugar= new LugarItem();
//		lugar.setDescripcion("la cuesta update2");
//		lugar.setLatitud(20.0);
//		lugar.setLongitud(30.0);
//		EventoItem evento = new EventoItem();
//		evento.setDescripcion("evento update2");
//		evento.setDuracion(20L);
//		evento.setFecha(new Date());
//		evento.setLugar(lugar);
//		evento.setRealizado(false);
//		evento.setTitulo("prueba");
//		evento.setId(9L);
//		ServiceFactory.getEventosService().actualizarEvento(evento);
		
		JPAUtil.closeEntityManagerFactory();
	}

}
