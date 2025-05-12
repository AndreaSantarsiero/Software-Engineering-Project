package it.polimi.ingsw.gc11.view.cli.utils;

import com.github.kwhat.jnativehook.GlobalScreen;
import com.github.kwhat.jnativehook.NativeHookException;
import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;
import com.github.kwhat.jnativehook.keyboard.NativeKeyListener;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;
import static it.polimi.ingsw.gc11.view.cli.MainCLI.functionKey;
import static it.polimi.ingsw.gc11.view.cli.MainCLI.otherFunctionKeys;



public class Menu {

    public static AtomicBoolean isTerminalInFocus = new AtomicBoolean(false);



    public static void initialize() throws NativeHookException {
        Logger logger = Logger.getLogger(GlobalScreen.class.getPackage().getName());
        logger.setLevel(Level.OFF);
        GlobalScreen.registerNativeHook();
    }



    public static int interactiveMenu(String title, List<String> options) {
        AtomicInteger selected = new AtomicInteger(0);
        AtomicInteger previouslySelected = new AtomicInteger(-1);
        AtomicBoolean confirmed = new AtomicBoolean(false);
        ReentrantLock lock = new ReentrantLock();
        Condition change = lock.newCondition();

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

                lock.lock();
                try {
                    switch (keyCode) {
                        case NativeKeyEvent.VC_UP:
                            selected.set((selected.get() - 1 + options.size()) % options.size());
                            change.signal();
                            break;
                        case NativeKeyEvent.VC_DOWN:
                            selected.set((selected.get() + 1) % options.size());
                            change.signal();
                            break;
                        case NativeKeyEvent.VC_ENTER:
                            confirmed.set(true);
                            change.signal();
                            break;
                    }
                } finally {
                    lock.unlock();
                }
            }

            @Override public void nativeKeyReleased(NativeKeyEvent e) {}
            @Override public void nativeKeyTyped(NativeKeyEvent e) {}
        };

        GlobalScreen.addNativeKeyListener(listener);

        lock.lock();
        try {
            renderMenu(title, options, selected.get());
            previouslySelected.set(selected.get());

            while (!confirmed.get()) {
                change.await();  // wait for change
                if (previouslySelected.get() != selected.get()) {
                    renderMenu(title, options, selected.get());
                    previouslySelected.set(selected.get());
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            lock.unlock();
            GlobalScreen.removeNativeKeyListener(listener);
        }

        return selected.get();
    }



    public static String readLine(String message) {
        System.out.print(message);
        AtomicReference<StringBuilder> inputBuilder = new AtomicReference<>(new StringBuilder());
        CountDownLatch latch = new CountDownLatch(1);

        NativeKeyListener listener = new NativeKeyListener() {
            @Override
            public void nativeKeyPressed(NativeKeyEvent e) {
                int keyCode = e.getKeyCode();

                if (functionKey != null && keyCode == functionKey) {
                    isTerminalInFocus.set(true);
                }
                else if (functionKey != null && otherFunctionKeys.contains(keyCode)) {
                    isTerminalInFocus.set(false);
                }
            }

            @Override
            public void nativeKeyReleased(NativeKeyEvent e) {}

            @Override
            public void nativeKeyTyped(NativeKeyEvent e) {
                if (!isTerminalInFocus.get()) {
                    return;
                }

                char keyChar = e.getKeyChar();
                switch (keyChar) {
                    case '\n':
                    case '\r':
                        latch.countDown();
                        System.out.println();
                        break;
                    case '\b':
                        if (!inputBuilder.get().isEmpty()) {
                            inputBuilder.get().deleteCharAt(inputBuilder.get().length() - 1);
                            System.out.print("\b \b");
                        }
                        break;
                    default:
                        if (keyChar >= 32 && keyChar <= 126) {
                            inputBuilder.get().append(keyChar);
                            System.out.print(keyChar);
                        }
                        break;
                }
            }
        };

        GlobalScreen.addNativeKeyListener(listener);

        try {
            latch.await();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        GlobalScreen.removeNativeKeyListener(listener);
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
}
