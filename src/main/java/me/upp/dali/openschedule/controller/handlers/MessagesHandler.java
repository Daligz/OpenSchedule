package me.upp.dali.openschedule.controller.handlers;

import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import it.auties.whatsapp4j.listener.WhatsappListener;
import it.auties.whatsapp4j.manager.WhatsappDataManager;
import it.auties.whatsapp4j.protobuf.chat.Chat;
import it.auties.whatsapp4j.protobuf.info.MessageInfo;
import it.auties.whatsapp4j.response.impl.json.UserInformationResponse;
import it.auties.whatsapp4j.whatsapp.WhatsappAPI;
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import me.upp.dali.openschedule.controller.Booter;
import me.upp.dali.openschedule.controller.handlers.messages.ResponsesTypes;
import me.upp.dali.openschedule.view.ViewLoader;

import java.awt.image.BufferedImage;
import java.io.IOException;

@AllArgsConstructor
public class MessagesHandler implements WhatsappListener {

    private final WhatsappDataManager manager;
    private final WhatsappAPI whatsappAPI;

    @Override
    public void onLoggedIn(@NonNull final UserInformationResponse info) {
        Platform.runLater(() -> {
            Booter.getInstance().bootStatusText.setText("Conectado a WhatsApp!");
            System.out.println("Logged in!, loading main view...");
            try {
                ViewLoader.getInstance().updateView(ViewLoader.ViewType.MAIN);
            } catch (final IOException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void onDisconnected() {
        Platform.runLater(() -> {
            Booter.getInstance().bootStatusText.setText("Desconectado de WhatsApp!");
            System.out.println("Disconected!");
        });
    }

    @Override
    public void onNewMessage(@NonNull final Chat chat, @NonNull final MessageInfo message) {
        if (chat.isGroup()) {
            this.whatsappAPI.sendMessage(chat, "No se aceptan mensajes en grupo.");
            return;
        }
        final String text = message.container().textMessage().text();
        final ResponsesTypes answer = ResponsesTypes.getByAnswer(text);
        answer.getResponseCallback().execute(chat, message, this.whatsappAPI);
    }

    @Override
    public void onQRCode(@NonNull final BitMatrix bitMatrix) {
        Platform.runLater(() -> {
            Booter.getInstance().bootStatusText.setText("Escanea el codigo qr...");
            Booter.getInstance().bootMainImage.setImage(
                    SwingFXUtils.toFXImage(this.convertToImage(bitMatrix), null)
            );
            System.out.println("Mostrando qr!");
        });
    }

    private BufferedImage convertToImage(@NonNull final BitMatrix bitMatrix) {
        /* final int height = bitMatrix.getHeight();
        final int width = bitMatrix.getWidth();
        final BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        bufferedImage.createGraphics();

        final Graphics2D graphics = (Graphics2D) bufferedImage.getGraphics();
        graphics.setColor(Color.WHITE);
        graphics.fillRect(0, 0, width, height);
        graphics.setColor(Color.BLACK);

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if (bitMatrix.get(i, j)) {
                    graphics.fillRect(i, j, 1, 1);
                }
            }
        }*/

        //final Image scaledInstance = MatrixToImageWriter.toBufferedImage(bitMatrix).getScaledInstance(256, 256, Image.SCALE_SMOOTH););

        //return bufferedImage;
        return MatrixToImageWriter.toBufferedImage(bitMatrix);
    }
}
