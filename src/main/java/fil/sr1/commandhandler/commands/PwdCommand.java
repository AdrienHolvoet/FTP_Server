package fil.sr1.commandhandler.commands;

import java.io.File;

import fil.sr1.commandhandler.CommandHandler;
import fil.sr1.common.Response;
import fil.sr1.implementation.ClientFtp;

/**
 * Handle the FTP command "PWD". This command display the full name of the
 * current directory.
 *
 * @author Adrien Holvoet and Anthony Mendez
 */
public class PwdCommand implements CommandHandler {
	// Private because it is only used in this class
	private ClientFtp clientFtp;

	/**
	 * Constructor
	 * 
	 * @param clientFtp the ClientFtp that requested this command
	 */
	public PwdCommand(ClientFtp clientFtp) {
		this.clientFtp = clientFtp;
	}

	/**
	 * Send the current directory to the client
	 */
	@Override
	public void handle() {
		String directory = clientFtp.getCurrentDirectory();
		File f = new File(directory);
		if (f.exists() && f.isDirectory()) {
			this.clientFtp.sendMessage(String.format(Response.PWD_OK, clientFtp.getCurrentDirectory()));
		} else {
			this.clientFtp.sendMessage(String.format(Response.PWD_NOK, clientFtp.getCurrentDirectory()));
		}
	}
}
