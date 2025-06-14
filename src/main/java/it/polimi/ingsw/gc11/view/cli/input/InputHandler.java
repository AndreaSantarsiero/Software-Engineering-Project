package it.polimi.ingsw.gc11.view.cli.input;

import it.polimi.ingsw.gc11.model.shipboard.ShipBoard;
import it.polimi.ingsw.gc11.view.GamePhaseData;
import it.polimi.ingsw.gc11.view.PlayerContext;
import it.polimi.ingsw.gc11.view.cli.controllers.CLIController;
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



    public void interactiveMenu(GamePhaseData data, CLIController controller, int size, int previouslySelected) {
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
                    controller.setMenuChoice(selected);
                    break;
                case "down":
                    selected = (selected + 1) % size;
                    controller.setMenuChoice(selected);
                    break;
                case "enter":
                    controller.confirmMenuChoice();
                    return;
                default:
                    // ignore any other key
            }
        }
    }



    public void interactiveHorizontalMenu(GamePhaseData data, CLIController controller, int size, int previouslySelected) {
        if (!context.getCurrentPhase().equals(data)) {
            return;
        }

        int selected = previouslySelected;

        KeyMap<String> keyMap = new KeyMap<>();
        keyMap.bind("left", "\033[D", "a", "A");    // Left
        keyMap.bind("right", "\033[C", "d", "D");   // Right
        keyMap.bind("enter", "\r", "\n");           // Enter (Windows/Linux)

        while (true) {
            String key = bindingReader.readBinding(keyMap);

            switch (key) {
                case "left":
                    selected = (selected - 1 + size) % size;
                    break;
                case "right":
                    selected = (selected + 1) % size;
                    break;
                case "enter":
                    controller.confirmIntegerChoice();
                    return;
                default:
                    // ignore any other key
            }

            controller.setIntegerChoice(selected);
        }
    }



    public void interactiveGridMenu(GamePhaseData data, CLIController controller, int size, int cols, int previouslySelected) {
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
                    controller.confirmIntegerChoice();
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

            controller.setIntegerChoice(selected);
        }
    }



    public void interactiveMatrixSelector(GamePhaseData data, CLIController controller, ShipBoard shipBoard, int previouslySelectedI, int previouslySelectedJ) {
        if (!context.getCurrentPhase().equals(data)) {
            return;
        }

        KeyMap<String> keyMap = new KeyMap<>();
        keyMap.bind("up", "\033[A", "w", "W");      // Up
        keyMap.bind("down", "\033[B", "s", "S");    // Down
        keyMap.bind("left", "\033[D", "a", "A");    // Left
        keyMap.bind("right", "\033[C", "d", "D");   // Right
        keyMap.bind("enter", "\r", "\n");           // Enter (Windows/Linux)

        int rows = shipBoard.getLength();
        int cols = shipBoard.getWidth();
        int i = previouslySelectedI;
        int j = previouslySelectedJ;

        while (true) {
            String key = bindingReader.readBinding(keyMap);
            int oldI = i;
            int oldJ = j;

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
                    controller.confirmCoordinatesChoice();
                    return;
                default:
                    // ignore any other key
            }

            boolean success = false;
            if (i > oldI){
                while(!success){
                    try {
                        success = shipBoard.validateIndexes(j, i);
                        if (!success) {
                            i = (i + 1) % rows;
                        }
                    } catch (Exception ignored){
                        i = (i + 1) % rows;
                    }
                }
            }
            else if (i < oldI){
                while(!success){
                    try {
                        success = shipBoard.validateIndexes(j, i);
                        if (!success) {
                            i = (i - 1 + rows) % rows;
                        }
                    } catch (Exception ignored){
                        i = (i - 1 + rows) % rows;
                    }
                }
            }
            else if (j > oldJ){
                while(!success){
                    try {
                        success = shipBoard.validateIndexes(j, i);
                        if (!success) {
                            j = (j + 1) % cols;
                        }
                    } catch (Exception ignored){
                        j = (j + 1) % cols;
                    }
                }
            }
            else if (j < oldJ){
                while(!success){
                    try {
                        success = shipBoard.validateIndexes(j, i);
                        if (!success) {
                            j = (j - 1 + cols) % cols;
                        }
                    } catch (Exception ignored){
                        j = (j - 1 + cols) % cols;
                    }
                }
            }

            controller.setCoordinatesChoice(j, i);
        }
    }



    public void interactiveNumberSelector(GamePhaseData data, CLIController controller, int minValue, int maxValue, int previouslySelected) {
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
                    controller.setIntegerChoice(selected);
                    break;
                case "down":
                    selected = (selected - 1 < minValue) ? maxValue : selected - 1;
                    controller.setIntegerChoice(selected);
                    break;
                case "right":
                    selected = ((selected - minValue + 3) % range) + minValue;
                    controller.setIntegerChoice(selected);
                    break;
                case "left":
                    selected = ((selected - minValue - 3 + range) % range) + minValue;
                    controller.setIntegerChoice(selected);
                    break;
                case "enter":
                    controller.confirmIntegerChoice();
                    return;
                default:
                    // Ignore unknown keys
            }
        }
    }




    public void readLine(GamePhaseData data, CLIController controller) {
        if (context.getCurrentPhase().equals(data)) {
            controller.setStringInput(lineReader.readLine());
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
