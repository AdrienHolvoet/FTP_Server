package fil.sr1.commandhandler.commands;

import java.io.IOException;
import java.nio.file.DirectoryNotEmptyException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import fil.sr1.commandhandler.CommandHandler;
import fil.sr1.common.Response;
import fil.sr1.implementation.ClientFtp;

/**
 * Handle the FTP command "RMD". This command remove the directory at the
 * specified path.
 *
 * @author Adrien Holvoet and Anthony Mendez
 */
public class RmdCommand implements CommandHandler {
	// All private because there are all only used inside the class
	private ClientFtp clientFtp;
	private String pathName;

	/**
	 * Constructor
	 *
	 * @param pathName  the path of the directory to remove
	 * @param clientFtp the ClientFtp that requested this command
	 */
	public RmdCommand(String pathName, ClientFtp clientFtp) {
		this.clientFtp = clientFtp;
		this.pathName = pathName;
	}

	/**
	 * Remove the directory at the specified path.
	 *
	 * @throws IOException when an error occur during the removing process
	 */
	@Override
	public void handle() throws IOException {
		Path p = Paths.get(pathName);
		Path path;

		if (p.isAbsolute()) {
			path = Paths.get(pathName);
		} else {
			path = Paths.get(clientFtp.getCurrentDirectory(), pathName);
		}
		boolean dirHasBeenRemoved = false;
		try {
			if (path.toFile().exists()) {
				if (path.toFile().isDirectory()) {
					dirHasBeenRemoved = Files.deleteIfExists(path);
				} else {
					clientFtp.sendMessage(String.format(Response.RMD_NOT_A_DIR, pathName));
					return;
				}
			}
			if (dirHasBeenRemoved) {
				clientFtp.sendMessage(String.format(Response.RMD_OK, pathName));
				return;
			}
			clientFtp.sendMessage(String.format(Response.RMD_DIR_NOT_FOUND, pathName));
		} catch (DirectoryNotEmptyException e) {
			clientFtp.sendMessage(String.format(Response.RMD_DIR_NOT_EMPTY, pathName));
		}
	}
}
