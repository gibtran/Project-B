import java.util.List;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

interface BookFilter {
    // Returns all digital books.
    LibraryModel filterDigitalBook();

    // Returns all digital books by author.
    LibraryModel filterDigitalBook(String author);

    // Returns all digital books by genre.
    LibraryModel filterDigitalBook(Genre genre);

    // Returns all print books.
    LibraryModel filterPrintBook();

    // Returns all print books by author.
    LibraryModel filterPrintBook(String author);

    // Returns all print books by genre.
    LibraryModel filterPrintBook(Genre genre);

    // Returns all books by author.
    LibraryModel filterBooks(String author);

    // Returns all books in genre.
    LibraryModel filterBooks(Genre genre);
}

interface BookSort {
    void sortByAuthor();

    void sortByTitle();
}

class UserModel {
    private List<Book> borrowedBooks;
    private List<Book> downloadedBooks;
    private LibraryModel library;
    private ObservableList<Book> userBooks;

    public UserModel(LibraryModel library) {
        this.borrowedBooks = new ArrayList<>();
        this.downloadedBooks = new ArrayList<>();
        this.library = library;

        this.userBooks = FXCollections.observableArrayList();
    }

    public String toString() {
        return "" + borrowedBooks + "\n" + downloadedBooks;
    }

    public void returnBook(Book book) {
        if (borrowedBooks.contains(book)) {
            borrowedBooks.remove(book);
            ((PrintBook) book).setAvailable(true);
        } else {
            System.out.println(book.getFormattedTitle() + " is not borrowed by this user.");
        }
    }

    public void checkoutBook(Book book) {
        if (book != null && library.hasBook(book)) {
            if (book instanceof DigitalBook) {
                this.downloadedBooks.add(book);
            } else {
                this.borrowedBooks.add(book);
                ((PrintBook) book).setAvailable(false);
            }
        } else {
            System.out.println("The book does not exist!");
        }
    }

    public List<Book> getDownloadedBooks() {
        return this.downloadedBooks;
    }

    public ObservableList<Book> getUserBooks() {
        List<Book> merged = new ArrayList<>();
        merged.addAll(borrowedBooks);
        merged.addAll(downloadedBooks);
        userBooks.addAll(merged);
        return userBooks;
    }

    public DigitalBook getDownloadedBook(int index) {
        if (index < this.downloadedBooks.size()) {
            return (DigitalBook) this.downloadedBooks.get(index);
        } else {
            System.out.println("Not in index!");
            return null;
        }
    }

    public List<Book> getBorrowedBooks() {
        return this.borrowedBooks;
    }

    public PrintBook getBorrowedBook(int index) {
        if (index < this.borrowedBooks.size()) {
            return (PrintBook) this.borrowedBooks.get(index);
        } else {
            System.out.println("Not in index!");
            return null;
        }
    }

    public void listDownloadedBooks() {
        String result = "";
        int count = 1;

        for (Book book : this.downloadedBooks) {
            result = result + count + ". " + book.getFormattedTitle() + "\n";
            count++;
        }

        System.out.println(result);
    }

    public void listBorrowedBooks() {
        String result = "";
        int count = 1;

        for (Book book : this.borrowedBooks) {
            result = result + count + ". " + book.getFormattedTitle() + "\n";
            count++;
        }

        System.out.println(result);
    }

    public LibraryModel getLibrary() {
        return this.library;
    }
}

public class LibraryModel implements BookFilter, BookSort {
    private ObservableList<Book> books;

    public LibraryModel() {
        this.books = FXCollections.observableArrayList();
    }

    public LibraryModel(ObservableList<Book> books) {
        this.books = books;
    }

    public Book getBook(int index) {
        if (index < books.size()) {
            return this.books.get(index);
        } else {
            System.out.println("Not in index!");
            return null;
        }
    }

    public Book getBook(String title) {
        for (Book book : books) {
            if (book.getTitle().equals(title)) {
                return book;
            }
        }
        return null;
    }

    public boolean hasBook(Book book) {
        return this.books.contains(book);
    }

    public void addBook(Book book) {
        this.books.add(book);
    }

    public ObservableList<Book> libraryProperty() {
        return this.books;
    }

    public String toString() {
        String result = "";
        int count = 1;

        for (Book book : this.books) {
            result = result + count + ". " + book.getFormattedTitle() + "\n";
            count++;
        }

        return result;
    }

