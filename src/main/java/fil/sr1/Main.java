package fil.sr1;

import java.net.SocketException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import fil.sr1.exceptions.ServerSocketException;
import fil.sr1.exceptions.WrongArgumentException;
import fil.sr1.implementation.ServerFtp;

/**
 * Application entry point with 3 arguments : 1 - the path of the local
 * repository of the FTP server (mandatory), 2 - the port number (mandatory), 3
 * - the number of users who can connect simultaneously, this number is 5 by
 * default (optionnal)
 * 
 * @author Adrien Holvoet and Anthony Mendez
 */

public class Main {
	// Static because it's a sharing global value and private because only use in
	// this class
	private static int maxUser = 5;

	public static void main(String[] args) throws ServerSocketException, SocketException {

		if (args.length < 2) {
			throw new WrongArgumentException(
					"You must at least specify two arguments corresponding to the root repository of the FTP server and the port on which the server will listen, under this format : executable <repository_path> <port_number> <max_user(optionnal)>");
		}

		if (args.length > 3) {
			throw new WrongArgumentException(
					"The maximum number of arguments is 3 under this format : executable <repository_path> <port_number> <max_user(optionnal)>");
		}

		if (args.length == 3) {
			maxUser = Integer.parseInt(args[2]);
		}

		Path path = Paths.get(args[0]);
		if (!Files.isDirectory(path)) {
			throw new WrongArgumentException("The home directory specified when starting the server does not exist");
		}

		try {
			ServerFtp.getInstance(args[0], Integer.parseInt(args[1]), maxUser).startServer();
		} catch (ServerSocketException e) {
			throw new WrongArgumentException(e.getMessage() + ", The specified port is not available");
		}
	}
}