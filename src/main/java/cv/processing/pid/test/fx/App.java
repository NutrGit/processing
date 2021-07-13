package cv.processing.pid.test.fx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.File;

public class App extends Application {

    public Controller controller;

    @Override
    public void start(Stage primaryStage) throws Exception {
        File file = new File("src/main/java/cv/processing/pid/test/fx/sample.fxml");

        FXMLLoader loader = new FXMLLoader(file.toURI().toURL());
        Parent parent = loader.load();
        Pane pane = new Pane();

//        Scene scene = new Scene(pane, 1024, 768);
        Scene scene = new Scene(parent, 1024, 768);
        primaryStage.setScene(scene);
        primaryStage.setTitle("test");
        primaryStage.show();

        controller = loader.getController();
        controller.init();
        System.out.println(controller);
    }

    public static void main(String[] args) {
        launch(args);
    }

    public Controller getController() {
        return controller;
    }
}