    // All methods below are for sorting / filtering.
    @Override
    public LibraryModel filterDigitalBook() {
        LibraryModel digitalBooks = new LibraryModel();

        for (Book book : books) {
            if (book instanceof DigitalBook) {
                digitalBooks.addBook(book);
            }
        }

        digitalBooks.sortByTitle();
        return digitalBooks;
    }

    @Override
    public LibraryModel filterDigitalBook(String author) {
        LibraryModel digitalBooks = new LibraryModel();

        for (Book book : this.books) {
            if (book instanceof DigitalBook && book.getAuthor().equals(author)) {
                digitalBooks.addBook(book);
            }
        }

        digitalBooks.sortByAuthor();
        return digitalBooks;
    }

    @Override
    public LibraryModel filterDigitalBook(Genre genre) {
        LibraryModel digitalBooks = new LibraryModel();

        for (Book book : this.books) {
            if (book instanceof DigitalBook && book.getGenre() == genre) {
                digitalBooks.addBook(book);
            }
        }

        digitalBooks.sortByTitle();
        return digitalBooks;
    }

    @Override
    public LibraryModel filterPrintBook() {
        LibraryModel printBooks = new LibraryModel();

        for (Book book : this.books) {
            if (book instanceof PrintBook) {
                printBooks.addBook(book);
            }
        }

        printBooks.sortByTitle();
        return printBooks;
    }

    @Override
    public LibraryModel filterPrintBook(String author) {
        LibraryModel printBooks = new LibraryModel();

        for (Book book : this.books) {
            if (book instanceof PrintBook && book.getAuthor().equals(author)) {
                printBooks.addBook(book);
            }
        }

        printBooks.sortByTitle();
        return printBooks;
    }

    @Override
    public LibraryModel filterPrintBook(Genre genre) {
        LibraryModel printBooks = new LibraryModel();

        for (Book book : this.books) {
            if (book instanceof PrintBook && book.getGenre() == genre) {
                printBooks.addBook(book);
            }
        }

        return printBooks;
    }

    @Override
    public LibraryModel filterBooks(String author) {
        LibraryModel books = new LibraryModel();

        for (Book book : this.books) {
            if (book.getAuthor().equals(author)) {
                books.addBook(book);
            }
        }

        books.sortByTitle();
        return books;
    }

    @Override
    public LibraryModel filterBooks(Genre genre) {
        LibraryModel books = new LibraryModel();

        for (Book book : this.books) {
            if (book.getGenre() == genre) {
                books.addBook(book);
            }
        }

        return books;
    }

    @Override
    public void sortByTitle() {
        Collections.sort(books, Book.byTitle);
    }

    @Override
    public void sortByAuthor() {
        Collections.sort(books, Book.byAuthor);
    }
}

enum Genre {
    FICTION,
    NON_FICTION,
    MYSTERY,
    FANTASY,
    SCIENCE_FICTION,
    ROMANCE,
    POETRY;
}

abstract class Book {
    protected SimpleStringProperty title;
    protected SimpleStringProperty author;
    protected SimpleObjectProperty<Genre> genre;
    protected SimpleIntegerProperty pageCount;

    public Book(SimpleStringProperty title, SimpleStringProperty author, SimpleObjectProperty<Genre> genre,
            SimpleIntegerProperty pageCount) {
        this.title = title;
        this.author = author;
        this.genre = genre;
        this.pageCount = pageCount;
    }

    static final Comparator<Book> byTitle = Comparator.comparing(Book::getTitle);
    static final Comparator<Book> byAuthor = Comparator.comparing(Book::getAuthor);
    static final Comparator<Book> byGenre = Comparator.comparing(Book::getGenre);

    public String toString() {
        return "Title: " + this.title + " By Author: " + this.author + " (Genre: " + this.genre + " )";
    }

    public String getFormattedTitle() {
        return this.title + " by " + this.author;
    }

    public SimpleStringProperty titleProperty() {
        return title;
    }

    public String getTitle() {
        return titleProperty().get();
    }

    public SimpleStringProperty authorProperty() {
        return author;
    }

    public String getAuthor() {
        return authorProperty().get();
    }

    public SimpleObjectProperty<Genre> genreProperty() {
        return genre;
    }

    public Genre getGenre() {
        return genreProperty().get();
    }

    public SimpleIntegerProperty pageCountProperty() {
        return pageCount;
    }

    public int getPageCount() {
        return pageCountProperty().get();
    }

    public void displayInfo() {
        System.out.println(
                "Title: " + this.title +
                        "\nAuthor: " + this.author +
                        "\nGenre: " + this.genre +
                        "\nPage Count: " + this.pageCount);
    }
}

