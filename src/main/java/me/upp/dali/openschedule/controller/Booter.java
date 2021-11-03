package me.upp.dali.openschedule.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;

import java.net.URL;
import java.util.ResourceBundle;

public class Booter implements Initializable {

    @FXML
    public ImageView bootMainImage;
    @FXML
    public Label bootStatusText;

    private static Booter INSTANCE;

    @Override
    public void initialize(final URL url, final ResourceBundle resourceBundle) {
        INSTANCE = this;
    }

    public static Booter getInstance() {
        if (INSTANCE == null) INSTANCE = new Booter();
        return INSTANCE;
    }
}
