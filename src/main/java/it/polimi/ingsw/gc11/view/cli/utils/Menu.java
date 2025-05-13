package it.polimi.ingsw.gc11.view.cli.utils;

import org.jline.keymap.BindingReader;
import org.jline.keymap.KeyMap;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import org.jline.utils.AttributedString;
import org.jline.utils.AttributedStyle;
import java.io.IOException;
import java.util.List;



public class Menu {

    private static final Terminal terminal;
    private static final LineReader lineReader;
    private static final BindingReader bindingReader;



    static {
        try {
            terminal = TerminalBuilder.builder().system(true).jna(true).nativeSignals(true).build();
            lineReader = LineReaderBuilder.builder().terminal(terminal).build();
            bindingReader = new BindingReader(terminal.reader());
        } catch (IOException e) {
            throw new RuntimeException("Failed to initialize JLine", e);
        }
    }



    public static int interactiveMenu(String title, List<String> options) {
        int selected = 0;

        KeyMap<String> keyMap = new KeyMap<>();
        keyMap.bind("up", "w", "W");       // Up
        keyMap.bind("down", "s", "S");     // Down
        keyMap.bind("enter", "\r", "\n");  // Enter (Windows/Linux)

        while (true) {
            renderMenu(title, options, selected);
            String key = bindingReader.readBinding(keyMap);

            switch (key) {
                case "up":
                    selected = (selected - 1 + options.size()) % options.size();
                    break;
                case "down":
                    selected = (selected + 1) % options.size();
                    break;
                case "enter":
                    return selected;
                default:
                    // ignore any other key
            }
        }
    }



    private static String key(int finalChar) {
        return "\033[" + (char) finalChar;
    }



    public static String readLine(String message) {
        return lineReader.readLine(message);
    }



    private static void renderMenu(String title, List<String> options, int selected) {
        clearView();
        if (title != null && !title.isEmpty()) {
            System.out.println(title + " (↑↓ and press Enter):");
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



    public static void clearView() {
        System.out.print("\u001b[H\u001b[2J");
        System.out.flush();
        System.out.println("***    Galaxy Truckers    ***\n");
    }
}
