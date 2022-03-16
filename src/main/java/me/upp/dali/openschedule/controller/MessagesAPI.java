package me.upp.dali.openschedule.controller;

import it.auties.whatsapp4j.whatsapp.WhatsappAPI;
import it.auties.whatsapp4j.whatsapp.WhatsappConfiguration;
import lombok.Getter;

public class MessagesAPI {

    @Getter
    private final WhatsappAPI whatsappAPI;

    public MessagesAPI() {
        var configuration = WhatsappConfiguration.builder()
                .whatsappUrl("wss://web.whatsapp.com/ws") // WhatsappWeb's WebSocket URL
                .builder(); // Builds an istance of WhatsappConfiguration
                .async(true) // Determines wheter request sent to whatsapp should be asyncronous or not
                .description("openschedule")
                .build(); // Builds an istance of WhatsappConfiguration
                .shortDescription("OS") // An acronym for the description
                .async(false) // Determines wheter request sent to whatsapp should be asyncronous
                .description("openschedule") // The description provided to whatsapp the authentication process

                //whatsapp connector
                final MessagesAPI messagesAPI = new messagesAPI();
                final whatsappAPI whatsappAPI = messagesAPI.getWhatsappAPI();
                final whatsappDataManager manager = whatsappAPI.manager();
                if (!(force)){
                    whatsappAPI.registerListener(new MessagesHandler(manager, whatsappAPI));
                }
        this.whatsappAPI = new WhatsappAPI(configuration);
        whatsappAPI.connect();
    }
}
