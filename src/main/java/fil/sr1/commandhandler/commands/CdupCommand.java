package fil.sr1.commandhandler.commands;

import java.nio.file.Path;
import java.nio.file.Paths;

import fil.sr1.commandhandler.CommandHandler;
import fil.sr1.common.Response;
import fil.sr1.implementation.ClientFtp;
import fil.sr1.implementation.ServerFtp;

/**
 * Handle the FTP command "CDUP". This command will change the client's current
 * working directory to its parent's directory path.
 *
 * @author Adrien Holvoet and Anthony Mendez
 */
public class CdupCommand implements CommandHandler {

	// Private because it is only used in this class
	private final ClientFtp clientFtp;

	/**
	 * Constructor
	 * 
	 * @param clientFtp the ClientFtp that requested this command
	 */
	public CdupCommand(ClientFtp clientFtp) {
		this.clientFtp = clientFtp;
	}

	/**
	 * Replace the current directory with the parent's directory path.
	 */
	@Override
	public void handle() {
		Path currDir = Paths.get(clientFtp.getCurrentDirectory());
		Path parentPath = currDir.getParent();
		if (parentPath != null && parentPath.toString().contains(ServerFtp.home)) {
			clientFtp.setCurrentDirectory(parentPath.toString());
		}
		clientFtp.sendMessage(Response.CDUP_OK);
	}
}
