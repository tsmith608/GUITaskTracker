package com.taskmanager;

import com.formdev.flatlaf.FlatDarkLaf;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

public class LoginDialog extends JDialog {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton signUpButton;
    private boolean succeeded;
    private Map<String, String> userMap;
    private static String loggedinInUsername;

    public LoginDialog(Frame parent, Map<String, String> userMap) {
        super(parent, "Login", true);
        try {
            UIManager.setLookAndFeel(new FlatDarkLaf());
        } catch (Exception e) {
            e.printStackTrace();
        }
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
                try {
                    String enteredUsername = usernameField.getText().trim();
                    String enteredPassword = new String(passwordField.getPassword());
                    if (authenticate(enteredUsername, enteredPassword)) {
                        JOptionPane.showMessageDialog(LoginDialog.this, usernameField.getText() + " You have successfully logged in!",
                                "Login",
                                JOptionPane.INFORMATION_MESSAGE);
                        loggedinInUsername = enteredUsername;
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
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
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

    private boolean authenticate(String username, String password) throws SQLException {
        String sql = "SELECT * FROM users WHERE username = ?";
        try(Connection conn = DatabaseManager.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                String storedHash = rs.getString("password");
                String enteredHash = hashPassword(password);
                if (storedHash.equals(enteredHash)) {
                    System.out.println("User authenticated");
                    return true;
                } else {
                    System.out.println("Wrong password");
                    return false;
                }
            } else {
                System.out.println("User not found");
                return false;
            }
        } catch (SQLException e) {
            System.out.println("Error while authenticating");
            e.printStackTrace();
            return false;
        }
    }

    private boolean signUp(String username, String password) {
        String sql = "INSERT INTO users VALUES (?, ?)";
        try (Connection conn = DatabaseManager.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, username);
                ps.setString(2, hashPassword(password));
                ps.executeUpdate();
            System.out.println("User Registered Successfully");
            return true;
        } catch (SQLException e) {
            if (e.getMessage().contains("UNIQUE constraint failed")) {
                System.out.println("User already exists");
            } else {
                System.out.println("error occurred during sign-up");
                e.printStackTrace();
            } return false;
        }
    }

    public boolean isSucceeded(){
        return succeeded;
    }

    public static String getLoggedInUsername() {
        return loggedinInUsername;
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
