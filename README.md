# Application «Serveur FTP»

Anthony Mendez

Adrien Holvoet

04/03/2021

## Introduction

Cette application met en oeuvre un serveur conforme au protocole applicatif File Transfer Protocol (FTP). Il est implémenté en Java avec l'API Socket TCP et peut échanger avec n'importe quel client FTP qui respecte les standards FTP.

## Comment l'utiliser ?

La javadoc a déjà été générée, elle est placée dans le dossier "doc/javadoc", il est possible de la regénérer dans le dossier target qui se créera automatiquement.
Pour générer la Javadoc :
`mvn javadoc:javadoc`  
L'exécutable a déjà été généré et il se trouve dans le dossier doc/executable du projet.
Il suffit donc de se placer dans ce directory et de lancer la commande :

```
java -jar ServeurFtp.jar <repository_path> <port_number> <max_user(optionnal)>
```

Le premier argument est le répertoire racine du serveur, le second est le numéro de port sur lequel le serveur ftp écoutera et le troisième est le nombre d'utilisateurs pouvant se connecter simultanément au serveur FTP (ce nombre est 5 par défaut). Les deux premiers arguments sont obligatoires, le dernier est optionnel.

Il est également possible de regénérer le .jar en allant se placer à la racine du projet et en lançant la commande maven : `mvn package`
l'exécutable se trouvera dans le répertoire "doc/executable" du projet.

**Démo** : on retrouve dans le dossier _doc_ le fichier _demo.mkv_, celui-ci est une vidéo démo d'utilisation du Serveur Ftp mettant en relief l'ensemble des commandes implémentées.

## Fonctionnalités

| Commande |                                          Description                                           |
| -------- | :--------------------------------------------------------------------------------------------: |
| CDUP     |                               Transformer en répertoire parent.                                |
| CWD      |                               Changer le répertoire de travail.                                |
| DELE     |                                     Supprimer un fichier.                                      |
| LIST     | Affiche les informations d'un fichier ou d'un répertoire spécifique, ou du répertoire courant. |
| MKD      |                                      Créer un répertoire                                       |
| PASS     |                                         Mot de passe.                                          |
| PASV     |                                   Connexion en mode passif.                                    |
| PORT     |                         Spécifier une adresse et un port de connexion.                         |
| PWD      |               Afficher le répertoire de travail actuel sur la machine distante.                |
| QUIT     |                                          Déconnecter.                                          |
| RETR     |                                Récupérer la copie d'un fichier                                 |
| RMD      |                                    Supprimer un répertoire                                     |
| RNFR     |                                Fichier à renommer (rename from)                                |
| RNTO     |                                    Renommer en (rename to)                                     |
| STOR     |            Accepter les données et les enregistrer dans un fichier sur le serveur.             |
| SYST     |                                   Afficher le type système.                                    |
| USER     |                                Nom d'utilisateur, identifiant.                                 |

## Architecture

La classe Main du package sr1 est la classe principale du projet. C'est elle qui va prendre en compte les arguments passés à l'application et qui va démarrer le serveur FTP.
Une fois démarré, le serveur va se mettre à l'écoute des connexions entrantes des clients. Lorsqu'un client se connecte au serveur, le serveur va lui attribuer un profil défini par la classe ClientFtp.
Ce profil va contenir un status mais aussi des méthodes permettant la communication (envoie/lecture de messages) entre le serveur et le client.

### Classes/Interfaces

- _CommandHandler_ : Interface qui définit ce qu'un gestionnaire de commandes doit faire. Les gestionnaires de commandes sont créés par la classe _CommandHandlerFactory_ et sont utilisés par le client FTP.

- _CommandHandlerFactory_ : Classe Singleton, c'est le point centrale qui récupère toutes les commandes envoyées par le client et renvoie le gestionnaire de commande correspondant.

- _CdupCommand_ : Classe qui implémente l'interface _CommandHandler_. Celle-ci gère la commande FTP CDUP

- _CwdCommand_ : Classe qui implémente l'interface _CommandHandler_. Celle-ci gère la commande FTP CWD

- _DeleCommand_ : Classe qui implémente l'interface _CommandHandler_. Celle-ci gère la commande FTP DELE

- _ListCommand_ : Classe qui implémente l'interface _CommandHandler_. Celle-ci gère la commande FTP LIST

- _MkdCommand_ : Classe qui implémente l'interface _CommandHandler_. Celle-ci gère la commande FTP MKD

- _PassCommand_ : Classe qui implémente l'interface _CommandHandler_. Celle-ci gère la commande FTP PASS

- _PasvCommand_ : Classe qui implémente l'interface _CommandHandler_. Celle-ci gère la commande FTP PASV

- _PortCommand_ : Classe qui implémente l'interface _CommandHandler_. Celle-ci gère la commande FTP PORT

