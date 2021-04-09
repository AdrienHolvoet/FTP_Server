package fil.sr1.commandhandler.commands;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.junit.Test;
import org.mockito.Mockito;

import fil.sr1.commandhandler.CommandHandlerTest;
import fil.sr1.common.Response;
import fil.sr1.implementation.ServerFtp;

public class MkdCommandTest extends CommandHandlerTest {
    MkdCommand command;

    @Test
    public void return257_WhenOkToMkdir() throws IOException {
        String tmpMkDir = "tmp-mk-dir";
        command = new MkdCommand(tmpMkDir, clientFtp);
        Mockito.doReturn(Paths.get(ServerFtp.home, TMP_DIR).toString())
                .when(clientFtp).getCurrentDirectory();
        command.handle();

        String expectedResult = String.format(Response.MKD_OK, Paths.get(ServerFtp.home, TMP_DIR, tmpMkDir));

        Mockito.verify(clientFtp).sendMessage(captor.capture());
        assertEquals(expectedResult, captor.getValue());

        Files.deleteIfExists(Paths.get(ServerFtp.home, TMP_DIR, tmpMkDir));
    }

    @Test
    public void return521_WhenDirAlreadyExists() throws IOException {
        command = new MkdCommand(TMP_DIR, clientFtp);
        Mockito.doReturn(Paths.get(ServerFtp.home).toString())
                .when(clientFtp).getCurrentDirectory();
        command.handle();

        String expectedResult = String.format(Response.MKD_NOK_EXIST, Paths.get(ServerFtp.home, TMP_DIR));

        Mockito.verify(clientFtp).sendMessage(captor.capture());
        assertEquals(expectedResult, captor.getValue());

    }


}
