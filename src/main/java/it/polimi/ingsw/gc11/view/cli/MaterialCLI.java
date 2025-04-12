package it.polimi.ingsw.gc11.view.cli;

import it.polimi.ingsw.gc11.model.Material;
import org.fusesource.jansi.Ansi;
import java.util.List;



/**
 * Handles the CLI (Command Line Interface) representation of materials using colored symbols
 * Prints colored blocks representing each material type
 */
public class MaterialCLI {

    /**
     * Prints the given list of materials to the CLI using colored blocks
     * Each material is represented by a colored "■" symbol corresponding to its type
     *
     * @param materials the list of materials to print
     */
    public static void print(List<Material> materials) {
        for(Material material : materials) {
            if (material.getType().equals(Material.Type.BLUE)) {
                System.out.print(Ansi.ansi().reset().fg(Ansi.Color.BLUE) + "■ ");
            }
            else if (material.getType().equals(Material.Type.GREEN)) {
                System.out.print(Ansi.ansi().reset().fg(Ansi.Color.GREEN) + "■ ");
            }
            else if (material.getType().equals(Material.Type.YELLOW)) {
                System.out.print(Ansi.ansi().reset().fg(Ansi.Color.YELLOW) + "■ ");
            }
            else if (material.getType().equals(Material.Type.RED)) {
                System.out.print(Ansi.ansi().reset().fg(Ansi.Color.RED) + "■ ");
            }
        }
    }



    /**
     * Prints an empty material slot using the "□" symbol
     */
    public static void printEmpty(){
        System.out.print("□ ");
    }
}
