package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import sample.Controller;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("sampleTestPagination.fxml"));
        primaryStage.setTitle("Bauhaus database");
        primaryStage.setMinHeight(480);
        primaryStage.setMinWidth(750);
        primaryStage.setScene(new Scene(root, 750, 480));

        Controller.init(root, primaryStage);

        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
