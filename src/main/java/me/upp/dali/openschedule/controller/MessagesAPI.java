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
                .requestTag("OpenSchedule") // The tag used for requests made to WhatsappWeb's WebSocket
                .description("OpenSchedule") // The description provided to Whatsapp during the authentication process
                .shortDescription("OS") // An acronym for the description
                .reconnectWhenDisconnected((reason) -> true) // Determines whether the connection should be reclaimed
                .async(true) // Determines whether requests sent to whatsapp should be asyncronous or not
                .build(); // Builds an instance of WhatsappConfiguration
        this.whatsappAPI = new WhatsappAPI(configuration);
    }
}
