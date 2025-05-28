package it.polimi.ingsw.gc11.view.cli;

import it.polimi.ingsw.gc11.controller.ServerMAIN;
import it.polimi.ingsw.gc11.controller.network.Utils;
import it.polimi.ingsw.gc11.controller.network.client.VirtualServer;
import it.polimi.ingsw.gc11.exceptions.NetworkException;
import it.polimi.ingsw.gc11.view.GamePhaseData;
import it.polimi.ingsw.gc11.view.PlayerContext;
import it.polimi.ingsw.gc11.view.cli.input.InputHandler;
import it.polimi.ingsw.gc11.view.cli.input.InputRequest;
import it.polimi.ingsw.gc11.view.cli.templates.*;
import java.io.InputStream;
import java.util.Properties;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;



public class MainCLI {

    private String serverIp;
    private Integer serverPort;
    private VirtualServer virtualServer;
    private PlayerContext context;
    private final BlockingQueue<InputRequest> inputQueue = new LinkedBlockingQueue<>();
    private final Object shutdownMonitor = new Object();



    public void run(String[] args) {
        context = new PlayerContext();
        GamePhaseData data = context.getCurrentPhase();
        InputHandler inputHandler = new InputHandler(context);
        data.setListener(new JoiningTemplate(this));
        startInputHandler();

        try {
            parseArgs(args);
            data.notifyListener();
        } catch (Exception e) {
            System.out.println("FATAL ERROR: " + e.getMessage());
            System.out.println("Aborting...");
            System.exit(0);
        }

        synchronized (shutdownMonitor) {
            while (context.isAlive()) {
                try {
                    shutdownMonitor.wait();
                } catch (InterruptedException ignored) {}
            }
        }
        System.out.println("Game ended. Exiting...");
    }



    public void shutdown() {
        synchronized (shutdownMonitor) {
            shutdownMonitor.notify();
        }
    }




    private void startInputHandler() {
        InputHandler inputHandler = new InputHandler(context);

        Thread inputThread = new Thread(() -> {
            while (true) {
                try {
                    InputRequest request = inputQueue.take();
                    request.execute(inputHandler);
                }
                catch (Exception e) {
                    System.err.println("[InputHandlerThread] Errore durante la gestione input:");
                    e.printStackTrace();
                }
            }
        }, "InputHandlerThread");

        inputThread.setDaemon(true);
        inputThread.start();
    }



    public void addInputRequest(InputRequest request) {
        inputQueue.add(request);
    }



    public void virtualServerSetup(int connectionChoice) throws NetworkException {
        Utils.ConnectionType connectionType = connectionTypeSetup(connectionChoice);
        boolean defaultAddress = false;

        try (InputStream input = ServerMAIN.class.getClassLoader().getResourceAsStream("config.properties")) {
            Properties prop = new Properties();
            prop.load(input);

            if (serverIp == null) {
                serverIp = prop.getProperty("serverIp");
                defaultAddress = true;
            }
            if (serverPort == null) {
                if (connectionType == Utils.ConnectionType.RMI) {
                    serverPort = Integer.parseInt(prop.getProperty("serverRMIPort"));
                } else {
                    serverPort = Integer.parseInt(prop.getProperty("serverSocketPort"));
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("error loading config.properties: " + e.getMessage());
        }

        if (defaultAddress) {
            System.out.println("Using default address " + serverIp + ":" + serverPort);
        } else {
            System.out.println("Using custom address " + serverIp + ":" + serverPort);
        }

        virtualServer = new VirtualServer(connectionType, serverIp, serverPort, context);
    }

    public VirtualServer getVirtualServer() {
        return virtualServer;
    }

    public PlayerContext getContext() {
        return context;
    }



    public Utils.ConnectionType connectionTypeSetup(int choice) {
        if (choice == 0) {
            return Utils.ConnectionType.RMI;
        }
        else {
            return Utils.ConnectionType.SOCKET;
        }
    }



    private void parseArgs(String[] args) {
        boolean cliMode = false;

        for (int i = 0; i < args.length; i++) {
            String arg = args[i];

            if (arg.equals("-cli")) {
                if (cliMode) {
                    throw new RuntimeException("Invalid argument order: -cli can only appear once.");
                }
                cliMode = true;
            }
            else if (serverIp == null) {
                serverIp = arg;
            }
            else if (serverPort == null) {
                if (!args[i - 1].equals(serverIp)) {
                    throw new RuntimeException("Invalid argument order: port should come after IP.");
                }
                try {
                    serverPort = Integer.parseInt(arg);
                } catch (NumberFormatException e) {
                    throw new RuntimeException("Invalid port number: " + arg);
                }
            }
            else {
                throw new RuntimeException("Too many arguments. Usage: java GalaxyTrucker [-cli] [server-ip] [server-port]");
            }
        }

        if (!cliMode) {
            throw new RuntimeException("Missing required flag: -cli");
        }
    }



    public void changeTemplate(JoiningTemplate joiningTemplate) {
        GamePhaseData data = context.getCurrentPhase();
        data.setListener(new BuildingTemplate(this));
    }

    public void changeTemplate(BuildingTemplate buildingTemplate) {
        GamePhaseData data = context.getCurrentPhase();
        data.setListener(new CheckTemplate(this));
    }

    public void changeTemplate(CheckTemplate checkTemplate) {
        GamePhaseData data = context.getCurrentPhase();
        data.setListener(new AdventureTemplate(this));
    }

    public void changeTemplate(AdventureTemplate adventureTemplate) {
        GamePhaseData data = context.getCurrentPhase();
        data.setListener(new EndTemplate(this));
    }

    public void changeTemplate(EndTemplate endTemplate) {
        GamePhaseData data = context.getCurrentPhase();
        data.setListener(new JoiningTemplate(this));
    }
}
