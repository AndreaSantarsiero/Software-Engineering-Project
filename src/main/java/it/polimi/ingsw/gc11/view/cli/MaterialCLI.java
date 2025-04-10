package it.polimi.ingsw.gc11.view.cli;

import it.polimi.ingsw.gc11.model.Material;
import org.fusesource.jansi.Ansi;
import java.util.List;



public class MaterialCLI {

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



    public static void printEmpty(){
        System.out.print("□ ");
    }
}
