package dad.todo.services.jpa.utils;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public final class JPAUtil {
	
	private static EntityManagerFactory managerFactory;

	private JPAUtil() {}
	
	public static void initEntityManagerFactory(String persistenceUnitName) {
		if (managerFactory == null) {
			try { 
				managerFactory = Persistence.createEntityManagerFactory(persistenceUnitName);
			} catch (Throwable e) {
				System.err.println("Error al crear el EntityManagerFactory: " + e.getMessage());
				throw new ExceptionInInitializerError(e);
			}	
		}
	}
	
	public static EntityManagerFactory getEntityManagerFactory() {
		return managerFactory;
	}
	
	public static void closeEntityManagerFactory() {
		if (managerFactory != null) {
			managerFactory.close();
		}
	}
	

}
