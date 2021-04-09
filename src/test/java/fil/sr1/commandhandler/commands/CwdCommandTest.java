package fil.sr1.commandhandler.commands;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.nio.file.Paths;

import org.junit.Test;
import org.mockito.Mockito;

import fil.sr1.commandhandler.CommandHandlerTest;
import fil.sr1.common.Response;
import fil.sr1.implementation.ServerFtp;

public class CwdCommandTest extends CommandHandlerTest {
    CwdCommand command;

    @Test
    public void sendMessage250_WhenDirectoryIsSuccessfullyChanged() {
        command = new CwdCommand(TMP_DIR, clientFtp);
        Mockito.doReturn(Paths.get(ServerFtp.home).toString())
                .when(clientFtp).getCurrentDirectory();
        command.handle();

        Mockito.verify(clientFtp).sendMessage(captor.capture());
        assertEquals(Response.CWD_OK, captor.getValue());
    }

    @Test
    public void changeDirectory_WhenItExists() {
        command = new CwdCommand(TMP_DIR, clientFtp);
        Mockito.doReturn(Paths.get(ServerFtp.home).toString())
                .when(clientFtp).getCurrentDirectory();
        command.handle();

        Mockito.verify(clientFtp).setCurrentDirectory(captor.capture());
        String expectedPath = Paths.get(ServerFtp.home, TMP_DIR).toString();
        assertEquals(expectedPath, captor.getValue());
    }

    @Test
    public void sendMessage550_WhenDirectoryDoesNotExists() throws IOException {
        command = new CwdCommand("not-existing-dir", clientFtp);
        Mockito.doReturn(Paths.get(ServerFtp.home).toString())
                .when(clientFtp).getCurrentDirectory();
        command.handle();

        Mockito.verify(clientFtp).sendMessage(captor.capture());
        assertEquals(Response.CWD_NOK, captor.getValue());
    }


}
