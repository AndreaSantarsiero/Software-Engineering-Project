package it.polimi.ingsw.gc11.view.cli.utils;

import com.github.kwhat.jnativehook.GlobalScreen;
import com.github.kwhat.jnativehook.NativeHookException;
import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;
import com.github.kwhat.jnativehook.keyboard.NativeKeyListener;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;



public class Menu {
    public static int interactiveMenu(List<String> options) {
        AtomicInteger selected = new AtomicInteger(0);
        AtomicBoolean confirmed = new AtomicBoolean(false);

        // Listener dei tasti
        NativeKeyListener listener = new NativeKeyListener() {
            @Override
            public void nativeKeyPressed(NativeKeyEvent e) {
                switch (e.getKeyCode()) {
                    case NativeKeyEvent.VC_UP -> selected.set((selected.get() - 1 + options.size()) % options.size());
                    case NativeKeyEvent.VC_DOWN -> selected.set((selected.get() + 1) % options.size());
                    case NativeKeyEvent.VC_ENTER -> confirmed.set(true);
                }
            }

            @Override public void nativeKeyReleased(NativeKeyEvent e) {}
            @Override public void nativeKeyTyped(NativeKeyEvent e) {}
        };

        try {
            // Disattiva logging JNativeHook
            Logger logger = Logger.getLogger(GlobalScreen.class.getPackage().getName());
            logger.setLevel(Level.OFF);
            GlobalScreen.registerNativeHook();
            GlobalScreen.addNativeKeyListener(listener);

            // Loop di rendering
            while (!confirmed.get()) {
                renderMenu(options, selected.get());
                Thread.sleep(100); // per ridurre il flickering
            }

            return selected.get();

        } catch (NativeHookException | InterruptedException e) {
            e.printStackTrace();
            return -1;
        } finally {
            try {
                GlobalScreen.removeNativeKeyListener(listener);
                GlobalScreen.unregisterNativeHook();
            } catch (NativeHookException ignored) {}
        }
    }



    // Metodo per stampare il menu
    private static void renderMenu(List<String> options, int selected) {
        System.out.print("\u001b[H\u001b[2J"); // pulisci schermo ANSI
        System.out.flush();
        System.out.println("Seleziona un'opzione (↑↓ e premi Invio):\n");
        for (int i = 0; i < options.size(); i++) {
            if (i == selected) {
                System.out.println("  > \u001b[47m" + options.get(i) + "\u001b[0m");
            } else {
                System.out.println("    " + options.get(i));
            }
        }
    }



    public static void clearView(){
        System.out.print("\033[H\033[2J");
        System.out.flush();
        System.out.println("***    Galaxy Truckers    ***\n");
    }
}
