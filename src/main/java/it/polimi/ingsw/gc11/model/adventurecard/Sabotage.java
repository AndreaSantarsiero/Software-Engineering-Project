package it.polimi.ingsw.gc11.model.adventurecard;

import it.polimi.ingsw.gc11.model.Dice;
import it.polimi.ingsw.gc11.model.Player;
import it.polimi.ingsw.gc11.model.GameModel;
import it.polimi.ingsw.gc11.model.shipboard.ShipBoard;



public class Sabotage extends AdventureCard {

    public Sabotage(AdventureCard.Type type) {
        super(type);
    }


//    public void handler(GameModel gameModel, Player player) {
//        //Trovo la nave con il numero minore di membri,
//        //Per navi con stesso numero si prende la più avanti nel percorso
//        int num = Integer.MAX_VALUE;;
//        int position = -1;
//        ShipBoard ResultShipboard = null;
//        Player[] players = gameModel.getPlayers();
//
//        for (int i = 0; i < gameModel.getNumPlayers(); i++){
//            ShipBoard shipBoard = players[i].getShipBoard();
//
//            if(num > shipBoard.getMembers() || (num == shipBoard.getMembers() && position > players[i].getPosition())){
//                num = shipBoard.getMembers();
//                position = players[i].getPosition();
//                ResultShipboard = shipBoard;
//            }
//        }
//
//        if (ResultShipboard == null) {
//            throw new IllegalStateException("Nessuna nave valida trovata.");
//        }
//
//        //Seleziono le coordinate dove distruggere ed elimina la carta
//        //Le coordinate sono selezionate lanciando due dadi per individuare la colonna e due per la riga
//        //In caso di coordinate non valide i dadi vengono rilanciati per un massimo di 3 volte
//        int x = 0;
//        int y = 0;
//        Dice dice1 = new Dice();
//        Dice dice2 = new Dice();
//        int flag = 0;
//
//        for (int i = 0; i < 3 && flag == 0; i++) {
//            try{
//                x = dice1.roll() + dice2.roll();
//                y = dice1.roll() + dice2.roll();
//                if(ResultShipboard.getShipCard(x, y) != null){
//                    flag = 1;
//                    ResultShipboard.removeShipCard(x, y);
//                }
//            } catch (IllegalArgumentException e) {
//                System.out.println("Coordinate non valide");
//            }
//        }
//    }
}
