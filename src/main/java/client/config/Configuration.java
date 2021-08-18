package client.config;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class Configuration {
    public static final String CONFIG = "src/main/resources/settings.conf";

    private final String serverHost;
    private final String usernameCommand;
    private final String exitCommand;
    private final String clientsInChat;
    private final String clientWindowTitle;
    private final int port;
    private final int clientWindowBoundX;
    private final int clientWindowBoundY;
    private final int clientWindowBoundWidth;
    private final int clientWindowBoundHeight;
    private final int enterWindowNameInputColumns;
    private final int enterWindowBoundX;
    private final int enterWindowBoundY;
    private final int enterWindowBoundWidth;
    private final int enterWindowBoundHeight;
    private final int enterWindowGBCInsetTop;
    private final int enterWindowGBCInsetBottom;
    private final int enterWindowGBCInsetRight;
    private final int enterWindowGBCInsetLeft;


    public Configuration() throws IOException {
        Properties props = new Properties();
        props.load(new FileInputStream(CONFIG));

        serverHost = props.getProperty("SERVER_HOST", "localhost");
        port = Integer.parseInt(props.getProperty("PORT", "3443"));
        usernameCommand = props.getProperty("USERNAME_COMMAND", "/username:");
        exitCommand = props.getProperty("EXIT_COMMAND", "/exit");
        clientsInChat = props.getProperty("CLIENTS_IN_CHAT");
        clientWindowTitle = props.getProperty("TITLE", "Chat");
        clientWindowBoundX = Integer.parseInt(props.getProperty("CHAT_WINDOW_BOUND_X", "600"));
        clientWindowBoundY = Integer.parseInt(props.getProperty("CHAT_WINDOW_BOUND_Y", "300"));
        clientWindowBoundWidth = Integer.parseInt(props.getProperty("CHAT_WINDOW_BOUND_WIDTH", "600"));
        clientWindowBoundHeight = Integer.parseInt(props.getProperty("CHAT_WINDOW_BOUND_HEIGHT", "500"));
        enterWindowNameInputColumns = Integer.parseInt(props.getProperty("ENTER_WINDOW_NAME_INPUT_COLUMNS", "20"));
        enterWindowBoundX = Integer.parseInt(props.getProperty("ENTER_WINDOW_BOUND_X", "300"));
        enterWindowBoundY = Integer.parseInt(props.getProperty("ENTER_WINDOW_BOUND_Y", "150"));
        enterWindowBoundWidth = Integer.parseInt(props.getProperty("ENTER_WINDOW_BOUND_WIDTH", "300"));
        enterWindowBoundHeight = Integer.parseInt(props.getProperty("ENTER_WINDOW_BOUND_HEIGHT", "200"));
        enterWindowGBCInsetTop = Integer.parseInt(props.getProperty("ENTER_WINDOW_GBC_INSET_TOP", "5"));
        enterWindowGBCInsetBottom = Integer.parseInt(props.getProperty("ENTER_WINDOW_GBC_INSET_BOTTOM", "5"));
        enterWindowGBCInsetRight = Integer.parseInt(props.getProperty("ENTER_WINDOW_GBC_INSET_RIGHT", "5"));
        enterWindowGBCInsetLeft = Integer.parseInt(props.getProperty("ENTER_WINDOW_GBC_INSET_LEFT", "5"));
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

    public int getPort() {
        return port;
    }

    public String getClientsInChat() {
        return clientsInChat;
    }

    public String getClientWindowTitle() {
        return clientWindowTitle;
    }

    public int getClientWindowBoundX() {
        return clientWindowBoundX;
    }

    public int getClientWindowBoundY() {
        return clientWindowBoundY;
    }

    public int getClientWindowBoundWidth() {
        return clientWindowBoundWidth;
    }

    public int getClientWindowBoundHeight() {
        return clientWindowBoundHeight;
    }

    public int getEnterWindowNameInputColumns() {
        return enterWindowNameInputColumns;
    }

    public int getEnterWindowBoundX() {
        return enterWindowBoundX;
    }

    public int getEnterWindowBoundY() {
        return enterWindowBoundY;
    }

    public int getEnterWindowBoundWidth() {
        return enterWindowBoundWidth;
    }

    public int getEnterWindowBoundHeight() {
        return enterWindowBoundHeight;
    }

    public int getEnterWindowGBCInsetTop() {
        return enterWindowGBCInsetTop;
    }

    public int getEnterWindowGBCInsetBottom() {
        return enterWindowGBCInsetBottom;
    }

    public int getEnterWindowGBCInsetRight() {
        return enterWindowGBCInsetRight;
    }

    public int getEnterWindowGBCInsetLeft() {
        return enterWindowGBCInsetLeft;
    }
}
