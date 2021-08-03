package client;

import org.apache.log4j.Logger;

import javax.swing.*;
import java.awt.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class EnterWindow extends JFrame {
    private final Logger logger = Logger.getLogger(EnterWindow.class);
    protected final JTextField nameInput;
    private String TITLE;
    private final GridBagConstraints constr = new GridBagConstraints();
    private final JLabel welcomeLabel = new JLabel("Welcome to chat!");
    private final JLabel nameLabel = new JLabel("Enter your name:");
    protected final JButton enterButton = new JButton("Enter!");
    private final String SERVER_NOT_AVAIlABLE_MESSAGE = "Server is not available!";
    private final String INCORRECT_NAME_MESSAGE = "Incorrect name!";

    private int NAME_INPUT_COLUMNS;
    private int BOUND_X;
    private int BOUND_Y;
    private int BOUND_WIDTH;
    private int BOUND_HEIGHT;
    private int GBC_INSET_TOP;
    private int GBC_INSET_BOTTOM;
    private int GBC_INSET_RIGHT;
    private int GBC_INSET_LEFT;

    public EnterWindow() throws IOException {
        getSettings();
        setTitle(TITLE);
        JPanel panel = new JPanel(new GridBagLayout());
        constr.insets = new Insets(GBC_INSET_TOP, GBC_INSET_LEFT, GBC_INSET_BOTTOM, GBC_INSET_RIGHT);
        constr.gridx = 0;
        constr.gridy = 0;
        constr.anchor = GridBagConstraints.CENTER;
        panel.add(welcomeLabel, constr);
        constr.anchor = GridBagConstraints.WEST;
        nameInput = new JTextField("", NAME_INPUT_COLUMNS);
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
        setBounds(BOUND_X, BOUND_Y, BOUND_WIDTH, BOUND_HEIGHT);
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
                    logger.debug(String.format("New Error Message Dialog created with message %s", SERVER_NOT_AVAIlABLE_MESSAGE));
                }
                dispose();
                logger.debug("EnterWindow has been closed");
            } else {
                JOptionPane.showMessageDialog(getComponent(0), INCORRECT_NAME_MESSAGE);
                logger.debug("User tried to enter the Chat with empty nameInput field");
                logger.debug(String.format("New Error Message Dialog created with message %s", INCORRECT_NAME_MESSAGE));
            }
        });
        logger.debug("ClientWindow configuration has been set");
    }

    private void getSettings() throws IOException {
        Properties props = new Properties();
        props.load(new FileInputStream(Client.CONFIG));
        logger.debug("Settings file has been loaded:" + Client.CONFIG);
        TITLE = props.getProperty("TITLE", "Chat");
        logger.debug("EnterWindow TITLE value has been set to " + TITLE);
        NAME_INPUT_COLUMNS = Integer.parseInt(props.getProperty("ENTER_WINDOW_NAME_INPUT_COLUMNS", "20"));
        logger.debug("EnterWindow NAME_INPUT_COLUMNS value has been set to " + NAME_INPUT_COLUMNS);
        BOUND_X = Integer.parseInt(props.getProperty("ENTER_WINDOW_BOUND_X", "300"));
        logger.debug("EnterWindow BOUND_X value has been set to " + BOUND_X);
        BOUND_Y = Integer.parseInt(props.getProperty("ENTER_WINDOW_BOUND_Y", "150"));
        logger.debug("EnterWindow BOUND_Y value has been set to " + BOUND_Y);
        BOUND_WIDTH = Integer.parseInt(props.getProperty("ENTER_WINDOW_BOUND_WIDTH", "300"));
        logger.debug("EnterWindow BOUND_WIDTH value has been set to " + BOUND_WIDTH);
        BOUND_HEIGHT = Integer.parseInt(props.getProperty("ENTER_WINDOW_BOUND_HEIGHT", "200"));
        logger.debug("EnterWindow BOUND_HEIGHT value has been set to " + BOUND_HEIGHT);
        GBC_INSET_TOP = Integer.parseInt(props.getProperty("ENTER_WINDOW_GBC_INSET_TOP", "5"));
        logger.debug("EnterWindow GBC_INSET_TOP value has been set to " + GBC_INSET_TOP);
        GBC_INSET_BOTTOM = Integer.parseInt(props.getProperty("ENTER_WINDOW_GBC_INSET_BOTTOM", "5"));
        logger.debug("EnterWindow GBC_INSET_BOTTOM value has been set to " + GBC_INSET_BOTTOM);
        GBC_INSET_RIGHT = Integer.parseInt(props.getProperty("ENTER_WINDOW_GBC_INSET_RIGHT", "5"));
        logger.debug("EnterWindow GBC_INSET_RIGHT value has been set to " + GBC_INSET_RIGHT);
        GBC_INSET_LEFT = Integer.parseInt(props.getProperty("ENTER_WINDOW_GBC_INSET_LEFT", "5"));
        logger.debug("EnterWindow GBC_INSET_LEFT value has been set to " + GBC_INSET_LEFT);
    }
}
