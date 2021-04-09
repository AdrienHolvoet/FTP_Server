package fil.sr1.commandhandler.commands;

import static org.junit.Assert.assertEquals;

import java.nio.file.Paths;

import org.junit.Test;
import org.mockito.Mockito;

import fil.sr1.commandhandler.CommandHandlerTest;
import fil.sr1.common.Response;
import fil.sr1.implementation.ServerFtp;

public class RnfrCommandTest extends CommandHandlerTest {
    RnfrCommand command;

    @Test
    public void sendMessage350Pending_whenRnfrIsSuccessfullyExecuted() {
        command = new RnfrCommand(TMP_FILE_ONE, clientFtp);
        Mockito.doReturn(Paths.get(ServerFtp.home, TMP_DIR).toString())
                .when(clientFtp).getCurrentDirectory();
        command.handle();
        Mockito.verify(clientFtp).sendMessage(captor.capture());
        assertEquals(Response.PENDING_FURTHER_INFORMATION, captor.getValue());
    }

    @Test
    public void sendMessage550Nok_whenFileDoesNotExist() {
        command = new RnfrCommand("not-existing-file", clientFtp);
        Mockito.doReturn(Paths.get(ServerFtp.home, TMP_DIR).toString())
                .when(clientFtp).getCurrentDirectory();
        command.handle();
        Mockito.verify(clientFtp).sendMessage(captor.capture());
        assertEquals(Response.FILE_NOT_FOUND, captor.getValue());
    }
}
