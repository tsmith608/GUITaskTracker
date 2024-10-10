package com.taskmanager;

import java.util.*;
import java.text.SimpleDateFormat;
import javax.swing.*;
import javax.swing.plaf.basic.BasicScrollBarUI;
import java.awt.*;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import static com.taskmanager.TaskManagerApp.tasks;


import com.formdev.flatlaf.FlatDarkLaf;
import org.jdatepicker.UtilDateModel;



public class TaskManagerGUI extends JFrame {
    private static String username;
    private JTextArea taskDisplayArea;
    private static List<Task> tasks = new ArrayList<>();

    public TaskManagerGUI(String username) {
        this.username = username;
        try {
            UIManager.setLookAndFeel(new FlatDarkLaf());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Set up the frame
        setTitle("Task Manager");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());


        // Set dark background for the frame
        getContentPane().setBackground(new Color(45, 45, 45));

        // Create and configure the text area
        taskDisplayArea = new JTextArea();
        taskDisplayArea.setEditable(false);
        taskDisplayArea.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        // Set dark background and light foreground for the text area
        taskDisplayArea.setBackground(new Color(30, 30, 30));
        taskDisplayArea.setForeground(new Color(220, 220, 220));
        taskDisplayArea.setCaretColor(new Color(30, 30, 30)); // Hide caret

        // Remove border
        taskDisplayArea.setBorder(null);

        // Add the text area to a scroll pane
        JScrollPane scrollPane = new JScrollPane(taskDisplayArea);
        scrollPane.setBorder(null);

        // Customize scroll bar colors
        scrollPane.getVerticalScrollBar().setUI(new CustomScrollBarUI());
        scrollPane.getHorizontalScrollBar().setUI(new CustomScrollBarUI());

        // Add the scroll pane to the frame's content pane
        add(scrollPane, BorderLayout.CENTER);
        JPanel bottomPanel = new JPanel();
        bottomPanel.setBackground(new Color(45, 45, 45));

        JButton addButton = new JButton("Add Task");
        JButton updateButton = new JButton("Update Task");
        JButton deleteButton = new JButton("Delete Task");

        // Set button styles
        styleButton(addButton);
        styleButton(updateButton);
        styleButton(deleteButton);

        bottomPanel.add(addButton);
        bottomPanel.add(updateButton);
        bottomPanel.add(deleteButton);

        add(bottomPanel, BorderLayout.SOUTH);


        addButton.addActionListener(e -> {
            JTextField taskDescriptionField = new JTextField();

            String[] priorityOptions = { "Low", "Medium", "High" };
            JComboBox<String> priorityComboBox = new JComboBox<>(priorityOptions);

            //date picker
            SpinnerDateModel dateModel = new SpinnerDateModel();
            JSpinner dateSpinner = new JSpinner(dateModel);
            JSpinner.DateEditor dateEditor = new JSpinner.DateEditor(dateSpinner, "yyyy-MM-dd");
            dateSpinner.setEditor(dateEditor);


            //create panel
            JPanel panel = new JPanel();
            panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
            panel.add(new JLabel("Task Description:"));
            panel.add(taskDescriptionField);
            panel.add(Box.createVerticalStrut(10)); // Add some spacing
            panel.add(new JLabel("Select Priority:"));
            panel.add(priorityComboBox);
            panel.add(Box.createVerticalStrut(10));
            panel.add(new JLabel("Select Due Date:"));
            panel.add(dateSpinner);

            int result = JOptionPane.showConfirmDialog(null, panel, "Add Task", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            if (result == JOptionPane.OK_OPTION) {
                String taskDescription = taskDescriptionField.getText().trim();
                String selectedPriority = Objects.requireNonNull(priorityComboBox.getSelectedItem()).toString();
                Date selecedDate = dateModel.getDate();

                if (taskDescription.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Task description cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    String status = "todo"; // Default status
                    //format date as string
                    String dueDate = new SimpleDateFormat("yyyy-MM-dd").format(selecedDate);
                    Task task = new Task(taskDescription, status, selectedPriority, dueDate);
                    tasks.add(task);
                    saveTasks(username);
                    displayTasks(tasks);
                }
            } else {
                System.out.println("Task addition canceled.");
            }
        });

        updateButton.addActionListener(e -> {
            String taskId = JOptionPane.showInputDialog(TaskManagerGUI.this, "Enter task ID to update:");
            if (taskId != null && !(taskId = taskId.trim()).isEmpty()) {
                Task taskToUpdate = findTaskById(taskId);
                if (taskToUpdate != null) {
                    String[] options = {"In Progress", "Done"};
                    int newStatus = JOptionPane.showOptionDialog(
                            TaskManagerGUI.this,
                            "Select the new status for the task:",
                            "Update Task Status",
                            JOptionPane.DEFAULT_OPTION,
                            JOptionPane.INFORMATION_MESSAGE,
                            null,
                            options,
                            options[0]
                    );

                        if (newStatus>= 0) {
                            String selectedStatus = options[newStatus];
                            taskToUpdate.setStatus(selectedStatus);
                            saveTasks(username);
                            displayTasks(tasks);  // Refresh task display
                        }
                    } else {
                        JOptionPane.showMessageDialog(this, "Task not found.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
        });

        deleteButton.addActionListener(e -> {
            String taskId = JOptionPane.showInputDialog(this, "Enter task ID to delete:");
            if (taskId != null && !taskId.trim().isEmpty()) {
                Task taskToDelete = findTaskById(taskId);
                if (taskToDelete != null) {
                    tasks.remove(taskToDelete);
                    saveTasks(username);
                    displayTasks(tasks);  // Refresh task display
                } else {
                    JOptionPane.showMessageDialog(this, "Task not found.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        revalidate();
        repaint();
    }
    private Task findTaskById(String id) {
        for (Task task : tasks)
            if (task.getId().equals(id))
                return task;
        return null;
    }



    public static void saveTasks(String username) {
        String tasksFileName = TaskManagerApp.getTasksFileName(username);

        try (PrintWriter writer = new PrintWriter(new FileWriter(tasksFileName))) {
            writer.println("[");
            for (int i = 0; i < tasks.size(); i++) {
                Task task = tasks.get(i);
                String taskJson = String.format("{\"id\":\"%s\",\"description\":\"%s\",\"status\":\"%s\",\"createdAt\":\"%s\",\"updatedAt\":\"%s\",\"priority\":\"%s\",\"dueDate\":\"%s\"}",
                        task.getId(), task.getDescription(), task.getStatus(), task.getCreatedAt(), task.getUpdatedAt(), task.getPriority(), task.getDueDate()
                );
                writer.print(taskJson);
                if (i < tasks.size() - 1) {
                    writer.write(",");
                }
            }
            writer.println("]");
        } catch (IOException e) {
            System.out.println("Error writing tasks.json" + e.getMessage());
        }
    }



    // Method to display tasks in the text area
    private void styleButton(JButton button) {
        button.setFocusPainted(false);
        button.setBackground(new Color(70, 70, 70));
        button.setForeground(new Color(220, 220, 220));
        button.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        button.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
    }

    public void displayTasks(List<Task> tasks) {
        StringBuilder taskText = new StringBuilder();
        for (Task task : tasks) {
            taskText.append(task.toString()).append("\n");
        }
        taskDisplayArea.setText(taskText.toString());
    }


    // Custom Scroll Bar UI Class
    class CustomScrollBarUI extends BasicScrollBarUI {
        private final Dimension d = new Dimension();

        @Override
        protected JButton createDecreaseButton(int orientation) {
            return createZeroButton();
        }

        @Override
        protected JButton createIncreaseButton(int orientation) {
            return createZeroButton();
        }

        private JButton createZeroButton() {
            JButton button = new JButton();
            button.setPreferredSize(d);
            button.setMinimumSize(d);
            button.setMaximumSize(d);
            return button;
        }

        @Override
        protected void configureScrollBarColors() {
            thumbColor = new Color(70, 70, 70);
            trackColor = new Color(45, 45, 45);
        }

    }


}
