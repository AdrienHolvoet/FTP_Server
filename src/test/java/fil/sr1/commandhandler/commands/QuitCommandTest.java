package fil.sr1.commandhandler.commands;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.times;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import fil.sr1.commandhandler.CommandHandlerTest;

public class QuitCommandTest extends CommandHandlerTest {
    QuitCommand command;

    @Before
    public void setupBeforeEach() {
        super.setupBeforeEach();
        command = new QuitCommand(clientFtp);
        try {
            Mockito.doNothing().when(clientFtp).close();
        } catch (IOException e) {
            System.err.println("Error occurred while trying to mock close() from clientFtp.");
        }
    }

    @Test
    public void sendMessage221GoodBye_whenClientFtpIsSuccessfullyClosed() throws IOException {
        command.handle();

        Mockito.verify(clientFtp).sendMessage(captor.capture());
        assertTrue(captor.getValue().contains("221"));
    }

    @Test
    public void ftpClientCloseIsCalled_whenQuitCommandIsRequested() throws IOException {
        command.handle();
        Mockito.verify(clientFtp, times(1)).close();
    }
}
