package me.upp.dali.openschedule.controller.handlers.messages;

import it.auties.whatsapp4j.protobuf.chat.Chat;
import it.auties.whatsapp4j.protobuf.info.MessageInfo;
import it.auties.whatsapp4j.utils.WhatsappUtils;
import it.auties.whatsapp4j.whatsapp.WhatsappAPI;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import me.upp.dali.openschedule.OpenSchedule;
import me.upp.dali.openschedule.controller.MainView;
import me.upp.dali.openschedule.controller.client.ClientState;
import me.upp.dali.openschedule.controller.client.ClientStorage;
import me.upp.dali.openschedule.controller.client.Code;
import me.upp.dali.openschedule.model.database.tables.TableUser;
import me.upp.dali.openschedule.model.database.tables.TableUserTime;

import javax.swing.*;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Timestamp;

@Getter
@AllArgsConstructor
public enum ResponsesTypes {
    INFORMATION(
            "$!#!$",
            new Response(DefaultMessages.INFORMATION.getMessage()),
            (chat, message, whatsappAPI) -> {
                final ClientState clientState = ClientState.getInstance();
                final String phone = WhatsappUtils.phoneNumberFromJid(chat.jid());
                final MainView mainView = MainView.getInstance();
                if (clientState.contains(phone)) {
                    final ClientState.Client client = clientState.get(phone);
                    final ClientState.Status status = client.getStatus();
                    final String text = message.container().textMessage().text();
                    final OpenSchedule openSchedule = OpenSchedule.getINSTANCE();
                    if (status == ClientState.Status.REGISTER_NAME_REQUEST) {
                        whatsappAPI.sendMessage(chat, "Registrando como " + text + ", espera un momento...");
                        openSchedule.getDatabase().insert(
                                TableUser.TABLE_NAME.getValue(),
                                String.format("(%s, %s) VALUES (\"%s\", \"%s\")", TableUser.NAME.getValue(), TableUser.PHONE.getValue(), text, client.getPhone())
                        ).whenComplete((aBoolean, throwable) -> {
                            if (throwable != null) return;
                            final Timer timer = new Timer(1800000, event -> { // 1800000 milisengundos = 30 minutos
                                if (clientState.contains(phone)) {
                                    clientState.remove(phone);
                                    final String codeText = mainView.msg_clients_code_expired.getText()
                                            .replace("%cliente%", client.getName())
                                            .replace("%codigo%", client.getCode().getCode());
                                    whatsappAPI.sendMessage(chat, codeText);
                                }
                            });
                            timer.setRepeats(false);
                            timer.start();
                            client.setName(text);
                            client.getCode().generateCode(client);
                            final String codeText = mainView.msg_clients_code.getText()
                                    .replace("%cliente%", client.getName())
                                    .replace("%codigo%", client.getCode().getCode())
                                    .replace("%tiempo%", "30 minutos");
                            whatsappAPI.sendMessage(chat, codeText);
                            client.setStatus(ClientState.Status.NONE);
                        });
                        return;
                    }
                }
                whatsappAPI.sendMessage(
                        chat,
                        mainView.msg_information.getText()
                );
            }
    ),
    CLIENTS_AMOUNT(
            "1",
            new Response(DefaultMessages.CLIENTS_AMOUNT.getMessage()),
            (chat, message, whatsappAPI) -> {
                final MainView mainView = MainView.getInstance();
                final String text = mainView.msg_clients_amount.getText()
                        .replace("%clientes%", mainView.spn_clients_amount.getValue().toString());
                whatsappAPI.sendMessage(chat, text);
            }
    ),
    CLIENTS_REGISTER(
            "2",
            new Response("CLIENTS_REGISTER"),
            (chat, message, whatsappAPI) -> {
            final ClientState clientState = ClientState.getInstance();
                final String name = chat.displayName();
                final String jid = chat.jid();
                final String phone = WhatsappUtils.phoneNumberFromJid(jid);
                final OpenSchedule openSchedule = OpenSchedule.getINSTANCE();
                if (ClientStorage.getInstance().checkLimit()) {
                    whatsappAPI.sendMessage(chat, "Actualmente alcanzamos nuestro limite de clientes!, por favor espera para entrar.");
                    return;
                }
                openSchedule.getDatabase().get(
                        TableUserTime.TABLE_NAME.getValue(),
                        String.format("%s = \"%s\"", TableUserTime.PHONE.getValue(), phone)
                ).whenComplete((result, throwable2) -> {
                    if (result != null) {
                        whatsappAPI.sendMessage(chat, "Ya te encuentras en el establecimiento!");
                        return;
                    }
                    openSchedule.getDatabase().get(
                            TableUser.TABLE_NAME.getValue(),
                            String.format("%s = \"%s\"", TableUser.PHONE.getValue(), phone)
                    ).whenComplete((resultSet, throwable) -> {
                        if (resultSet == null || throwable != null) {
                            clientState.set(
                                    phone,
                                    new ClientState.Client(
                                            name, phone, ClientState.Status.REGISTER_CLIENT, new Code(), new Date(System.currentTimeMillis()), jid
                                    )
                            );
                        } else {
                            try {
                                clientState.set(
                                        phone,
                                        new ClientState.Client(
                                                resultSet.getString(TableUser.NAME.getValue()),
                                                resultSet.getString(TableUser.PHONE.getValue()),
                                                ClientState.Status.REGISTER_KNOWN_CLIENT,
                                                new Code(),
                                                new Date(System.currentTimeMillis()),
                                                jid
                                        )
                                );
                            } catch (final SQLException e) {
                                e.printStackTrace();
                            }
                        }
                        final ClientState.Client client = clientState.get(phone);
                        final MainView mainView = MainView.getInstance();
                        if (client.getStatus() == ClientState.Status.REGISTER_CLIENT) {
                            final String text = mainView.msg_clients_register_name.getText();
                            client.setStatus(ClientState.Status.REGISTER_NAME_REQUEST);
                            whatsappAPI.sendMessage(chat, text);
                        } else if (client.getStatus() == ClientState.Status.REGISTER_KNOWN_CLIENT) {
                            final Timer timer = new Timer(1800000, event -> { // 1800000 milisengundos = 30 minutos
                                if (clientState.contains(phone)) {
                                    clientState.remove(phone);
                                    final String codeText = mainView.msg_clients_code_expired.getText()
                                            .replace("%cliente%", client.getName())
                                            .replace("%codigo%", client.getCode().getCode());
                                    whatsappAPI.sendMessage(chat, codeText);
                                }
                            });
                            timer.setRepeats(false);
                            timer.start();
                            client.getCode().generateCode(client);
                            final String codeText = mainView.msg_clients_code.getText()
                                    .replace("%cliente%", client.getName())
                                    .replace("%codigo%", client.getCode().getCode())
                                    .replace("%tiempo%", "30 minutos");
                            whatsappAPI.sendMessage(chat, codeText);
                            client.setStatus(ClientState.Status.NONE);
                        } else {
                            whatsappAPI.sendMessage(chat, "Ya tienes un código, registralo en la entrada del establecimiento.");
                        }
                    /*else if (client.getStatus() == ClientState.Status.REGISTER_KNOWN_CLIENT) {
                        final String text = mainView.msg_clients_register_known_client.getText()
                                .replace("%cliente%", client.getName());
                        client.setStatus(ClientState.Status.YES_NO_REQUEST);
                        whatsappAPI.sendMessage(chat, text);
                    }*/
                    });
                });
            }),
    CLIENTS_TIME(
            "3",
            new Response(DefaultMessages.CLIENTS_TIME.getMessage()),
            (chat, message, whatsappAPI) -> {
                final OpenSchedule openSchedule = OpenSchedule.getINSTANCE();
                final String phone = WhatsappUtils.phoneNumberFromJid(chat.jid());
                openSchedule.getDatabase().get(
                        TableUserTime.TABLE_NAME.getValue(),
                        String.format("%s = \"%s\"", TableUserTime.PHONE.getValue(), phone)
                ).whenComplete((resultSet, throwable) -> {
                    if (resultSet == null || throwable != null) {
                        whatsappAPI.sendMessage(chat, "Debes de ingresar al establecimiento!");
                        return;
                    }
                    String timeText = "";
                    try {
                        final Timestamp startTime = resultSet.getTimestamp(TableUserTime.TIME_START.getValue());
                        final Timestamp finishTime = resultSet.getTimestamp(TableUserTime.TIME_FINISH.getValue());
                        timeText = MainView.getInstance().msg_clients_time.getText()
                                .replace("%tiempo-inicio%", startTime.toString())
                                .replace("%tiempo-fin%", finishTime.toString());
                    } catch (final SQLException e) {
                        e.printStackTrace();
                    }
                    if (timeText.isEmpty()) return;
                    whatsappAPI.sendMessage(chat, timeText);
                });
            }
    ),
    CLIENT_CANCEL_PROCESS(
            "cancelar",
            new Response("CLIENT_CANCEL_PROCESS"),
            (chat, message, whatsappAPI) -> {
                final String phone = WhatsappUtils.phoneNumberFromJid(chat.jid());
                final ClientState clientState = ClientState.getInstance();
                if (!(clientState.contains(phone))) {
                    whatsappAPI.sendMessage(chat, "No hay algo para cancelar!");
                    return;
                }
                final ClientState.Client client = clientState.get(phone);
                if (client.getStatus() == ClientState.Status.NONE) {
                    whatsappAPI.sendMessage(chat, "No hay algo para cancelar!");
                    return;
                } else if (client.getStatus() == ClientState.Status.REGISTER_CLIENT ||
                    client.getStatus() == ClientState.Status.REGISTER_NAME_REQUEST) {
                    clientState.remove(phone);
                } else {
                    client.setStatus(ClientState.Status.NONE);
                }
                whatsappAPI.sendMessage(chat, "Cancelado correctamente!");
            }
    ),
    CLIENT_REGISTER_CANCEL(
            "cancelar acceso",
            new Response(DefaultMessages.CLIENTS_TIME.getMessage()),
            (chat, message, whatsappAPI) -> {
                final ClientState clientState = ClientState.getInstance();
                final String phone = WhatsappUtils.phoneNumberFromJid(chat.jid());
                if (!(clientState.contains(phone))) {
                    whatsappAPI.sendMessage(chat, "No tienes ningun código!");
                    return;
                }
                final ClientState.Client client = clientState.get(phone);
                if (client.getStatus() == ClientState.Status.NONE && !(client.getCode().getCode().isEmpty())) {
                    final String codeText = MainView.getInstance().msg_clients_code_expired.getText()
                            .replace("%cliente%", client.getName())
                            .replace("%codigo%", client.getCode().getCode());
                    whatsappAPI.sendMessage(chat, codeText);
                    clientState.remove(phone);
                } else {
                    whatsappAPI.sendMessage(chat, "El código no existe!");
                }
            }
    ),
    RESPONSE_YES(
            "si",
            new Response("RESPONSE_YES"),
            (chat, message, whatsappAPI) ->
                whatsappAPI.sendMessage(
                        chat,
                        MainView.getInstance().msg_clients_amount.getText()
                )
    ),
    RESPONSE_NO(
            "no",
            new Response("RESPONSE_NO"),
            (chat, message, whatsappAPI) ->
                whatsappAPI.sendMessage(
                        chat,
                        MainView.getInstance().msg_clients_amount.getText()
                )
    );

    private final String answer;
    private final Response response;
    private final ResponseCallback responseCallback;

    @Getter @Setter
    @AllArgsConstructor
    public static class Response {
        private String response;
        public Response() { }
    }

    public interface ResponseCallback {
        void execute(@NonNull final Chat chat, @NonNull final MessageInfo message, @NonNull WhatsappAPI whatsappAPI);
    }

    public static ResponsesTypes getByAnswer(final String answer) {
        for (final ResponsesTypes value : values()) {
            final String valueAnswer = value.getAnswer();
            if (valueAnswer.equalsIgnoreCase(answer)) {
                return value;
            }
        }
        return INFORMATION;
    }

    public String getAnswer() {
        return answer;
    }
}
