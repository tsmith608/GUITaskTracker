package com.taskmanager;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.nio.file.Files;
import java.nio.file.Paths;


public class UserDataManager {
    private static final String USERS_FILE_NAME = "users.txt"; // or "users.json"

    public static Map<String, String> loadUserMap() {
        Map<String, String> userMap = new HashMap<>();
        File file = new File(USERS_FILE_NAME);

        if (!file.exists()) {
            System.out.println("No user data file found, starting anew");
            return userMap;
        }

        try {
            String content = new String(Files.readAllBytes(Paths.get(USERS_FILE_NAME)));
            content = content.trim();

            if (content.isEmpty() || content.equals("[]")) {
                return userMap;
            }

            // Remove the surrounding brackets
            if (content.startsWith("[") && content.endsWith("]")) {
                content = content.substring(1, content.length() - 1);
            }

            String[] userStrings = content.split("\\},\\{");

            for (String userString : userStrings) {
                // Clean up each userString
                userString = userString.replace("{", "").replace("}", "").replace("\"", "");

                String[] attributes = userString.split(",");

                String username = null;
                String password = null;

                for (String attribute : attributes) {
                    String[] keyValue = attribute.split(":", 2);
                    if (keyValue.length == 2) {
                        String key = keyValue[0].trim();
                        String value = keyValue[1].trim();

                        if (key.equals("username")) {
                            username = value;
                        } else if (key.equals("password")) {
                            password = value;
                        }
                    }
                }

                if (username != null && password != null) {
                    userMap.put(username, password);
                } else {
                    System.out.println("Error parsing user data: missing username or password");
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading " + USERS_FILE_NAME + ": " + e.getMessage());
        }

        return userMap;
    }

    public static void saveUserMap(Map<String, String> userMap) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(USERS_FILE_NAME))) {
            writer.println("[");
            int i = 0;
            for (Map.Entry<String, String> entry : userMap.entrySet()) {
                String username = entry.getKey();
                String password = entry.getValue();

                String userJson = String.format("{\"username\":\"%s\",\"password\":\"%s\"}", username, password);
                writer.print(userJson);

                if (i < userMap.size() - 1) {
                    writer.print(",");
                }
                i++;
            }
            writer.println("]");
        } catch (IOException e) {
            System.out.println("Error writing to " + USERS_FILE_NAME + ": " + e.getMessage());
        }
    }


}
