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
        //Parent root = FXMLLoader.load(getClass().getResource("sampleTestPagination.fxml"));
        Parent root = FXMLLoader.load(getClass().getResource("RegistrationScene.fxml"));
        primaryStage.setTitle("RTC workitems creator");
        //primaryStage.setMinHeight(480);
        //primaryStage.setMinWidth(750);
        primaryStage.setMinHeight(323);
        primaryStage.setMinWidth(515);
        primaryStage.setResizable(false);
        primaryStage.setScene(new Scene(root, 505, 313));

        //Controller.init(root, primaryStage);
        Controller.init1(root, primaryStage);

        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
