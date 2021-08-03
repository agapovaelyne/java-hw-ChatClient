package client;

import org.apache.log4j.Logger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ClientWindow extends JFrame {
    private final Logger logger = Logger.getLogger(ClientWindow.class);
    private final JTextField jtfMessage;
    private final JTextArea jtaTextAreaMessage;
    private final String clientsInChatLabelText = "Users online: ";
    private final String MESSAGE_INPUT_TEXT = "Enter your message:";
    private final String SEND_BUTTON_TEXT = "Send";
    private String TITLE;
    private String USERNAME_COMMAND;
    private String EXIT_COMMAND;
    private String CLIENTS_IN_CHAT;
    private int BOUND_X;
    private int BOUND_Y;
    private int BOUND_WIDTH;
    private int BOUND_HEIGHT;

    protected ClientWindow(String clientName) throws IOException {
        Client client = setConnection(clientName);
        getSettings(client);
        client.sendMessage(USERNAME_COMMAND + clientName);
        setBounds(BOUND_X, BOUND_Y, BOUND_WIDTH, BOUND_HEIGHT);
        setTitle(TITLE + " : " + clientName);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        jtaTextAreaMessage = new JTextArea();
        jtaTextAreaMessage.setEditable(false);
        jtaTextAreaMessage.setLineWrap(true);
        JScrollPane jsp = new JScrollPane(jtaTextAreaMessage);
        add(jsp, BorderLayout.CENTER);

        JLabel jlNumberOfClients = new JLabel(clientsInChatLabelText);
        add(jlNumberOfClients, BorderLayout.NORTH);

        JPanel bottomPanel = new JPanel(new BorderLayout());
        add(bottomPanel, BorderLayout.SOUTH);
        JButton jbSendMessage = new JButton(SEND_BUTTON_TEXT);
        bottomPanel.add(jbSendMessage, BorderLayout.EAST);
        jtfMessage = new JTextField(MESSAGE_INPUT_TEXT);
        bottomPanel.add(jtfMessage, BorderLayout.CENTER);

        jbSendMessage.addActionListener(e -> {
            if (!jtfMessage.getText().trim().isEmpty()) {
                boolean hasQuit = client.sendMessage(jtfMessage.getText());
                logger.debug(String.format("Message has been sent from client to server: '%s'", jtfMessage.getText()));
                if (!hasQuit) {
                    logger.debug("Client quited the chat");
                    dispose();
                    logger.debug("Chat window has been closed");
                }
                jtfMessage.grabFocus();
            }
        });

        jtfMessage.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                jtfMessage.setText("");
                logger.debug("Message text form has been cleared");
            }
        });

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                try {
                    client.exit();
                    logger.debug("Chat window has been closed by user");
                } catch (IOException ex) {
                    logger.error(ex);
                }
            }
        });

        setVisible(true);
        logger.debug("ClientWindow configuration has been set");

        new Thread(() -> {
            try {
                while (true) {
                    if (client.getMessageInput().hasNext()) {
                        String clientsMessage = client.getMessageInput().nextLine();

                        if (clientsMessage.startsWith(CLIENTS_IN_CHAT)) {
                            logger.debug(String.format("Service message (CLIENTS_IN_CHAT) has been received from server: '%s'", clientsMessage));
                            jlNumberOfClients.setText(clientsMessage.replace(CLIENTS_IN_CHAT, clientsInChatLabelText));
                            logger.debug(String.format("New value has been set to jlNumberOfClients form: %s", clientsMessage.replace(CLIENTS_IN_CHAT, clientsInChatLabelText)));
                        } else {
                            logger.debug(String.format("New message has been received from server: '%s'", clientsMessage));
                            String sender = clientsMessage.contains(":") ? clientsMessage.substring(0, clientsMessage.indexOf(":")) : "Server";
                            logger.info(String.format("Message to chat from %s: '%s'", sender, clientsMessage.substring(clientsMessage.indexOf(":") + 1)));
                            jtaTextAreaMessage.append(clientsMessage);
                            jtaTextAreaMessage.append("\n");
                            logger.debug(String.format("jtaTextAreaMessage form has been updated with message: '%s'", clientsMessage));
                        }
                    }
                }
            } catch (IllegalStateException e) {
                Thread.currentThread().interrupt();
                logger.debug("ClientWindow thread haas been interrupted");
            } catch (Exception e) {
                logger.error(e);
            }
        }).start();
        logger.debug("New ClientWindow Thread has been started");
    }

    protected Client setConnection(String clientName) throws IOException {
        return new Client(clientName);
    }

    private void getSettings(Client client) throws IOException {
        Properties props = new Properties();
        props.load(new FileInputStream(Client.CONFIG));
        logger.debug("Settings file has been loaded:" + Client.CONFIG);
        USERNAME_COMMAND = client.getUSERNAME_COMMAND();
        logger.debug("ClientWindow USERNAME_COMMAND value has been set to " + USERNAME_COMMAND);
        EXIT_COMMAND = client.getEXIT_COMMAND();
        logger.debug("ClientWindow EXIT_COMMAND value has been set to " + EXIT_COMMAND);
        CLIENTS_IN_CHAT = props.getProperty("CLIENTS_IN_CHAT");
        logger.debug("ClientWindow CLIENTS_IN_CHAT value has been set to " + CLIENTS_IN_CHAT);
        TITLE = props.getProperty("TITLE", "Chat");
        logger.debug("ClientWindow TITLE value has been set to " + TITLE);
        BOUND_X = Integer.parseInt(props.getProperty("CHAT_WINDOW_BOUND_X", "600"));
        logger.debug("ClientWindow BOUND_X value has been set to " + BOUND_X);
        BOUND_Y = Integer.parseInt(props.getProperty("CHAT_WINDOW_BOUND_Y", "300"));
        logger.debug("ClientWindow BOUND_Y value has been set to " + BOUND_Y);
        BOUND_WIDTH = Integer.parseInt(props.getProperty("CHAT_WINDOW_BOUND_WIDTH", "600"));
        logger.debug("ClientWindow BOUND_WIDTH value has been set to " + BOUND_WIDTH);
        BOUND_HEIGHT = Integer.parseInt(props.getProperty("CHAT_WINDOW_BOUND_HEIGHT", "500"));
        logger.debug("ClientWindow BOUND_HEIGHT value has been set to " + BOUND_HEIGHT);
    }
}