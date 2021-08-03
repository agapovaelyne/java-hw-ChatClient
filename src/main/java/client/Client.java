package client;

import org.apache.log4j.Logger;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Properties;
import java.util.Scanner;

public class Client {

    private final Logger logger = Logger.getLogger(Client.class);
    protected static final String CONFIG = "src/main/resources/settings.conf";

    protected String SERVER_HOST;
    protected int SERVER_PORT;
    protected String USERNAME_COMMAND;
    protected String EXIT_COMMAND;
    protected Socket clientSocket;
    protected Scanner messageInput;
    protected PrintWriter messageOutput;
    protected String name;

    protected Client(String name) throws IOException {
        setSettings();
        this.name = name;
        logger.debug("Client configuration has been set");
        try {
            clientSocket = new Socket(SERVER_HOST, SERVER_PORT);
            logger.debug(String.format("Client connection to %s:%s", SERVER_HOST, SERVER_PORT));
            messageInput = new Scanner(clientSocket.getInputStream());
            messageOutput = new PrintWriter(clientSocket.getOutputStream());
        } catch (IOException e) {
            logger.error(e);
        }

    }

    private void setSettings() throws IOException {
        Properties props = new Properties();
        props.load(new FileInputStream(CONFIG));
        logger.debug("Settings file has been loaded:" + CONFIG);
        SERVER_HOST = props.getProperty("SERVER_HOST", "localhost");
        logger.debug("Client SERVER_HOST value has been set to " + SERVER_HOST);
        SERVER_PORT = Integer.parseInt(props.getProperty("PORT", "3443"));
        logger.debug("Client SERVER_PORT value has been set to " + SERVER_PORT);
        USERNAME_COMMAND = props.getProperty("USERNAME_COMMAND", "/username:");
        logger.debug("Client USERNAME_COMMAND value has been set to " + USERNAME_COMMAND);
        EXIT_COMMAND = props.getProperty("EXIT_COMMAND", "/exit");
        logger.debug("Client EXIT_COMMAND value has been set to " + EXIT_COMMAND);
    }

    protected Scanner getMessageInput() {
        return messageInput;
    }

    protected boolean sendMessage(String message) {
        if (message.equals(EXIT_COMMAND)) {
            logger.debug("Client has been sent service message (EXIT_COMMAND)");
            try {
                exit();
            } catch (IOException e) {
                logger.error(e);
            }
            return false;
        }
        if (!message.startsWith(USERNAME_COMMAND)) {
            logger.debug(String.format("Message text has been received by client:'%s'", message));
            message = name + ": " + message;
            logger.debug(String.format("Full message string has been formed by client (username added):'%s'", message));
        }

        messageOutput.println(message);
        messageOutput.flush();
        logger.debug(String.format("Full message string has been sent to server:'%s'", message));
        return true;
    }

    protected void exit() throws IOException {
        messageOutput.println(EXIT_COMMAND);
        logger.debug("Service messages (EXIT_COMMAND) has been sent to server");
        messageOutput.flush();
        messageOutput.close();
        messageInput.close();
        clientSocket.close();
        logger.debug("Client's connection has been closed");
    }

    protected String getUSERNAME_COMMAND() {
        return USERNAME_COMMAND;
    }

    protected String getEXIT_COMMAND() {
        return EXIT_COMMAND;
    }
}
