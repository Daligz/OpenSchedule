package me.upp.dali.openschedule.controller.others;

import lombok.experimental.UtilityClass;

@UtilityClass
public class Alert {

    public void send(final String title, final String body, final javafx.scene.control.Alert.AlertType alertType) {
        try {
            javafx.scene.control.Alert alert = new javafx.scene.control.Alert(alertType);
            alert.setHeaderText(title);
            alert.setContentText(body);
            alert.showAndWait();
        } catch (final Exception ignored) { }
    }
}
