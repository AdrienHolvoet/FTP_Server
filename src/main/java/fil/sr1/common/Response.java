package fil.sr1.common;

/**
 * Represents the responses sent by the server to the client according to the
 * commands sent
 *
 * @author Adrien Holvoet and Anthony Mendez
 */

public final class Response {
	private Response() {
		// Constructor made private to be sure this class is not instantiated
	}
	// All static final because there are all constants

	// Welcome
	public static final String WELCOME_MESSAGE = "220 Ftp Sever, Welcome.";

	// USER Command
	public static final String PLEASE_LOGIN = "530 Please login with USER and PASS.";
	public static final String USER_OK = "331 Please specify the password.";
	public static final String USER_NOK = "530 The user does not exist.";

	// PASS Command
	public static final String PASS_OK = "230 Login successful.";
	public static final String PASS_NOK = "530 The password is wrong.";

	public static final String SYST = "215 UNIX Type: L8";

	// PWD Command
	public static final String PWD_OK = "257 '%s' is the current directory";
	public static final String PWD_NOK = "550 Directory does not exist";

	// PASV and PORT Command
	public static final String PASV_OK = "227 Entering Passive Mode(%s)";
	public static final String PORT_OK = "200 Port command successful";
	public static final String DATA_CHANNEL_NOK = "425 Can't open data connection";

	// LIST Command
	public static final String START_LIST_OK = "150 Here comes the directory listing ";
	public static final String END_LIST_OK = "226 Directory send OK";
	public static final String LIST_NOK = "425 Can't open data connection";
	public static final String LIST_NOT_UNIX = "451 the server can only be used on Unix System";

	// CWD Command
	public static final String CWD_OK = "250 Directory successfully changed.";
	public static final String CWD_NOK = "550 Failed to change directory.";

	// CDUP Command
	public static final String CDUP_OK = "250 Directory successfully changed.";

	// QUIT Command
	public static final String QUIT_OK = "221 Goodbye.";

	// MKD Command
	public static final String MKD_OK = "257 %s directory created.";
	public static final String MKD_NOK_EXIST = "521 %s directory already exists.";
	public static final String FILE_NOT_FOUND = "550 Requested action not taken(no access, file unavailable, ...)";

	// RMD Command
	public static final String RMD_OK = "250 %s directory removed.";
	public static final String RMD_DIR_NOT_FOUND = "550 directory %s doesn't exists.";
	public static final String RMD_NOT_A_DIR = "550 %s is not a directory";
	public static final String RMD_DIR_NOT_EMPTY = "550 Could not remove %s because the directory is not empty.";

	// STOR and RETR Commands
	public static final String TRANSFERT_START = "125 Start tranferring.";
	public static final String TRANSFERT_OK = "226 File successfully transferred.";
	public static final String TRANSFERT_NOK = "451 Error transferring file.";
	public static final String TRANSFERT_OPEN_BINARY = "150 Opening BINARY mode data connection for %s.";

	// RNFR Command
	public static final String BAD_COMMAND_SEQUENCE = "503 Bad sequence of commands.";
	public static final String PENDING_FURTHER_INFORMATION = "350 Requested file action pending further information.";
	public static final String FILE_ALREADY_EXISTS = "553 Requested action not taken. (target file name already exist.";
	public static final String FILE_NAME_CHANGED = "250 File name successfully changed.";

	// Commande DELE
	public static final String DELE_OK = "200 file has been deleted";
	public static final String DELE_NOK = "550 Cannot delete file";

	public static final String COMMAND_OK = "200 Command Okay.";
	public static final String SERVICE_READY = "220 Service ready for new user.";
	public static final String ENTERING_PASV_MODE = "227 Entering Passive Mode";
	public static final String COMMAND_NOT_IMPLEMENTED = "202 Command not implemented, superfluous at this site.";
	public static final String NO_DATA_CHANNEL = "425 Use PORT or PASV first.";
	public static final String NOT_ALLOW = "550 Permission denied.";
}
