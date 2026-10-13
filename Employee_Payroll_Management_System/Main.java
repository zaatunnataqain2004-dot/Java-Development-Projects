
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        PayrollManager payroll = new PayrollManager();

        while (true) {
            System.out.println("\n===== EMPLOYEE PAYROLL MANAGEMENT SYSTEM =====");
            System.out.println("1. Add Employee");
            System.out.println("2. Display All Employees");
            System.out.println("3. Search Employee");
            System.out.println("4. Update Employee");
            System.out.println("5. Delete Employee");
            System.out.println("6. Generate Salary Slip");
            System.out.println("7. Exit");
            System.out.print("Enter your choice: ");

            String choice = sc.nextLine().trim();

            switch (choice) {
                case "1":
                    payroll.addEmployee(sc);
                    break;

                case "2":
                    payroll.displayEmployees();
                    break;

                case "3":
                    payroll.searchEmployee(sc);
                    break;

                case "4":
                    payroll.updateEmployee(sc);
                    break;

                case "5":
                    payroll.deleteEmployee(sc);
                    break;

                case "6":
                    payroll.generateSalarySlip(sc);
                    break;

                case "7":
                    System.out.println("Thank you for using the payroll system!");
                    sc.close();
                    return;

                default:
                    System.out.println("Invalid choice. Please select 1-7.");
            }
        }
    }
}
