package com.taskmanager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

public class LoginDialog extends JDialog {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton signUpButton;
    private boolean succeeded;
    private Map<String, String> userMap;
    private String loggedinInUsername;

    public LoginDialog(Frame parent, Map<String, String> userMap) {
        super(parent, "Login", true);
        this.userMap = userMap;

        JPanel panel = new JPanel(new GridLayout(3, 2));
        panel.setPreferredSize(new Dimension(300, 100));

        JLabel usernameLabel = new JLabel("Username: ");
        usernameField = new JTextField(20);

        JLabel passwordLabel = new JLabel("Password: ");
        passwordField = new JPasswordField(20);

        loginButton = new JButton("Login");
        signUpButton = new JButton("Sign Up");

        panel.add(usernameLabel);
        panel.add(usernameField);
        panel.add(passwordLabel);
        panel.add(passwordField);
        panel.add(loginButton);
        panel.add(signUpButton);

        getContentPane().add(panel);
        pack();
        setResizable(false);
        setLocationRelativeTo(parent);

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if ((authenticate(usernameField.getText().trim(), new String(passwordField.getPassword())))) {
                    JOptionPane.showMessageDialog(LoginDialog.this, usernameField.getText() + " You have successfully logged in!",
                            "Login",
                            JOptionPane.INFORMATION_MESSAGE);
                    succeeded = true;
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(LoginDialog.this,
                            "Invalid username or password",
                            "Login",
                            JOptionPane.ERROR_MESSAGE);

                    usernameField.setText("");
                    passwordField.setText("");
                    succeeded = false;
                }
            }
        });


        signUpButton.addActionListener(new ActionListener() {
           @Override
           public void actionPerformed(ActionEvent e) {
               signUp(usernameField.getText().trim(), new String(passwordField.getPassword()));
           }
        });
    }

    private boolean authenticate(String username, String password) {
        String storedHash = userMap.get(username);
        if (storedHash != null && storedHash.equals(hashPassword(password))) {
            loggedinInUsername = username;
            return true;
        }
        return false;
    }
    public String getLoggedInUsername() {
        return loggedinInUsername;
    }

    private void signUp(String username, String password) {
        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(LoginDialog.this,
                    "Username and password cannot be empty",
                            "Sign up",
                            JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (userMap.containsKey(username)) {
            JOptionPane.showMessageDialog(LoginDialog.this,
                    "Username already exists",
                            "Sign up",
                            JOptionPane.ERROR_MESSAGE);
            return;
        }
        userMap.put(username, hashPassword(password));
        UserDataManager.saveUserMap(userMap);
        JOptionPane.showMessageDialog(LoginDialog.this,
                        "Account created!",
                                "Sign up",
                                    JOptionPane.INFORMATION_MESSAGE);

    }

    public boolean isSucceeded(){
        return succeeded;
    }

    private static String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashedBytes = digest.digest(password.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : hashedBytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

}
