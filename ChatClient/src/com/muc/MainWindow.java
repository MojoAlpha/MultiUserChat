package com.muc;

import javax.swing.*;
import java.awt.*;

public class MainWindow extends JFrame {
    private final ChatClient client;

    public MainWindow(ChatClient client) {
        super("MultiUserChat");
        this.client = client;

        UserListPane userListPane = new UserListPane(client, this);

        this.setSize(600, 600);

        this.getContentPane().add(userListPane, BorderLayout.EAST);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);

        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }
}
