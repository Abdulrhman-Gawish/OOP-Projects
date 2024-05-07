import java.util.*;

class Book {
    public int id;
    public String name;
    public int total_quantity;
    public int total_borrowed;

    public Book() {
        total_quantity = total_borrowed = 0;
        id = -1;
        name = "";
    }

    public void read() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter book info: id & name & total quantity:");
        id = scanner.nextInt();
        name = scanner.next();
        total_quantity = scanner.nextInt();
        total_borrowed = 0;
    }

    public boolean borrow(int _user_id) {
        if (total_quantity - total_borrowed == 0)
            return false;
        ++total_borrowed;
        return true;
    }

    public void return_copy() {
        assert total_borrowed > 0;
        --total_borrowed;
    }

    public boolean has_prefix(String _prefix) {
        if (name.length() < _prefix.length())
            return false;

        for (int i = 0; i < _prefix.length(); ++i) {
            if (_prefix.charAt(i) != name.charAt(i))
                return false;
        }
        return true;
    }

    public void print() {
        System.out.println("id = " + id + " name = " + name + " total quantity "
                + total_quantity + " total borrowed " + total_borrowed);
    }
}

class User {
    public int id;
    public String name;
    public Set<Integer> borrowed_books_ids;

    public User() {
        name = "";
        id = -1;
        borrowed_books_ids = new HashSet<>();
    }

    public void read() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter user name & national id:");
        name = scanner.next();
        id = scanner.nextInt();
    }

    public void borrow(int _book_id) {
        borrowed_books_ids.add(_book_id);
    }

    public void return_copy(int _book_id) {
        if (borrowed_books_ids.contains(_book_id))
            borrowed_books_ids.remove(_book_id);
        else
            System.out.println("User " + name + " never borrowed book id " + _book_id);
    }

    public boolean is_borrowed(int _book_id) {
        return borrowed_books_ids.contains(_book_id);
    }

    public void print() {
        System.out.print("user " + name + " id " + id + " borrowed books ids: ");
        for (int book_id : borrowed_books_ids)
            System.out.print(book_id + " ");
        System.out.println();
    }
}

class LibrarySystem {
    public List<Book> books;
    public List<User> users;

    public LibrarySystem() {
        books = new ArrayList<>();
        users = new ArrayList<>();
    }

    public void run() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            int choice = menu(scanner);

            if (choice == 1)
                add_book();
            else if (choice == 2)
                search_books_by_prefix();
            else if (choice == 3)
                print_who_borrowed_book_by_name();
            else if (choice == 4)
                print_library_by_id();
            else if (choice == 5)
                print_library_by_name();
            else if (choice == 6)
                add_user();
            else if (choice == 7)
                user_borrow_book();
            else if (choice == 8)
                user_return_book();
            else if (choice == 9)
                print_users();
            else
                break;
        }
    }

    public int menu(Scanner scanner) {
        int choice = -1;
        while (choice == -1) {
            System.out.println("\nLibrary Menu:");
            System.out.println("1) Add Book");
            System.out.println("2) Search books by Prefix");
            System.out.println("3) Print Who Borrowed Book By Name");
            System.out.println("4) Print Library By Id");
            System.out.println("5) Print library by name");
            System.out.println("6) Add user");
            System.out.println("7) User borrow book");
            System.out.println("8) User return book");
            System.out.println("9) Print users");
            System.out.println("10) Exit");

            System.out.print("\nEnter your menu choice [1 - 10]: ");
            choice = scanner.nextInt();

            if (!(1 <= choice && choice <= 10)) {
                System.out.println("Invalid choice. Try again");
                choice = -1;	// loop keep working
            }
        }
        return choice;
    }

    public void add_book() {
        Book b = new Book();
        b.read();
        books.add(b);
    }

    public void search_books_by_prefix() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter book name prefix: ");
        String prefix = scanner.next();

        int cnt = 0;
        for (Book b : books) {
            if (b.has_prefix(prefix)) {
                System.out.println(b.name);
                ++cnt;
            }
        }

        if (cnt == 0)
            System.out.println("No books with such prefix");
    }

    public void add_user() {
        User u = new User();
        u.read();
        users.add(u);
    }

    public int find_book_idx_by_name(String _name) {
        for (int i = 0; i < books.size(); ++i) {
            if (_name.equals(books.get(i).name))
                return i;
        }
        return -1;
    }

    public int find_user_idx_by_name(String _name) {
        for (int i = 0; i < users.size(); ++i) {
            if (_name.equals(users.get(i).name))
                return i;
        }
        return -1;
    }

    public boolean read_user_name_and_book_name(Scanner scanner, int[] indexes, int _trials) {
        String user_name;
        String book_name;

        while (_trials-- > 0) {
            System.out.println("Enter user name and book name:");
            user_name = scanner.next();
            book_name = scanner.next();

            indexes[0] = find_user_idx_by_name(user_name);

            if (indexes[0] == -1) {
                System.out.println("Invalid user name, Try again please");
                continue;
            }

            indexes[1] = find_book_idx_by_name(book_name);

            if (indexes[1] == -1) {
                System.out.println("Invalid book name, Try again please");
                continue;
            }

            return true;
        }
        System.out.println("You did several trials! Try later.");
        return false;
    }

    public void user_borrow_book() {
        int[] indexes = new int[2];
        Scanner scanner = new Scanner(System.in);

        if (!read_user_name_and_book_name(scanner, indexes, 3))
            return;

        int user_id = users.get(indexes[0]).id;
        int book_id = books.get(indexes[1]).id;

        if (!books.get(indexes[1]).borrow(user_id))
            System.out.println("No more copies available right now");
        else
            users.get(indexes[0]).borrow(book_id);
    }

    public void user_return_book() {
        int[] indexes = new int[2];
        Scanner scanner = new Scanner(System.in);

        if (!read_user_name_and_book_name(scanner, indexes, 3))
            return;

        int book_id = books.get(indexes[1]).id;
        books.get(indexes[1]).return_copy();
        users.get(indexes[0]).return_copy(book_id);
    }

    public void print_library_by_id() {
        books.sort(Comparator.comparingInt(a -> a.id));

        System.out.println();
        for (Book b : books)
            b.print();
    }

    public void print_library_by_name() {
        books.sort(Comparator.comparing(a -> a.name));

        System.out.println();
        for (Book b : books)
            b.print();
    }

    public void print_users() {
        System.out.println();
        for (User u : users)
            u.print();
    }

    public void print_who_borrowed_book_by_name() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter book name: ");
        String book_name = scanner.next();

        int book_idx = find_book_idx_by_name(book_name);

        if (book_idx == -1) {
            System.out.println("Invalid book name.");
            return;
        }
        int book_id = books.get(book_idx).id;

        if (books.get(book_idx).total_borrowed == 0) {
            System.out.println("No borrowed copies");
            return;
        }

        for (User u : users) {
            if (u.is_borrowed(book_id))
                System.out.println(u.name);
        }
    }
}

public class Main {
    public static void main(String[] args) {
        LibrarySystem librarySystem = new LibrarySystem();
        librarySystem.run();
    }
}
