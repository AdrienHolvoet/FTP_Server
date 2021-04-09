package fil.sr1.commandhandler.commands;

import fil.sr1.commandhandler.CommandHandler;
import fil.sr1.common.Response;
import fil.sr1.implementation.ClientFtp;

/**
 * Handle the FTP command "PASS password". This command is required for the
 * client to connect to the server.
 *
 * @author Adrien Holvoet and Anthony Mendez
 */
public class PassCommand implements CommandHandler {
	// All private because there are all only used inside the class
	private String pass;
	private ClientFtp clientFtp;

	/**
	 * Constructor
	 * 
	 * @param pass      the password sent by the client
	 * @param clientFtp the ClientFtp that requested this command
	 */
	public PassCommand(String pass, ClientFtp clientFtp) {
		this.pass = pass;
		this.clientFtp = clientFtp;
	}

	/**
	 * Handle the authentication to the server. Check if the password matches with
	 * the user. If the authentication succeed, the user status is set to LOGGEDIN
	 * and a success message is sent to the client. Otherwise the user status remain
	 * the same and a failure message is sent to the client.
	 */
	@Override
	public void handle() {
		if (clientFtp.getUser().authenticate(pass)) {
			clientFtp.sendMessage(Response.PASS_OK);
		} else {
			clientFtp.sendMessage(Response.PASS_NOK);
		}
	}
}
