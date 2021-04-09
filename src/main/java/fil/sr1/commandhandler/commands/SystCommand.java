package fil.sr1.commandhandler.commands;

import fil.sr1.commandhandler.CommandHandler;
import fil.sr1.common.Response;
import fil.sr1.implementation.ClientFtp;

/**
 * Handle SYST command which ask informations about the server's operating
 * system. Response answered by most servers is "215 UNIX Type : L8"
 *
 * @author Adrien Holvoet and Anthony Mendez
 */
public class SystCommand implements CommandHandler {
	// Private because it is only used in this class
	private ClientFtp clientFtp;

	/**
	 * Constructor
	 * 
	 * @param clientFtp the ClientFtp that requested this command
	 */
	public SystCommand(ClientFtp clientFtp) {
		this.clientFtp = clientFtp;
	}

	/**
	 * Send informations about the server's operating system to the ClientFTP.
	 */
	@Override
	public void handle() {
		clientFtp.sendMessage(Response.SYST);
	}
}
