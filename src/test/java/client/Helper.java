package client;

import org.junit.jupiter.api.Assertions;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Helper {

    protected static String getUsernameCommandText(Properties PROPS, String CONFIG) throws IOException {
        PROPS.load(new FileInputStream(new File(CONFIG)));
        return PROPS.getProperty("USERNAME_COMMAND", "/username:");
    }

    protected static String getExitCommandText(Properties PROPS, String CONFIG) throws IOException {
        PROPS.load(new FileInputStream(new File(CONFIG)));
        return PROPS.getProperty("EXIT_COMMAND", "/exit");
    }

    protected static String getUsersOnlineCommand(Properties PROPS, String CONFIG) throws IOException {
        PROPS.load(new FileInputStream(new File(CONFIG)));
        return PROPS.getProperty("CLIENTS_IN_CHAT", "/users_online:");
    }

    protected static int getPort(Properties PROPS, String CONFIG) throws IOException {
        PROPS.load(new FileInputStream(new File(CONFIG)));
        return Integer.parseInt(PROPS.getProperty("PORT", "3443"));
    }

    protected static String getHost(Properties PROPS, String CONFIG) throws IOException {
        PROPS.load(new FileInputStream(new File(CONFIG)));
        return PROPS.getProperty("SERVER_HOST", "localhost");
    }
}
