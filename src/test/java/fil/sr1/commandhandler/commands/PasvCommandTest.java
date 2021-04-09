package fil.sr1.commandhandler.commands;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.times;

import java.net.InetAddress;
import java.net.Socket;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import fil.sr1.commandhandler.CommandHandlerTest;
import fil.sr1.exceptions.ServerSocketException;
import fil.sr1.implementation.DataSocket;

public class PasvCommandTest extends CommandHandlerTest {
    PasvCommand command;
    DataSocket dataSocket;

    @Before
    public void setupBeforeEach() {
        super.setupBeforeEach();
        command = new PasvCommand(clientFtp);
        dataSocket = Mockito.mock(DataSocket.class);
        try {
            Mockito.doNothing().when(clientFtp).setDataSocket();
        } catch (ServerSocketException e) {
            System.err.println("Error occurred while trying to mock setDataSocket() from clientFtp.");
        }
        Mockito.doReturn(dataSocket).when(clientFtp).getDataSocket();
        Socket clientSocket = Mockito.mock(Socket.class);
        Mockito.doReturn(clientSocket).when(clientFtp).getClientSocket();
        InetAddress inetAddress = Mockito.mock(InetAddress.class);
        Mockito.doReturn(inetAddress).when(clientSocket).getInetAddress();
        Mockito.doReturn("1.1.1.1").when(inetAddress).getHostAddress();
    }

    @Test
    public void sendMessage227Ok_whenEnteringPassiveMode() {
        command.handle();

        Mockito.verify(clientFtp).sendMessage(captor.capture());
        assertTrue(captor.getValue().contains("227"));
    }

    @Test
    public void acceptDataSocketConnection_whenEnteringPassiveMode() throws ServerSocketException {
        command.handle();
        Mockito.verify(dataSocket, times(1)).acceptConnection();
    }
}


