import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
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
import javafx.stage.Modality;
import javafx.stage.Stage;

public class LibraryView {
    VBox view;
    Stage primaryStage;
    Scene scene;

    LibraryView(Stage primaryStage) {
        this.primaryStage = primaryStage;
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
        Label option1 = new Label("1. View all books");
        Label option2 = new Label("2. Checkout a book");
        Label option3 = new Label("3. View your books");
        Label option4 = new Label("4. Return a print book");

        Label titleLabel = new Label("Please Select Option from 1-4");
        titleLabel.setFont(new Font("Arial", 20));

        VBox optionBox = new VBox(10, option1, option2, option3, option4);
        optionBox.setAlignment(Pos.CENTER);

        TextField inputField = new TextField();
        configTextFieldForInts(inputField);
        inputField.setMaxWidth(100);
        inputField.setPromptText("Enter option number (1-4)");
        Button submiBtn = new Button("Enter");
        submiBtn.setOnAction(e -> {
            String input = inputField.getText();
            if (input.equals("1")) {
                primaryStage.setScene(this.createSceneOpt1());
            } else if (input.equals("2")) {
                primaryStage.setScene(this.createSceneOp2t());
            }
        });

        view.getChildren().addAll(headingLabel, titleLabel, optionBox,
                inputField, submiBtn);
    }

    public Scene createSceneOpt1() {
        Label heading = new Label("List of Books: ");
        TableView<Library> tableView = new TableView<>();
        TableColumn<Library, String> col1 = new TableColumn<>("Title");
        col1.setMinWidth(220);
        TableColumn<Library, String> col2 = new TableColumn<>("Author");
        TableColumn<Library, String> col3 = new TableColumn<>("Genre");
        TableColumn<Library, Integer> col4 = new TableColumn<>("Page Count");

        tableView.getColumns().addAll(col1, col2, col3, col4);
        tableView.setItems(null);

        Button backButton = new Button("Back to Menu");
        backButton.setOnAction(e -> {
            primaryStage.setScene(scene);
        });

        VBox viewOpt1 = new VBox(20, tableView, backButton);

        Scene scene1 = new Scene(viewOpt1);
        return scene1;
    }

    public Scene createSceneOp2t() {
        Label heading = new Label("Checking Out Books");
        heading.setFont(new Font("Arial", 20));
        TextField inputField = new TextField();
        inputField.setPromptText("Enter Book Title: ");
        inputField.setMaxWidth(300);
        Button searchBtn = new Button("Search");
        Button backBtn = new Button("Back to Menu");
        backBtn.setOnAction(e -> {
            primaryStage.setScene(scene);
        });

        HBox buttonRow = new HBox(10, searchBtn, backBtn);
        buttonRow.setAlignment(Pos.CENTER);

        VBox viewOpt2 = new VBox(20, heading, inputField, buttonRow);
        viewOpt2.setAlignment(Pos.CENTER);
        Scene scene2 = new Scene(viewOpt2, 500, 500);
        return scene2;
    }

    public void updateControllerFromListeners() {

    }

    public void observeModelAndUpdateControls() {

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