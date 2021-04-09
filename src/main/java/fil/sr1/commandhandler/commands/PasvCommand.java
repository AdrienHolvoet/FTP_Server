package fil.sr1.commandhandler.commands;

import java.net.InetAddress;

import fil.sr1.commandhandler.CommandHandler;
import fil.sr1.common.Response;
import fil.sr1.exceptions.ServerSocketException;
import fil.sr1.implementation.ClientFtp;

/**
 * Handle the FTP command "PASV ". This command is required to allow the client
 * behind routers/firewalls to connect over FTP when they might not be able to
 * connect over an Active (PORT) FTP session. PASV mode has the server tell the
 * client where to connect the data port on the server.
 *
 * @author Adrien Holvoet and Anthony Mendez
 */
public class PasvCommand implements CommandHandler {
	// Static because it is a constant and it is linked only to this class
	public static final int PORT_MULTIPLIER = 256;

	// Private because it is only used in this class
	private ClientFtp clientFtp;

	/**
	 * Constructor
	 * 
	 * @param clientFtp the ClientFtp that requested this command
	 */
	public PasvCommand(ClientFtp clientFtp) {
		this.clientFtp = clientFtp;
	}

	/**
	 * 
	 * Create and send the address of the data channel in passive mode to the client
	 * to this format(IP1,IP2,IP3,IP4,PORT1,PORT2)
	 */
	@Override
	public void handle() {
		try {
			// Create a new data channel in passive mode
			clientFtp.setDataSocket();
			String address = getHostAdress();
			int port = clientFtp.getDataSocket().getLocalPort();
			int port1 = (port / PORT_MULTIPLIER);
			int port2 = port % PORT_MULTIPLIER;
			clientFtp.sendMessage(String.format(Response.PASV_OK, address + "," + port1 + "," + port2));
			// listen new socket connection on data channel
			clientFtp.getDataSocket().acceptConnection();
		} catch (ServerSocketException e) {
			clientFtp.sendMessage(Response.DATA_CHANNEL_NOK);
		}
	}

	/**
	 * Get the address to which the socket is connected and format it. Private
	 * because only use inside this class
	 */
	private String getHostAdress() {
		InetAddress address = clientFtp.getClientSocket().getInetAddress();
		return address.getHostAddress().replace(".", ",");
	}
}
