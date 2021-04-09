package fil.sr1.commandhandler.commands;

import static org.junit.Assert.assertEquals;

import java.nio.file.Paths;

import org.junit.Test;
import org.mockito.Mockito;

import fil.sr1.commandhandler.CommandHandlerTest;
import fil.sr1.common.Response;
import fil.sr1.implementation.ServerFtp;

public class PwdCommandTest extends CommandHandlerTest {
    PwdCommand command;

    @Test
    public void sendMessage257_whenCurrentDirectoryExists() {
        command = new PwdCommand(clientFtp);
        Mockito.doReturn(Paths.get(ServerFtp.home, TMP_DIR).toString())
                .when(clientFtp).getCurrentDirectory();
        command.handle();

        String expectedResult = String.format(Response.PWD_OK, Paths.get(ServerFtp.home, TMP_DIR));

        Mockito.verify(clientFtp).sendMessage(captor.capture());
        assertEquals(expectedResult, captor.getValue());
    }

    @Test
    public void sendMessage257_whenCurrentDirectoryDoesNotExists() {
        command = new PwdCommand(clientFtp);
        Mockito.doReturn(Paths.get(ServerFtp.home, "not-existing-dir").toString())
                .when(clientFtp).getCurrentDirectory();
        command.handle();

        Mockito.verify(clientFtp).sendMessage(captor.capture());
        assertEquals(Response.PWD_NOK, captor.getValue());
    }
}
