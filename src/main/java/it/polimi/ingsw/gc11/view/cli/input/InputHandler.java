package it.polimi.ingsw.gc11.view.cli.input;

import it.polimi.ingsw.gc11.view.GamePhaseData;
import it.polimi.ingsw.gc11.view.PlayerContext;
import org.jline.keymap.BindingReader;
import org.jline.keymap.KeyMap;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import java.io.IOException;



public class InputHandler {

    private final LineReader lineReader;
    private final BindingReader bindingReader;
    private final PlayerContext context;



    public InputHandler(PlayerContext context) {
        try {
            Terminal terminal = TerminalBuilder.builder().system(true).jna(true).nativeSignals(true).build();
            terminal.enterRawMode();
            lineReader = LineReaderBuilder.builder().terminal(terminal).build();
            bindingReader = new BindingReader(terminal.reader());
            this.context = context;
        } catch (IOException e) {
            throw new RuntimeException("Failed to initialize JLine", e);
        }
    }



    public void interactiveMenu(GamePhaseData data, int size, int previouslySelected) {
        if (!context.getCurrentPhase().equals(data)){
            return;
        }
        int selected = previouslySelected;

        KeyMap<String> keyMap = new KeyMap<>();
        keyMap.bind("up", "\033[A", "w", "W");      // Up
        keyMap.bind("down", "\033[B", "s", "S");    // Down
        keyMap.bind("enter", "\r", "\n");           // Enter (Windows/Linux)

        while (true) {
            String key = bindingReader.readBinding(keyMap);

            switch (key) {
                case "up":
                    selected = (selected - 1 + size) % size;
                    data.setMenuChoice(selected);
                    break;
                case "down":
                    selected = (selected + 1) % size;
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



    public void interactiveGridMenu(GamePhaseData data, int size, int cols, int previouslySelected) {
        if (!context.getCurrentPhase().equals(data)){
            return;
        }
        int selected = previouslySelected;
        int rows = (int) Math.ceil((double) size / cols);

        KeyMap<String> keyMap = new KeyMap<>();
        keyMap.bind("up", "\033[A", "w", "W");      // Up
        keyMap.bind("down", "\033[B", "s", "S");    // Down
        keyMap.bind("left", "\033[D", "a", "A");    // Left
        keyMap.bind("right", "\033[C", "d", "D");   // Right
        keyMap.bind("enter", "\r", "\n");           // Enter (Windows/Linux)

        while (true) {
            String key = bindingReader.readBinding(keyMap);
            int i = selected / cols;
            int j = selected % cols;

            switch (key) {
                case "up":
                    i = (i - 1 + rows) % rows;
                    break;
                case "down":
                    i = (i + 1) % rows;
                    break;
                case "left":
                    j = (j - 1 + cols) % cols;
                    break;
                case "right":
                    j = (j + 1) % cols;
                    break;
                case "enter":
                    data.confirmIntegerChoice();
                    return;
                default:
                    // ignore any other key
            }

            selected = i * cols + j;

            // in case the last row contains less elements
            if (selected >= size) {
                if(selected == size){
                    selected -= selected % cols;
                }
                else {
                    selected = (i - 1) * cols + j;
                }
            }

            data.setIntegerChoice(selected);
        }
    }



    public void interactiveNumberSelector(GamePhaseData data, int minValue, int maxValue, int previouslySelected) {
        if (!context.getCurrentPhase().equals(data)){
            return;
        }
        int selected = previouslySelected;
        int range = maxValue - minValue + 1;

        KeyMap<String> keyMap = new KeyMap<>();
        keyMap.bind("up", "\033[A", "w", "W", "+");     // Increment by 1
        keyMap.bind("down", "\033[B", "s", "S", "-");   // Decrement by 1
        keyMap.bind("right", "\033[C", "d", "D");       // Increment by 3
        keyMap.bind("left", "\033[D", "a", "A");        // Decrement by 3
        keyMap.bind("enter", "\r", "\n");               // Enter

        while (true) {
            String key = bindingReader.readBinding(keyMap);

            switch (key) {
                case "up":
                    selected = (selected + 1 > maxValue) ? minValue : selected + 1;
                    data.setIntegerChoice(selected);
                    break;
                case "down":
                    selected = (selected - 1 < minValue) ? maxValue : selected - 1;
                    data.setIntegerChoice(selected);
                    break;
                case "right":
                    selected = ((selected - minValue + 3) % range) + minValue;
                    data.setIntegerChoice(selected);
                    break;
                case "left":
                    selected = ((selected - minValue - 3 + range) % range) + minValue;
                    data.setIntegerChoice(selected);
                    break;
                case "enter":
                    data.confirmIntegerChoice();
                    return;
                default:
                    // Ignore unknown keys
            }
        }
    }




    public void readLine(GamePhaseData data) {
        if (context.getCurrentPhase().equals(data)) {
            data.setStringInput(lineReader.readLine());
        }
    }

    public void pressEnterToContinue(GamePhaseData data) {
        if (context.getCurrentPhase().equals(data)) {
            lineReader.readLine();
            data.updateState();
        }
    }





//    KeyMap<String> keyMap = new KeyMap<>();
//    keyMap.bind("up", "\033[A", "w", "W");
//    keyMap.bind("down", "\033[B", "s", "S");
//    keyMap.bind("left", "\033[D", "a", "A");
//    keyMap.bind("right", "\033[C", "d", "D");
//    keyMap.bind("enter", "\r", "\n");
}
