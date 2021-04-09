package fil.sr1.implementation;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;

import fil.sr1.commandhandler.CommandHandlerFactory;
import fil.sr1.common.Response;
import fil.sr1.exceptions.IllegalCommandException;
import fil.sr1.exceptions.ServerSocketException;

/**
 * Class which characterizes the session with a client and allows the command
 * exchange between the client and server.
 *
 * @author Adrien Holvoet and Anthony Mendez
 */
public class ClientFtp extends Thread {
	// All private because there are all only used inside the class

	/* Represent the command channel */
	private Socket clientSocket;

	private PrintWriter printer;
	private BufferedReader reader;

	private User user;
	private String currentDirectory;

	// Used during the file renaming process
	private boolean inFileRenamingProcess;
	private String fileToRename;

	private DataSocket dataSocket = null;

	/**
	 * Constructor, it instantiates all the objects necessary for the proper
	 * functioning of a client of an FTP server
	 * 
	 * @param clientSocket     the TCP connection between the server and the client
	 * @param currentDirectory the current directory see by the client
	 */
	public ClientFtp(Socket clientSocket, String currentDirectory) throws IOException {
		super();
		inFileRenamingProcess = false;
		this.currentDirectory = currentDirectory;
		this.clientSocket = clientSocket;
		OutputStream out = clientSocket.getOutputStream();
		printer = new PrintWriter(out, true);
		InputStream in = clientSocket.getInputStream();
		InputStreamReader isr = new InputStreamReader(in);
		reader = new BufferedReader(isr);
		user = new User();
	}

	/**
	 * Allows to launch the logic linked to the connection of a new user on the
	 * server, this logic is just to listen to the commands sent by this one
	 * 
	 */
	@Override
	public void run() {
		try {
			this.sendMessage(Response.WELCOME_MESSAGE);
			// Get new command from client
			while (true) {
				this.executeCommand();
			}
		} catch (Exception e) {
			// If a exception is catch we just close the client connection
			try {
				this.close();
			} catch (IOException e1) {
				/// just throw the exception of the close
				throw new RuntimeException(e1);
			}
		}
	}

	/**
	 * To print message on the command channel
	 * 
	 * @param message
	 */
	public void sendMessage(String message) {
		System.out.println("DEBUG : response sent by server = " + message);
		printer.println(message);
	}

	/**
	 * To read message on the command channel
	 *
	 * @throws IOException
	 * 
	 */
	public String readMessage() throws IOException {
		return reader.readLine();
	}

	private void executeCommand() throws IOException {
		try {
			CommandHandlerFactory.build(this.readMessage(), this).handle();
		} catch (IllegalCommandException e) {
			sendMessage(e.getMessage());
		}
	}

	/**
	 * Method used to close the connection from the client to the server
	 * 
	 * @throws IOException
	 */
	public void close() throws IOException {
		try {
			printer.close();
			reader.close();
			clientSocket.close();
		} catch (IOException e) {
			throw new SocketException("Something went wrong when closing the command channel : " + e.getMessage());
		}

	}

	public User getUser() {
		return user;
	}

	public String getCurrentDirectory() {
		return currentDirectory;
	}

	public void setCurrentDirectory(String currentDirectory) {
		this.currentDirectory = currentDirectory;
	}

	public DataSocket getDataSocket() {
		return dataSocket;
	}

	public void setDataSocket(String address, int port) throws SocketException {
		this.dataSocket = new DataSocket(address, port);
	}

	public void setDataSocket() throws ServerSocketException {
		this.dataSocket = new DataSocket();
	}

	public Socket getClientSocket() {
		return clientSocket;
	}

	public void setClientSocket(Socket clientSocket) {
		this.clientSocket = clientSocket;
	}

	public boolean isInFileRenamingProcess() {
		return inFileRenamingProcess;
	}

	public void setInFileRenamingProcess(boolean inFileRenamingProcess) {
		this.inFileRenamingProcess = inFileRenamingProcess;
	}

	public void setFileToRename(String fileToRename) {
		this.fileToRename = fileToRename;
	}

	public String getFileToRename() {
		return fileToRename;
	}
}
