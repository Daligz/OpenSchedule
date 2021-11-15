package me.upp.dali.openschedule.controller.client;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Random;

@Getter
@AllArgsConstructor
public class Code {

    private String code = "";

    private static final Random random = new Random();
    private static final String[] AVAILABLE_LETTERS = {
            "A", "B", "C", "D",
            "1", "2", "3", "4"
    };

    public Code() { }

    public void generateCode(final ClientState.Client client) {
        this.code = client.getName().substring(0, 3).concat("-").concat(getLetters());
    }

    private String getLetters() {
        final StringBuilder letters = new StringBuilder();
        for (int i = 0; i < 3; i++) {
            letters.append(AVAILABLE_LETTERS[random.nextInt(AVAILABLE_LETTERS.length)]);
        }
        return letters.toString();
    }
}
