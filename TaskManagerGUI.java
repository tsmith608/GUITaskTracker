
import javax.swing.*;
import java.awt.*;
import java.util.List;

public class TaskManagerGUI extends JFrame {
    private JTextArea taskDisplayArea;

    public TaskManagerGUI() {
        // Set up the frame
        setTitle("Task Manager");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Create and configure the text area
        taskDisplayArea = new JTextArea();
        taskDisplayArea.setEditable(false); // Users shouldn't edit the displayed tasks
        taskDisplayArea.setFont(new Font("Monospaced", Font.PLAIN, 12));

        // Add the text area to a scroll pane
        JScrollPane scrollPane = new JScrollPane(taskDisplayArea);

        // Add the scroll pane to the frame's content pane
        add(scrollPane, BorderLayout.CENTER);
    }

    // Method to display tasks in the text area
    public void displayTasks(List<Task> tasks) {
        StringBuilder taskText = new StringBuilder();
        for (Task task : tasks) {
            taskText.append(task.toString()).append("\n"); // Append each task's string representation
        }
        taskDisplayArea.setText(taskText.toString());
    }
}
