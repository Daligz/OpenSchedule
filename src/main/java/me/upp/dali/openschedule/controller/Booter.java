package me.upp.dali.openschedule.controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import lombok.Setter;
import me.upp.dali.openschedule.OpenSchedule;
import me.upp.dali.openschedule.controller.others.Alert;
import me.upp.dali.openschedule.view.ViewLoader;

import javax.swing.*;
import java.net.URL;
import java.util.ResourceBundle;

public class Booter implements Initializable {

    @FXML
    public ImageView bootMainImage;
    @FXML
    public Label bootStatusText;

    private static Booter INSTANCE;
    @Setter
    private boolean isInMainView = false;

    @Override
    public void initialize(final URL url, final ResourceBundle resourceBundle) {
        INSTANCE = this;
        final Timer retryTimer = new Timer(15000, retryEvent -> { // 15000 milisengundos = 15 segundos
            if (!(isInMainView)) {
                Platform.runLater(() -> {
                    INSTANCE.bootStatusText.setText("Reintentando conectar con WhatsApp...");
                    OpenSchedule.load(OpenSchedule.ARGS, true);
                });
                final Timer closing = new Timer(30000, closingEvent -> { // 30000 milisengundos = 30 segundos
                    if (!(isInMainView)) {
                        Platform.runLater(() -> {
                            INSTANCE.bootStatusText.setText("Conexión fallida...");
                            ViewLoader.getInstance().getStage().hide();
                            Alert.send("Conexión fallida...", "Vuelve a ejecutar la aplicación.", javafx.scene.control.Alert.AlertType.INFORMATION);
                            System.exit(0);
                        });
                    }
                });
                closing.setRepeats(false);
                closing.start();
            }
        });
        retryTimer.setRepeats(false);
        retryTimer.start();
    }

    public static Booter getInstance() {
        if (INSTANCE == null) INSTANCE = new Booter();
        return INSTANCE;
    }
}
