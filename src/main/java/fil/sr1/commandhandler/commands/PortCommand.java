package fil.sr1.commandhandler.commands;

import java.net.SocketException;

import fil.sr1.commandhandler.CommandHandler;
import fil.sr1.common.Response;
import fil.sr1.implementation.ClientFtp;

/**
 * Handle the FTP command "PORT ". This command is required to allow the client
 * to specifies which client-side port it has opened up for the data channel,
 * and the server initiates the connection
 *
 * @author Adrien Holvoet and Anthony Mendez
 */
public class PortCommand implements CommandHandler {
	// Static because it is a constant and it is linked only to this class
	public static final int PORT_MULTIPLIER = 256;

	// All private because there are all only used inside the class
	private ClientFtp clientFtp;
	private String ipAndPort;

	/**
	 * Constructor
	 * 
	 * @param ipAndPort format (h1,h2,h3,h4,p1,p2)
	 * @param clientFtp the ClientFtp that requested this command
	 */
	public PortCommand(String ipAndPort, ClientFtp clientFtp) {
		this.clientFtp = clientFtp;
		this.ipAndPort = ipAndPort;
	}

	/**
	 * 
	 * Create and send the address of the data channel in passive mode to the client
	 * to this format(IP1,IP2,IP3,IP4,PORT1,PORT2)
	 */
	@Override
	public void handle() {
		try {

			String[] ipAndPort = this.ipAndPort.split(",");
			if (ipAndPort.length != 6) {
				throw new SocketException();
			}

			String address = ipAndPort[0] + "." + ipAndPort[1] + "." + ipAndPort[2] + "." + ipAndPort[3];

			int port = (Integer.parseInt(ipAndPort[4]) * PORT_MULTIPLIER) + Integer.parseInt(ipAndPort[5]);
			// Create a new data channel in active mode
			clientFtp.setDataSocket(address, port);
			clientFtp.sendMessage(Response.PORT_OK);

		} catch (SocketException e) {
			clientFtp.sendMessage(Response.DATA_CHANNEL_NOK);
		}
	}
}
