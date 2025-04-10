package it.polimi.ingsw.gc11.controller.Network;



public class ServerTest {
    public static void main(String[] args) {
        ServerNetwork serverNetwork = new ServerNetwork();

        try {
            serverNetwork.initialize(1234);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
