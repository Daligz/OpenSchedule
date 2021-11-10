package me.upp.dali.openschedule.controller.handlers.messages;

import it.auties.whatsapp4j.protobuf.chat.Chat;
import it.auties.whatsapp4j.protobuf.info.MessageInfo;
import it.auties.whatsapp4j.whatsapp.WhatsappAPI;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Getter
@AllArgsConstructor
public enum ResponsesTypes {
    INFORMATION(
            "$!#!$",
            new Response("Ejecucion general"),
            (chat, message, whatsappAPI) -> {
                whatsappAPI.sendMessage(chat, "Ejecucion general");
            }
    ),
    CLIENTS_AMOUNT(
            "clientes",
            new Response("Ejecucion de cantidad de clientes"),
            (chat, message, whatsappAPI) -> {
                whatsappAPI.sendMessage(chat, "Ejecucion de cantidad de clientes");
            }
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
