import javax.swing.*;
import java.nio.file.*;
import java.io.*;
import java.util.*;

public class TaskManagerApp {
    private static final String TASKS_FILE = "tasks.json"; // Path to your JSON file
    private static List<Task> tasks = new ArrayList<>();

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

    // Existing loadTasks() method (copy it here)
    private static void loadTasks() {
        // (Same code as in Step 3)
        // Ensure 'tasks' is accessible in this method
    }
}
