package fil.sr1.commandhandler.commands;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.times;

import java.net.SocketException;
import java.nio.file.Paths;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import fil.sr1.commandhandler.CommandHandlerTest;
import fil.sr1.common.Response;
import fil.sr1.implementation.DataSocket;
import fil.sr1.implementation.ServerFtp;

public class ListCommandTest extends CommandHandlerTest {
    ListCommand command;

    @Before
    public void setupBeforeEach() {
        super.setupBeforeEach();
        command = new ListCommand(clientFtp);
    }

    @Test
    public void sendListToDataSocket_whenListIsRequested() throws SocketException{
        Mockito.doReturn(Paths.get(ServerFtp.home, TMP_DIR).toString())
                .when(clientFtp).getCurrentDirectory();
        DataSocket dataSocket = Mockito.mock(DataSocket.class);
        Mockito.doReturn(dataSocket).when(clientFtp).getDataSocket();
        command.handle();

        Mockito.verify(dataSocket).sendData(captor.capture());
        assertTrue(captor.getValue().contains(TMP_FILE_ONE));
    }

    @Test
    public void sendMessage150And226Ok_whenListIsRequested() {
        Mockito.doReturn(Paths.get(ServerFtp.home, TMP_DIR).toString())
                .when(clientFtp).getCurrentDirectory();
        DataSocket dataSocket = Mockito.mock(DataSocket.class);
        Mockito.doReturn(dataSocket).when(clientFtp).getDataSocket();
        command.handle();

        Mockito.verify(clientFtp, times(2)).sendMessage(captor.capture());
        List<String> clientFtpMessages = captor.getAllValues();

        assertEquals(Response.START_LIST_OK, clientFtpMessages.get(0));
        assertEquals(Response.END_LIST_OK, clientFtpMessages.get(1));

    }
}
