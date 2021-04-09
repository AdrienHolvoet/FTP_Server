package fil.sr1.commandhandler.commands;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import fil.sr1.commandhandler.CommandHandler;
import fil.sr1.common.Response;
import fil.sr1.implementation.ClientFtp;
import fil.sr1.implementation.ServerFtp;

/**
 * Handle the FTP command "MKD". This command create a directory in the current
 * directory or in the specified path. Can only be used by a logged in user(not
 * anonymous)
 *
 * @author Adrien Holvoet and Anthony Mendez
 */
public class MkdCommand implements CommandHandler {
	// All private because there are all only used inside the class
	private ClientFtp clientFtp;
	private String pathName;

	/**
	 * Constructor
	 * 
	 * @param pathName  the path of directory to create
	 * @param clientFtp the ClientFtp that requested this command
	 */
	public MkdCommand(String pathName, ClientFtp clientFtp) {
		this.clientFtp = clientFtp;
		this.pathName = pathName;
	}

	/**
	 * Send informations on the status of the directory creation process and create
	 * it if possible
	 */
	@Override
	public void handle() {
		Path p = Paths.get(pathName);
		File file = null;
		String path = null;

		if (p.isAbsolute()) {
			path = pathName;
			file = new File(path);
		} else {
			path = clientFtp.getCurrentDirectory() + "/" + pathName;
			file = new File(path);
		}
		// Check if the client doesn't try to create a directory outside the root
		// directory
		if (path.contains(ServerFtp.home)) {
			if (file.exists()) {
				clientFtp.sendMessage(String.format(Response.MKD_NOK_EXIST, path));
			} else {
				// Creating the directory
				boolean bool = file.mkdir();
				if (bool) {
					clientFtp.sendMessage(String.format(Response.MKD_OK, path));
				} else {
					clientFtp.sendMessage(Response.FILE_NOT_FOUND);
				}
			}
		} else {
			clientFtp.sendMessage(Response.FILE_NOT_FOUND);
		}
	}
}
