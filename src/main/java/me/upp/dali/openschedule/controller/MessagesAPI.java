package me.upp.dali.openschedule.controller;

import it.auties.whatsapp4j.whatsapp.WhatsappAPI;
import lombok.Getter;

public class MessagesAPI {

    @Getter
    private final WhatsappAPI whatsappAPI;

    public MessagesAPI() {
        this.whatsappAPI = new WhatsappAPI();
    }
}
