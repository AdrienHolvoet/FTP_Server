package fil.sr1.commandhandler.commands;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.times;

import java.net.SocketException;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import fil.sr1.commandhandler.CommandHandlerTest;
import fil.sr1.common.Response;

public class PortCommandTest extends CommandHandlerTest {
    PortCommand command;

    @Before
    public void setupBeforeEach() {
        super.setupBeforeEach();
        command = new PortCommand("1,2,3,4,5,6", clientFtp);
        try {
            Mockito.doNothing().when(clientFtp).setDataSocket(Mockito.anyString(), Mockito.anyInt());
        } catch (SocketException e) {
            System.err.println("Error occurred while trying to mock setDataSocket() from clientFtp.");
        }
    }

    @Test
    public void sendMessage200Ok_whenEnteringActiveMode() {
        command.handle();

        Mockito.verify(clientFtp).sendMessage(captor.capture());
        assertEquals(Response.PORT_OK, captor.getValue());
    }

    @Test
    public void setDataSocketConnection_whenEnteringActiveMode() throws SocketException {
        command.handle();
        Mockito.verify(clientFtp, times(1)).setDataSocket(Mockito.anyString(), Mockito.anyInt());
    }
}
