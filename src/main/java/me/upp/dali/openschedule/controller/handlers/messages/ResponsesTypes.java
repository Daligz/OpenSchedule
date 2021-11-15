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
import me.upp.dali.openschedule.controller.client.Code;
import me.upp.dali.openschedule.model.database.tables.TableUser;

import java.sql.Date;
import java.sql.SQLException;

@Getter
@AllArgsConstructor
public enum ResponsesTypes {
    INFORMATION(
            "$!#!$",
            new Response(DefaultMessages.INFORMATION.getMessage()),
            // Verificar si no esta en algun estado el que envio el mensaje
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
                            System.out.println("1");
                            client.setName(text);
                            System.out.println("1.5");
                            client.getCode().generateCode(client);
                            System.out.println("2");
                            final String codeText = mainView.msg_clients_code.getText()
                                    .replace("%cliente%", client.getName())
                                    .replace("%codigo%", client.getCode().getCode())
                                    .replace("%tiempo%", "2 horas");
                            whatsappAPI.sendMessage(chat, codeText);
                            System.out.println("3");
                            client.setStatus(ClientState.Status.NONE);
                            System.out.println("4");
                        });
                        return;
                    } else if (status == ClientState.Status.YES_NO_REQUEST) {
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
            (chat, message, whatsappAPI) ->
                whatsappAPI.sendMessage(
                        chat,
                        MainView.getInstance().msg_clients_amount.getText()
                )
    ),
    /**
     * Aqui se envian dos mensajes se debe de verificar cual es el correcto
     * Esto se hace con @{@link me.upp.dali.openschedule.controller.client.ClientState}
     */
    CLIENTS_REGISTER(
            "2",
            new Response("CLIENTS_REGISTER"),
            (chat, message, whatsappAPI) -> {
            final ClientState clientState = ClientState.getInstance();
                final String name = chat.displayName();
                final String phone = WhatsappUtils.phoneNumberFromJid(chat.jid());
                final OpenSchedule openSchedule = OpenSchedule.getINSTANCE();
                openSchedule.getDatabase().get(
                        TableUser.TABLE_NAME.getValue(),
                        String.format("%s = \"%s\"", TableUser.PHONE.getValue(), phone)
                ).whenComplete((resultSet, throwable) -> {
                    if (resultSet == null || throwable != null && !(clientState.contains(phone))) {
                        clientState.set(
                                phone,
                                new ClientState.Client(
                                        name, phone, ClientState.Status.REGISTER_CLIENT, new Code(), new Date(System.currentTimeMillis())
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
                                            new Date(System.currentTimeMillis())
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
                        final String text = mainView.msg_clients_register_known_client.getText()
                                .replace("%cliente%", client.getName());
                        client.setStatus(ClientState.Status.YES_NO_REQUEST);
                        whatsappAPI.sendMessage(chat, text);
                    }
                });
            }),
    CLIENTS_TIME(
            "3",
            new Response(DefaultMessages.CLIENTS_TIME.getMessage()),
            (chat, message, whatsappAPI) ->
                whatsappAPI.sendMessage(
                        chat,
                        MainView.getInstance().msg_clients_amount.getText()
                )
    ),
    CLIENT_CANCEL_PROCESS(
            "cancelar",
            new Response("CLIENT_CANCEL_PROCESS"),
            (chat, message, whatsappAPI) -> {
                final String phone = WhatsappUtils.phoneNumberFromJid(chat.jid());
                final ClientState clientState = ClientState.getInstance();
                if (clientState.contains(phone)) {
                    whatsappAPI.sendMessage(chat, phone + " | Encontrado!" + clientState.get(phone).toString());
                } else {
                    whatsappAPI.sendMessage(chat, phone + " | No encontrado :c");
                }
            }
    ),
    CLIENT_REGISTER_CANCEL(
            "cancelar%contains%",
            new Response(DefaultMessages.CLIENTS_TIME.getMessage()),
            (chat, message, whatsappAPI) ->
                whatsappAPI.sendMessage(
                        chat,
                        MainView.getInstance().msg_clients_amount.getText()
                )
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
            if (valueAnswer.contains("%contains%") &&
                    valueAnswer.replace("%contains%", "").toLowerCase().contains(answer.toLowerCase())) {
                return value;
            } else if (valueAnswer.equalsIgnoreCase(answer)) {
                return value;
            }
        }
        return INFORMATION;
    }

    public String getAnswer() {
        return answer;
    }
}
