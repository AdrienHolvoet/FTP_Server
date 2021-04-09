package fil.sr1.implementation;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import fil.sr1.exceptions.ServerSocketException;

/**
 * Singleton class which creates a ServerSocket on a specific port waits for
 * connections as long as it is not closed
 * 
 * @author Adrien Holvoet and Anthony Mendez
 */
public class ServerFtp {

	/* All private because only used inside the class */
	// final because it will not change
	private final int port;
	private int maxUser;
	private ServerSocket serverSocket;
	private Socket client;
	private ExecutorService executor;
	// Single instance not pre-initialized
	private static ServerFtp instance = null;

	/*
	 * Represent the root directory of the FTP server, put as public because it need
	 * to be need to be globally accessed and static because related to the type
	 * itself not a instance
	 */
	public static String home;

	/**
	 * Private constructor
	 * 
	 * @param port : the port on which the server will listen
	 */
	private ServerFtp(int port, int maxUser) {
		this.port = port;
		this.maxUser = maxUser;
	}

	/**
	 *
	 * Method used to return an instance of the Singleton class
	 *
	 * @return Returns the singleton instance.
	 */
	public static ServerFtp getInstance(String directory, int port, int maxUser) {
		if (ServerFtp.instance == null) {
			ServerFtp.instance = new ServerFtp(port, maxUser);
			home = directory;
		}
		return ServerFtp.instance;
	}

	/**
	 * Method used to create the ServerSocket instance and start waiting for
	 * connection
	 * 
	 * @throws ServerSocketException
	 * @throws SocketException
	 */
	public void startServer() throws ServerSocketException, SocketException {
		try {
			serverSocket = new ServerSocket(port);
		} catch (IOException e) {
			throw new ServerSocketException("Unable to start the server : " + e.getMessage());
		}
		System.out.println("The server is running and is waiting for connection...");
		this.waitingConnection();
	}

	/**
	 * Method which consists of an infinite loop waiting for a connection from a new
	 * client and creates a new session with this one
	 * 
	 * @throws ServerSocketException
	 * @throws SocketException
	 *
	 */
	private void waitingConnection() throws ServerSocketException, SocketException {

		// Used to control the number of threads the application is creating
		executor = Executors.newFixedThreadPool(maxUser);
		try {
			while (true) {
				client = serverSocket.accept();
				ClientFtp clientFtp = new ClientFtp(client, home);
				executor.execute(clientFtp);
			}
		} catch (SocketException e) {
			// Exception throw by .accept() when we call serverSocket.close(), it means that
			// the serverSocket is close
			throw new SocketException("The server is closed");
		} catch (Exception e) {
			throw new ServerSocketException(e.getMessage());
		}
	}

	/**
	 * Method used to properly close the ftp server
	 * 
	 * @throws IOException
	 * 
	 */
	public void close() throws IOException {
		if (serverSocket != null && !serverSocket.isClosed()) {
			serverSocket.close();
		}
	}
}
