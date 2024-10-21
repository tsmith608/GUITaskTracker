package com.taskmanager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static com.taskmanager.DatabaseManager.conn;

public class TaskDAO {
    public static List<Task> getTasksByUsername(String username) throws SQLException {
        List<Task> tasks = new ArrayList<Task>();
        String sql = "SELECT * FROM tasks where username = ?";

        try(Connection conn = DatabaseManager.getConnection();
        PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();

            while(rs.next()) {
                Task task = new Task(
                        rs.getString("id"),
                        rs.getString("username"),
                        rs.getString("description"),
                        rs.getString("status"),
                        rs.getString("priority"),
                        rs.getString("dueDate"),
                        rs.getString("createdAt"),
                        rs.getString("updatedAt")
                );
                tasks.add(task);
            }
            } catch (SQLException e) {
                System.out.println("error ocured while retrieving tasks");
                e.printStackTrace();
            }
        return tasks;
    }

    public static void addTask(Task task) {
        //debug
//        System.out.println("Adding task with details:");
//        System.out.println("id: " + task.getId());
//        System.out.println("Username: " + task.getUsername() + LoginDialog.getLoggedInUsername());
//        System.out.println("Description: " + task.getDescription());
//        System.out.println("Status: " + task.getStatus());
//        System.out.println("Priority: " + task.getPriority());
//        System.out.println("Due Date: " + task.getDueDate());


        String sql = "INSERT INTO tasks(username, description, status, priority, dueDate) VALUES (?, ?, ?, ?, ?)";

        try(Connection con = DatabaseManager.getConnection();
            PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setString(1, LoginDialog.getLoggedInUsername());
            pstmt.setString(2, task.getDescription());
            pstmt.setString(3, task.getStatus());
            pstmt.setString(4, task.getPriority());
            pstmt.setString(5, task.getDueDate());

            pstmt.executeUpdate();
            System.out.println("Task Added Successfully");
        } catch (SQLException e) {
            System.out.println("error ocured while adding task");
            e.printStackTrace();
        }

    }
    public static void updateTask(String newStatus, String id) {
        String sql = "UPDATE tasks SET status = ? WHERE id = ?";

        try (Connection con = DatabaseManager.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)){
             pstmt.setString(1, newStatus);
             pstmt.setString(2, id);

             pstmt.executeUpdate();
             System.out.println("Task Updated Successfully");
        } catch (SQLException e) {
            System.out.println("error ocured while updating task");
        }
    }

    public static void deleteTask(String id) {
        String sql = "DELETE FROM tasks WHERE id = ?";

        try (Connection con = DatabaseManager.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setString(1, id);

            pstmt.executeUpdate();
            System.out.println("Task Deleted Successfully");
        } catch (SQLException e) {
            System.out.println("error ocured while updating task");
        }
    }
}
