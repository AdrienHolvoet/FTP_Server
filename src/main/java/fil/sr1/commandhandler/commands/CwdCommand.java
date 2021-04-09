package fil.sr1.commandhandler.commands;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

import fil.sr1.commandhandler.CommandHandler;
import fil.sr1.common.Response;
import fil.sr1.implementation.ClientFtp;
import fil.sr1.implementation.ServerFtp;

/**
 * Handle the FTP command "CWD". This command will change the client's current
 * working directory to the path specified with the command
 *
 * @author Adrien Holvoet and Anthony Mendez
 */
public class CwdCommand implements CommandHandler {

	// All private because there are all only used inside the class
	private ClientFtp clientFtp;
	private String directory;

	/**
	 * Constructor
	 * 
	 * @param directory the new current directory
	 * @param clientFtp the ClientFtp that requested this command
	 */
	public CwdCommand(String directory, ClientFtp clientFtp) {
		this.clientFtp = clientFtp;
		this.directory = directory;
	}

	/**
	 * Replace the current directory with the directory sent by the client
	 */
	@Override
	public void handle() {
		Path p = Paths.get(directory);
		File f = null;
		String fullpath = null;
		if (p.isAbsolute()) {
			f = new File(directory);
			fullpath = directory;
		} else {
			fullpath = clientFtp.getCurrentDirectory() + "/" + directory;
			f = new File(fullpath);
		}

		// Check if the client doesn't try to create a directory outside the root
		// directory
		if (fullpath.contains(ServerFtp.home)) {
			if (f.exists() && f.isDirectory()) {
				clientFtp.setCurrentDirectory(fullpath);
				clientFtp.sendMessage(Response.CWD_OK);
			} else {
				this.clientFtp.sendMessage(Response.CWD_NOK);
			}
		} else {
			this.clientFtp.sendMessage(Response.CWD_NOK);
		}
	}
}
