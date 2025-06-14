package it.polimi.ingsw.gc11.view.cli.templates;

import org.jline.utils.AttributedString;
import org.jline.utils.AttributedStyle;
import java.util.List;



public abstract class CLITemplate {

    protected void clearView() {
        System.out.print("\u001b[H\u001b[2J");
        System.out.flush();
    }



    protected void renderMenu(String title, List<String> options, int selected) {
        if (title != null && !title.isEmpty()) {
            System.out.println(title);
        }
        for (int i = 0; i < options.size(); i++) {
            if (i == selected) {
                AttributedString highlighted = new AttributedString(
                        "  > " + options.get(i),
                        AttributedStyle.DEFAULT.background(235)
                );
                System.out.println(highlighted.toAnsi());
            } else {
                System.out.println("    " + options.get(i));
            }
        }
    }



    protected int renderMultiLevelMenu(List<List<String>> options, int i, int j, int selected) {
        if (i == selected) {
            AttributedString highlighted = new AttributedString(
                    "   " + options.get(i).get(j) + "   ",
                    AttributedStyle.DEFAULT.background(235)
            );
            System.out.print(highlighted.toAnsi());
        } else {
            System.out.print("   " + options.get(i).get(j) + "   ");
        }

        return (options.get(i).get(j).length() + 6);
    }



    protected void renderIntegerChoice(String label, int value) {
        String line = label + ": " + value;
        AttributedString highlighted = new AttributedString(line, AttributedStyle.DEFAULT.background(235));
        System.out.print(highlighted.toAnsi());
    }
}
