import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MVC extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        LibraryView view = new LibraryView(stage);
        stage.setScene(view.getScene());
        stage.show();

    }

}
