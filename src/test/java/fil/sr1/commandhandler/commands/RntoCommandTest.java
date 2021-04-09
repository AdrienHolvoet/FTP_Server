package fil.sr1.commandhandler.commands;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.junit.Test;
import org.mockito.Mockito;

import fil.sr1.commandhandler.CommandHandlerTest;
import fil.sr1.common.Response;
import fil.sr1.implementation.ServerFtp;

public class RntoCommandTest extends CommandHandlerTest {
    RntoCommand command;

    @Test
    public void sendMessage250Ok_whenRntoIsSuccessfullyExecuted() throws IOException {
        command = new RntoCommand("new-name", clientFtp);
        Mockito.doReturn(Paths.get(ServerFtp.home, TMP_DIR).toString())
                .when(clientFtp).getCurrentDirectory();
        Mockito.doReturn(true).when(clientFtp).isInFileRenamingProcess();
        Mockito.doReturn(TMP_FILE_ONE).when(clientFtp).getFileToRename();
        command.handle();
        Mockito.verify(clientFtp).sendMessage(captor.capture());
        assertEquals(Response.FILE_NAME_CHANGED, captor.getValue());
        assertTrue(Files.exists(Paths.get(ServerFtp.home, TMP_DIR, "new-name")));
        assertTrue(Files.notExists(Paths.get(ServerFtp.home, TMP_DIR, TMP_FILE_ONE)));
        Files.deleteIfExists(Paths.get(ServerFtp.home, TMP_DIR, "new-name"));
    }	

    @Test
    public void sendMessage503Nok_whenDidNotRnfrBefore() throws IOException {
        command = new RntoCommand("not-existing-file", clientFtp);
        Mockito.doReturn(Paths.get(ServerFtp.home, TMP_DIR).toString())
                .when(clientFtp).getCurrentDirectory();
        Mockito.doReturn(false).when(clientFtp).isInFileRenamingProcess();
        command.handle();
        Mockito.verify(clientFtp).sendMessage(captor.capture());
        assertEquals(Response.BAD_COMMAND_SEQUENCE, captor.getValue());
    }
}
