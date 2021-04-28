package com.muc;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class LoginWindow extends JFrame {

    private final ChatClient client;
    JTextField loginField = new JTextField();
    JButton loginButton = new JButton("Login");

    public LoginWindow(ChatClient client) {
        super("Login");
        this.client = client;

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel p = new JPanel();
        p.setLayout(new BorderLayout());
        p.add(loginField, BorderLayout.NORTH);
        p.add(loginButton, BorderLayout.SOUTH);
        p.setSize(400, 400);

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                doLogin();
            }
        });

        getContentPane().add(p, BorderLayout.CENTER);
        this.setSize(400, 600);

        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void doLogin() {
        String login = loginField.getText();

        try {
            if (client.login(login)) {
                setVisible(false);
                // bring up user list window
//                UserListPane userListPane = new UserListPane(client);
//                JFrame frame = new JFrame("User List");
//                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//                frame.setSize(400, 600);
//
//                frame.getContentPane().add(userListPane, BorderLayout.CENTER);
//                frame.setVisible(true);
//
//                setVisible(false);
                new MainWindow(client);
            } else {
                // show error message
                JOptionPane.showMessageDialog(this, "Invalid login/password");
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
