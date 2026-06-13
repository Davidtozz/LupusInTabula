package it.unicam.cs.mpgc.rpg125950.lupusintabula;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public final class MainApplication extends Application {

    @Override
    public void start(@NotNull Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("main.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1280, 720);
        scene.getStylesheets().add(MainApplication.class.getResource("style.css").toExternalForm());
        stage.setTitle("LupusInTabula");
        stage.setScene(scene);
        stage.show();
    }
}
