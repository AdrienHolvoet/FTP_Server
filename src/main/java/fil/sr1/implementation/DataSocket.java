package fil.sr1.implementation;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

import fil.sr1.exceptions.ServerSocketException;

/**
 * Class which characterizes the data channel with a client and allows data
 * exchange between the client and server.
 *
 * @author Adrien Holvoet and Anthony Mendez
 */
public class DataSocket {
	// All private because only used inside the class
	/* Represent the data channel */
	private ServerSocket dataSocketServer;
	private Socket dataSocket;

	/* To write on the data channel */
	private OutputStream dataSocketOutput;
	private PrintWriter dataPrinter;

	/* To read on the data channel */
	private InputStream in;

	/**
	 * Constructor in passive mode to allocate the port dynamically
	 * 
	 * @throws ServerSocketException
	 * 
	 */
	public DataSocket() throws ServerSocketException {
		try {
			dataSocketServer = new ServerSocket(0);
		} catch (IOException e) {
			throw new ServerSocketException("Unable to create the data channel in passive mode");
		}
	}

	/**
	 * Constructor in active mode to allocate the port sent by the client
	 * 
	 * 
	 * @param address address sent by the client on which to initiate the connection
	 * @param port    port sent by the client on which to initiate the connection
	 * 
	 * @throws SocketException
	 */
	public DataSocket(String address, int port) throws SocketException {
		try {
			dataSocket = new Socket(address, port);
		} catch (IOException e) {
			throw new SocketException("Unable to create the data channel in active mode : " + e.getMessage());
		}
	}

	/**
	 * Method to accept a connection from a client to the data channal server
	 * 
	 * @throws ServerSocketException
	 */
	public void acceptConnection() throws ServerSocketException {
		try {
			dataSocket = dataSocketServer.accept();
		} catch (IOException e) {
			throw new ServerSocketException("The data channel doesn't accept connection");
		}
	}

	/**
	 * Method to close the data channel
	 * 
	 * @throws IOException
	 */
	public void close() throws IOException {
		try {
			dataSocket.close();
			if (dataSocketServer != null) {
				dataSocketServer.close();
			}
		} catch (IOException e) {
			throw new SocketException("Something went wrong when closing the data channel");

		}
	}

	/**
	 * To print message on the data channel
	 * 
	 * @param message
	 * 
	 * @throws SocketException
	 */
	public void sendData(String message) throws SocketException {
		try {
			configureStreamOut();
			dataPrinter.println(message);

			// Close the channel after every use
			close();

		} catch (IOException e) {
			throw new SocketException("Unable to write on the data channel");
		}

	}

	/**
	 * To store a file on the server by reading the bytes on the data channel
	 * 
	 * @param file file to store
	 * 
	 * @throws IOException
	 */
	public void storeFile(File file) throws IOException {
		try {
			// Create the file
			file.createNewFile();
			in = dataSocket.getInputStream();
			FileOutputStream fileOutput = new FileOutputStream(file);
			int size = dataSocket.getSendBufferSize();
			byte[] buffer = new byte[size];
			int line;
			while ((line = in.read(buffer)) != -1) {
				fileOutput.write(buffer, 0, line);
			}
			// Close data channel
			close();
			fileOutput.close();
			fileOutput.flush();

		} catch (Exception e) {
			throw new IOException(e);
		}
	}

	/**
	 * To Retrieve a copy of a file specified in parameter
	 * 
	 * @param file file to retrieve
	 * 
	 * @throws IOException
	 */
	public void retrieveFile(File file) throws IOException {
		try {
			if (!file.exists()) {
				// To stop the process and notify the client
				throw new IOException("File not found");
			} else {
				dataSocketOutput = dataSocket.getOutputStream();
				FileInputStream fileInput = new FileInputStream(file);
				int size = dataSocket.getReceiveBufferSize();
				byte[] buffer = new byte[size];
				int line;
				while ((line = fileInput.read(buffer)) != -1) {
					// Write all the bytes of the server's file in the newly created file
					dataSocketOutput.write(buffer, 0, line);
				}
				fileInput.close();
				// Close data channel
				close();
				dataSocketOutput.close();
				dataSocketOutput.flush();

			}
		} catch (Exception e) {
			// To stop the process and notify the client
			throw new IOException(e);
		}
	}

	/**
	 * To check if the data channel is closed
	 * 
	 * @return true if closed
	 * 
	 */
	public boolean isClosed() {
		if (dataSocket == null || dataSocket.isClosed()) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * To check if the data channel is closed
	 * 
	 * @return true if closed
	 * 
	 */
	public int getLocalPort() {
		return dataSocketServer.getLocalPort();
	}

	/**
	 * Private method only used in this class configure the stream write on the data
	 * channel
	 * 
	 * @throws IOException
	 */
	private void configureStreamOut() throws IOException {
		try {
			dataSocketOutput = dataSocket.getOutputStream();
			dataPrinter = new PrintWriter(this.dataSocketOutput, true);
		} catch (IOException e) {
			throw new SocketException("Unable to write on the data channel");
		}
	}
}
