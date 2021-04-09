package fil.sr1.commandhandler.commands;

import java.io.File;
import java.io.IOException;

import fil.sr1.commandhandler.CommandHandler;
import fil.sr1.common.Response;
import fil.sr1.implementation.ClientFtp;

/**
 * Handle the FTP command "RETR". This command retrieve a copy of a file form
 * the server. Can only be used by a logged in user(not anonymous)
 */

public class RetrCommand implements CommandHandler {
	// All private because there are all only used inside the class
	private ClientFtp clientFtp;
	private String fileName;

	/**
	 * Constructor
	 *
	 * @param fileName  name of the file to store
	 * @param clientFtp to get the connected data channel
	 */
	public RetrCommand(String fileName, ClientFtp clientFtp) {
		this.clientFtp = clientFtp;
		this.fileName = fileName;
	}

	/**
	 * Send informations on the status of the file storage process and store it if
	 * possible
	 */
	@Override
	public void handle() {
		File file = new File(clientFtp.getCurrentDirectory() + "/" + fileName);
		clientFtp.sendMessage(Response.TRANSFERT_START);
		try {
			clientFtp.getDataSocket().retrieveFile(file);
			clientFtp.sendMessage(Response.TRANSFERT_OK);
		} catch (IOException e) {
			clientFtp.sendMessage(Response.TRANSFERT_NOK);
		}

	}
}
