package com.taskmanager;

import javax.swing.*;
import javax.swing.plaf.basic.BasicScrollBarUI;
import java.awt.*;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.UUID;

import com.formdev.flatlaf.FlatDarkLaf;

import static com.taskmanager.TaskManagerApp.TASKS_FILE;
import static com.taskmanager.TaskManagerApp.tasks;

public class TaskManagerGUI extends JFrame {
    private JTextArea taskDisplayArea;

    public TaskManagerGUI() {
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
            String taskDescription = JOptionPane.showInputDialog(this, "Enter task description:");
            if (taskDescription != null && !taskDescription.trim().isEmpty()) {
                String status = "todo";
                Task task = new Task(taskDescription, status);
                tasks.add(task);
                saveTasks();
                displayTasks(tasks);
            }
        });

        updateButton.addActionListener(e -> {
            String taskId = JOptionPane.showInputDialog(this, "Enter task ID to update:");
            if (taskId != null && !taskId.trim().isEmpty()) {
                Task taskToUpdate = findTaskById(taskId);
                if (taskToUpdate != null) {
                    String newStatus = JOptionPane.showInputDialog(this, "Enter new task status:", taskToUpdate.getStatus());
                    if (newStatus != null && !newStatus.trim().isEmpty()) {
                        taskToUpdate.setStatus(newStatus);
                        saveTasks();
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
                    saveTasks();
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
    public static void saveTasks() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(TASKS_FILE))) {
            writer.println("[");
            for (int i = 0; i < tasks.size(); i++) {
                Task task = tasks.get(i);
                String taskJson = String.format("{\"id\":\"%s\",\"description\":\"%s\",\"status\":\"%s\",\"createdAt\":\"%s\",\"updatedAt\":\"%s\"}",
                        task.getId(), task.getDescription(), task.getStatus(), task.getCreatedAt(), task.getUpdatedAt()
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
