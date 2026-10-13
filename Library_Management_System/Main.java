import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Library library = new Library();

        while (true) {
            System.out.println("\n===== LIBRARY MANAGEMENT SYSTEM =====");
            System.out.println("1. Add Book");
            System.out.println("2. Display Books");
            System.out.println("3. Search Book");
            System.out.println("4. Update Book");
            System.out.println("5. Remove Book");
            System.out.println("6. Register Member");
            System.out.println("7. Display Members");
            System.out.println("8. Issue Book");
            System.out.println("9. Return Book");
            System.out.println("10. Display Current Loans");
            System.out.println("11. Exit");
            System.out.print("Enter your choice: ");

            String choice = sc.nextLine().trim();

            switch (choice) {
                case "1":
                    library.addBook(sc);
                    break;
                case "2":
                    library.displayBooks();
                    break;
                case "3":
                    library.searchBook(sc);
                    break;
                case "4":
                    library.updateBook(sc);
                    break;
                case "5":
                    library.removeBook(sc);
                    break;
                case "6":
                    library.addMember(sc);
                    break;
                case "7":
                    library.displayMembers();
                    break;
                case "8":
                    library.issueBook(sc);
                    break;
                case "9":
                    library.returnBook(sc);
                    break;
                case "10":
                    library.displayLoans();
                    break;
                case "11":
                    System.out.println("Thank you for using the library system!");
                    sc.close();
                    return;
                default:
                    System.out.println("Invalid choice! Please select 1-11.");
            }
        }
    }
}

