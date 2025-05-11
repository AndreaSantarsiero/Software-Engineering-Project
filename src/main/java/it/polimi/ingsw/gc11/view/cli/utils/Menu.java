package it.polimi.ingsw.gc11.view.cli.utils;

import java.io.IOException;
import java.util.List;



public class Menu {

    public static int interactiveMenu(List<String> options) throws IOException {
        int selected = 0;

        renderMenu(options, selected);

        while (true) {
            int c = System.in.read();

            if (c == 27) { // Escape character
                if (System.in.read() == 91) { // '['
                    int arrow = System.in.read();
                    switch (arrow) {
                        case 65: // 'A' - Arrow Up
                            selected = (selected - 1 + options.size()) % options.size();
                            renderMenu(options, selected);
                            break;
                        case 66: // 'B' - Arrow Down
                            selected = (selected + 1) % options.size();
                            renderMenu(options, selected);
                            break;
                    }
                }
            } else if (c == 10) { // Enter key (LF)
                return selected;
            }
        }
    }



    private static void renderMenu(List<String> options, int selected) {
        clearView();
        System.out.println("Choose an option (↑↓ and press Enter):\n");
        for (int i = 0; i < options.size(); i++) {
            if (i == selected) {
                System.out.println("  > \u001b[47m" + options.get(i) + "\u001b[0m");
            } else {
                System.out.println("    " + options.get(i));
            }
        }
    }



    public static void clearView() {
        System.out.print("\u001b[H\u001b[2J");
        System.out.flush();
        System.out.println("***    Galaxy Truckers    ***\n");
    }
}
