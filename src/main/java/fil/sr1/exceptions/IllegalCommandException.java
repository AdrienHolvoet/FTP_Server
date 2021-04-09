package fil.sr1.exceptions;

/**
 * Exception throw when an unimplemented command is received by the server
 * 
 * @author Adrien Holvoet and Anthony Mendez
 */
public class IllegalCommandException extends Exception {
	private static final long serialVersionUID = 1L;
	
	/**
	 * Constructor
	 * 
	 * @param message Exception message
	 */
	public IllegalCommandException(String message) {
		super(message);
	}
}
