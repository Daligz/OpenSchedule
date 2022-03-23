package me.upp.dali.openschedule.controller.client;

import lombok.Getter;
import lombok.Setter;
import me.upp.dali.openschedule.controller.MainView;
import me.upp.dali.openschedule.controller.others.Alert;

@Getter @Setter
public class ClientStorage {

    private int clients;

    public ClientStorage() {
        this.clients = 0;
    }

    private static ClientStorage INSTANCE;

    public static ClientStorage getInstance() {
        if (INSTANCE == null) INSTANCE = new ClientStorage();
        return INSTANCE;
    }

    public void add() {
        final MainView mainView = MainView.getInstance();
        if (mainView.check_clients_limit.isSelected()) {
            if (this.clients > mainView.spn_clients_limit.getValue()) {
                Alert.send("Limite de clientes", "Se alcanzo el limite de clientes!", javafx.scene.control.Alert.AlertType.ERROR);
                System.out.println("Limite superado");
            }
        }
        this.clients++;
        mainView.spn_clients_amount.getValueFactory().setValue(this.clients);
    }

    public void remove() {
        this.clients--;
        final MainView mainView = MainView.getInstance();
        mainView.spn_clients_amount.getValueFactory().setValue(this.clients);
    }

    public boolean checkLimit() {
        final MainView mainView = MainView.getInstance();
        if (!(mainView.check_clients_limit.isSelected())) return false;
        return ((this.clients + 1) > mainView.spn_clients_limit.getValue());
    }
    
    public void generateCode(final ClientState.Client client) {
        this.code = client.getName().substring(0, 3).concat("-").concat(getLetters());
    }
    
}
