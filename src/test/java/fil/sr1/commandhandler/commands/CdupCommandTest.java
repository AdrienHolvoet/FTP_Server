package fil.sr1.commandhandler.commands;

import static org.junit.Assert.assertEquals;

import java.nio.file.Paths;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import fil.sr1.commandhandler.CommandHandlerTest;
import fil.sr1.common.Response;
import fil.sr1.implementation.ServerFtp;

public class CdupCommandTest extends CommandHandlerTest {
    CdupCommand command;

    @Before
    public void setupBeforeEach() {
        super.setupBeforeEach();
        command = new CdupCommand(clientFtp);
    }

    @Test
    public void sendMessage250_WhenCdupIsRequested() {
        Mockito.doReturn(Paths.get(ServerFtp.home, TMP_DIR).toString())
                .when(clientFtp).getCurrentDirectory();
        command.handle();

        Mockito.verify(clientFtp).sendMessage(captor.capture());
        assertEquals(Response.CDUP_OK, captor.getValue());
    }

    @Test
    public void changeDirectory_WhenIsNotDeeperThanRoot() {
        Mockito.doReturn(Paths.get(ServerFtp.home, TMP_DIR).toString())
                .when(clientFtp).getCurrentDirectory();
        command.handle();

        Mockito.verify(clientFtp).setCurrentDirectory(captor.capture());
        String expectedPath = Paths.get(ServerFtp.home).toString();
        assertEquals(expectedPath, captor.getValue());
    }

    @Test
    public void directoryChangeIsNotRequested_WhenCdupIsRequestedFromRoot() {
        Mockito.doReturn(Paths.get(ServerFtp.home).toString())
                .when(clientFtp).getCurrentDirectory();
        command.handle();

        Mockito.verify(clientFtp, Mockito.never()).setCurrentDirectory(captor.capture());
    }

}
