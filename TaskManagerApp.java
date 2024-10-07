package com.taskmanager;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class TaskManagerApp {
    public static final String TASKS_FILE = "tasks.json"; // Path to your JSON file
    static List<Task> tasks = new ArrayList<>();

    public static void main(String[] args) {
        // Load tasks from the JSON file
        loadTasks();

        // Initialize the GUI on the Event Dispatch Thread for thread safety
        SwingUtilities.invokeLater(() -> {
            TaskManagerGUI gui = new TaskManagerGUI();
            gui.displayTasks(tasks);
            gui.setVisible(true);
        });
    }

    // Existing loadTasks() method
    public static void loadTasks() {
        File file = new File(TASKS_FILE);


        if (!file.exists()) {
            System.out.println("No tasks file found, starting anew");
            return;
        }

        try {
            String content = new String(Files.readAllBytes(Paths.get(TASKS_FILE)));
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

                Task task = new Task(id, description, status, createdAt, updatedAt);
                tasks.add(task);

                //System.out.println("Successfully loaded task: " + description);
                //System.out.println("loaded " + tasks.size() + " tasks");

            }
        } catch (IOException e) {
            System.out.println("Error reading tasks.json" + e.getMessage());
    }
}


}
