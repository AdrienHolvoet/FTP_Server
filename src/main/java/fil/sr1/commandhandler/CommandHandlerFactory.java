package fil.sr1.commandhandler;

import fil.sr1.commandhandler.commands.CdupCommand;
import fil.sr1.commandhandler.commands.CwdCommand;
import fil.sr1.commandhandler.commands.DeleCommand;
import fil.sr1.commandhandler.commands.ListCommand;
import fil.sr1.commandhandler.commands.MkdCommand;
import fil.sr1.commandhandler.commands.PassCommand;
import fil.sr1.commandhandler.commands.PasvCommand;
import fil.sr1.commandhandler.commands.PortCommand;
import fil.sr1.commandhandler.commands.PwdCommand;
import fil.sr1.commandhandler.commands.QuitCommand;
import fil.sr1.commandhandler.commands.RetrCommand;
import fil.sr1.commandhandler.commands.RmdCommand;
import fil.sr1.commandhandler.commands.RnfrCommand;
import fil.sr1.commandhandler.commands.RntoCommand;
import fil.sr1.commandhandler.commands.StorCommand;
import fil.sr1.commandhandler.commands.SystCommand;
import fil.sr1.commandhandler.commands.UserCommand;
import fil.sr1.common.Command;
import fil.sr1.common.Response;
import fil.sr1.common.UserStatus;
import fil.sr1.exceptions.IllegalCommandException;
import fil.sr1.implementation.ClientFtp;

/**
 * Create command handler to handle FTP commands. It is used by the ClientFtp.
 *
 * @author Adrien Holvoet and Anthony Mendez
 */
public final class CommandHandlerFactory {
	private CommandHandlerFactory() {
		// No need to instantiate CommandHandlerFactory as it has only one method and it
		// is static.
	}

	/**
	 * Return the command handler matching the FTP command and its argument received
	 * if the user has the right status. If the user status is : NOTLOGGEDIN, the
	 * only acceptable command is USER. GAVENAME, the only acceptable command is
	 * PASS. LOGGEDIN, any existing (and handled) command can be handled. ANONYMOUS
	 * some commands are prohibited
	 *
	 * @param command FTP command sent by the FTP client
	 * @return a command handler
	 * @throws IllegalCommandException if there is no command handler matching the
	 *                                 incoming command
	 */
	public static CommandHandler build(String command, ClientFtp clientFtp) throws IllegalCommandException {
		System.out.println("DEBUG : Commande sent by client = " + command);
		String[] commandAndArg = command.split(" ");
		Command commandName;

		// The command might not matches an existing Command.enum, which would throw an
		// IllegalArgumentException
		try {
			commandName = Command.valueOf(commandAndArg[0].toUpperCase());
		} catch (IllegalArgumentException e) {
			throw new IllegalCommandException(Response.COMMAND_NOT_IMPLEMENTED);
		}

		String arg = "";
		// check if there is an argument for the command
		if (commandAndArg.length > 1) {
			for (int i = 1; i < commandAndArg.length; i++) {
				arg += commandAndArg[i];
				if (i != commandAndArg.length - 1) {
					arg += " ";
				}
			}
		}

		// Check if the user try commands when not logged in yet
		if (clientFtp.getUser().getUserStatus() == UserStatus.NOTLOGGEDIN && commandName != Command.USER) {
			throw new IllegalCommandException(Response.PLEASE_LOGIN);
		} else if (clientFtp.getUser().getUserStatus() == UserStatus.GAVENAME && commandName != Command.PASS) {
			throw new IllegalCommandException(Response.USER_OK);
		}

		// Check if the client try to use data channel when it is not yet configured
		if ((clientFtp.getDataSocket() == null || clientFtp.getDataSocket().isClosed())
				&& (commandName == Command.LIST || commandName == Command.STOR || commandName == Command.RETR)) {
			throw new IllegalCommandException(Response.NO_DATA_CHANNEL);
		}

		// To prohibit certain commands from the anonymous user
		if (clientFtp.getUser().getUserStatus() == UserStatus.ANONYMOUS && notAllowedAnonymousCommand(commandName)) {
			throw new IllegalCommandException(Response.NOT_ALLOW);
		}

		// return the right command handler
		switch (commandName) {
		case USER:
			return new UserCommand(arg, clientFtp);
		case PASS:
			return new PassCommand(arg, clientFtp);
		case SYST:
			return new SystCommand(clientFtp);
		case PWD:
			return new PwdCommand(clientFtp);
		case PASV:
			return new PasvCommand(clientFtp);
		case PORT:
			return new PortCommand(arg, clientFtp);
		case LIST:
			return new ListCommand(clientFtp);
		case CWD:
			return new CwdCommand(arg, clientFtp);
		case CDUP:
			return new CdupCommand(clientFtp);
		case MKD:
			return new MkdCommand(arg, clientFtp);
		case STOR:
			return new StorCommand(arg, clientFtp);
		case RETR:
			return new RetrCommand(arg, clientFtp);
		case RNFR:
			return new RnfrCommand(arg, clientFtp);
		case RNTO:
			return new RntoCommand(arg, clientFtp);
		case RMD:
			return new RmdCommand(arg, clientFtp);
		case DELE:
			return new DeleCommand(arg, clientFtp);
		case QUIT:
			return new QuitCommand(clientFtp);
		default:
			throw new IllegalCommandException(Response.COMMAND_NOT_IMPLEMENTED);
		}
	}

	/**
	 * Private method because only used in this class, it returns the set of
	 * commands prohibit for an anonymous user
	 */
	private static boolean notAllowedAnonymousCommand(Command commandName) {
		return commandName.equals(Command.MKD) || commandName.equals(Command.STOR) || commandName.equals(Command.RETR)
				|| commandName.equals(Command.RNFR) || commandName.equals(Command.RMD)
				|| commandName.equals(Command.DELE);
	}
}
