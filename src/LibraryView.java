import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.TextFormatter.Change;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class LibraryView {
    TableView<Book> userBookView;
    TableView<Book> tableView;
    UserModel model;
    VBox view;
    Stage primaryStage;
    Scene scene;

    LibraryView(Stage primaryStage, UserModel model) {
        this.primaryStage = primaryStage;
        this.model = model;
        createAndConfigurePane();
        createAndLayoutControls();
        updateControllerFromListeners();
        observeModelAndUpdateControls();
    }

    public Scene getScene() {
        return scene;
    }

    public void createAndConfigurePane() {
        view = new VBox(10);
        view.setAlignment(Pos.CENTER);

        scene = new Scene(view, 500, 500);
    };

    public void createAndLayoutControls() {
        Label headingLabel = new Label("Welcom to the Library App");
        headingLabel.setFont(new Font("Arial", 32));
        Button option1 = new Button("1. View all books");
        Button option2 = new Button("2. Checkout a book");
        Button option3 = new Button("3. View your books");
        Button option4 = new Button("4. Return a print book");

        Label titleLabel = new Label("Select Option from 1-4");
        titleLabel.setFont(new Font("Arial", 20));

        VBox optionBox = new VBox(10, option1, option2, option3, option4);
        optionBox.setAlignment(Pos.CENTER);

        option1.setOnAction(e -> primaryStage.setScene(createSceneOpt1()));
        option2.setOnAction(e -> primaryStage.setScene(createSceneOp2t()));
        option3.setOnAction(e -> primaryStage.setScene(createSceneOpt3()));
        option4.setOnAction(e -> primaryStage.setScene(createSceneOpt4()));

        view.getChildren().addAll(headingLabel, titleLabel, optionBox);

    }

    public Scene createSceneOpt1() {
        Label heading = new Label("List of Books: ");
        heading.setFont(new Font("Arial", 20));
        tableView = new TableView<>();
        TableColumn<Book, String> col1 = new TableColumn<>("Title");
        col1.setCellValueFactory(cell -> cell.getValue().titleProperty());
        col1.setMinWidth(220);
        TableColumn<Book, String> col2 = new TableColumn<>("Author");
        col2.setCellValueFactory(cell -> cell.getValue().authorProperty());

        TableColumn<Book, Genre> col3 = new TableColumn<>("Genre");
        col3.setCellValueFactory(cell -> cell.getValue().genreProperty());
        TableColumn<Book, Integer> col4 = new TableColumn<>("Page Count");
        col4.setCellValueFactory(cell -> cell.getValue().pageCountProperty().asObject());
        tableView.getColumns().addAll(col1, col2, col3, col4);
        tableView.setItems(model.getLibrary().libraryProperty());

        Button backButton = new Button("Back to Menu");
        backButton.setOnAction(e -> {
            primaryStage.setScene(scene);
        });

        Button checkoutButton = new Button("Check-out");
        checkoutButton.setOnAction(e -> {
            int index = tableView.getSelectionModel().getSelectedIndex();
            if (index != -1) {
                this.checkOutForm(index);
            }
        });
        Button filterButton = new Button("Filter");
        filterButton.setOnAction(e -> filterForm());
        Button sorButton = new Button("Sort");
        sorButton.setOnAction(e -> sortForm());
        HBox buttonRow = new HBox(10, checkoutButton, filterButton, sorButton, backButton);
        buttonRow.setPadding(new Insets(2.0, 30.0, 19.0, 30.0));

        VBox viewOpt1 = new VBox(20, heading, tableView, buttonRow);
        Scene scene1 = new Scene(viewOpt1, 500, 500);
        return scene1;
    }

    public void filterForm() {
        Stage filter = new Stage();
        filter.initOwner(primaryStage);
        filter.initModality(Modality.APPLICATION_MODAL);

        Label heading = new Label("Filter Library By: ");

        ToggleGroup toggleGroup = new ToggleGroup();
        RadioButton filter1 = new RadioButton("Print Books");
        RadioButton filter2 = new RadioButton("Digital Books");

        filter1.setToggleGroup(toggleGroup);
        filter2.setToggleGroup(toggleGroup);

        HBox filterRow = new HBox(10, filter1, filter2);
        filterRow.setAlignment(Pos.CENTER);
        Button submitBtn = new Button("Choose");
        submitBtn.setOnAction(e -> {
            if (filter1.isSelected()) {
                LibraryModel printLibrary = model.getLibrary().filterPrintBook();
                tableView.setItems(printLibrary.libraryProperty());
                filter.close();

            } else if (filter2.isSelected()) {
                LibraryModel digitalLibrary = model.getLibrary().filterDigitalBook();
                tableView.setItems(digitalLibrary.libraryProperty());
                filter.close();
            }
        });

        Button cancelBtn = new Button("Cancel");
        cancelBtn.setOnAction(e -> filter.close());
        HBox btnRow = new HBox(10, submitBtn, cancelBtn);
        VBox root = new VBox(10, heading, filterRow, btnRow);
        Scene filterScene = new Scene(root, 300, 100);
        filter.setScene(filterScene);
        filter.show();
    }

    public void sortForm() {
        Stage sort = new Stage();
        sort.initOwner(primaryStage);
        sort.initModality(Modality.APPLICATION_MODAL);

        Label heading = new Label("Sort Library by: ");
        ToggleGroup toggleGroup = new ToggleGroup();
        RadioButton sort1 = new RadioButton("Title");
        RadioButton sort2 = new RadioButton("Author");

        sort1.setToggleGroup(toggleGroup);
        sort2.setToggleGroup(toggleGroup);

        HBox sortRow = new HBox(10, sort1, sort2);

        sortRow.setAlignment(Pos.CENTER);
        Button submitBtn = new Button("Choose");
        submitBtn.setOnAction(e -> {
            if (sort1.isSelected()) {
                model.getLibrary().sortByTitle();
                tableView.setItems(model.getLibrary().libraryProperty());
                sort.close();

            } else if (sort2.isSelected()) {
                model.getLibrary().sortByAuthor();
                ;
                tableView.setItems(model.getLibrary().libraryProperty());
                sort.close();
            }
        });

        Button cancelBtn = new Button("Cancel");
        cancelBtn.setOnAction(e -> sort.close());
        HBox btnRow = new HBox(10, submitBtn, cancelBtn);

        VBox root = new VBox(10, heading, sortRow, btnRow);
        Scene sortScene = new Scene(root, 300, 100);
        sort.setScene(sortScene);
        sort.show();
    }

    public void checkOutForm(int index) {
        Stage checkOut = new Stage();
        checkOut.initOwner(primaryStage);
        checkOut.initModality(Modality.APPLICATION_MODAL);

        Book selectedBook = model.getLibrary().libraryProperty().get(index);

        Label heading = new Label("Book Details: ");
        heading.setFont(new Font("Arial", 20));
        String bookName = selectedBook.getTitle();
        String bookAuthor = selectedBook.getAuthor();
        String bookType = selectedBook.getGenre().name();

        Label nameLabel = new Label(bookName);
        Label authorLabel = new Label(bookAuthor);
        Label typeLabel = new Label(bookType);

        HBox nameRow = new HBox(10, new Label("Title: "), nameLabel);
        HBox authorRow = new HBox(10, new Label("Author: "), authorLabel);
        HBox typeRow = new HBox(10, new Label("Type: "), typeLabel);

        Button checkoutBtn;
        Label successLabel = new Label();
        successLabel.setAlignment(Pos.CENTER);
        if (selectedBook instanceof DigitalBook) {
            checkoutBtn = new Button("Download ");
            checkoutBtn.setOnAction(e -> {
                model.checkoutBook(selectedBook);
                successLabel.setText("You have success downloaded the book!");
            });
        } else {
            checkoutBtn = new Button("Borrow ");
            checkoutBtn.setOnAction(e -> {
                model.checkoutBook(selectedBook);
                successLabel.setText("You have success borrow edthe book!");
            });
        }

        Button cancelBtn = new Button("Cancel");
        cancelBtn.setOnAction(e -> checkOut.close());

        HBox buttonRow = new HBox(10, checkoutBtn, cancelBtn);
        buttonRow.setAlignment(Pos.CENTER);
        VBox root = new VBox(10, heading, nameRow, authorRow, typeRow, buttonRow, successLabel);
        Scene checkoutScene = new Scene(root, 300, 300);
        checkOut.setScene(checkoutScene);
        checkOut.show();
    }

    public Scene createSceneOp2t() {
        Label heading = new Label("Checking Out Books");
        heading.setFont(new Font("Arial", 20));
        TextField inputField = new TextField();
        inputField.setPromptText("Enter Book Title: ");
        inputField.setMaxWidth(300);
        Button searchBtn = new Button("Search");
        Label message = new Label();
        searchBtn.setOnAction(e -> {
            String bookTitle = inputField.getText().trim();
            Book foundedBook = model.getLibrary().getBook(bookTitle);
            if (foundedBook == null) {
                message.setText("The Book Title Is Not Exist!");
            } else {
                String bookName = foundedBook.getTitle();
                String bookAuthor = foundedBook.getAuthor();
                String bookType = foundedBook.getGenre().name();

                message.setText(
                        "Book Information: " + "\nTitle: " + bookName + "\nAuthor: " + bookAuthor + "\nType: "
                                + bookType);
            }
        });
        Button backBtn = new Button("Back to Menu");
        backBtn.setOnAction(e -> {
            primaryStage.setScene(scene);
        });

        HBox buttonRow = new HBox(10, searchBtn, backBtn);
        buttonRow.setAlignment(Pos.CENTER);

        VBox viewOpt2 = new VBox(20, heading, inputField, buttonRow, message);
        viewOpt2.setAlignment(Pos.CENTER);
        Scene scene2 = new Scene(viewOpt2, 500, 500);
        return scene2;
    }

    public void updateControllerFromListeners() {

    }

    public void observeModelAndUpdateControls() {

    }

    public Scene createSceneOpt3() {
        Label heading = new Label("List of Your Books: ");
        heading.setFont(new Font("Arial", 20));
        userBookView = new TableView<>();

        TableColumn<Book, String> col1 = new TableColumn<>("Title");
        col1.setCellValueFactory(cell -> cell.getValue().titleProperty());
        col1.setMinWidth(220);
        TableColumn<Book, String> col2 = new TableColumn<>("Author");
        col2.setCellValueFactory(cell -> cell.getValue().authorProperty());
        TableColumn<Book, Genre> col3 = new TableColumn<>("Genre");
        col3.setCellValueFactory(cell -> cell.getValue().genreProperty());
        TableColumn<Book, Integer> col4 = new TableColumn<>("Page Count");
        col4.setCellValueFactory(cell -> cell.getValue().pageCountProperty().asObject());
        userBookView.getColumns().addAll(col1, col2, col3, col4);
        userBookView.setItems(model.getUserBooks());

        Button backButton = new Button("Back to Menu");
        backButton.setOnAction(e -> {
            primaryStage.setScene(scene);
        });

        VBox root = new VBox(10, heading, userBookView, backButton);
        Scene userBookScene = new Scene(root, 500, 500);
        return userBookScene;
    }

    public Scene createSceneOpt4() {
        return null;
    }

    private void configTextFieldForInts(TextField field) {
        field.setTextFormatter(new TextFormatter<Integer>((Change c) -> {
            if (c.getControlNewText().matches("-?\\d*")) {
                return c;
            }
            return null;
        }));
    }
}