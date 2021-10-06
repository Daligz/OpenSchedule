package me.upp.dali.openschedule.view;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lombok.AllArgsConstructor;

import java.util.Objects;

@AllArgsConstructor
public class ViewLoader extends Application {

    public static void main(final String[] args) {
        launch(args);
    }

    @Override
    public void start(final Stage stage) throws Exception {
        final Parent mainView = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/views/MainView.fxml")));
        final Scene scene = new Scene(mainView);
        configureStage(stage);
        stage.setScene(scene);
    }

    private void configureStage(final Stage stage) {
        stage.setTitle("DocMan | Gestión de documentos");
        stage.setResizable(false);
        stage.show();
    }
}
