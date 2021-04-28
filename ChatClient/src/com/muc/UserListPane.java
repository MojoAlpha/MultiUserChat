package com.muc;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

public class UserListPane extends JPanel implements UserStatusListener {

    private final ChatClient client;
    private final JFrame parentFrame;
    private JList<String> userListUI;
    private DefaultListModel<String> userListModel;

    public UserListPane(ChatClient client, JFrame parentFrame) {
        this.client = client;
        this.parentFrame = parentFrame;
        this.client.addUserStatusListener(this);

        userListModel = new DefaultListModel<>();
        userListUI = new JList<>(userListModel);
        setLayout(new BorderLayout());
        add(new JScrollPane(userListUI), BorderLayout.CENTER);

        this.setSize(300, 600);

        userListUI.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(e.getClickCount() > 1) {
                    String login = userListUI.getSelectedValue();
                    MessagePane messagePane = new MessagePane(client, login);

                    parentFrame.getContentPane().add(messagePane, BorderLayout.WEST);
                    messagePane.setVisible(true);

//                    JFrame f = new JFrame("Message : " + login);
//                    f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
//                    f.setSize(500, 500);
//                    f.getContentPane().add(messagePane, BorderLayout.CENTER);
//                    f.setVisible(true);
                }
            }
        });
    }

    @Override
    public void online(String login) {
        userListModel.addElement(login);
    }

    @Override
    public void offline(String login) {
        userListModel.removeElement(login);
    }
}
