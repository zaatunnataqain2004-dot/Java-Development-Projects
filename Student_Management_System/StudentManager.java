
import java.util.ArrayList;
import java.util.Scanner;
import java.io.*;
import java.nio.file.*;
import java.nio.charset.StandardCharsets;

public class StudentManager {
    private final ArrayList<Student> students = new ArrayList<>();
    private final Path file = Paths.get("students.txt");

    public StudentManager() {
        loadStudents();
    }

    public void addStudent(Scanner sc) {
        int id = readInt(sc, "Enter student ID: ");

        if (findStudent(id) != null) {
            System.out.println("Student ID already exists!");
            return;
        }

        String name = readText(sc, "Enter name: ");
        String course = readText(sc, "Enter course: ");
        double marks = readMarks(sc);

        students.add(new Student(id, name, course, marks));
        saveStudents();
        System.out.println("Student added successfully!");
    }

    public void displayStudents() {
        if (students.isEmpty()) {
            System.out.println("No student records found.");
            return;
        }

        for (Student student : students) {
            System.out.println(student);
        }
    }

    public void searchStudent(Scanner sc) {
        int id = readInt(sc, "Enter student ID to search: ");
        Student student = findStudent(id);

        if (student == null) {
            System.out.println("Student not found.");
        } else {
            System.out.println(student);
        }
    }

    public void updateStudent(Scanner sc) {
        int id = readInt(sc, "Enter student ID to update: ");
        Student student = findStudent(id);

        if (student == null) {
            System.out.println("Student not found.");
            return;
        }

        student.setName(readText(sc, "Enter new name: "));
        student.setCourse(readText(sc, "Enter new course: "));
        student.setMarks(readMarks(sc));

        saveStudents();
        System.out.println("Student updated successfully!");
    }

    public void deleteStudent(Scanner sc) {
        int id = readInt(sc, "Enter student ID to delete: ");
        Student student = findStudent(id);

        if (student == null) {
            System.out.println("Student not found.");
            return;
        }

        students.remove(student);
        saveStudents();
        System.out.println("Student deleted successfully!");
    }

    private Student findStudent(int id) {
        for (Student student : students) {
            if (student.getId() == id) {
                return student;
            }
        }
        return null;
    }

    private int readInt(Scanner sc, String message) {
        while (true) {
            System.out.print(message);
            try {
                int value = Integer.parseInt(sc.nextLine().trim());

                if (value <= 0) {
                    System.out.println("Enter a positive ID.");
                    continue;
                }

                return value;
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Enter a whole number.");
            }
        }
    }

    private String readText(Scanner sc, String message) {
        while (true) {
            System.out.print(message);
            String value = sc.nextLine().trim();

            if (!value.isEmpty() && !value.contains("|")) {
                return value;
            }

            System.out.println("Input cannot be empty or contain |.");
        }
    }

    private double readMarks(Scanner sc) {
        while (true) {
            System.out.print("Enter marks (0-100): ");
            try {
                double marks = Double.parseDouble(sc.nextLine().trim());

                if (Double.isFinite(marks) && marks >= 0 && marks <= 100) {
                    return marks;
                }

                System.out.println("Marks must be between 0 and 100.");
            } catch (NumberFormatException e) {
                System.out.println("Invalid marks. Enter a number.");
            }
        }
    }

    private void saveStudents() {
        try (BufferedWriter writer = Files.newBufferedWriter(
                file, StandardCharsets.UTF_8)) {
            for (Student student : students) {
                writer.write(student.toFileString());
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saving records: " + e.getMessage());
        }
    }

    private void loadStudents() {
        if (!Files.exists(file)) {
            return;
        }

        try {
            for (String line : Files.readAllLines(
                    file, StandardCharsets.UTF_8)) {
                String[] data = line.split("\\|", -1);

                if (data.length != 4) {
                    System.out.println("Skipping invalid student record.");
                    continue;
                }

                try {
                    int id = Integer.parseInt(data[0]);
                    String name = data[1];
                    String course = data[2];
                    double marks = Double.parseDouble(data[3]);

                    if (id <= 0 || name.isBlank() || course.isBlank()
                            || !Double.isFinite(marks)
                            || marks < 0 || marks > 100
                            || findStudent(id) != null) {
                        System.out.println("Skipping invalid student record.");
                        continue;
                    }

                    students.add(new Student(id, name, course, marks));
                } catch (NumberFormatException e) {
                    System.out.println("Skipping invalid student record.");
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading records: " + e.getMessage());
        }
    }
}
