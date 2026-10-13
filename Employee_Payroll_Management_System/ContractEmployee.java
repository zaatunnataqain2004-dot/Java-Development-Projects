
public class ContractEmployee extends Employee {

    public ContractEmployee(int id, String name, double monthlyPay,
                            double bonus, double deductions) {
        super(id, name, monthlyPay, 0, bonus, deductions);
    }

    @Override
    public String getEmployeeType() {
        return "Contract";
    }

    @Override
    public double calculateGrossSalary() {
        return getBasicSalary() + getBonus();
    }
}

