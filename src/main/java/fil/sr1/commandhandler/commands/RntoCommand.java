package fil.sr1.commandhandler.commands;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import fil.sr1.commandhandler.CommandHandler;
import fil.sr1.common.Response;
import fil.sr1.implementation.ClientFtp;

/**
 * Handle the FTP command "RNTO" (rename to). This command change the name of
 * the file given with the logical previous command RNFR (rename from) <old
 * filename> by the given new name (eg. "RNFR oldname" -> "RNTO newname").
 *
 * @author Adrien Holvoet and Anthony Mendez
 */
public class RntoCommand implements CommandHandler {
	// All private because there are all only used inside the class
	private ClientFtp clientFtp;
	private String fileName;

	/**
	 * Constructor
	 *
	 * @param fileName  the file to rename
	 * @param clientFtp the ClientFtp that requested this command
	 */
	public RntoCommand(String fileName, ClientFtp clientFtp) {
		this.clientFtp = clientFtp;
		this.fileName = fileName;
	}

	/**
	 * Rename the source file with the new given name.
	 *
	 * @throws IOException if an issue occur while renaming the file.
	 */
	@Override
	public void handle() throws IOException {
		if (!clientFtp.isInFileRenamingProcess()) {
			clientFtp.sendMessage(Response.BAD_COMMAND_SEQUENCE);
		} else {
			Path fileToRename = Paths.get(clientFtp.getCurrentDirectory(), clientFtp.getFileToRename());
			Files.move(fileToRename, fileToRename.resolveSibling(fileName));
			clientFtp.sendMessage(Response.FILE_NAME_CHANGED);
		}
	}
}
