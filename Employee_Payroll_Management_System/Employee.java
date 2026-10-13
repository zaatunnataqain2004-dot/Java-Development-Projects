
public abstract class Employee {
    private int id;
    private String name;
    private double basicSalary;
    private double allowances;
    private double bonus;
    private double deductions;

    public Employee(int id, String name, double basicSalary,
                    double allowances, double bonus, double deductions) {
        this.id = id;
        this.name = name;
        this.basicSalary = basicSalary;
        this.allowances = allowances;
        this.bonus = bonus;
        this.deductions = deductions;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getBasicSalary() {
        return basicSalary;
    }

    public double getAllowances() {
        return allowances;
    }

    public double getBonus() {
        return bonus;
    }

    public double getDeductions() {
        return deductions;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSalaryDetails(double basicSalary, double allowances,
                                 double bonus, double deductions) {
        this.basicSalary = basicSalary;
        this.allowances = allowances;
        this.bonus = bonus;
        this.deductions = deductions;
    }

    public abstract String getEmployeeType();

    public abstract double calculateGrossSalary();

    public double calculateNetSalary() {
        return calculateGrossSalary() - deductions;
    }

    public void displaySalarySlip() {
        System.out.println("\n========== SALARY SLIP ==========");
        System.out.println("Employee ID : " + id);
        System.out.println("Name        : " + name);
        System.out.println("Employee Type: " + getEmployeeType());
        System.out.printf("Basic Salary: Rs. %.2f%n", basicSalary);
        System.out.printf("Allowances  : Rs. %.2f%n", allowances);
        System.out.printf("Bonus       : Rs. %.2f%n", bonus);
        System.out.printf("Deductions  : Rs. %.2f%n", deductions);
        System.out.printf("Gross Salary: Rs. %.2f%n",
                          calculateGrossSalary());
        System.out.printf("Net Salary  : Rs. %.2f%n",
                          calculateNetSalary());
        System.out.println("=================================");
    }

    public String toFileString() {
        return getEmployeeType() + "|" + id + "|" + name + "|"
                + basicSalary + "|" + allowances + "|"
                + bonus + "|" + deductions;
    }

    @Override
    public String toString() {
        return "ID: " + id
                + " | Name: " + name
                + " | Type: " + getEmployeeType()
                + " | Net Salary: Rs. "
                + String.format("%.2f", calculateNetSalary());
    }
}

