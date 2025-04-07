package it.polimi.ingsw.gc11.view.cli;

import it.polimi.ingsw.gc11.model.adventurecard.AdventureCard;



public class AdventureCardCLI {

    public static int cardWidth = 23;
    public static int cardLength = 15;



    public static void string(AdventureCard adventureCard){
        for(int i = 0; i < cardLength; i++){
            if (i == 0) {
                System.out.print("┌─────────────────────┐");
            }
            else if (i == (cardLength - 1)){
                System.out.print("└─────────────────────┘");
            }
            else {
                StringBuilder currentLine = new StringBuilder();
                currentLine.append("│");

                if (adventureCard != null) {
                    currentLine.append("                 ");
                }
                else {
                    if (i == 1){
                        currentLine.append("      StarDust       ");
                    }
                    else {
                        currentLine.append("                     ");
                    }
                }

                currentLine.append("│");
                System.out.print(currentLine.toString());
            }

            System.out.println();
        }
    }
}
