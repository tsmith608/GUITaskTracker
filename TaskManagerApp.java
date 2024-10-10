package com.taskmanager;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TaskManagerApp {
    static List<Task> tasks = new ArrayList<>();
    static Map<String, String> userMap;
    public static void main(String[] args) {


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
                        //retrieve username
                        String username = loginDialog.getLoggedInUsername();

                        //load tasks for user
                        loadTasks(username);

                        // Initialize the GUI
                        TaskManagerGUI gui = new TaskManagerGUI(username);
                        gui.displayTasks(tasks);
                        gui.setVisible(true);
                    } else {
                        System.exit(0);
                    }
                });
    }
    public static String getTasksFileName(String username) {
        String sanitizedUsername = username.replaceAll("[^a-zA-Z0-9_-]", "_");
        return sanitizedUsername + "_tasks.json";
    }


    // Existing loadTasks() method
    public static void loadTasks(String username) {
        tasks.clear();

        String tasksFileName = getTasksFileName(username);
        File file = new File(tasksFileName);


        if (!file.exists()) {
            System.out.println("No tasks file found, starting anew");
            return;
        }

        try {
            String content = new String(Files.readAllBytes(Paths.get(tasksFileName)));
            content = content.trim();

            if (content.isEmpty() || content.equals("[]")) {
                return;
            }
            content = content.substring(1, content.length() - 1);

            String[] taskStrings = content.split("\\},\\{");

            for (String taskString : taskStrings) {
                taskString = taskString.replace("}", "").replace("}", "").replace("\"", "");
                String[] attributes = taskString.split(",");

                String id = attributes[0].split(":")[1];
                String description = attributes[1].split(":")[1];
                String status = attributes[2].split(":")[1];
                String createdAt = attributes[3].split(":")[1];
                String updatedAt = attributes[4].split(":")[1];
                String priority = attributes[5].split(":")[1];
                String dueDate = attributes[6].split(":")[1];

                Task task = new Task(id, description, status, createdAt, updatedAt, priority, dueDate);
                tasks.add(task);

                //System.out.println("Successfully loaded task: " + description);
                //System.out.println("loaded " + tasks.size() + " tasks");

            }
        } catch (IOException e) {
            System.out.println("Error reading tasks.json" + e.getMessage());
    }



}


}
