package me.upp.dali.openschedule.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextArea;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import me.upp.dali.openschedule.OpenSchedule;
import me.upp.dali.openschedule.model.database.tables.TableConfig;

import java.net.URL;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
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
        this.loadMessages();
        this.registerButtonEvents();
    }

    private void loadMessages() {
        final OpenSchedule openSchedule = OpenSchedule.getINSTANCE();
        this.getMessagesObjects().forEach(messagesObjects -> openSchedule.getDatabase().get(
                TableConfig.TABLE_NAME.getValue(),
                TableConfig.ID.getValue() + " = " + String.format("\"%s\"", messagesObjects.getTextArea().getId())
        ).whenComplete((resultSet, throwable) -> {
            if (resultSet == null || throwable != null) return;
            try {
                messagesObjects.getTextArea().setText(resultSet.getString(TableConfig.VALUE.getValue()));
            } catch (final SQLException e) {
                e.printStackTrace();
            }
        }));
    }

    private void registerButtonEvents() {
        // On enable/disable clients limit enable/disable spinner to select amount
        this.check_clients_limit.setOnMouseClicked(mouseEvent -> this.spn_clients_limit.setDisable(
                !(this.check_clients_limit.isSelected())
        ));

        // Register multiple buttons for message update
        final OpenSchedule openSchedule = OpenSchedule.getINSTANCE();
        this.getMessagesObjects().forEach(messagesObjects -> messagesObjects.getButton().setOnMouseClicked(mouseEvent -> openSchedule.getDatabase().get(
                TableConfig.TABLE_NAME.getValue(),
                TableConfig.ID.getValue() + " = " + String.format("\"%s\"", messagesObjects.getTextArea().getId())
        ).whenComplete((resultSet, throwable) -> {
            final String textId = messagesObjects.getTextArea().getId();
            final String text = messagesObjects.getTextArea().getText();
            if (resultSet == null || throwable != null) {
                openSchedule.getDatabase().insert(
                        TableConfig.TABLE_NAME.getValue(),
                        String.format("(%s, %s) VALUES (\"%s\", \"%s\")", TableConfig.ID.getValue(), TableConfig.VALUE.getValue(), textId, text)
                );
            } else {
                openSchedule.getDatabase().update(
                        TableConfig.TABLE_NAME.getValue(),
                        String.format("%s = \"%s\"", TableConfig.VALUE.getValue(), text),
                        String.format("%s = \"%s\"", TableConfig.ID.getValue(), textId)
                );
            }
        })));
    }

    private List<MessagesObjects> getMessagesObjects() {
        return Arrays.asList(
                new MessagesObjects(this.msg_information, this.msg_information_button),
                new MessagesObjects(this.msg_clients_amount, this.msg_clients_amount_button),
                new MessagesObjects(this.msg_clients_register_name, this.msg_clients_register_name_button),
                new MessagesObjects(this.msg_clients_register_known_client, this.msg_clients_register_known_client_button),
                new MessagesObjects(this.msg_clients_code, this.msg_clients_code_button),
                new MessagesObjects(this.msg_clients_code_expired, this.msg_clients_code_expired_button),
                new MessagesObjects(this.msg_clients_time_finished, this.msg_clients_time_finished_button),
                new MessagesObjects(this.msg_clients_time, this.msg_clients_time_button)
        );
    }

    private record MessagesObjects(@NonNull TextArea textArea, @NonNull Button button) {

        public TextArea getTextArea() {
            return this.textArea;
        }

        public Button getButton() {
            return this.button;
        }
    }

    public static MainView getInstance() {
        if (INSTANCE == null) INSTANCE = new MainView();
        return INSTANCE;
    }
}