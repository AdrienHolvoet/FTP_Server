package fil.sr1.commandhandler.commands;

import java.io.IOException;

import fil.sr1.commandhandler.CommandHandler;
import fil.sr1.common.Response;
import fil.sr1.implementation.ClientFtp;

/**
 * Handle the FTP command "USER username". This command is required for the
 * client to connect to the server.
 *
 * @author Adrien Holvoet and Anthony Mendez
 */
public class UserCommand implements CommandHandler {
	// All private because there are all only used inside the class
	private String username;
	private ClientFtp clientFtp;

	/**
	 * Constructor
	 *
	 * @param username  username of the client
	 * @param clientFtp the ClientFtp that requested this command
	 */
	public UserCommand(String username, ClientFtp clientFtp) {
		this.username = username;
		this.clientFtp = clientFtp;
	}

	/**
	 * If the username exist in the "database", set the user name with the given
	 * name and set the user status to GAVENAME, then a message requesting for its
	 * password is sent to the client. Otherwise the user status remain the same and
	 * a failure message is sent to the client.
	 * 
	 * @throws IOException
	 */
	public void handle() throws IOException {
		try {

			if (clientFtp.getUser().checkUser(username)) {
				clientFtp.sendMessage(Response.USER_OK);
			} else {
				clientFtp.sendMessage(Response.USER_NOK);
			}
		} catch (IOException e) {
			// if something went wrong by reading the file
			clientFtp.sendMessage(Response.USER_NOK);
		}
	}
}
