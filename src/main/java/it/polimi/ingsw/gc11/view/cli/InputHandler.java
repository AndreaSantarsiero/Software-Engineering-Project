package it.polimi.ingsw.gc11.view.cli;

import it.polimi.ingsw.gc11.view.GamePhaseData;
import org.jline.keymap.BindingReader;
import org.jline.keymap.KeyMap;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import java.io.IOException;
import java.util.List;



public class InputHandler {

    private LineReader lineReader;
    private BindingReader bindingReader;



    public InputHandler() {
        try {
            Terminal terminal = TerminalBuilder.builder().system(true).jna(true).nativeSignals(true).build();
            terminal.enterRawMode();
            lineReader = LineReaderBuilder.builder().terminal(terminal).build();
            bindingReader = new BindingReader(terminal.reader());
        } catch (IOException e) {
            throw new RuntimeException("Failed to initialize JLine", e);
        }
    }



    public void interactiveMenu(GamePhaseData data, List<String> options, int previouslySelected) {
        int selected = previouslySelected;

        KeyMap<String> keyMap = new KeyMap<>();
        keyMap.bind("up", "\033[A", "w", "W");      // Up
        keyMap.bind("down", "\033[B", "s", "S");    // Down
        keyMap.bind("enter", "\r", "\n");           // Enter (Windows/Linux)

        while (true) {
            String key = bindingReader.readBinding(keyMap);

            switch (key) {
                case "up":
                    selected = (selected - 1 + options.size()) % options.size();
                    data.setMenuChoice(selected);
                    break;
                case "down":
                    selected = (selected + 1) % options.size();
                    data.setMenuChoice(selected);
                    break;
                case "enter":
                    data.confirmMenuChoice();
                    return;
                default:
                    // ignore any other key
            }
        }
    }



    public void readLine(GamePhaseData data) {
        data.setStringInput(lineReader.readLine());
    }





//    KeyMap<String> keyMap = new KeyMap<>();
//    keyMap.bind("up", "\033[A", "w", "W");
//    keyMap.bind("down", "\033[B", "s", "S");
//    keyMap.bind("left", "\033[D", "a", "A");
//    keyMap.bind("right", "\033[C", "d", "D");
//    keyMap.bind("enter", "\r", "\n");
}
