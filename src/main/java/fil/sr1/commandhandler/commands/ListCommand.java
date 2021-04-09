package fil.sr1.commandhandler.commands;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.PosixFileAttributes;
import java.nio.file.attribute.PosixFilePermissions;
import java.text.SimpleDateFormat;
import java.util.Locale;

import fil.sr1.commandhandler.CommandHandler;
import fil.sr1.common.Response;
import fil.sr1.implementation.ClientFtp;

/**
 * Handle the FTP command "LIST". This command is used to list the contents of a
 * remote directory
 *
 * @author Adrien Holvoet and Anthony Mendez
 */
public class ListCommand implements CommandHandler {
	// All private because there are all only used inside the class
	private ClientFtp clientFtp;

	/**
	 * Constructor
	 * 
	 * @param clientFtp the ClientFtp that requested this command
	 */
	public ListCommand(ClientFtp clientFtp) {
		this.clientFtp = clientFtp;
	}

	/**
	 * 
	 * Send the contents of the current directory on the data channel and informs
	 * the user of the status on the command channel
	 */
	@Override
	public void handle() {
		try {
			File dir = new File(clientFtp.getCurrentDirectory());
			File[] list = dir.listFiles();
			String listString = "";
			for (File item : list) {
				if (item.getName().charAt(0) != '.') // remove hidden files
				{
					listString += this.formatFileWithMetadata(item);
				}
			}
			this.clientFtp.sendMessage(Response.START_LIST_OK);
			this.clientFtp.getDataSocket().sendData(listString);
			this.clientFtp.sendMessage(Response.END_LIST_OK);

		} catch (IOException | UnsupportedOperationException e) {
			this.clientFtp.sendMessage(Response.LIST_NOT_UNIX);
			this.clientFtp.sendMessage(Response.LIST_NOK);
		}
	}

	/**
	 * 
	 * Private method used only in this class to format a file with its metadatas
	 * 
	 * @throws IOException
	 */
	private String formatFileWithMetadata(File file) throws IOException {

		Path filePath = file.toPath();

		PosixFileAttributes posixFileAttributes = Files.readAttributes(filePath, PosixFileAttributes.class);
		BasicFileAttributes basicFileAttributes = Files.readAttributes(filePath, BasicFileAttributes.class);

		String typeOfFile = "-";

		if (posixFileAttributes.isSymbolicLink()) {
			typeOfFile = "l";
			return null;
		}
		if (posixFileAttributes.isDirectory()) {
			typeOfFile = "d";
		}

		SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd HH:mm", Locale.ENGLISH);

		return String.format("%s %s %s %s %s %s \r\n",
				typeOfFile + PosixFilePermissions.toString(posixFileAttributes.permissions()),
				posixFileAttributes.owner(), posixFileAttributes.group(), file.length(),
				dateFormat.format(basicFileAttributes.lastModifiedTime().toMillis()), file.getName());
	}

}
