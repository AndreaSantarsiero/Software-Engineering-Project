package it.polimi.ingsw.gc11.view.cli;

import it.polimi.ingsw.gc11.model.Material;
import it.polimi.ingsw.gc11.model.adventurecard.*;
import org.fusesource.jansi.Ansi;
import java.util.List;



public class AdventureCardCLI {

    public static int cardWidth = 23;
    public static int cardLength = 15;



    public void print(AdventureCard adventureCard, int i){
        setColor(adventureCard);

        if (i == 0) {
            System.out.print("┌─────────────────────┐");
        }
        else if (i == (cardLength - 1)){
            System.out.print("└─────────────────────┘");
        }
        else {
            System.out.print("│");

            if (adventureCard != null) {
                if (!adventureCard.isUsed()){
                    switch (adventureCard.getType()){
                        case TRIAL, LEVEL1 -> printCoveredLevel1(i);
                        case LEVEL2 -> printCoveredLevel2(i);
                        case null, default -> System.out.print("Unknown adventure card type");
                    }
                }
                else{
                    adventureCard.print(this, i);
                }
            }
            else{
                printInvalid(i);
            }

            System.out.print("│");
        }
    }



    public static void setColor(AdventureCard adventureCard){
        if (adventureCard != null) {
            switch (adventureCard.getType()){
                case TRIAL, LEVEL1 -> System.out.print(Ansi.ansi().reset().fg(Ansi.Color.BLUE));
                case LEVEL2 -> System.out.print(Ansi.ansi().reset().fg(Ansi.Color.MAGENTA));
                //case LEVEL3 -> System.out.print(Ansi.ansi().reset().fg(Ansi.Color.RED));
                case null, default -> System.out.print(Ansi.ansi().reset());
            }
        }
        else{
            System.out.print(Ansi.ansi().reset());
        }

    }



    public static void printCoveredLevel1(int i){
        if (i == 2){
            System.out.print("       ══╦═╦══       ");
        }
        else if (i > 2 && i < (cardLength - 3)){
            System.out.print("         ║ ║         ");
        }
        else if (i == (cardLength - 3)){
            System.out.print("       ══╩═╩══       ");
        }
        else {
            System.out.print("                     ");
        }
    }



    public static void printCoveredLevel2(int i){
        if (i == 2){
            System.out.print("    ══╦═╦═══╦═╦══    ");
        }
        else if (i > 2 && i < (cardLength - 3)){
            System.out.print("      ║ ║   ║ ║      ");
        }
        else if (i == (cardLength - 3)){
            System.out.print("    ══╩═╩═══╩═╩══    ");
        }
        else {
            System.out.print("                     ");
        }
    }



    public static void printCoveredLevel3(int i){
        if (i == 2){
            System.out.print(" ══╦═╦═══╦═╦═══╦═╦══ ");
        }
        else if (i > 2 && i < (cardLength - 3)){
            System.out.print("   ║ ║   ║ ║   ║ ║   ");
        }
        else if (i == (cardLength - 3)){
            System.out.print(" ══╩═╩═══╩═╩═══╩═╩══ ");
        }
        else {
            System.out.print("                     ");
        }
    }



    public void draw(AbandonedShip abandonedShip, int i){
        if (i == 1){
            System.out.print("   ABANDONED SHIP    ");
        }
        else {
            System.out.print("                     ");
        }
    }



    public void draw(AbandonedStation abandonedStation, int i){
        if (i == 1){
            System.out.print("  ABANDONED STATION  ");
        }
        else {
            System.out.print("                     ");
        }
    }



    public void draw(CombatZoneLv1 combatZone, int i){
        if (i == 1){
            System.out.print("     COMBAT ZONE     ");
        }
        else {
            System.out.print("                     ");
        }
    }

    public void draw(CombatZoneLv2 combatZone, int i){
        if (i == 1){
            System.out.print("     COMBAT ZONE     ");
        }
        else {
            System.out.print("                     ");
        }
    }



    public void draw(Epidemic epidemic, int i){
        if (i == 1){
            System.out.print("      EPIDEMIC       ");
        }
        else {
            System.out.print("                     ");
        }
    }



    public void draw(MeteorSwarm meteorSwarm, int i){
        if (i == 1){
            System.out.print("     METEOR SWARM    ");
        }
        else {
            System.out.print("                     ");
        }
    }



    public void draw(OpenSpace openSpace, int i){
        if (i == 1){
            System.out.print("     OPEN SPACE      ");
        }
        else {
            System.out.print("                     ");
        }
    }



    public void draw(Pirates pirates, int i){
        if (i == 1){
            System.out.print("       PIRATES       ");
        }
        else {
            System.out.print("                     ");
        }
    }



    public void draw(PlanetsCard planetsCard, int i){
        if (i == 1){
            System.out.print("       PLANETS       ");
        }
        else if ((i % 3) == 0){
            System.out.print(" ");
            try{
                List<Material> materials = planetsCard.getFreePlanets().get((i/3) - 1).getMaterials();
                MaterialCLI.print(materials);
                setColor(planetsCard);
                for (int j = 0; j < (10 - materials.size()); j++) {
                    System.out.print("  ");
                }
            }
            catch (Exception _){
                System.out.print("                    ");
            }
        }
        else{
            System.out.print("                     ");
        }
    }



    public void draw(Slavers slavers, int i){
        if (i == 1){
            System.out.print("       SLAVERS       ");
        }
        else {
            System.out.print("                     ");
        }
    }



    public void draw(Smugglers smugglers, int i){
        if (i == 1){
            System.out.print("      SMUGGLERS      ");
        }
        else {
            System.out.print("                     ");
        }
    }



    public void draw(StarDust starDust, int i){
        if (i == 1){
            System.out.print("      STARDUST       ");
        }
        else {
            System.out.print("                     ");
        }
    }



    public static void printInvalid(int i){
        if (i == 1){
            System.out.print("    INVALID CARD     ");
        }
        else {
            System.out.print("                     ");
        }
    }

}
