package client;

import org.junit.jupiter.api.Test;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertFalse;

public class EnterWindowTest {

    @Test
    public void userEnters_test1() throws IOException {
        String username = "Test user";
        EnterWindow client = new EnterWindow();
        client.getNameInput().setText(username);
        client.getEnterButton().doClick();
        assertFalse(client.isShowing());
        for (Window window : JFrame.getWindows()) {
            window.dispose();
        }
    }
}
