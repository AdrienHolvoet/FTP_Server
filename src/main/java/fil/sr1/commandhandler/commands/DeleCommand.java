package fil.sr1.commandhandler.commands;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import fil.sr1.commandhandler.CommandHandler;
import fil.sr1.common.Response;
import fil.sr1.implementation.ClientFtp;

/**
 * Handle the FTP command "DELE". This command remove the file at the specified
 * path. Can only be used by a logged in user(not anonymous)
 *
 * @author Adrien Holvoet and Anthony Mendez
 */
public class DeleCommand implements CommandHandler {
	// All private because there are all only used inside the class
	private ClientFtp clientFtp;
	private String pathName;

	/**
	 * Constructor
	 * 
	 * @param pathName  the path of the file to remove
	 * @param clientFtp the ClientFtp that requested this command
	 */
	public DeleCommand(String pathName, ClientFtp clientFtp) {
		this.clientFtp = clientFtp;
		this.pathName = pathName;
	}

	/**
	 * Remove the file at the specified path.
	 *
	 */
	@Override
	public void handle() {
		Path p = Paths.get(pathName);
		Path path;
		if (p.isAbsolute()) {
			path = Paths.get(pathName);
		} else {
			path = Paths.get(clientFtp.getCurrentDirectory(), pathName);
		}

		if (Files.exists(path) && path.toFile().isFile()) {

			if (path.toFile().delete()) {
				clientFtp.sendMessage(Response.DELE_OK);
			} else {
				// can't delete
				clientFtp.sendMessage(Response.DELE_NOK);
			}
		} else {
			clientFtp.sendMessage(Response.DELE_NOK);
		}
	}
}