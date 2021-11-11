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
            new Response("Ejecucion general"),
            // El valor de los mensajes se debe de obtener de la db por que
            // se deben de guardar en caso de querer modificarlos y cargarse al iniciar
            (chat, message, whatsappAPI) ->
                whatsappAPI.sendMessage(
                        chat,
                        MainView.getInstance().msg_information.getText()
                )
    ),
    CLIENTS_AMOUNT(
            "clientes",
            new Response("Ejecucion de cantidad de clientes"),
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
            if (value.getAnswer().equalsIgnoreCase(answer)) {
                return value;
            }
        }
        return INFORMATION;
    }

    public String getAnswer() {
        return answer;
    }
}
