package it.polimi.ingsw.gc11.controller.Network;



public class ClientTest {
    public static void main(String[] args) {
        ClientNetwork clientNetwork = new ClientNetwork("Bob");

        try {
            clientNetwork.initialize(1234);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
