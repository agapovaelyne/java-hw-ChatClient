package client;

import org.apache.log4j.Logger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

public class ClientWindow extends JFrame {
    private final Logger logger = Logger.getLogger(ClientWindow.class);
    private final JTextField jtfMessage;
    private final JTextArea jtaTextAreaMessage;
    private final String clientsInChatLabelText = "Users online: ";
    private final String messageInputText = "Enter your message:";
    private final String sendButtonText = "Send";
    private final String title;
    private final String usernameCommand;
    private final String clientsInChat;
    private final int boundX;
    private final int boundY;
    private final int boundWidth;
    private final int boundHeight;

    protected ClientWindow(String clientName) throws IOException {
        Client client = setConnection(clientName);
        usernameCommand = client.getConfig().getUsernameCommand();
        clientsInChat = client.getConfig().getClientsInChat();
        title = client.getConfig().getClientWindowTitle();
        boundX = client.getConfig().getClientWindowBoundX();
        boundY = client.getConfig().getClientWindowBoundY();
        boundWidth = client.getConfig().getClientWindowBoundWidth();
        boundHeight = client.getConfig().getClientWindowBoundHeight();

        client.sendMessage(usernameCommand + clientName);
        setBounds(boundX, boundY, boundWidth, boundHeight);
        setTitle(title + " : " + clientName);
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
        JButton jbSendMessage = new JButton(sendButtonText);
        bottomPanel.add(jbSendMessage, BorderLayout.EAST);
        jtfMessage = new JTextField(messageInputText);
        bottomPanel.add(jtfMessage, BorderLayout.CENTER);

        jbSendMessage.addActionListener(e -> {
            if (!jtfMessage.getText().trim().isEmpty()) {
                boolean hasQuit = client.sendMessage(jtfMessage.getText());
                if (!hasQuit) {
                    dispose();
                }
                jtfMessage.grabFocus();
            }
        });

        jtfMessage.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                jtfMessage.setText("");
            }
        });

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                try {
                    client.exit();
                } catch (IOException ex) {
                    logger.error(ex);
                }
            }
        });

        setVisible(true);

        new Thread(() -> {
            try {
                while (true) {
                    if (client.getMessageInput().hasNext()) {
                        String clientsMessage = client.getMessageInput().nextLine();
                        if (clientsMessage.startsWith(clientsInChat)) {
                            jlNumberOfClients.setText(clientsMessage.replace(clientsInChat, clientsInChatLabelText));
                        } else {
                            String sender = clientsMessage.contains(":") ? clientsMessage.substring(0, clientsMessage.indexOf(":")) : "Server";
                            logger.info(String.format("Message to chat from %s: '%s'", sender, clientsMessage.substring(clientsMessage.indexOf(":") + 1)));
                            jtaTextAreaMessage.append(clientsMessage);
                            jtaTextAreaMessage.append("\n");
                        }
                    }
                }
            } catch (IllegalStateException e) {
                Thread.currentThread().interrupt();
            } catch (Exception e) {
                logger.error(e);
            }
        }).start();
    }

    protected Client setConnection(String clientName) throws IOException {
        return new Client(clientName);
    }

}