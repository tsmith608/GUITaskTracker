package com.taskmanager;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TaskManagerApp {
    static List<Task> tasks = new ArrayList<>();
    static Map<String, String> userMap;

    public static void main(String[] args) {

        //initialize database
        DatabaseManager.initializeDatabase();

        //load user data
        userMap = UserDataManager.loadUserMap();
        System.out.println();

        SwingUtilities.invokeLater(() -> {

            JFrame frame = new JFrame();
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            //display login dialog
            LoginDialog loginDialog = new LoginDialog(frame, userMap);
            loginDialog.setVisible(true);

            //check login
            if (loginDialog.isSucceeded()) {
                //load tasks for user
                    String username = loginDialog.getLoggedInUsername();
                List<Task> tasks = null;
                try {
                    tasks = TaskDAO.getTasksByUsername(username);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                System.out.println("Username before creating TaskManagerGUI: " + username);

                    // Initialize the GUI
                    TaskManagerGUI gui = new TaskManagerGUI(username, tasks);
                    gui.displayTasks();
                    gui.setVisible(true);
                }
        });
    }
}