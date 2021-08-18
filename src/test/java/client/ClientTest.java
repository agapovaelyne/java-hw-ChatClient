package client;

import client.config.Configuration;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.Socket;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ClientTest {

    private static final String CONFIG = Configuration.CONFIG;
    private static final Properties PROPS = new Properties();

    //checks if user can connect to the server
    @Test
    public void ServerIsUp_test() {
        boolean isConnected = false;
        try (Socket clientSocket = new Socket(Helper.getHost(PROPS, CONFIG), Helper.getPort(PROPS, CONFIG))) {
            isConnected = clientSocket.isConnected();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        assertTrue(isConnected);
    }

    //checks if settings have been set correctly
    @Test
    public void clientSettings_test() throws IOException {
        String username = "Test user";
        Client client = new Client(username);
        client.exit();
        assertEquals(client.getName(), username);
        assertEquals(client.getServerHost(), Helper.getHost(PROPS, CONFIG));
        assertEquals(client.getPort(), Helper.getPort(PROPS, CONFIG));
        assertEquals(client.getUsernameCommand(), Helper.getUsernameCommandText(PROPS, CONFIG));
        assertEquals(client.getExitCommand(), Helper.getExitCommandText(PROPS, CONFIG));
    }

    //checks if user's message has been sent to chat
    @Test
    public void sendMessage_test1() throws IOException {
        String username = "Test user";
        String message = "Test message";
        String expectedMessageText = username + ": " + message;

        Client client = new Client(username);
        client.sendMessage(message);
        String input;

        boolean messageReceived = false;
        for (int i = 0; i < 10; i++) {
            if (client.getMessageInput().hasNext()) {
                input = client.getMessageInput().nextLine();
                if (input.equals(expectedMessageText)) {
                    messageReceived = true;
                    break;
                }
            }
        }
        client.exit();
        assertTrue(messageReceived);
    }

    //checks if 'users online' server message has been sent to chat
    @Test
    public void sendMessage_test2() throws IOException {
        String username = "Test user";
        Client client = new Client(username);
        String expectedServiceMessage = Helper.getUsersOnlineCommand(PROPS, CONFIG);
        String input;

        boolean messageReceived = false;
        for (int i = 0; i < 10; i++) {
            if (client.getMessageInput().hasNext()) {
                input = client.getMessageInput().nextLine();
                if (input.contains(expectedServiceMessage)) {
                    messageReceived = true;
                    break;
                }
            }
        }
        client.exit();
        assertTrue(messageReceived);
    }

    //checks if 'user exits' server message has been sent to chat
    @Test
    public void clientExit_test1() throws IOException {
        String username1 = "Test user1";
        String username2 = "Test user2";
        String expectedServerMessage = username1 + " has gone!";

        Client client1 = new Client(username1);
        client1.sendMessage(Helper.getUsernameCommandText(PROPS, CONFIG) + username1);
        Client client2 = new Client(username2);
        client1.exit();

        String input;
        boolean messageReceived = false;
        for (int i = 0; i < 10; i++) {
            if (client2.getMessageInput().hasNext()) {
                input = client2.getMessageInput().nextLine();
                if (input.equals(expectedServerMessage)) {
                    messageReceived = true;
                    break;
                }
            }
        }
        client2.exit();
        assertTrue(messageReceived);
    }

    //checks if 'user enters the chat' server message has been sent to chat
    @Test
    public void clientEnters_test() throws IOException {
        String username = "Test user";
        String expectedServerMessage = username + " enters the chat!";

        Client client = new Client(username);
        client.sendMessage(Helper.getUsernameCommandText(PROPS, CONFIG) + username);

        String input;
        boolean messageReceived = false;
        for (int i = 0; i < 10; i++) {
            if (client.getMessageInput().hasNext()) {
                input = client.getMessageInput().nextLine();
                if (input.equals(expectedServerMessage)) {
                    messageReceived = true;
                    break;
                }
            }
        }
        client.exit();
        assertTrue(messageReceived);
    }
}