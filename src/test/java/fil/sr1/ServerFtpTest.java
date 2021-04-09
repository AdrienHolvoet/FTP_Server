package fil.sr1;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import fil.sr1.common.Response;
import fil.sr1.exceptions.ServerSocketException;
import fil.sr1.implementation.ServerFtp;

public class ServerFtpTest {

	private static ServerFtp serverFtp;
	private static String home;
	private static int port;
	private static int maxUser;
	private static String localhost;
	private static Socket socket1;
	private static Socket socket2;
	private static Socket socket3;
	private InputStream clCommand;
	private BufferedReader readerClCommand;

	@BeforeClass
	public static void setup() throws ServerSocketException, InterruptedException {
		home = "/";
		port = 10000;
		maxUser = 2;
		localhost = "127.0.0.1";
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
	}

	@Test
	public void checkIfTheServerAcceptMultipleConnection()
			throws UnknownHostException, IOException, InterruptedException {

		socket1 = new Socket(localhost, port);
		socket2 = new Socket(localhost, port);
		socket3 = new Socket(localhost, port);

		assertTrue(socket1.isConnected());
		assertTrue(socket2.isConnected());
		assertTrue(socket3.isConnected());

	}

	@Test
	public void checkIfTheServerManageOnlyMaxUserSimultaneously()
			throws UnknownHostException, IOException, InterruptedException {

		// MAX USER IS EQUAL TO 2
		socket1 = new Socket(localhost, port);
		clCommand = socket1.getInputStream();
		readerClCommand = new BufferedReader(new InputStreamReader(clCommand));
		String message1 = readerClCommand.readLine();

		socket2 = new Socket(localhost, port);
		clCommand = socket2.getInputStream();

		readerClCommand = new BufferedReader(new InputStreamReader(clCommand));
		String message2 = readerClCommand.readLine();

		socket3 = new Socket(localhost, port);// show that you can connect
		clCommand = socket3.getInputStream();
		readerClCommand = new BufferedReader(new InputStreamReader(clCommand));

		socket2.close(); // close socket 2 so that the server finally responds to socket 3 otherwise
							// socket 3 waits indefinitely for a new location to be available

		String message3 = readerClCommand.readLine();

		assertEquals(message1, Response.WELCOME_MESSAGE);
		assertEquals(message2, Response.WELCOME_MESSAGE);
		assertEquals(message3, Response.WELCOME_MESSAGE);
	}

	@After
	public void clean() throws IOException {
		socket1.close();
		socket2.close();
		socket3.close();
	}

	@AfterClass
	public static void cleanOnce() throws ServerSocketException, IOException, InterruptedException {
		serverFtp.close();

	}
}