- _PwdCommand_ : Classe qui implémente l'interface _CommandHandler_. Celle-ci gère la commande FTP PWD

- _QuitCommand_ : Classe qui implémente l'interface _CommandHandler_. Celle-ci gère la commande FTP QUIT

- _RetrCommand_ : Classe qui implémente l'interface _CommandHandler_. Celle-ci gère la commande FTP RETR

- _RmdCommand_ : Classe qui implémente l'interface _CommandHandler_. Celle-ci gère la commande FTP RMD

- _RnfrCommand_ : Classe qui implémente l'interface _CommandHandler_. Celle-ci gère la commande FTP RNFR

- _RntoCommand_ : Classe qui implémente l'interface _CommandHandler_. Celle-ci gère la commande FTP RNTO

- _StorCommand_ : Classe qui implémente l'interface _CommandHandler_. Celle-ci gère la commande FTP STOR

- _SystCommand_ : Classe qui implémente l'interface _CommandHandler_. Celle-ci gère la commande FTP SYST

- _UserCommand_ : Classe qui implémente l'interface _CommandHandler_. Celle-ci gère la commande FTP USER

- _Response_ : Classe Singleton représente les réponses envoyées par le serveur au client en fonction des commandes envoyées.

- _ClientFtp_ : Classe qui caractérise la session avec un client et permet l'échange de commandes entre le client et le serveur.

- _DataSocket_ : Classe qui caractérise le canal de données et permet donc l'échange de données entre le client et le serveur.

- _ServerFtp_ : Classe Singleton qui crée un ServerSocket sur un port spécifique et attend les connexions tant qu'elle n'est pas fermée.

- _User_ : Classe qui gère tous les statuts des utilisateurs essayant de se connecter au serveur. Elle gère donc également la connexion d'un utilisateur anonyme ou connu.

### Gestion d'erreur

De manière générale la gestion des exceptions a pour but de notifier le client pas un code de réponse FTP NOK lorsque une erreur occurre lors de la gestion d'une des commandes.

#### Catch

