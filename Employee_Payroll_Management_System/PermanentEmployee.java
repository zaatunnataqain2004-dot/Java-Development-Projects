
public class PermanentEmployee extends Employee {

    public PermanentEmployee(int id, String name, double basicSalary,
                             double allowances, double bonus,
                             double deductions) {
        super(id, name, basicSalary, allowances, bonus, deductions);
    }

    @Override
    public String getEmployeeType() {
        return "Permanent";
    }

    @Override
    public double calculateGrossSalary() {
        return getBasicSalary() + getAllowances() + getBonus();
    }
}
