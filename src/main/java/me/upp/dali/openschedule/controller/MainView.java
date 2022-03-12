package me.upp.dali.openschedule.controller;

import it.auties.whatsapp4j.manager.WhatsappDataManager;
import it.auties.whatsapp4j.whatsapp.WhatsappAPI;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import lombok.NonNull;
import me.upp.dali.openschedule.OpenSchedule;
import me.upp.dali.openschedule.controller.client.ClientState;
import me.upp.dali.openschedule.controller.client.ClientStorage;
import me.upp.dali.openschedule.controller.others.Alert;
import me.upp.dali.openschedule.model.database.tables.TableConfig;
import me.upp.dali.openschedule.model.database.tables.TableInventory;
import me.upp.dali.openschedule.model.database.tables.TableUserTime;
import me.upp.dali.openschedule.model.database.utils.DataTime;

import javax.swing.*;
import java.net.URL;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
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
    public Button contact_button;

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
    public TextField text_search_name;
    @FXML
    public Button button_search;
    @FXML
    public Button button_update;
    @FXML
    public TableView<ClientState.ClientTable> table_client_info;
    @FXML
    public TableColumn<ClientState.ClientTable, String> column_phone_name;
    @FXML
    public TableColumn<ClientState.ClientTable, String> column_time_start;
    @FXML
    public TableColumn<ClientState.ClientTable, String> column_time_finish;
    @FXML
    public TableColumn<ClientState.ClientTable, String> column_code;

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

    // Inventory : Items section
    @FXML
    public TableView<ClientState.ItemsTable> table_inv_info;
    @FXML
    public TableColumn<ClientState.ItemsTable, String> column_inv_id;
    @FXML
    public TableColumn<ClientState.ItemsTable, String> column_inv_name;

    // Inventory : Inventory section
    @FXML
    public Button button_inv_save;
    @FXML
    public Button button_inv_search;
    @FXML
    public Button button_inv_update;
    @FXML
    public Button button_inv_delete;
    @FXML
    public TextField text_inv_id;
    @FXML
    public TextField text_inv_name;
    @FXML
    public TextField text_inv_cost;
    @FXML
    public TextArea text_inv_state;

    private static MainView INSTANCE;

    @Override
    public void initialize(final URL url, final ResourceBundle resourceBundle) {
        INSTANCE = this;
        this.initializeColumns();
        this.loadMessages();
        this.registerButtonEvents();
        this.updateTable("");
        Booter.getInstance().setInMainView(true);
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

    private void initializeColumns() {
        this.column_phone_name.setCellValueFactory(new PropertyValueFactory<>("Phone"));
        this.column_time_start.setCellValueFactory(new PropertyValueFactory<>("Start"));
        this.column_time_finish.setCellValueFactory(new PropertyValueFactory<>("End"));
        this.column_code.setCellValueFactory(new PropertyValueFactory<>("Code"));
    }

    private void updateTable(final String where) {
        final OpenSchedule openSchedule = OpenSchedule.getINSTANCE();
        final String finalWhere = (where.isEmpty()) ? "1 = 1" : where;
        openSchedule.getDatabase().get(
                TableUserTime.TABLE_NAME.getValue(),
                finalWhere
        ).whenComplete((resultSet, throwable) -> {
            if (resultSet == null) {
                this.table_client_info.setItems(FXCollections.observableArrayList());
                return;
            }
            try {
                final ArrayList<ClientState.ClientTable> clientTables = new ArrayList<>();
                do {
                    clientTables.add(
                            new ClientState.ClientTable(
                                    resultSet.getString(TableUserTime.PHONE.getValue()),
                                    resultSet.getString(TableUserTime.CODE.getValue()),
                                    resultSet.getTimestamp(TableUserTime.TIME_START.getValue()),
                                    resultSet.getTimestamp(TableUserTime.TIME_FINISH.getValue())
                            )
                    );
                } while (resultSet.next());
                Platform.runLater(() -> this.table_client_info.setItems(FXCollections.observableArrayList(clientTables)));
            } catch (final SQLException ex) {
                ex.printStackTrace();
            }
        });
    }

    private void registerButtonEvents() {
        final OpenSchedule openSchedule = OpenSchedule.getINSTANCE();

        // Section contact information
        this.contact_button.setOnMouseClicked(mouseEvent -> {
            Platform.runLater(() -> Alert.send(
                    "Información de Contacto",
                    "Imagenes: lacuerda.net" +
                            "\nIconos: iconos8.es" +
                            "\nLibreria de WhatsApp4J: Alessandro Autiero" +
                            "\nProgramador: Oscar Dali G.Z.",
                    javafx.scene.control.Alert.AlertType.INFORMATION
            ));
        });

        // Search section
        this.button_search.setOnMouseClicked(mouseEvent -> {
            final String text = this.text_search_name.getText();
            final String where = (text.isEmpty() ? "" : String.format("%s = \"%s\" COLLATE NOCASE", TableUserTime.PHONE.getValue(), this.text_search_name.getText()));
            this.updateTable(where);
            this.text_search_name.setText("");
        });

        this.button_update.setOnMouseClicked(mouseEvent -> this.updateTable(""));

        this.table_client_info.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection == null) {
                this.text_client_code.setText("");
                return;
            }
            if (this.text_client_code.isDisable()) return;
            this.text_client_code.setText(newSelection.getCode());
        });

        this.button_client_leave.setOnMouseClicked(mouseEvent -> {
            final String codeText = this.text_client_code.getText();
            if (codeText.isEmpty()) return;
            openSchedule.getDatabase().delete(
                    TableUserTime.TABLE_NAME.getValue(),
                    String.format("%s = \"%s\"", TableUserTime.CODE.getValue(), codeText)
            ).whenComplete((aBoolean1, throwable1) -> {
                this.updateTable("");
                ClientStorage.getInstance().remove();
            });
        });

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
            final String code = (client == null) ? phone : client.getCode().getCode();
            if (ClientStorage.getInstance().checkLimit()) {
                Alert.send("Limite de clientes", "Se alcanzo el limite de clientes!", javafx.scene.control.Alert.AlertType.ERROR);
                this.setDefaultButtonStates();
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
            openSchedule.getDatabase().get(
                    TableUserTime.TABLE_NAME.getValue(),
                    String.format("%s = \"%s\" COLLATE NOCASE", TableUserTime.CODE.getValue(), code)
            ).whenComplete((resultSet, throwable) -> {
                if (resultSet != null) {
                    this.setDefaultButtonStates();
                    Platform.runLater(() -> Alert.send("Usuario ya registrado!", "El usuario ya esta registrado", javafx.scene.control.Alert.AlertType.INFORMATION));
                    if (finalClient != null) {
                        clientState.remove(finalClient.getPhone());
                    }
                    return;
                }
                openSchedule.getDatabase().insert(
                        TableUserTime.TABLE_NAME.getValue(),
                        String.format("(%s, %s, %s) VALUES (\"%s\", \"%s\", \"%s\")",
                                TableUserTime.PHONE.getValue(), TableUserTime.CODE.getValue(), TableUserTime.TIME_FINISH.getValue(),
                                phone, code, timeNow)
                ).whenComplete((aBoolean, throwable2) -> {
                    clientState.remove(phone);
                    this.setDefaultButtonStates();
                    ClientStorage.getInstance().add();
                    final WhatsappAPI whatsappAPI = openSchedule.getMessagesAPI().getWhatsappAPI();
                    final WhatsappDataManager manager = whatsappAPI.manager();
                    if (finalClient != null) {
                        manager.findChatByJid(finalClient.getJid()).ifPresent(chat -> whatsappAPI.sendMessage(chat, finalClient.getName() + " disfruta tu estancia!"));
                    }
                    if (finalClient != null && finalTimeToLeave) {
                        final Timer timer = new Timer(DataTime.timeToMilliseconds(
                                (timeNow.getHours() - DataTime.getTimeNow().getHours()),
                                (timeNow.getMinutes() - DataTime.getTimeNow().getMinutes())
                        ), event -> {
                            openSchedule.getDatabase().get(
                                    TableUserTime.TABLE_NAME.getValue(),
                                    String.format("%s = \"%s\"", TableUserTime.PHONE.getValue(), phone)
                            ).whenComplete((resultSet3, throwable3) -> {
                                if (resultSet3 == null) return;
                                openSchedule.getDatabase().delete(
                                        TableUserTime.TABLE_NAME.getValue(),
                                        String.format("%s = \"%s\"", TableUserTime.PHONE.getValue(), phone)
                                ).whenComplete((aBoolean1, throwable1) -> {
                                    this.updateTable("");
                                    ClientStorage.getInstance().remove();
                                    final String text = this.msg_clients_time_finished.getText()
                                            .replace("%cliente%", finalClient.getName());
                                    manager.findChatByJid(finalClient.getJid()).ifPresent(chat -> whatsappAPI.sendMessage(chat, text));
                                    Platform.runLater(() -> Alert.send("Tiempo de usuario terminado", "El tiempo de " + finalClient.getName() + " termino.", javafx.scene.control.Alert.AlertType.INFORMATION));
                                });
                            });
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
                            ).whenComplete((aBoolean1, throwable1) -> {
                                this.updateTable("");
                                ClientStorage.getInstance().remove();
                            });
                            Platform.runLater(() -> Alert.send("Tiempo de usuario terminado", "El tiempo de " + clientName + " termino.", javafx.scene.control.Alert.AlertType.INFORMATION));
                        });
                        timer.setRepeats(false);
                        timer.start();
                    }
                    this.updateTable("");
                    Platform.runLater(() -> Alert.send("Registro de usuario", "Usuario registrado.", javafx.scene.control.Alert.AlertType.INFORMATION));
                });
            });
        });
        this.button_client_cancel.setOnMouseClicked(mouseEvent -> this.setDefaultButtonStates());

        // Inventory save items section
        this.button_inv_save.setOnMouseClicked(mouseEvent -> {
            if (this.text_inv_name.getText().isEmpty() || this.text_inv_state.getText().isEmpty()
                    || this.text_inv_cost.getText().isEmpty()) {
                Alert.send("Error", "No puedes dejar campos vacios!", javafx.scene.control.Alert.AlertType.WARNING);
                return;
            }
            final String text;
            try {
                text = String.valueOf(Integer.parseInt(this.text_inv_cost.getText()));
            } catch (final Exception ignored) {
                Alert.send("Error", "El valor " + this.text_inv_cost.getText() + " no es un valor valido!", javafx.scene.control.Alert.AlertType.ERROR);
                this.setDefaultButtonStates();
                return;
            }
            openSchedule.getDatabase().insert(
                    TableInventory.TABLE_NAME.getValue(),
                    String.format("(%s, %s, %s) VALUES (\"%s\", \"%s\", \"%s\")", TableInventory.NAME.getValue(), TableInventory.STATE.getValue(), TableInventory.COST.getValue(),
                            this.text_inv_name.getText(), this.text_inv_state.getText(), text)
            ).whenComplete((aBoolean, throwable) -> {
                this.setDefaultButtonStates();
                if (aBoolean) Alert.send("Articulo guardado", "Articulo agregado al inventario!", javafx.scene.control.Alert.AlertType.INFORMATION);
            });
        });

        // Inventory search items section
        this.button_inv_search.setOnMouseClicked(mouseEvent -> {
            if (this.text_inv_id.getText().isEmpty()) {
                Alert.send("Error", "No puedes dejar este campo vacio!", javafx.scene.control.Alert.AlertType.WARNING);
                return;
            }
            openSchedule.getDatabase().get(
                    TableInventory.TABLE_NAME.getValue(),
                    String.format("%s = \"%s\"", TableInventory.ID.getValue(), this.text_inv_id.getText())
            ).whenComplete((resultSet, throwable) -> {
                if (resultSet == null || throwable != null) {
                    Alert.send("Error", "No se encontraron resultados!", javafx.scene.control.Alert.AlertType.INFORMATION);
                    return;
                }
                try {
                    this.text_inv_name.setText(resultSet.getString(TableInventory.NAME.getValue()));
                    this.text_inv_cost.setText(resultSet.getString(TableInventory.COST.getValue()));
                    this.text_inv_state.setText(resultSet.getString(TableInventory.STATE.getValue()));
                } catch (final SQLException sqlException) {
                    sqlException.printStackTrace();
                }
            });
        });

        // Inventory update items section
        this.button_inv_update.setOnMouseClicked(mouseEvent -> {
            if (this.text_inv_id.getText().isEmpty()) {
                Alert.send("Error", "Debes de colocar un ID para editar!", javafx.scene.control.Alert.AlertType.ERROR);
                return;
            }
            openSchedule.getDatabase().update(
                    TableInventory.TABLE_NAME.getValue(),
                    String.format("%s = \"%s\", %s = \"%s\", %s = \"%s\"",
                            TableInventory.NAME.getValue(), this.text_inv_name.getText(),
                            TableInventory.STATE.getValue(), this.text_inv_state.getText(),
                            TableInventory.COST.getValue(), this.text_inv_cost.getText()),
                    String.format("%s = \"%s\"", TableInventory.ID.getValue(), this.text_inv_id.getText())
            ).whenComplete((aBoolean, throwable) -> this.setDefaultButtonStates());
        });

        // Inventory delete items section
        this.button_inv_delete.setOnMouseClicked(mouseEvent -> {
            if (this.text_inv_id.getText().isEmpty()) {
                Alert.send("Error", "Debes de colocar un ID para poder eliminarlo!", javafx.scene.control.Alert.AlertType.ERROR);
                return;
            }
            openSchedule.getDatabase().delete(
                    TableInventory.TABLE_NAME.getValue(),
                    String.format("%s = \"%s\"", TableInventory.ID.getValue(), this.text_inv_id.getText())
            ).whenComplete((aBoolean, throwable) -> {
                if (throwable == null) Alert.send("Elemento eliminado", "Elemento eliminado del inventario!", javafx.scene.control.Alert.AlertType.INFORMATION);
                this.setDefaultButtonStates();
            });
        });
    }

    private void setDefaultButtonStates() {
        this.text_inv_name.setText("");
        this.text_inv_state.setText("");
        this.text_inv_cost.setText("");
        this.text_inv_id.setText("");
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