enum Format {
    PDF,
    EPUB,
    AZW,
    DJVU,
    TXT,
    HTML,
    DOCX;
}

class DigitalBook extends Book {
    private Format format;
    private boolean canDownload;

    public DigitalBook(SimpleStringProperty title, SimpleStringProperty author, SimpleObjectProperty<Genre> genre,
            SimpleIntegerProperty pageCount, Format format) {
        super(title, author, genre, pageCount);
        this.format = format;
        this.canDownload = true;
    }

    public Format getFormat() {
        return this.format;
    }

    public boolean canDownload() {
        return this.canDownload;
    }

    @Override
    public void displayInfo() {
        super.displayInfo();
        System.out.println(
                "Format: " + this.format +
                        "\nCan download: " + this.canDownload);
    }
}

enum CoverType {
    HARDCOVER,
    PAPERBACK,
    SPIRAL_BOUND,
    LEATHER_BOUND,
    MAGAZINE,
    GRAPHIC_NOVEL;
}

class PrintBook extends Book {
    private CoverType coverType;
    private boolean available;

    PrintBook(SimpleStringProperty title, SimpleStringProperty author, SimpleObjectProperty<Genre> genre,
            SimpleIntegerProperty pageCount, CoverType coverType) {
        super(title, author, genre, pageCount);
        this.coverType = coverType;
        this.available = true;
    }

    public CoverType getCoverType() {
        return this.coverType;
    }

    public boolean getAvailable() {
        return this.available;
    }

    public void setAvailable(Boolean available) {
        this.available = available;
    }

    @Override
    public void displayInfo() {
        super.displayInfo();
        System.out.println(
                "Cover Type: " + this.coverType.name());
    };

}

class Booklist {
    static Book[] list = {
            new DigitalBook(new SimpleStringProperty("Code Unleashed"),
                    new SimpleStringProperty("Grace Hopper"),
                    new SimpleObjectProperty<>(Genre.FANTASY),
                    new SimpleIntegerProperty(320),
                    Format.AZW),

            new DigitalBook(new SimpleStringProperty("Minimalist Mindset"),
                    new SimpleStringProperty("Leo Babauta"),
                    new SimpleObjectProperty<>(Genre.FANTASY),
                    new SimpleIntegerProperty(180),
                    Format.TXT),

            new DigitalBook(new SimpleStringProperty("The Future of AI"),
                    new SimpleStringProperty("Andrew Ng"),
                    new SimpleObjectProperty<>(Genre.FANTASY),
                    new SimpleIntegerProperty(400),
                    Format.DOCX),

            new PrintBook(new SimpleStringProperty("Digital Zen"),
                    new SimpleStringProperty("Thich Nhat Hanh"),
                    new SimpleObjectProperty<>(Genre.FANTASY),
                    new SimpleIntegerProperty(220),
                    CoverType.GRAPHIC_NOVEL),

            new DigitalBook(new SimpleStringProperty("Trail Maps Online"),
                    new SimpleStringProperty("Bear Grylls"),
                    new SimpleObjectProperty<>(Genre.FANTASY),
                    new SimpleIntegerProperty(250),
                    Format.PDF),

            new DigitalBook(new SimpleStringProperty("JavaFX in Action"),
                    new SimpleStringProperty("Bao Nguyen"),
                    new SimpleObjectProperty<>(Genre.FANTASY),
                    new SimpleIntegerProperty(360),
                    Format.HTML),

            new DigitalBook(new SimpleStringProperty("The Resilient Athlete"),
                    new SimpleStringProperty("Kobe Bryant"),
                    new SimpleObjectProperty<>(Genre.FANTASY),
                    new SimpleIntegerProperty(190),
                    Format.DJVU),

            new DigitalBook(new SimpleStringProperty("Think Big, Code Bigger"),
                    new SimpleStringProperty("Steve Jobs"),
                    new SimpleObjectProperty<>(Genre.FANTASY),
                    new SimpleIntegerProperty(280),
                    Format.DOCX),

            new DigitalBook(new SimpleStringProperty("Graphic Design Basics"),
                    new SimpleStringProperty("Paul Rand"),
                    new SimpleObjectProperty<>(Genre.FANTASY),
                    new SimpleIntegerProperty(300),
                    Format.PDF),

            new DigitalBook(new SimpleStringProperty("The Explorer's Log"),
                    new SimpleStringProperty("Amelia Earhart"),
                    new SimpleObjectProperty<>(Genre.FANTASY),
                    new SimpleIntegerProperty(270),
                    Format.AZW)
    };
}