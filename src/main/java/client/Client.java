package client;

import client.config.Configuration;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    private final Logger logger = Logger.getLogger(Client.class);
    private final Configuration config;
    private final int port;
    private final String serverHost;
    private final String usernameCommand;
    private final String exitCommand;
    private final String name;
    private Socket clientSocket;
    private Scanner messageInput;
    private PrintWriter messageOutput;

    protected Client(String name) throws IOException {
        config = new Configuration();
        serverHost = config.getServerHost();
        port = config.getPort();
        usernameCommand = config.getUsernameCommand();
        exitCommand = config.getExitCommand();
        this.name = name;
        try {
            clientSocket = new Socket(serverHost, port);
            logger.debug(String.format("Client connection to %s:%s", serverHost, port));
            messageInput = new Scanner(clientSocket.getInputStream());
            messageOutput = new PrintWriter(clientSocket.getOutputStream());
        } catch (IOException e) {
            logger.error(e);
        }
    }

    protected Scanner getMessageInput() {
        return messageInput;
    }

    protected boolean sendMessage(String message) {
        if (message.equals(exitCommand)) {
            logger.debug("Exit command has been sent");
            try {
                exit();
            } catch (IOException e) {
                logger.error(e);
            }
            return false;
        }
        if (!message.startsWith(usernameCommand)) {
            message = name + ": " + message;
        }

        messageOutput.println(message);
        messageOutput.flush();
        logger.debug(String.format("Message has been sent to server:'%s'", message));
        return true;
    }

    protected void exit() throws IOException {
        messageOutput.println(exitCommand);
        messageOutput.flush();
        messageOutput.close();
        messageInput.close();
        clientSocket.close();
        logger.debug("Client's connection has been closed");
    }

    public int getPort() {
        return port;
    }

    public String getServerHost() {
        return serverHost;
    }

    public String getUsernameCommand() {
        return usernameCommand;
    }

    public String getExitCommand() {
        return exitCommand;
    }

    public String getName() {
        return name;
    }

    public Configuration getConfig() {
        return config;
    }
}
