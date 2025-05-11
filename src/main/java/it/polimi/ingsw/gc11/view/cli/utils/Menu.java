package it.polimi.ingsw.gc11.view.cli.utils;

import com.github.kwhat.jnativehook.GlobalScreen;
import com.github.kwhat.jnativehook.NativeHookException;
import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;
import com.github.kwhat.jnativehook.keyboard.NativeKeyListener;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.Level;
import java.util.logging.Logger;
import static it.polimi.ingsw.gc11.view.cli.MainCLI.functionKey;
import static it.polimi.ingsw.gc11.view.cli.MainCLI.otherFunctionKeys;



public class Menu {

    public static AtomicBoolean isTerminalInFocus = new AtomicBoolean(false);



    public static int interactiveMenu(String title, List<String> options) {
        AtomicInteger selected = new AtomicInteger(0);
        AtomicInteger previouslySelected = new AtomicInteger(-1);
        AtomicBoolean confirmed = new AtomicBoolean(false);

        NativeKeyListener listener = new NativeKeyListener() {
            @Override
            public void nativeKeyPressed(NativeKeyEvent e) {
                int keyCode = e.getKeyCode();

                if (functionKey != null && keyCode == functionKey) {
                    isTerminalInFocus.set(true);
                    return;
                }

                if (functionKey != null && otherFunctionKeys.contains(keyCode)) {
                    isTerminalInFocus.set(false);
                    return;
                }

                if (!isTerminalInFocus.get()) {
                    return;
                }

                switch (keyCode) {
                    case NativeKeyEvent.VC_UP -> selected.set((selected.get() - 1 + options.size()) % options.size());
                    case NativeKeyEvent.VC_DOWN -> selected.set((selected.get() + 1) % options.size());
                    case NativeKeyEvent.VC_ENTER -> confirmed.set(true);
                }
            }

            @Override public void nativeKeyReleased(NativeKeyEvent e) {}
            @Override public void nativeKeyTyped(NativeKeyEvent e) {}
        };

        try {
            Logger logger = Logger.getLogger(GlobalScreen.class.getPackage().getName());
            logger.setLevel(Level.OFF);
            GlobalScreen.registerNativeHook();
            GlobalScreen.addNativeKeyListener(listener);

            while (!confirmed.get()) {
                if (previouslySelected.get() != selected.get()) {
                    renderMenu(title, options, selected.get());
                    previouslySelected.set(selected.get());
                }
            }

            return selected.get();

        } catch (NativeHookException e) {
            System.out.println(e.getMessage());
            return -1;
        } finally {
            try {
                GlobalScreen.removeNativeKeyListener(listener);
                GlobalScreen.unregisterNativeHook();
                clearStdin();
            } catch (NativeHookException ignored) {}
        }
    }



    public static String readLine(String prompt) {
        System.out.print(prompt);

        AtomicReference<StringBuilder> inputBuilder = new AtomicReference<>(new StringBuilder());
        AtomicBoolean isCompleted = new AtomicBoolean(false);

        NativeKeyListener listener = new NativeKeyListener() {
            @Override
            public void nativeKeyPressed(NativeKeyEvent e) {
                int keyCode = e.getKeyCode();

                if (keyCode == NativeKeyEvent.VC_ENTER) {
                    isCompleted.set(true);
                    System.out.println();
                }
                else if (keyCode == NativeKeyEvent.VC_BACKSPACE) {
                    if (!inputBuilder.get().isEmpty()) {
                        inputBuilder.get().deleteCharAt(inputBuilder.get().length() - 1);
                        System.out.print("\b \b");
                    }
                }
                else {
                    char keyChar = e.getKeyChar();

                    if (keyChar >= 32 && keyChar <= 126) {
                        inputBuilder.get().append(keyChar);
                        System.out.print(keyChar);
                    }
                }
            }

            @Override public void nativeKeyReleased(NativeKeyEvent e) {}
            @Override public void nativeKeyTyped(NativeKeyEvent e) {}
        };

        try {
            GlobalScreen.registerNativeHook();
            GlobalScreen.addNativeKeyListener(listener);

            while (!isCompleted.get()) {
                Thread.sleep(50); // avoid busy waiting
            }
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                GlobalScreen.removeNativeKeyListener(listener);
                GlobalScreen.unregisterNativeHook();
                clearStdin();
            } catch (NativeHookException ignored) {}
        }

        return inputBuilder.get().toString();
    }



    private static void renderMenu(String title, List<String> options, int selected) {
        clearView();
        if(title != null && !title.isEmpty()) {
            System.out.println(title + " (↑↓ and press Enter):");
        }
        for (int i = 0; i < options.size(); i++) {
            if (i == selected) {
                System.out.println("  > \u001b[48;5;235m" + options.get(i) + "\u001b[0m");
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



    public static void clearStdin() {
        try {
            while (System.in.available() > 0) {
                System.in.read();
            }
        } catch (Exception ignored) {}
    }
}
