package fil.sr1.commandhandler.commands;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.junit.Test;
import org.mockito.Mockito;

import fil.sr1.commandhandler.CommandHandlerTest;
import fil.sr1.implementation.ServerFtp;

public class RmdCommandTest extends CommandHandlerTest {
    private final String DIR_TO_RMD = "dir-to-rmd";

    RmdCommand command;

    @Test
    public void sendMessage250Ok_whenDirectoryIsSuccessfullyRemoved() throws IOException {
        Files.createDirectory(Paths.get(ServerFtp.home, TMP_DIR, DIR_TO_RMD));
        command = new RmdCommand(DIR_TO_RMD, clientFtp);
        Mockito.doReturn(Paths.get(ServerFtp.home, TMP_DIR).toString())
                .when(clientFtp).getCurrentDirectory();
        command.handle();

        Mockito.verify(clientFtp).sendMessage(captor.capture());
        assertTrue(captor.getValue().contains("250"));
    }

    @Test
    public void sendMessage500Nok_whenDirectoryWasNotFound() throws IOException {
        command = new RmdCommand("not-existing-dir", clientFtp);
        Mockito.doReturn(Paths.get(ServerFtp.home, TMP_DIR).toString())
                .when(clientFtp).getCurrentDirectory();
        command.handle();

        Mockito.verify(clientFtp).sendMessage(captor.capture());
        assertTrue(captor.getValue().contains("550"));
    }

    @Test
    public void sendMessage500Nok_whenTryingToRmdAFile() throws IOException {
        command = new RmdCommand(TMP_FILE_ONE, clientFtp);
        Mockito.doReturn(Paths.get(ServerFtp.home, TMP_DIR).toString())
                .when(clientFtp).getCurrentDirectory();
        command.handle();

        Mockito.verify(clientFtp).sendMessage(captor.capture());
        assertTrue(captor.getValue().contains("550"));
    }

}
