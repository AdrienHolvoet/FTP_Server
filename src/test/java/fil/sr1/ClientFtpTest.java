package fil.sr1;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import fil.sr1.common.Response;
import fil.sr1.exceptions.ServerSocketException;
import fil.sr1.implementation.ClientFtp;
import fil.sr1.implementation.ServerFtp;

public class ClientFtpTest {

	private static ServerFtp serverFtp;
	private static String home;
	private static int port;
	private static int maxUser;
	private static ClientFtp clientFtp;
	private static String localhost;
	private static Socket client;

	@BeforeClass
	public static void setup() throws ServerSocketException, UnknownHostException, IOException, InterruptedException {
		home = "/";
		port = 10000;
		maxUser = 2;
		localhost = "127.0.0.1";

		// start the server
		serverFtp = ServerFtp.getInstance(home, port, maxUser);
		new Thread() {
			public void run() {
				try {
					serverFtp.startServer();
				} catch (ServerSocketException e) {
					throw new RuntimeException(e);
				} catch (SocketException e) {
					System.out.println(e.getMessage());
				}

			}
		}.start();
		Thread.sleep(250);

		// start the client connection
		client = new Socket(localhost, port);
		clientFtp = new ClientFtp(client, home);
	}

	@Test
	public void client_canReceiveMessageFromTheServer_whenHeConnects() throws IOException {

		String reponseFromServer = clientFtp.readMessage();

		assertEquals(Response.WELCOME_MESSAGE, reponseFromServer);
	}

	@Test
	public void client_canSendMessage_OnServer() throws IOException {

		clientFtp.sendMessage("TEST");
		String reponseFromServer = clientFtp.readMessage();

		assertEquals(Response.COMMAND_NOT_IMPLEMENTED, reponseFromServer);
	}

	@AfterClass
	public static void cleanOnce() throws ServerSocketException, IOException, InterruptedException {
		clientFtp.close();
		serverFtp.close();
	}
}