| Où? (classe)          | Exception                                    | Catch quand                                                                                                                                                                                                                                                                        | Réponse                                                                                                                                                          |
| --------------------- | -------------------------------------------- | ---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- | ---------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| Main                  | ServerSocketException                        | le port spécifié lors du lancement du serveur est déjà utilis                                                                                                                                                                                                                      | Lance une WrongArgumentException (plus spécifique que ServerSocketException)                                                                                     |
| CommandHandlerFactory | IllegalArgumentException                     | une commande envoyée par le client n'est pas implémentée par le serveur                                                                                                                                                                                                            | Lance une IllegalCommandException (plus spécifique que IllegalArgumentException)                                                                                 |
| ListCommand           | IOException \| UnsupportedOperationException | une des opérations d'I/O a échouée ou interrompue ou quand on essaie de lister un dossier qui n'est pas sur un système Unix                                                                                                                                                        | Envoie 2 messages au client, le premier avec un code 451 (the server can only be used on Unix System) et le second avec un code 425 (Can't open data connection) |
| PasvCommand           | ServerSocketException                        | une erreur survient lors d'un processus de création de ServerSocket(= canal de données) en mode passif                                                                                                                                                                             | Envoie d'un message au client avec un code 425 (Can't open data connection)                                                                                      |
| PortCommand           | SocketException                              | une erreur survient lors d'un processus de création d'un Socket(= canal de données) en mode actif                                                                                                                                                                                  | Envoie d'un message au client avec un code 425 (Can't open data connection)                                                                                      |
| RetrCommand           | IOException                                  | il y a une erreur I/O lors de la copie du fichier local vers le serveur                                                                                                                                                                                                            | Envoie d'un message au client avec un code 451 (Error transferring file)                                                                                         |
| RmdCommand            | DirectoryNotEmptyException                   | on essaye de supprimer un dossier non vide sur le serveur                                                                                                                                                                                                                          | Envoie d'un message au client avec un code 550 (Could not remove X because the directory is not empty)                                                           |
| StorCommand           | IOException                                  | il y a une erreur I/O lors de la copie du fichier du serveur vers le client                                                                                                                                                                                                        | Envoie d'un message au client avec un code 451 (Error transferring file)                                                                                         |
| ClientFtp             | Exception                                    | une erreur inconnue arrive lors dans la session ouverte entre le serveur et le client                                                                                                                                                                                              | On tente de fermer le socket utilisé par le client, ainsi que son printer et reader                                                                              |
| ClientFtp             | IOException                                  | il y a une erreur I/O lors de la fermeture de la session serveur/client                                                                                                                                                                                                            | Lance une SocketException (plus spécifique qu'IOException)                                                                                                       |
| ClientFtp             | IllegalCommandException                      | une commande envoyée par le client n'est pas implémentée par le serveur ou quand un client essaye d'utiliser une commande à laquelle il n'a pas les accès nécessaires                                                                                                              | Envoie d'un message au client avec le message transmis dans l'exception                                                                                          |
| DataSocket            | IOException                                  | il y a une erreur I/O lors de la création du ServerSocket qui alloue un port disponible dynamiquement, lors de la création d'un socket en mode actif, lors ce que le canal n'accepte pas de connection, lors d'une erreur pendant la fermeture ou l'envoie sur le canal de données | Lance une SocketException (plus spécifique qu'IOException)                                                                                                       |
| DataSocket            | Exception                                    | il y a une erreur inconnue lors de la copie d'un fichier du serveur(resp. client) vers le client(resp. serveur)                                                                                                                                                                    | Lance une IOException (plus spécifique qu'Exception)                                                                                                             |
| ServerFtp             | IOException                                  | il y a une erreur I/O lors de la création du ServerSocket représentant le Serveur Ftp                                                                                                                                                                                              | Lance une ServerSocketException (plus spécifique qu'IOException)                                                                                                 |
| ServerFtp             | SocketException                              | on essaye de fermer le server quand il est encore en écoute de connection                                                                                                                                                                                                          | Relance SocketException avec le message "The server is closed."                                                                                                  |
| ServerFtp             | Exception                                    | il un a une erreur inconnue quand le serveur est démarré et en écoute de connection                                                                                                                                                                                                | Lance une ServerSocketException (plus spécifique qu'Exception)                                                                                                   |

#### Throw

- Dans **Main.java** :
  - throw new _WrongArgumentException_ : stoppe le serveur quand il y a un mauvais argument lors du lancement de l'exécutable (port, répertoire racine, ... )
- Dans **CommandHandlerFactory.java** :
  - throw new _IllegalCommandException_ : jetée lorsqu'une mauvaise commande est envoyée par le client( commande pas implémentée, pas les droits d'accès, mauvaise suite de commande, ...). Quand elle est catch, celle-ci envoie un message NOK au client sur le canal de commande
- Dans **ListCommand.java** :
  - throws _IOException_ : jetée lors d'une erreur quand on ajoute des métadonnées aux fichiers. Quand elle est catch, celle-ci envoie un message NOK au client sur le canal de commandes.
- Dans **PortCommand.jav** :
  - throw new _SocketException_ : jetée lorsque l'adresse IP envoyée par le client n'est pas bonne . Quand elle est catch, celle-ci envoie un message NOK au client sur le canal de commandes.
- Dans **QuitCommand.java** :
  - throws _IOException_ : stoppe la session client/serveur quand il y a une erreur lors de la fermeture de celle-ci
- Dans **ClientFtp.java** :
  - throw new _IOException_ : Stoppe la session client/serveur quand une erreur survient lors de la création/initialisation de celle-ci.
  - throw new _RuntimeException_ : Stoppe la session client/serveur quand une erreur survient lors de la fermeture de celle-ci.
  - throws _IOException_ : jetée lors d'une erreur sur la lecture/écriture sur le canal de commandes. Quand elle est catch, celle-ci fermera le Serveur
  - throws _ServerSocketException_ : jetée lors d'une erreur lors de la création du canal de données en mode passif. Quand elle est catch, celle-ci envoie un message NOK au client sur le canal de commandes.
  - throws _SocketException_ : jetée lors d'une erreur lors de la création du channel de donnée en mode actif. Quand elle est catch, celle-ci envoie un message NOK au client sur le canal de commandes.
- Dans **DataSocket.java** :

  - throw new _ServerSocketException_ : jetée lors d'une erreur lors de la création du channel de donnée en mode passif. Quand elle est catch, celle-ci envoie un message NOK au client sur le canal de commandes.
  - throw new _SocketException_ : jetée lors d'une erreur lors de la création du channel de donnée en mode actif ou lors de l'utilisation de celui-ci. Quand elle est catch, celle-ci envoie un message NOK au client sur le canal de commandes.
  - throw new _IOException_ : jetée lors d'une erreur lors du stockage/récupération dun fichier/dossier sur le serveur. Quand elle est catch, celle-ci envoie un message NOK au client sur le canal de commandes.

- Dans **ServerFtp.java** :

  - throw new _ServerSocketException_ : stoppe le serveur lors d'une erreur lors du démarrage de celui-ci ou lorsqu'une erreur occurre pendant qu'il est en attente de nouvelles connections.
  - throw new _SocketException_ : stoppe le serveur lorsqu'on essaye de l'éteindre alors qu'il essaye de toujours écouter de nouvelles connections.

- Dans **User.java** :
  - throw new _IOException_ : jetée lors d'une erreur quand on récupère les utilisateurs du système. Quand elle est catch, celle-ci envoie un message NOK au client sur le canal de commandes.

## Code samples

### Extrait 1

Cette méthode est privée car seulement utilisée dans la classe _ListCommand_. Elle permet de formater la liste des dossiers envoyée dans un format ressemblant à _ls -al_ sur linux. Elle permet donc d'ajouter les droits, le type, l'heure, le propriétaire, le groupe et le nom du fichier.

```
	private String formatFileWithMetadata(File file) throws IOException {

		Path filePath = file.toPath();

		PosixFileAttributes posixFileAttributes = Files.readAttributes(filePath, PosixFileAttributes.class);
		BasicFileAttributes basicFileAttributes = Files.readAttributes(filePath, BasicFileAttributes.class);

		String typeOfFile = "-";

		if (posixFileAttributes.isSymbolicLink()) {
			typeOfFile = "l";
			return null;
		}
		if (posixFileAttributes.isDirectory()) {
			typeOfFile = "d";
		}

		SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd HH:mm", Locale.ENGLISH);

		return String.format("%s %s %s %s %s %s \r\n",
				typeOfFile + PosixFilePermissions.toString(posixFileAttributes.permissions()),
				posixFileAttributes.owner(), posixFileAttributes.group(), file.length(),
				dateFormat.format(basicFileAttributes.lastModifiedTime().toMillis()), file.getName());
	}
```

### Extrait 2

Cette méthode est privée car seulement utilisée dans _ServerFtp_, elle est utilisée une fois que le serveur est démarré, celle-ci attend une connexion d'un nouveau client et crée une nouvelle session avec celui-ci. Elle lance un nouveau thread pour chaque client et gère le nombre maximum de thread possible en même temps pour éviter une surcharge.

```
    private void waitingConnection() throws ServerSocketException, SocketException {

    	// Used to control the number of threads the application is creating
    	executor = Executors.newFixedThreadPool(maxUser);
    	try {
    		while (true) {
    			client = serverSocket.accept();
    			ClientFtp clientFtp = new ClientFtp(client, home);
    			executor.execute(clientFtp);
    		}
    	} catch (SocketException e) {
    		// Exception throw when by accept we call serverSocket.close(), it means that
    		// the serverSocket is close
    		throw new SocketException("The server is close");
    	} catch (Exception e) {
    		throw new ServerSocketException(e.getMessage());
    	}
    }
```

### Extrait 3

Cette méthode permet de créer un canal de données en mode passif. Elle crée un DataServerSocket qui génère un port disponible et renvoie l'adresse sur laquelle se connecter au client sous la forme (IP1,IP2,IP3,IP4,PORT1,PORT2) avec PORT1 = (port généré)/256 et PORT2 = (port généré) % 256

```
    /**
	 *
	 * Create and send the address of the data channel in passive mode to the client
	 * to this format(IP1,IP2,IP3,IP4,PORT1,PORT2)
	 */
	@Override
	public void handle() {
		try {
			// Create a new data channel in passive mode
			clientFtp.setDataSocket();
			String address = getHostAdress();
			int port = clientFtp.getDataSocket().getLocalPort();
			int port1 = (port / PORT_MULTIPLIER);
			int port2 = port % PORT_MULTIPLIER;
			clientFtp.sendMessage(String.format(Response.PASV_OK, address + "," + port1 + "," + port2));
			// listen new socket connection on data channel
			clientFtp.getDataSocket().acceptConnection();
		} catch (ServerSocketException e) {
			clientFtp.sendMessage(Response.DATA_CHANNEL_NOK);
		}
	}

```

### Extrait 4

Cet extrait se trouve dans _CommandHandlerFactory_, il permet de rediriger les commandes et arguments envoyés par le client vers les bons handlers. Il permet également de gérer les problèmes de droits d'accès (utilisateur non connecté, connecté en anonyme, accession au canal de données non permis, ... )

```
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
            ...
```

### Extrait 5

Cette méthode contient la logique permettant de traiter la commande ftp "RETR" qui télécharge une copie d'un fichier (du serveur)

```
	public void retrieveFile(File file) throws IOException {
		try {
			if (!file.exists()) {
				// To stop the process and notify the client
				throw new IOException("File not found");
			} else {
				dataSocketOutput = dataSocket.getOutputStream();
				FileInputStream fileInput = new FileInputStream(file);
				int size = dataSocket.getReceiveBufferSize();
				byte[] buffer = new byte[size];
				int line;
				while ((line = fileInput.read(buffer)) != -1) {
					// Write all the bytes of the server's file in the newly created file
					dataSocketOutput.write(buffer, 0, line);
				}
				fileInput.close();
				// Close data channel
				close();
				dataSocketOutput.close();
				dataSocketOutput.flush();

			}
		} catch (Exception e) {
			// To stop the process and notify the client
			throw new IOException(e);
	}
```
