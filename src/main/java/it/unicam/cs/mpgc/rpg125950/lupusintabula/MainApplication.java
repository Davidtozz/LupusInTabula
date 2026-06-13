package it.unicam.cs.mpgc.rpg125950.lupusintabula;

import it.unicam.cs.mpgc.rpg125950.lupusintabula.data.DatabaseManager;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lombok.Data;
import org.jetbrains.annotations.NotNull;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;

public final class MainApplication extends Application {

    @Override
    public void init() {
        try {
            DatabaseManager.bootstrap();
        } catch(FileNotFoundException | SQLException e) {
            throw new RuntimeException(e);
        }
    }

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
