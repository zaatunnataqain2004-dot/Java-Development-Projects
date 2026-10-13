
import java.util.ArrayList;
import java.util.Scanner;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class PayrollManager {
    private final ArrayList<Employee> employees = new ArrayList<>();
    private final Path file = Paths.get("employees.txt");

    public PayrollManager() {
        loadEmployees();
    }

    // Add an employee
    public void addEmployee(Scanner sc) {
        int id = readPositiveInt(sc, "Enter employee ID: ");

        if (findEmployee(id) != null) {
            System.out.println("Employee ID already exists!");
            return;
        }

        String name = readText(sc, "Enter employee name: ");

        System.out.println("1. Permanent Employee");
        System.out.println("2. Contract Employee");
        String type;

        while (true) {
            System.out.print("Select employee type: ");
            type = sc.nextLine().trim();

            if (type.equals("1") || type.equals("2")) {
                break;
            }

            System.out.println("Please enter 1 or 2.");
        }

        double salary = readMoney(sc, "Enter basic/monthly salary: ");
        double bonus = readMoney(sc, "Enter bonus: ");
        double deductions = readMoney(sc, "Enter deductions: ");

        Employee employee;

        if (type.equals("1")) {
            double allowances = readMoney(sc, "Enter allowances: ");

            employee = new PermanentEmployee(
                    id, name, salary, allowances, bonus, deductions);
        } else {
            employee = new ContractEmployee(
                    id, name, salary, bonus, deductions);
        }

        employees.add(employee);
        saveEmployees();
        System.out.println("Employee added successfully!");
    }

    // Display all employees
    public void displayEmployees() {
        if (employees.isEmpty()) {
            System.out.println("No employee records found.");
            return;
        }

        System.out.println("\n===== EMPLOYEE RECORDS =====");

        for (Employee employee : employees) {
            System.out.println(employee);
        }
    }

    // Search employee by ID
    public void searchEmployee(Scanner sc) {
        int id = readPositiveInt(sc, "Enter employee ID: ");
        Employee employee = findEmployee(id);

        if (employee == null) {
            System.out.println("Employee not found.");
        } else {
            System.out.println(employee);
        }
    }

    // Update employee details
    public void updateEmployee(Scanner sc) {
        int id = readPositiveInt(sc, "Enter employee ID to update: ");
        Employee employee = findEmployee(id);

        if (employee == null) {
            System.out.println("Employee not found.");
            return;
        }

        String name = readText(sc, "Enter new employee name: ");
        double salary = readMoney(sc, "Enter new basic/monthly salary: ");
        double bonus = readMoney(sc, "Enter new bonus: ");
        double deductions = readMoney(sc, "Enter new deductions: ");

        double allowances = 0;

        if (employee instanceof PermanentEmployee) {
            allowances = readMoney(sc, "Enter new allowances: ");
        }

        employee.setName(name);
        employee.setSalaryDetails(
                salary, allowances, bonus, deductions);

        saveEmployees();
        System.out.println("Employee updated successfully!");
    }

    // Delete employee
    public void deleteEmployee(Scanner sc) {
        int id = readPositiveInt(sc, "Enter employee ID to delete: ");
        Employee employee = findEmployee(id);

        if (employee == null) {
            System.out.println("Employee not found.");
            return;
        }

        employees.remove(employee);
        saveEmployees();
        System.out.println("Employee deleted successfully!");
    }

    // Generate a salary slip
    public void generateSalarySlip(Scanner sc) {
        int id = readPositiveInt(sc, "Enter employee ID: ");
        Employee employee = findEmployee(id);

        if (employee == null) {
            System.out.println("Employee not found.");
            return;
        }

        employee.displaySalarySlip();
    }

    private Employee findEmployee(int id) {
        for (Employee employee : employees) {
            if (employee.getId() == id) {
                return employee;
            }
        }

        return null;
    }

    // Input validation
    private int readPositiveInt(Scanner sc, String message) {
        while (true) {
            System.out.print(message);

            try {
                int value = Integer.parseInt(sc.nextLine().trim());

                if (value > 0) {
                    return value;
                }

                System.out.println("ID must be positive.");
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

            System.out.println("Name cannot be empty or contain |.");
        }
    }

    private double readMoney(Scanner sc, String message) {
        while (true) {
            System.out.print(message);

            try {
                double value = Double.parseDouble(sc.nextLine().trim());

                if (Double.isFinite(value) && value >= 0) {
                    return value;
                }

                System.out.println("Amount must be a non-negative number.");
            } catch (NumberFormatException e) {
                System.out.println("Invalid amount. Try again.");
            }
        }
    }

    // Save employee records
    private void saveEmployees() {
        List<String> lines = new ArrayList<>();

        for (Employee employee : employees) {
            lines.add(employee.toFileString());
        }

        try {
            Files.write(file, lines, StandardCharsets.UTF_8);
        } catch (IOException e) {
            System.out.println("Error saving employees: " + e.getMessage());
        }
    }

    // Load employee records when the application starts
    private void loadEmployees() {
        if (!Files.exists(file)) {
            return;
        }

        try {
            for (String line : Files.readAllLines(
                    file, StandardCharsets.UTF_8)) {
                try {
                    String[] data = line.split("\\|", -1);

                    if (data.length != 7) {
                        continue;
                    }

                    String type = data[0];
                    int id = Integer.parseInt(data[1]);
                    String name = data[2];
                    double salary = Double.parseDouble(data[3]);
                    double allowances = Double.parseDouble(data[4]);
                    double bonus = Double.parseDouble(data[5]);
                    double deductions = Double.parseDouble(data[6]);

                    if (id <= 0 || name.isBlank()
                            || !Double.isFinite(salary)
                            || !Double.isFinite(allowances)
                            || !Double.isFinite(bonus)
                            || !Double.isFinite(deductions)
                            || salary < 0 || allowances < 0
                            || bonus < 0 || deductions < 0
                            || findEmployee(id) != null) {
                        continue;
                    }

                    Employee employee;

                    if (type.equals("Permanent")) {
                        employee = new PermanentEmployee(
                                id, name, salary, allowances,
                                bonus, deductions);
                    } else if (type.equals("Contract")
                            && allowances == 0) {
                        employee = new ContractEmployee(
                                id, name, salary, bonus, deductions);
                    } else {
                        continue;
                    }

                    employees.add(employee);

                } catch (NumberFormatException e) {
                    System.out.println("Skipping invalid employee record.");
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading employees: " + e.getMessage());
        }
    }
}

