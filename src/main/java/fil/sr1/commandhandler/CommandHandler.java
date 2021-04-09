package fil.sr1.commandhandler;

import java.io.IOException;

/**
 * Define what a command handler should do. CommandHandlers are build by the
 * CommandHandlerFactory class and are used by ClientFTP.
 *
 * @author Adrien Holvoet and Anthony Mendez
 */
public interface CommandHandler {
	void handle() throws IOException;
}
