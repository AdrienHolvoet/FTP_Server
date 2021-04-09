package fil.sr1.commandhandler.commands;

import java.io.IOException;

import fil.sr1.commandhandler.CommandHandler;
import fil.sr1.common.Response;
import fil.sr1.implementation.ClientFtp;

/**
 * Handle the FTP command "QUIT". This command properly disconnect the client
 * from the server.
 *
 * @author Adrien Holvoet and Anthony Mendez
 */
public class QuitCommand implements CommandHandler {
	// Private because it is only used in this class
	private ClientFtp clientFtp;

	/**
	 * Constructor
	 * 
	 * @param clientFtp the ClientFtp that requested this command
	 */
	public QuitCommand(ClientFtp clientFtp) {
		this.clientFtp = clientFtp;
	}

	/**
	 * Send informations to the client when he disconnects
	 * 
	 * @throws IOException
	 */
	@Override
	public void handle() throws IOException {
		clientFtp.sendMessage(Response.QUIT_OK);
		clientFtp.close();
	}
}
