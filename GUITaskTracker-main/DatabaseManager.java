package com.taskmanager;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseManager {
    static String url = "jdbc:sqlite:C:\\Users\\trent\\Desktop\\GUITaskTracker-main\\GUITaskTracker-main\\tasktracker.db";
    static Connection conn;

        public static Connection getConnection() {
            Connection conn = null;

            try {
                conn = DriverManager.getConnection(url);
                System.out.println("Connected to database");
            } catch (RuntimeException | SQLException e) {
                System.out.println("Failed to connect to database");
                e.printStackTrace();
                throw new RuntimeException(e);
            }

            return conn;
        }

    public static void initializeDatabase () {
        String createUsersTable = "CREATE TABLE IF NOT EXISTS users ("
                + "username TEXT PRIMARY KEY,"
                + "password TEXT NOT NULL"
                + ");";

        String createTasksTable = "CREATE TABLE IF NOT EXISTS tasks ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "username TEXT NOT NULL,"
                + "description TEXT NOT NULL,"
                + "status TEXT,"
                + "priority TEXT,"
                + "dueDate DATE,"
                + "createdAt DATETIME DEFAULT CURRENT_TIMESTAMP,"
                + "updatedAt DATETIME DEFAULT CURRENT_TIMESTAMP,"
                + "FOREIGN KEY (username) REFERENCES users(username)"
                + ");";

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            // Create tables
            stmt.execute(createUsersTable);
            stmt.execute(createTasksTable);
            System.out.println("Database tables created or already exist.");
        } catch (SQLException e) {
            System.out.println("An error occurred while initializing the database.");
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        initializeDatabase();
    }

}