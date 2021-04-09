package fil.sr1.commandhandler;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import fil.sr1.implementation.ClientFtp;
import fil.sr1.implementation.ServerFtp;

public class CommandHandlerTest {
	// used for file manipulation
	protected static final String TMP_DIR = "tmp_dir";
	protected static final String TMP_FILE_ONE = "tmp_file_1";
	protected static final String TMP_FILE_TWO = "tmp_file_2";
	protected static final String FULL_PATH_NAME = System.getProperty("user.dir");

	protected static ClientFtp clientFtp;

	protected ArgumentCaptor<String> captor;

	@BeforeClass
	public static void setup() throws IOException {
		ServerFtp.home = FULL_PATH_NAME;
		cleanTmpDirAndFiles();
		generateTmpDirAndFiles();
	}

	@Before
	public void setupBeforeEach() {
		clientFtp = Mockito.mock(ClientFtp.class);
		captor = ArgumentCaptor.forClass(String.class);
	}

	@AfterClass
	public static void clean() throws IOException {
		cleanTmpDirAndFiles();
	}

	private static void generateTmpDirAndFiles() throws IOException {
		Files.createDirectory(Paths.get(ServerFtp.home, TMP_DIR));
		Files.createFile(Paths.get(ServerFtp.home, TMP_DIR, TMP_FILE_ONE));
	}

	private static void cleanTmpDirAndFiles() throws IOException {
		Files.deleteIfExists(Paths.get(ServerFtp.home, TMP_DIR, TMP_FILE_ONE));
		Files.deleteIfExists(Paths.get(ServerFtp.home, TMP_DIR, TMP_FILE_TWO));
		Files.deleteIfExists(Paths.get(ServerFtp.home, TMP_DIR));
	}

}
