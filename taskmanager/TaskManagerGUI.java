package com.taskmanager;

import javax.swing.*;
import javax.swing.plaf.basic.BasicScrollBarUI;
import java.awt.*;
import java.util.List;
import java.util.UUID;

import com.formdev.flatlaf.FlatDarkLaf;

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
        revalidate();
        repaint();
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

    }

    private void saveTasks() {
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
