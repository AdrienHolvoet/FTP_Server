package fil.sr1.commandhandler.commands;

import java.io.File;

import fil.sr1.commandhandler.CommandHandler;
import fil.sr1.common.Response;
import fil.sr1.implementation.ClientFtp;

/**
 * Handle the FTP command "RNFR" (rename from). This command is used to specify
 * the name of the file to modify. And it must be followed by the command RNFR
 * (rename from) <old filename> (eg. "RNFR oldname" -> "RNTO newname").
 *
 * @author Adrien Holvoet and Anthony Mendez
 */
public class RnfrCommand implements CommandHandler {
	// All private because there are all only used inside the class
	private ClientFtp clientFtp;
	private String fileName;

	/**
	 * Constructor
	 *
	 * @param fileName  the file to rename
	 * @param clientFtp the ClientFtp that requested this command
	 */
	public RnfrCommand(String fileName, ClientFtp clientFtp) {
		this.clientFtp = clientFtp;
		this.fileName = fileName;
	}

	/**
	 * Check if the source file exists, then save its name and enter into file
	 * renaming process.
	 */
	@Override
	public void handle() {
		File file = new File(clientFtp.getCurrentDirectory(), fileName);
		if (!file.exists()) {
			clientFtp.sendMessage(Response.FILE_NOT_FOUND);
		} else {
			clientFtp.setInFileRenamingProcess(true);
			clientFtp.setFileToRename(fileName);
			clientFtp.sendMessage(Response.PENDING_FURTHER_INFORMATION);
		}
	}
}
