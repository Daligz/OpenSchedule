package me.upp.dali.openschedule.view;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Screen;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.NonNull;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;

public class ViewLoader extends Application {

    private final URL VIEW_MAIN_PATH = this.getClass().getResource("/views/MainView.fxml");
    private final URL VIEW_BOOTER_PATH = this.getClass().getResource("/views/BooterView.fxml");

    @Getter
    private Stage stage;

    private static ViewLoader INSTANCE;

    public static @NonNull ViewLoader getInstance() {
        return INSTANCE;
    }

    @Override
    public void start(final Stage stage) throws Exception {
        stage.getIcons().add(new Image(Objects.requireNonNull(this.getClass().getResourceAsStream("/images/logo.png"))));
        loadView(stage, VIEW_MAIN_PATH, callbackStage -> {
//            callbackStage.setAlwaysOnTop(true);
            callbackStage.setOnCloseRequest(windowEvent -> System.exit(0));
//            callbackStage.initStyle(StageStyle.UNDECORATED);
        });
        this.stage = stage;
        INSTANCE = this;
    }

    public enum ViewType {
        MAIN, BOOTER;
    }

    public void updateView(final ViewType viewType) throws IOException {
        if (viewType == ViewType.MAIN) {
            loadView(this.stage, VIEW_MAIN_PATH, callbackStage -> callbackStage.setAlwaysOnTop(false));
        } else {
            loadView(this.stage, VIEW_BOOTER_PATH, callbackStage -> callbackStage.setAlwaysOnTop(true));
        }
    }

    public void loadView(final Stage stage, final URL url) throws IOException {
        final Parent mainView = FXMLLoader.load(Objects.requireNonNull(url));
        final Scene scene = new Scene(mainView);
        stage.setTitle("OpenSchedule | Appointment calendar for gyms");
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
        centerStage(stage);
    }

    public void loadView(final Stage stage, final URL url, final StageCallback sceneCallback) throws IOException {
        sceneCallback.execute(stage);
        loadView(stage, url);
    }

    private void centerStage(final Stage stage) {
        final Rectangle2D rectangle2D = Screen.getPrimary().getVisualBounds();
        stage.setX((rectangle2D.getWidth() - stage.getWidth()) / 2);
        stage.setY((rectangle2D.getHeight() - stage.getHeight()) / 2);
    }
}
