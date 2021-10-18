package me.upp.dali.openschedule.controller.handlers;

import com.google.zxing.common.BitMatrix;
import it.auties.whatsapp4j.listener.WhatsappListener;
import lombok.NonNull;

public class MessagesHandler implements WhatsappListener {

    @Override
    public void onQRCode(@NonNull final BitMatrix qr) {

    }
}
