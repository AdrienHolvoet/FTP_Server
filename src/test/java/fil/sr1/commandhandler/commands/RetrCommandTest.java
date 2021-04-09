package fil.sr1.commandhandler.commands;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.times;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import fil.sr1.commandhandler.CommandHandlerTest;
import fil.sr1.common.Response;
import fil.sr1.implementation.DataSocket;
import fil.sr1.implementation.ServerFtp;

public class RetrCommandTest extends CommandHandlerTest {
    RetrCommand command;
    DataSocket dataSocket;

    @Before
    public void setupBeforeEach() {
        super.setupBeforeEach();
        command = new RetrCommand(TMP_FILE_ONE, clientFtp);
        Mockito.doReturn(Paths.get(ServerFtp.home, TMP_DIR).toString())
                .when(clientFtp).getCurrentDirectory();
        dataSocket = Mockito.mock(DataSocket.class);
        Mockito.doReturn(dataSocket).when(clientFtp).getDataSocket();
    }

    @Test
    public void sendMessage125And226Ok_whenRetrIsSuccessful() throws IOException {
        Mockito.doNothing().when(dataSocket).retrieveFile(Mockito.<File>any());
        command.handle();

        Mockito.verify(clientFtp, times(2)).sendMessage(captor.capture());
        List<String> clientFtpMessages = captor.getAllValues();

        assertEquals(Response.TRANSFERT_START, clientFtpMessages.get(0));
        assertEquals(Response.TRANSFERT_OK, clientFtpMessages.get(1));
    }

    @Test
    public void sendMessage451Nok_whenIOExceptionIsThrownWhileTryingToRetrieveFile() throws IOException {
        Mockito.doThrow(new IOException()).when(dataSocket).retrieveFile(Mockito.<File>any());
        command.handle();

        Mockito.verify(clientFtp, times(2)).sendMessage(captor.capture());
        List<String> clientFtpMessages = captor.getAllValues();

        assertEquals(Response.TRANSFERT_START, clientFtpMessages.get(0));
        assertEquals(Response.TRANSFERT_NOK, clientFtpMessages.get(1));
    }

}
