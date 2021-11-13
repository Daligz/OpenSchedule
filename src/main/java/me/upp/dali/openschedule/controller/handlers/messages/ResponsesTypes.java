package me.upp.dali.openschedule.controller.handlers.messages;

import it.auties.whatsapp4j.protobuf.chat.Chat;
import it.auties.whatsapp4j.protobuf.info.MessageInfo;
import it.auties.whatsapp4j.whatsapp.WhatsappAPI;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import me.upp.dali.openschedule.controller.MainView;

@Getter
@AllArgsConstructor
public enum ResponsesTypes {
    INFORMATION(
            "$!#!$",
            new Response(DefaultMessages.INFORMATION.getMessage()),
            // Verificar si no esta en algun estado el que envio el mensaje
            (chat, message, whatsappAPI) ->
                whatsappAPI.sendMessage(
                        chat,
                        MainView.getInstance().msg_information.getText()
                )
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
    CLIENTS_REGISTER(
            "2",
            new Response("CLIENTS_REGISTER"),
            // Aqui se envian dos mensajes se debe de verificar cual es el correcto
            (chat, message, whatsappAPI) ->
                whatsappAPI.sendMessage(
                        chat,
                        MainView.getInstance().msg_clients_amount.getText()
                )
    ),
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
            (chat, message, whatsappAPI) ->
                whatsappAPI.sendMessage(
                        chat,
                        MainView.getInstance().msg_clients_amount.getText()
                )
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
