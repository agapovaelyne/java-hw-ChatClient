package client;

import client.config.Configuration;
import org.apache.log4j.Logger;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class EnterWindow extends JFrame {
    private final Logger logger = Logger.getLogger(EnterWindow.class);

    private final Configuration config;
    private final JTextField nameInput;
    private final GridBagConstraints constr = new GridBagConstraints();
    private final JLabel welcomeLabel = new JLabel("Welcome to chat!");
    private final JLabel nameLabel = new JLabel("Enter your name:");
    private final JButton enterButton = new JButton("Enter!");

    private final String title;
    private final int nameInputColumns;
    private final int boundX;
    private final int boundY;
    private final int boundWidth;
    private final int boundHeight;
    private final int gbcInsetTop;
    private final int gbcInsetBottom;
    private final int gbcInsetRight;
    private final int gbcInsetLeft;

    private static final String SERVER_NOT_AVAIlABLE_MESSAGE = "Server is not available!";
    private static final String INCORRECT_NAME_MESSAGE = "Incorrect name!";

    public EnterWindow() throws IOException {
        config = new Configuration();
        title = config.getClientWindowTitle();
        nameInputColumns = config.getEnterWindowNameInputColumns();
        boundX = config.getEnterWindowBoundX();
        boundY = config.getClientWindowBoundY();
        boundWidth = config.getEnterWindowBoundWidth();
        boundHeight = config.getClientWindowBoundHeight();
        gbcInsetTop = config.getEnterWindowGBCInsetTop();
        gbcInsetBottom = config.getEnterWindowGBCInsetBottom();
        gbcInsetRight = config.getEnterWindowGBCInsetRight();
        gbcInsetLeft = config.getEnterWindowGBCInsetLeft();
        setTitle(title);
        JPanel panel = new JPanel(new GridBagLayout());
        constr.insets = new Insets(gbcInsetTop, gbcInsetLeft, gbcInsetBottom, gbcInsetRight);
        constr.gridx = 0;
        constr.gridy = 0;
        constr.anchor = GridBagConstraints.CENTER;
        panel.add(welcomeLabel, constr);
        constr.anchor = GridBagConstraints.WEST;
        nameInput = new JTextField("", nameInputColumns);
        constr.gridx = 0;
        constr.gridy = 1;
        panel.add(nameLabel, constr);
        constr.gridx = 1;
        panel.add(nameInput, constr);
        constr.gridx = 0;
        constr.gridy = 4;
        constr.gridwidth = 2;
        constr.anchor = GridBagConstraints.CENTER;
        panel.add(enterButton, constr);
        add(panel);
        setBounds(boundX, boundY, boundWidth, boundHeight);
        pack();
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);

        enterButton.addActionListener(e -> {
            if (!nameInput.getText().trim().isEmpty()) {
                try {
                    new ClientWindow(nameInput.getText());
                    logger.debug(String.format("New ClientWindow created for user %s", nameInput.getText()));
                } catch (IOException | NullPointerException ex) {
                    JOptionPane.showMessageDialog(getComponent(0), SERVER_NOT_AVAIlABLE_MESSAGE);
                    logger.error(ex);
                    logger.debug(String.format("Error Message: %s", SERVER_NOT_AVAIlABLE_MESSAGE));
                }
                dispose();
            } else {
                JOptionPane.showMessageDialog(getComponent(0), INCORRECT_NAME_MESSAGE);
                logger.debug(String.format("Error Message: %s", INCORRECT_NAME_MESSAGE));
            }
        });
    }

    public JTextField getNameInput() {
        return nameInput;
    }

    public JButton getEnterButton() {
        return enterButton;
    }
}
