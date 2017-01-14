package dad.todo.services;

public class ServiceException extends Exception {
	private static final long serialVersionUID = -8295292529890211379L;

	public ServiceException(String message) {
		super(message);
	}

	public ServiceException(Throwable cause) {
		super(cause);
	}

	public ServiceException(String message, Throwable cause) {
		super(message, cause);
	}

}
