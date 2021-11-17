package me.upp.dali.openschedule.controller;

import it.auties.whatsapp4j.manager.WhatsappDataManager;
import it.auties.whatsapp4j.whatsapp.WhatsappAPI;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import lombok.NonNull;
import me.upp.dali.openschedule.OpenSchedule;
import me.upp.dali.openschedule.controller.client.ClientState;
import me.upp.dali.openschedule.controller.client.ClientStorage;
import me.upp.dali.openschedule.controller.others.Alert;
import me.upp.dali.openschedule.model.database.tables.TableConfig;
import me.upp.dali.openschedule.model.database.tables.TableUserTime;
import me.upp.dali.openschedule.model.database.utils.DataTime;

import javax.swing.*;
import javax.swing.table.TableColumn;
import java.net.URL;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
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
    
    // Register : Search section
    @FXML
    public TextField text_search_code;
    @FXML
    public TextField text_search_name;
    @FXML
    public Button button_search;
    @FXML
    public TableColumn column_phone_name;
    @FXML
    public TableColumn column_time_start;
    @FXML
    public TableColumn column_time_finish;
    @FXML
    public TableColumn column_code;

    // Register : Register section
    @FXML
    public CheckBox check_register_manual;
    @FXML
    public TextField text_client_code;
    @FXML
    public TextField text_client_name;
    @FXML
    public CheckBox check_client_leave;
    @FXML
    public Spinner<LocalTime> spn_client_hour_leave;
    @FXML
    public Button button_plus_hour;
    @FXML
    public Button button_less_hour;
    @FXML
    public Button button_plus_minutes;
    @FXML
    public Button button_less_minutes;
    @FXML
    public Button button_client_register;
    @FXML
    public Button button_client_leave;
    @FXML
    public Button button_client_cancel;

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
        final OpenSchedule openSchedule = OpenSchedule.getINSTANCE();

        // Client time
        final SpinnerValueFactory<LocalTime> timeFactory = new SpinnerValueFactory<>() {

            {
                setValue(defaultValue());
            }

            private LocalTime defaultValue() {
                return LocalTime.now().truncatedTo(ChronoUnit.MINUTES);
            }

            @Override
            public void decrement(int steps) {
                final LocalTime value = getValue();
                setValue(value == null ? defaultValue() : value.minusMinutes(steps));
            }

            @Override
            public void increment(int steps) {
                final LocalTime value = getValue();
                setValue(value == null ? defaultValue() : value.plusMinutes(steps));
            }
        };
        this.spn_client_hour_leave.setValueFactory(timeFactory);
        this.button_plus_minutes.setOnMouseClicked(mouseEvent -> this.spn_client_hour_leave.getValueFactory().setValue(this.spn_client_hour_leave.getValue().plusMinutes(10)));
        this.button_less_minutes.setOnMouseClicked(mouseEvent -> this.spn_client_hour_leave.getValueFactory().setValue(this.spn_client_hour_leave.getValue().minusMinutes(10)));
        this.button_plus_hour.setOnMouseClicked(mouseEvent -> this.spn_client_hour_leave.getValueFactory().setValue(this.spn_client_hour_leave.getValue().plusHours(1)));
        this.button_less_hour.setOnMouseClicked(mouseEvent -> this.spn_client_hour_leave.getValueFactory().setValue(this.spn_client_hour_leave.getValue().minusHours(1)));

        // Spinners
        this.spn_clients_amount.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 50));
        this.spn_clients_amount.valueProperty().addListener((observableValue, oldValue, newValue) -> ClientStorage.getInstance().setClients(newValue));

        this.spn_clients_limit.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 50, 15));

        final String spinnerId = this.spn_clients_limit.getId();
        openSchedule.getDatabase().get(
                TableConfig.TABLE_NAME.getValue(),
                String.format("%s = \"%s\"", TableConfig.ID.getValue(), spinnerId)
        ).whenComplete((resultSet, throwable) -> {
            if (resultSet == null) {
                openSchedule.getDatabase().insert(
                        TableConfig.TABLE_NAME.getValue(),
                        String.format("(%s, %s) VALUES (\"%s\", \"%s\")", TableConfig.ID.getValue(), TableConfig.VALUE.getValue(),
                                spinnerId, this.spn_clients_limit.getValue().toString())
                );
            } else {
                try {
                    final int i = Integer.parseInt(resultSet.getString(TableConfig.VALUE.getValue()));
                    this.spn_clients_limit.getValueFactory().setValue(i);
                } catch (final SQLException e) {
                    e.printStackTrace();
                }
            }
        });

        this.clients_amount_save_button.setOnMouseClicked(mouseEvent -> {
            openSchedule.getDatabase().get(
                    TableConfig.TABLE_NAME.getValue(),
                    String.format("%s = \"%s\"", TableConfig.ID.getValue(), spinnerId)
            ).whenComplete((resultSet, throwable) -> {
                if (resultSet != null) {
                    openSchedule.getDatabase().update(
                            TableConfig.TABLE_NAME.getValue(),
                            String.format("%s = \"%s\"", TableConfig.VALUE.getValue(), this.spn_clients_limit.getValue().toString()),
                            String.format("%s = \"%s\"", TableConfig.ID.getValue(), spinnerId)
                    );
                }
            });
        });

        // On enable/disable clients limit enable/disable spinner to select amount
        final String checkId = this.check_clients_limit.getId();
        openSchedule.getDatabase().get(
                TableConfig.TABLE_NAME.getValue(),
                String.format("%s = \"%s\"", TableConfig.ID.getValue(), checkId)
        ).whenComplete((resultSet, throwable) -> {
            if (resultSet == null) {
                openSchedule.getDatabase().insert(
                        TableConfig.TABLE_NAME.getValue(),
                        String.format("(%s, %s) VALUES (\"%s\", \"%s\")", TableConfig.ID.getValue(), TableConfig.VALUE.getValue(),
                                checkId, this.check_clients_limit.isSelected())
                );
            } else {
                try {
                    final boolean aBoolean = Boolean.parseBoolean(resultSet.getString(TableConfig.VALUE.getValue()));
                    this.check_clients_limit.setSelected(aBoolean);
                    this.spn_clients_limit.setDisable(!(aBoolean));
                } catch (final SQLException e) {
                    e.printStackTrace();
                }
            }
        });
        this.check_clients_limit.setOnMouseClicked(mouseEvent -> {
            final boolean value = !(this.check_clients_limit.isSelected());
            this.spn_clients_limit.setDisable(value);
            openSchedule.getDatabase().update(
                    TableConfig.TABLE_NAME.getValue(),
                    String.format("%s = \"%s\"", TableConfig.VALUE.getValue(), !(value)),
                    String.format("%s = \"%s\"", TableConfig.ID.getValue(), checkId)
            );
        });

        this.check_register_manual.setOnMouseClicked(mouseEvent -> {
            final boolean isSelected = !(this.check_register_manual.isSelected());
            this.text_client_name.setDisable(isSelected);
            this.text_client_code.setDisable(!(isSelected));
        });

        this.check_client_leave.setOnMouseClicked(mouseEvent -> {
            final boolean isSelected = !(this.check_client_leave.isSelected());
            this.button_plus_hour.setDisable(isSelected);
            this.button_less_hour.setDisable(isSelected);
            this.button_plus_minutes.setDisable(isSelected);
            this.button_less_minutes.setDisable(isSelected);
        });

        // Register multiple buttons for message update
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

        // Register buttons
        this.button_client_register.setOnMouseClicked(mouseEvent -> {
            final String clientName = this.text_client_name.getText();
            final String clientCode = this.text_client_code.getText();

            if (this.check_register_manual.isSelected()) {
                if (clientName.isEmpty()) {
                    this.setDefaultButtonStates();
                    return;
                }
            } else {
                if (clientCode.isEmpty()) return;
            }

            final ClientState clientState = ClientState.getInstance();
            ClientState.Client client = null;
            for (final ClientState.Client value : clientState.getValues()) {
                if (value.getCode().getCode().equalsIgnoreCase(clientCode)) {
                    client = value;
                }
            }
            if (client == null && !(this.check_register_manual.isSelected())) {
                Alert.send("Codigo no encontrado", "El código no es valido!", javafx.scene.control.Alert.AlertType.ERROR);
                this.setDefaultButtonStates();
                return;
            }
            final String phone = (client == null) ? clientName : client.getPhone();
            if (ClientStorage.getInstance().checkLimit()) {
                Alert.send("Limite de clientes", "Se alcanzo el limite de clientes!", javafx.scene.control.Alert.AlertType.ERROR);
                return;
            }

            Timestamp timeNow = DataTime.getTimeNow();
            boolean timeToLeave = false;
            if (this.check_client_leave.isSelected()) {
                final LocalTime localTime = this.spn_client_hour_leave.getValue();
                timeNow.setHours(localTime.getHour());
                timeNow.setMinutes(localTime.getMinute());
                timeToLeave = true;
            }

            final ClientState.Client finalClient = client;
            final boolean finalTimeToLeave = timeToLeave;
            openSchedule.getDatabase().insert(
                TableUserTime.TABLE_NAME.getValue(),
                String.format("(%s, %s, %s) VALUES (\"%s\", \"%s\", \"%s\")",
                TableUserTime.PHONE.getValue(), TableUserTime.CODE.getValue(), TableUserTime.TIME_FINISH.getValue(),
                        phone, clientCode, timeNow)
            ).whenComplete((aBoolean, throwable) -> {
                clientState.remove(clientCode);
                this.setDefaultButtonStates();
                ClientStorage.getInstance().add();
                if (finalClient != null && finalTimeToLeave) {
                    final WhatsappAPI whatsappAPI = openSchedule.getMessagesAPI().getWhatsappAPI();
                    final WhatsappDataManager manager = whatsappAPI.manager();
                    manager.findChatByJid(finalClient.getJid()).ifPresent(chat -> whatsappAPI.sendMessage(chat, finalClient.getName() + " disfruta tu estancia!"));
                    final Timer timer = new Timer(DataTime.timeToMilliseconds(
                            (timeNow.getHours() - DataTime.getTimeNow().getHours()),
                            (timeNow.getMinutes() - DataTime.getTimeNow().getMinutes())
                    ), event -> {
                        final String text = this.msg_clients_time_finished.getText()
                                .replace("%cliente%", finalClient.getName());
                        manager.findChatByJid(finalClient.getJid()).ifPresent(chat -> whatsappAPI.sendMessage(chat, text));
                        Platform.runLater(() -> Alert.send("Tiempo de usuario terminado", "El tiempo de " + finalClient.getName() + " termino.", javafx.scene.control.Alert.AlertType.INFORMATION));
                    });
                    timer.setRepeats(false);
                    timer.start();
                } else if (finalTimeToLeave) {
                    final Timer timer = new Timer(DataTime.timeToMilliseconds(
                            (timeNow.getHours() - DataTime.getTimeNow().getHours()),
                            (timeNow.getMinutes() - DataTime.getTimeNow().getMinutes())
                    ), event -> {
                        openSchedule.getDatabase().delete(
                                TableUserTime.TABLE_NAME.getValue(),
                                String.format("%s = \"%s\"", TableUserTime.PHONE.getValue(), phone)
                        );
                        Platform.runLater(() -> Alert.send("Tiempo de usuario terminado", "El tiempo de " + clientName + " termino.", javafx.scene.control.Alert.AlertType.INFORMATION));
                    });
                    timer.setRepeats(false);
                    timer.start();
                }
                Platform.runLater(() -> Alert.send("Registro de usuario", "Usuario registrado.", javafx.scene.control.Alert.AlertType.INFORMATION));
            });
        });
        this.button_client_cancel.setOnMouseClicked(mouseEvent -> this.setDefaultButtonStates());
    }

    private void setDefaultButtonStates() {
        this.text_client_name.setText("");
        this.text_client_code.setText("");
        this.text_client_code.setDisable(false);
        this.check_register_manual.setSelected(false);
        this.text_client_name.setDisable(true);
        this.check_client_leave.setSelected(false);
        this.button_plus_hour.setDisable(true);
        this.button_less_hour.setDisable(true);
        this.button_plus_minutes.setDisable(true);
        this.button_less_minutes.setDisable(true);
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