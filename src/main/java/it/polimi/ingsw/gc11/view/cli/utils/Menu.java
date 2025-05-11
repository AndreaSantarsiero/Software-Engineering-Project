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
        AtomicInteger previouslySelected = new AtomicInteger(-1);
        AtomicBoolean confirmed = new AtomicBoolean(false);

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
            Logger logger = Logger.getLogger(GlobalScreen.class.getPackage().getName());
            logger.setLevel(Level.OFF);
            GlobalScreen.registerNativeHook();
            GlobalScreen.addNativeKeyListener(listener);

            while (!confirmed.get()) {
                if (previouslySelected.get() != selected.get()){
                    renderMenu(options, selected.get());
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
            } catch (NativeHookException ignored) {}
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



    public static void clearView(){
        System.out.print("\u001b[H\u001b[2J");
        System.out.flush();
        System.out.println("***    Galaxy Truckers    ***\n");
    }
}
