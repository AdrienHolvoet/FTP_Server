package fil.sr1.commandhandler.commands;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import fil.sr1.commandhandler.CommandHandlerTest;
import fil.sr1.common.Response;
import fil.sr1.implementation.User;

public class UserCommandTest extends CommandHandlerTest {
    private final String USERNAME = "username";

    UserCommand command;
    User user;

    @Before
    public void setupBeforeEach() {
        super.setupBeforeEach();
        command = new UserCommand(USERNAME, clientFtp);
        user = Mockito.mock(User.class);
        Mockito.doReturn(user).when(clientFtp).getUser();
    }

    @Test
    public void sendMessage331Ok_WhenCorrectUserIsSent() throws IOException {
        Mockito.doReturn(true).when(user).checkUser(Mockito.anyString());
        command.handle();
        Mockito.verify(clientFtp).sendMessage(captor.capture());

        assertEquals(Response.USER_OK, captor.getValue());
    }

    @Test
    public void sendMessage530Nok_WhenUserDoesNotExist() throws IOException {
        Mockito.doReturn(false).when(user).checkUser(Mockito.anyString());
        command.handle();
        Mockito.verify(clientFtp).sendMessage(captor.capture());

        assertEquals(Response.USER_NOK, captor.getValue());
    }
}
