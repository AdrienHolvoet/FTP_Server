package fil.sr1.commandhandler.commands;

import static org.junit.Assert.assertEquals;

import java.net.SocketException;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import fil.sr1.commandhandler.CommandHandlerTest;
import fil.sr1.common.Response;
import fil.sr1.exceptions.ServerSocketException;
import fil.sr1.implementation.User;

public class PassCommandTest extends CommandHandlerTest {
    PassCommand command;

    private final String GOOD_MOCKED_PASSWORD = "good-mocked-password";
    private final String WRONG_MOCKED_PASSWORD = "wrong-mocked-password";

    @Before
    public void setupBeforeEach() {
        super.setupBeforeEach();
    }

    @Test
    public void sendMessage230Ok_whenPassIsCorrect() throws SocketException, ServerSocketException {
        command = new PassCommand(GOOD_MOCKED_PASSWORD, clientFtp);
        User user = Mockito.mock(User.class);
        Mockito.doReturn(user).when(clientFtp).getUser();
        Mockito.doReturn(true).when(user).authenticate(GOOD_MOCKED_PASSWORD);
        command.handle();

        Mockito.verify(clientFtp).sendMessage(captor.capture());
        assertEquals(Response.PASS_OK,captor.getValue());
    }

    @Test
    public void sendMessage530Nok_whenPassIsCorrect() throws SocketException, ServerSocketException {
        command = new PassCommand(WRONG_MOCKED_PASSWORD, clientFtp);
        User user = Mockito.mock(User.class);
        Mockito.doReturn(user).when(clientFtp).getUser();
        Mockito.doReturn(false).when(user).authenticate(WRONG_MOCKED_PASSWORD);
        command.handle();

        Mockito.verify(clientFtp).sendMessage(captor.capture());
        assertEquals(Response.PASS_NOK,captor.getValue());
    }

}
