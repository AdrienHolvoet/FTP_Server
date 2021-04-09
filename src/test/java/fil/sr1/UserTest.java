package fil.sr1;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.junit.BeforeClass;
import org.junit.Test;

import fil.sr1.common.UserStatus;
import fil.sr1.implementation.User;

public class UserTest {

	private static String correctUser;
	private static String correctPassword;
	private static String wrongUser;
	private static String wrongPassword;
	private static String anonymous;
	private User user;

	@BeforeClass
	public static void setup() {
		correctUser = "root";
		correctPassword = "root";
		wrongUser = "wrong";
		wrongPassword = "wrong";
		anonymous = "anonymous";
	}

	@Test
	public void checkUser_returnTrueAndStatusAnonymous_WhenAnonymousUserTryToConnect() throws IOException {

		user = new User();

		boolean existUser = user.checkUser(anonymous);

		assertTrue(existUser);
		assertEquals(UserStatus.ANONYMOUS, user.getUserStatus());
		assertEquals(user.getUserName(), anonymous);
	}

	@Test
	public void checkUser_returnTrueAndStatusNotLoggedin_WhenWrongUserTryToConnect() throws IOException {

		user = new User();

		boolean existUser = user.checkUser(wrongUser);

		assertFalse(existUser);
		assertEquals(UserStatus.NOTLOGGEDIN, user.getUserStatus());
	}

	@Test
	public void checkUser_returnTrueAndStatusGavename_whenCorrectUserTryToConnect() throws IOException {

		user = new User();

		boolean existUser = user.checkUser(correctUser);

		assertTrue(existUser);
		assertEquals(UserStatus.GAVENAME, user.getUserStatus());
		assertEquals(user.getUserName(), correctUser);
	}

	@Test
	public void authenticate_returnTrueAndStatusLoggein_whenCorrectUserTryToAuthenticate() throws IOException {

		user = new User();

		user.checkUser(correctUser);
		boolean authenticate = user.authenticate(correctPassword);

		assertTrue(authenticate);
		assertEquals(UserStatus.LOGGEDIN, user.getUserStatus());
	}

	@Test
	public void authenticate_returnTrueAndStatusAnonymous_whenAnonymousUserTryToAuthenticate() throws IOException {

		user = new User();

		user.checkUser(anonymous);
		boolean authenticate = user.authenticate(anonymous);

		assertTrue(authenticate);
		assertEquals(UserStatus.ANONYMOUS, user.getUserStatus());
	}

	@Test
	public void authenticate_returnFalseAndStatusNotLoggedin_whenAnonymousUserTryToAuthentiicate() throws IOException {

		user = new User();

		user.checkUser(correctUser);
		boolean authenticate = user.authenticate(wrongPassword);

		assertFalse(authenticate);
		assertEquals(UserStatus.NOTLOGGEDIN, user.getUserStatus());
	}

}
