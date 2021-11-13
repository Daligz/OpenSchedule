package me.upp.dali.openschedule.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextArea;

import java.net.URL;
import java.util.ResourceBundle;

public class MainView implements Initializable {

    // Information : Configuration section
    @FXML
    public Spinner<Integer> spn_clients_amount;
    @FXML
    public CheckBox check_clients_limit;
    @FXML
    public Spinner<Integer> spn_clients_limit;
    @FXML
    public Button clients_amount_save_button;
    @FXML
    public Spinner<Integer> spn_code_duration;
    @FXML
    public Button spn_code_duration_button;

    // Information : Messages section
    @FXML
    public TextArea msg_information;
    @FXML
    public Button msg_information_button;
    @FXML
    public TextArea msg_clients_amount;
    @FXML
    public Button msg_clients_amount_button;
    @FXML
    public TextArea msg_clients_register_name;
    @FXML
    public Button msg_clients_register_name_button;
    @FXML
    public TextArea msg_clients_register_known_client;
    @FXML
    public Button msg_clients_register_known_client_button;
    @FXML
    public TextArea msg_clients_code;
    @FXML
    public Button msg_clients_code_button;
    @FXML
    public TextArea msg_clients_code_expired;
    @FXML
    public Button msg_clients_code_expired_button;
    @FXML
    public TextArea msg_clients_time_finished;
    @FXML
    public Button msg_clients_time_finished_button;
    @FXML
    public TextArea msg_clients_time;
    @FXML
    public Button msg_clients_time_button;

    // P E N D I N G //

    // Register : Search section

    // Register : Register section

    private static MainView INSTANCE;

    @Override
    public void initialize(final URL url, final ResourceBundle resourceBundle) {
        INSTANCE = this;
        this.registerButtonEvents();
    }

    private void loadMessages() {

    }

    private void registerButtonEvents() {
        // On enable/disable clients limit enable/disable spinner to select amount
        this.check_clients_limit.setOnMouseClicked(mouseEvent -> this.spn_clients_limit.setDisable(
                !(this.check_clients_limit.isSelected())
        ));
    }

    public static MainView getInstance() {
        if (INSTANCE == null) INSTANCE = new MainView();
        return INSTANCE;
    }
}