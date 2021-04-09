package fil.sr1.exceptions;

/**
 * Exception thrown when something went wrong on the ServerFtp class
 * 
 * @author Adrien Holvoet and Anthony Mendez
 */
public class ServerSocketException extends Exception {

	private static final long serialVersionUID = 1L;

	/**
	 * Constructor
	 * 
	 * @param message Exception message
	 */
	public ServerSocketException(String message) {
		super(message);
	}
}
