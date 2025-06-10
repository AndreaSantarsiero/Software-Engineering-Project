package it.polimi.ingsw.gc11.view.cli.utils;

import it.polimi.ingsw.gc11.model.Material;
import it.polimi.ingsw.gc11.model.adventurecard.*;
import org.fusesource.jansi.Ansi;
import java.util.List;



public class AdventureCardCLI {

    public static int cardWidth = 23;
    public static int cardLength = 15;
    List<String> stars = List.of(
            ".        .      *  . ",
                    "    *.   *           ",
                    " .     .        .    ",
                    ".        .      *  . ",
                    " .     .        .    ",
                    "    *.   *           ",
                    "      .  .  *   .    ",
                    "  .   *        *    .",
                    "        .            ",
                    ".        .      *  . ",
                    "       *             ",
                    " .          .      . ");



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
        else if (i == 5){
            System.out.print(" Lost days: " + printNumber(abandonedShip.getLostDays()) + "       ");
        }
        else if (i == 7){
            System.out.print(" Lost members: " + printNumber(abandonedShip.getLostMembers()) + "    ");
        }
        else if (i == 9){
            System.out.print(" Coins: " + printNumber(abandonedShip.getCoins()) + "           ");
        }
        else {
            System.out.print("                     ");
        }
    }



    public void draw(AbandonedStation abandonedStation, int i){
        if (i == 1){
            System.out.print("  ABANDONED STATION  ");
        }
        else if (i == 5){
            System.out.print(" Lost days: " + printNumber(abandonedStation.getLostDays()) + "       ");
        }
        else if (i == 7){
            System.out.print(" Members required: " + printNumber(abandonedStation.getMembersRequired()));
        }
        else if (i == 9){
            printMaterials(abandonedStation.getMaterials());
            setColor(abandonedStation);
        }
        else {
            System.out.print("                     ");
        }
    }



    public void draw(CombatZoneLv1 combatZone, int i){
        if (i == 1){
            System.out.print("     COMBAT ZONE     ");
        }
        else if (i == 3){
            System.out.print(" Least crew members: ");
        }
        else if (i == 4){
            System.out.print(" Lost days: " + printNumber(combatZone.getLostDays()) + "       ");
        }
        else if (i == 5){
            System.out.print(" ------------------- ");
        }
        else if (i == 6){
            System.out.print(" Least engine power: ");
        }
        else if (i == 7){
            System.out.print(" Lost members: " + printNumber(combatZone.getLostMembers()) + "    ");
        }
        else if (i == 8){
            System.out.print(" ------------------- ");
        }
        else if (i == 9){
            System.out.print(" Least fire power:   ");
        }
        else if (i == 10){
            System.out.print(" Shots:              ");
        }
        else {
            System.out.print("                     ");
        }
    }

    public void draw(CombatZoneLv2 combatZone, int i){
        if (i == 1){
            System.out.print("     COMBAT ZONE     ");
        }
        else if (i == 3){
            System.out.print(" Least fire power:   ");
        }
        else if (i == 4){
            System.out.print(" Lost days: " + printNumber(combatZone.getLostDays()) + "       ");
        }
        else if (i == 5){
            System.out.print(" ------------------- ");
        }
        else if (i == 6){
            System.out.print(" Least engine power: ");
        }
        else if (i == 7){
            System.out.print(" Lost materials: " + printNumber(combatZone.getLostMaterials()) + "  ");
        }
        else if (i == 8){
            System.out.print(" ------------------- ");
        }
        else if (i == 9){
            System.out.print(" Least crew members: ");
        }
        else if (i == 10){
            System.out.print(" Shots:              ");
        }
        else {
            System.out.print("                     ");
        }
    }



    public void draw(Epidemic epidemic, int i){
        if (i == 1){
            System.out.print("      EPIDEMIC       ");
        }
        else if (i == 4){
            System.out.print("        ,-^-.        ");
        }
        else if (i == 5){
            System.out.print("        |\\/\\|        ");
        }
        else if (i == 6){
            System.out.print("        `-V-'        ");
        }
        else if (i == 7 || i == 8 || i == 9){
            System.out.print("          H          ");
        }
        else if (i == 10){
            System.out.print("       .-;\":-.       ");
        }
        else if (i == 11){
            System.out.print("      ,'|  `; \\      ");
        }
        else {
            System.out.print("                     ");
        }
    }



    public void draw(MeteorSwarm meteorSwarm, int i){
        if (i == 1){
            System.out.print("     METEOR SWARM    ");
        }
        else if (i == 4){
            System.out.print(".          \\         ");
        }
        else if (i == 5){
            System.out.print("     .      \\   ,    ");
        }
        else if (i == 6){
            System.out.print("  .          o     . ");
        }
        else if (i == 9){
            System.out.print(" Meteors:            ");
        }
        else {
            System.out.print("                     ");
        }
    }



    public void draw(OpenSpace openSpace, int i){
        if (i == 1){
            System.out.print("     OPEN SPACE      ");
        }
        else if (i == 4){
            System.out.print("           _         ");
        }
        else if (i == 5){
            System.out.print("         -=\\`\\       ");
        }
        else if (i == 6){
            System.out.print("     |\\ ____\\_\\__    ");
        }
        else if (i == 7){
            System.out.print("   -=\\c`\"\"\"\"\"\"\" \"`)  ");
        }
        else if (i == 8){
            System.out.print("      `~~~~~/ /~~`   ");
        }
        else if (i == 9){
            System.out.print("        -==/ /       ");
        }
        else if (i == 10){
            System.out.print("          '-'        ");
        }
        else {
            System.out.print("                     ");
        }
    }



    public void draw(Pirates pirates, int i){
        if (i == 1){
            System.out.print("       PIRATES       ");
        }
        else if (i == 3){
            System.out.print("        \\   /        ");
        }
        else if (i == 4){
            System.out.print("   .____-/.\\-____.   ");
        }
        else if (i == 5){
            System.out.print("        ~`-'~        ");
        }
        else if (i == 7){
            System.out.print(" Lost days: " + printNumber(pirates.getLostDays()) + "       ");
        }
        else if (i == 8){
            System.out.print(" Fire power: " + printNumber(pirates.getFirePower()) + "      ");
        }
        else if (i == 9){
            System.out.print(" Coins: " + printNumber(pirates.getCoins()) + "           ");
        }
        else if (i == 10){
            System.out.print(" Shots:              ");
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
            catch (Exception ignored){
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
        else if (i == 4){
            System.out.print(" Fire power: " + printNumber(slavers.getFirePower()) + "      ");
        }
        else if (i == 6){
            System.out.print(" Lost days: " + printNumber(slavers.getLostDays()) + "       ");
        }
        else if (i == 8){
            System.out.print(" Lost members: " + printNumber(slavers.getLostMembers()) + "    ");
        }
        else if (i == 10){
            System.out.print(" Coins: " + printNumber(slavers.getCoins()) + "           ");
        }
        else {
            System.out.print("                     ");
        }
    }



    public void draw(Smugglers smugglers, int i){
        if (i == 1){
            System.out.print("      SMUGGLERS      ");
        }
        else if (i == 4){
            System.out.print(" Fire power: " + printNumber(smugglers.getFirePower()) + "      ");
        }
        else if (i == 6){
            System.out.print(" Lost days: " + printNumber(smugglers.getLostDays()) + "       ");
        }
        else if (i == 8){
            System.out.print(" Lost materials: " + printNumber(smugglers.getLostMaterials()) + "  ");
        }
        else if (i == 10){
            printMaterials(smugglers.getMaterials());
            setColor(smugglers);
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
            System.out.print(stars.get(i-2));
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



    public String printNumber(int number){
        if(number < 10){
            return number + " ";
        }
        else{
            return number + "";
        }
    }


    public void printMaterials(List<Material> materials){
        try{
            System.out.print(" Materials: ");
            MaterialCLI.print(materials);
            for (int j = 0; j < (4 - materials.size()); j++) {
                System.out.print("  ");
            }
            System.out.print(" ");
        }
        catch (Exception ignored){
            System.out.print("                    ");
        }
    }
}
