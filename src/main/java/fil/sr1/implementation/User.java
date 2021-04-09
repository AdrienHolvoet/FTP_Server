package fil.sr1.implementation;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Scanner;

import fil.sr1.common.UserStatus;

/**
 * The purpose of this class is to manage all the statuses of users trying to
 * connect to the server. It therefore also manages the connection of an
 * anonymous or known user
 * 
 * @author Adrien Holvoet and Anthony Mendez
 */
public class User {

	// Private constant only use in this class
	private static final String ANONYMOUS = "anonymous";
	private static final String USER_DB = "src/main/java/fil/sr1/common/user.txt";

	// Private because only use in this class
	private UserStatus userStatus = UserStatus.NOTLOGGEDIN;
	private String username;
	private String pass;

	/**
	 * Check if the pass sent match the password in the file
	 * 
	 * @param pass the pass enter in the command
	 * @return true if the pass is good false otherwise
	 */
	public boolean authenticate(String pass) {
		if (userStatus == UserStatus.ANONYMOUS) {
			return true;
		}

		if (pass.equals(this.pass)) {
			userStatus = UserStatus.LOGGEDIN;
			return true;
		} else {
			userStatus = UserStatus.NOTLOGGEDIN;
			return false;
		}
	}

	/**
	 * Check if the username exists(in a file) or if the user is anonymous
	 * 
	 * @param username the username enter in the command
	 * @return true if the user exist false otherwise
	 * @throws IOException
	 */
	public boolean checkUser(String username) throws IOException {
		if (username.equals(ANONYMOUS) || username.equals(ANONYMOUS.toUpperCase())) {
			userStatus = UserStatus.ANONYMOUS;
			this.username = username;
			return true;
		}

		// Input file
		FileInputStream file;
		try {
			file = new FileInputStream(USER_DB);
			Scanner scanner = new Scanner(file);
			boolean userFind = false;
			while (scanner.hasNextLine() && !userFind) {
				if (scanner.nextLine().equals(username)) {
					this.username = username;
					this.pass = scanner.nextLine();
					userFind = true;
					userStatus = UserStatus.GAVENAME;
				}
			}
			scanner.close();
			return userFind;
		} catch (IOException e) {
			throw new IOException(e);
		}
	}

	public UserStatus getUserStatus() {
		return userStatus;
	}

	public void setUserStatus(UserStatus userStatus) {
		this.userStatus = userStatus;
	}

	public String getUserName() {
		return username;
	}

	public void setUserName(String username) {
		this.username = username;
	}
}
