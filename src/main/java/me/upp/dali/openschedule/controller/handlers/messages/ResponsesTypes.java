package me.upp.dali.openschedule.controller.handlers.messages;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@AllArgsConstructor
public enum ResponsesTypes {
    INFORMATION(
            "$!#!$",
            new Response()
    ),
    CLIENTS_AMOUNT(
            "clientes",
            new Response()
    );

    private final String answer;
    private final Response response;

    @Getter @Setter
    public static class Response {
        private String response;
        public Response() { }
    }

    public static ResponsesTypes getByAnswer(final String answer) {
        for (final ResponsesTypes value : values()) {
            if (value.getAnswer().equalsIgnoreCase(answer)) {
                return value;
            }
        }
        return INFORMATION;
    }
}
