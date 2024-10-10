//import java.io.*;
//import java.util.*;
//import java.nio.file.*;
//
//    //cd C:\Users\trent\IdeaProjects\CLI-To-do-List-main\CLI-To-do-List-main
//    //javac Task.java TaskTracker.java
//    //{"id":"7302","description":"test2","status":"todo","createdAt":"2024-10-07","updatedAt":"2024-10-07"}
//public class TaskTracker {
//    private static final String TASKS_FILE = "tasks.json";
//    private static List<Task> tasks = new ArrayList<>();
//
//    public static void main(String[] args) {
//        if (args.length < 1) {
//            System.out.println("No command");
//        }
//
//        //loadTasks();
//
//        String command = args[0];
//
//
//
//
//
//        switch (command) {
//            case "add":
//                if (args.length != 2) {
//                    System.out.println("Usage: add <description>");
//                    return;
//                }
//                addTask(args[1]);
//                break;
//
//            case "update":
//                if (args.length != 3) {
//                    System.out.println("Usage: update <id> <new status>");
//                }
//                updateTask(args[1], args[2]);
//                break;
//
//            case "clear":
//                deleteAllTasks();
//                break;
//
//            case "delete":
//            if (args.length != 2) {
//                System.out.println("Usage: delete <id>");
//                return;
//            }
//            deleteTask(args[1]);
//            break;
//
//            case "list":
//                if (args.length == 1) {
//                    listAllTasks();
//                } else {
//                    if (args.length == 2)
//                        listTasksByStatus(args[1]);
//                    System.out.println("Usage: list [status]");
//                }
//                break;
//            default:
//                System.out.println("unknown command: " + command);
//                break;
//        }
//    }
//
////    public static void loadTasks() {
////        File file = new File(TASKS_FILE);
////
////
////        if (!file.exists()) {
////            System.out.println("No tasks file found, starting anew");
////            return;
////        }
////
////        try {
////            String content = new String(Files.readAllBytes(Paths.get(TASKS_FILE)));
////            content = content.trim();
////
////            if (content.isEmpty() || content.equals("[]")) {
////                return;
////            }
////            content = content.substring(1, content.length() - 1);
////
////            String[] taskStrings = content.split("\\},\\{");
////
////            for (String taskString : taskStrings) {
////                taskString = taskString.replace("}", "").replace("}", "").replace("\"", "");
////                String[] attributes = taskString.split(",");
////
////                String id = attributes[0].split(":")[1];
////                String description = attributes[1].split(":")[1];
////                String status = attributes[2].split(":")[1];
////                String createdAt = attributes[3].split(":")[1];
////                String updatedAt = attributes[4].split(":")[1];
////
////                Task task = new Task(id, description, status, createdAt, updatedAt);
////                tasks.add(task);
////
////                //System.out.println("Successfully loaded task: " + description);
////                //System.out.println("loaded " + tasks.size() + " tasks");
////
////            }
////        } catch (IOException e) {
////            System.out.println("Error reading tasks.json" + e.getMessage());
////        }
////    }
//
//    public static void saveTasks() {
//        try (PrintWriter writer = new PrintWriter(new FileWriter(TASKS_FILE))) {
//            writer.println("[");
//            for (int i = 0; i < tasks.size(); i++) {
//                Task task = tasks.get(i);
//                String taskJson = String.format("{\"id\":\"%s\",\"description\":\"%s\",\"status\":\"%s\",\"createdAt\":\"%s\",\"updatedAt\":\"%s\"}",
//                        task.getId(), task.getDescription(), task.getStatus(), task.getCreatedAt(), task.getUpdatedAt()
//                );
//                writer.print(taskJson);
//                if (i < tasks.size() - 1) {
//                    writer.write(",");
//                }
//            }
//            writer.println("]");
//        } catch (IOException e) {
//            System.out.println("Error writing tasks.json" + e.getMessage());
//        }
//    }
//
//    private static void addTask(String description) {
//        String status = "todo";
//        String id = UUID.randomUUID().toString();
//        Task task = new Task(description, status);
//        tasks.add(task);
//        saveTasks();
//        System.out.println("Task " + id + " added");
//    }
//
//    private static void updateTask(String id, String newStatus) {
//        for (Task task : tasks) {
//            if (task.getId().equals(id)) {
//                task.setStatus(newStatus);
//                saveTasks();
//                return;
//
//            }
//        }
//        System.out.println("Task not found with id " + id);
//    }
//
//    private static void deleteTask(String id) {
//        tasks.removeIf(task -> task.getId().equals(id));
//        saveTasks();
//    }
//    private static void deleteAllTasks() {
//        tasks.clear();
//        saveTasks();
//    }
//
//    private static void listAllTasks() {
//        for (Task task : tasks) {
//            System.out.println(task);
//        }
//    }
//
//    private static void listTasksByStatus(String status) {
//        for (Task task : tasks) {
//            if (task.getStatus().equalsIgnoreCase(status)) {
//                System.out.println(task);
//            }
//        }
//    }
//
//}
//
//
