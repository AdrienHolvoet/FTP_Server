package fil.sr1.exceptions;

/**
 * Exception thrown when there is a bad argument when launching the executable
 * 
 * @author Adrien Holvoet and Anthony Mendez
 */
public class WrongArgumentException extends RuntimeException {

	private static final long serialVersionUID = 3892287429020551760L;

	/**
	 * Constructor
	 * 
	 * @param message Exception message
	 */
	public WrongArgumentException(String message) {
		super(message);
	}
}
