import java.util.ArrayList;
import java.util.Scanner;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class Library {
    private final ArrayList<Book> books = new ArrayList<>();
    private final ArrayList<Member> members = new ArrayList<>();

    // Each loan stores {book ID, member ID}.
    private final ArrayList<int[]> loans = new ArrayList<>();

    private final Path booksFile = Paths.get("books.txt");
    private final Path membersFile = Paths.get("members.txt");
    private final Path loansFile = Paths.get("loans.txt");

    public Library() {
        loadData();
    }

    // ---------- BOOK MANAGEMENT ----------

    public void addBook(Scanner sc) {
        int id = readPositiveInt(sc, "Enter book ID: ");

        if (findBook(id) != null) {
            System.out.println("Book ID already exists!");
            return;
        }

        String title = readText(sc, "Enter book title: ");
        String author = readText(sc, "Enter author name: ");

        books.add(new Book(id, title, author));
        saveData();
        System.out.println("Book added successfully!");
    }

    public void displayBooks() {
        if (books.isEmpty()) {
            System.out.println("No books found.");
            return;
        }

        for (Book book : books) {
            System.out.println(book);
        }
    }

    public void searchBook(Scanner sc) {
        int id = readPositiveInt(sc, "Enter book ID: ");
        Book book = findBook(id);

        if (book == null) {
            System.out.println("Book not found.");
        } else {
            System.out.println(book);
        }
    }

    public void updateBook(Scanner sc) {
        int id = readPositiveInt(sc, "Enter book ID to update: ");
        Book book = findBook(id);

        if (book == null) {
            System.out.println("Book not found.");
            return;
        }

        if (!book.isAvailable()) {
            System.out.println("Return the book before updating it.");
            return;
        }

        book.setTitle(readText(sc, "Enter new title: "));
        book.setAuthor(readText(sc, "Enter new author: "));

        saveData();
        System.out.println("Book updated successfully!");
    }

    public void removeBook(Scanner sc) {
        int id = readPositiveInt(sc, "Enter book ID to remove: ");
        Book book = findBook(id);

        if (book == null) {
            System.out.println("Book not found.");
            return;
        }

        if (!book.isAvailable()) {
            System.out.println("Return the book before removing it.");
            return;
        }

        books.remove(book);
        saveData();
        System.out.println("Book removed successfully!");
    }

    // ---------- MEMBER MANAGEMENT ----------

    public void addMember(Scanner sc) {
        int id = readPositiveInt(sc, "Enter member ID: ");

        if (findMember(id) != null) {
            System.out.println("Member ID already exists!");
            return;
        }

        String name = readText(sc, "Enter member name: ");
        String phone = readText(sc, "Enter phone number: ");

        members.add(new Member(id, name, phone));
        saveData();
        System.out.println("Member registered successfully!");
    }

    public void displayMembers() {
        if (members.isEmpty()) {
            System.out.println("No members registered.");
            return;
        }

        for (Member member : members) {
            System.out.println(member);
        }
    }

    // ---------- ISSUE AND RETURN ----------

    public void issueBook(Scanner sc) {
        int bookId = readPositiveInt(sc, "Enter book ID: ");
        int memberId = readPositiveInt(sc, "Enter member ID: ");

        Book book = findBook(bookId);
        Member member = findMember(memberId);

        if (book == null) {
            System.out.println("Book not found.");
            return;
        }

        if (member == null) {
            System.out.println("Member not found.");
            return;
        }

        if (!book.isAvailable()) {
            System.out.println("Book is already issued.");
            return;
        }

        book.setAvailable(false);
        loans.add(new int[]{bookId, memberId});

        saveData();
        System.out.println("Book issued successfully to "
                + member.getName() + "!");
    }

    public void returnBook(Scanner sc) {
        int bookId = readPositiveInt(sc, "Enter book ID to return: ");
        Book book = findBook(bookId);

        if (book == null) {
            System.out.println("Book not found.");
            return;
        }

        if (book.isAvailable()) {
            System.out.println("This book has not been issued.");
            return;
        }

        int loanIndex = -1;

        for (int i = 0; i < loans.size(); i++) {
            if (loans.get(i)[0] == bookId) {
                loanIndex = i;
                break;
            }
        }

        if (loanIndex == -1) {
            System.out.println("Loan record not found.");
            return;
        }

        loans.remove(loanIndex);
        book.setAvailable(true);

        saveData();
        System.out.println("Book returned successfully!");
    }

    public void displayLoans() {
        if (loans.isEmpty()) {
            System.out.println("No books are currently issued.");
            return;
        }

        System.out.println("\n===== CURRENT LOANS =====");

        for (int[] loan : loans) {
            Book book = findBook(loan[0]);
            Member member = findMember(loan[1]);

            if (book != null && member != null) {
                System.out.println(
                    "Book: " + book.getTitle()
                    + " | Member: " + member.getName()
                    + " | Book ID: " + loan[0]
                    + " | Member ID: " + loan[1]
                );
            }
        }
    }

    // ---------- SEARCH HELPERS ----------

    private Book findBook(int id) {
        for (Book book : books) {
            if (book.getId() == id) {
                return book;
            }
        }
        return null;
    }

    private Member findMember(int id) {
        for (Member member : members) {
            if (member.getId() == id) {
                return member;
            }
        }
        return null;
    }

    // ---------- INPUT VALIDATION ----------

    private int readPositiveInt(Scanner sc, String message) {
        while (true) {
            System.out.print(message);

            try {
                int value = Integer.parseInt(sc.nextLine().trim());

                if (value > 0) {
                    return value;
                }

                System.out.println("Enter a positive number.");
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

    // ---------- FILE STORAGE ----------

    private void saveData() {
        try {
            List<String> bookLines = new ArrayList<>();

            for (Book book : books) {
                bookLines.add(book.getId() + "|"
                        + book.getTitle() + "|"
                        + book.getAuthor() + "|"
                        + book.isAvailable());
            }

            Files.write(booksFile, bookLines, StandardCharsets.UTF_8);

            List<String> memberLines = new ArrayList<>();

            for (Member member : members) {
                memberLines.add(member.getId() + "|"
                        + member.getName() + "|"
                        + member.getPhone());
            }

            Files.write(membersFile, memberLines, StandardCharsets.UTF_8);

            List<String> loanLines = new ArrayList<>();

            for (int[] loan : loans) {
                loanLines.add(loan[0] + "|" + loan[1]);
            }

            Files.write(loansFile, loanLines, StandardCharsets.UTF_8);

        } catch (IOException e) {
            System.out.println("Error saving data: " + e.getMessage());
        }
    }

    private void loadData() {
        // Load books.
        if (Files.exists(booksFile)) {
            try {
                for (String line : Files.readAllLines(
                        booksFile, StandardCharsets.UTF_8)) {
                    try {
                        String[] d = line.split("\\|", -1);

                        if (d.length != 4) {
                            continue;
                        }

                        int id = Integer.parseInt(d[0]);
                        boolean available = Boolean.parseBoolean(d[3]);

                        if (id <= 0 || d[1].isBlank() || d[2].isBlank()
                                || !(d[3].equals("true")
                                || d[3].equals("false"))
                                || findBook(id) != null) {
                            continue;
                        }

                        Book book = new Book(id, d[1], d[2]);
                        book.setAvailable(available);
                        books.add(book);
                    } catch (NumberFormatException e) {
                        System.out.println("Skipping invalid book record.");
                    }
                }
            } catch (IOException e) {
                System.out.println("Error loading books: " + e.getMessage());
            }
        }

        // Load members.
        if (Files.exists(membersFile)) {
            try {
                for (String line : Files.readAllLines(
                        membersFile, StandardCharsets.UTF_8)) {
                    try {
                        String[] d = line.split("\\|", -1);

                        if (d.length != 3) {
                            continue;
                        }

                        int id = Integer.parseInt(d[0]);

                        if (id <= 0 || d[1].isBlank() || d[2].isBlank()
                                || findMember(id) != null) {
                            continue;
                        }

                        members.add(new Member(id, d[1], d[2]));
                    } catch (NumberFormatException e) {
                        System.out.println("Skipping invalid member record.");
                    }
                }
            } catch (IOException e) {
                System.out.println("Error loading members: " + e.getMessage());
            }
        }

        // Load loans and reconcile book availability.
        for (Book book : books) {
            book.setAvailable(true);
        }

        if (Files.exists(loansFile)) {
            try {
                for (String line : Files.readAllLines(
                        loansFile, StandardCharsets.UTF_8)) {
                    try {
                        String[] d = line.split("\\|", -1);

                        if (d.length != 2) {
                            continue;
                        }

                        int bookId = Integer.parseInt(d[0]);
                        int memberId = Integer.parseInt(d[1]);
                        Book book = findBook(bookId);
                        Member member = findMember(memberId);

                        if (book != null && member != null
                                && book.isAvailable()) {
                            book.setAvailable(false);
                            loans.add(new int[]{bookId, memberId});
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Skipping invalid loan record.");
                    }
                }
            } catch (IOException e) {
                System.out.println("Error loading loans: " + e.getMessage());
            }
        }
    }
}
