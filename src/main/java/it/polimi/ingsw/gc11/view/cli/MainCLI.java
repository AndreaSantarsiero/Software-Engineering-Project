package it.polimi.ingsw.gc11.view.cli;

import it.polimi.ingsw.gc11.controller.ServerMAIN;
import it.polimi.ingsw.gc11.network.Utils;
import it.polimi.ingsw.gc11.network.client.VirtualServer;
import it.polimi.ingsw.gc11.exceptions.NetworkException;
import it.polimi.ingsw.gc11.view.*;
import it.polimi.ingsw.gc11.view.cli.controllers.*;
import it.polimi.ingsw.gc11.view.cli.input.InputHandler;
import it.polimi.ingsw.gc11.view.cli.input.InputRequest;
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
        data.setListener(new JoiningController(this, (JoiningPhaseData) data));
        startInputHandler(inputHandler);

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




    private void startInputHandler(InputHandler inputHandler) {
        Thread inputThread = new Thread(() -> {
            while (true) {
                try {
                    InputRequest request = inputQueue.take();
                    request.execute(inputHandler);
                    //System.out.println("[InputHandlerThread] Request: " + request + " executed. " + inputQueue.size() + " requests left");
                }
                catch (Exception e) {
                    System.err.println("[InputHandlerThread] Error during input setup: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        }, "InputHandlerThread");

        inputThread.setDaemon(true);
        inputThread.start();
    }



    public void addInputRequest(InputRequest request) {
        inputQueue.add(request);
        //System.out.println("[InputHandlerThread] Added request: " + request);
    }



    public void virtualServerSetup(JoiningPhaseData joiningPhaseData, int connectionChoice) throws NetworkException {
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

        virtualServer = new VirtualServer(context);
        joiningPhaseData.setVirtualServer(virtualServer);
        virtualServer.initializeConnection(connectionType, serverIp, serverPort);
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



    public void changeController() {
        GamePhaseData newPhase = context.getCurrentPhase();

        if (newPhase.isJoiningPhase()){
            newPhase.setListener(new JoiningController(this, (JoiningPhaseData) newPhase));
        }
        else if (newPhase.isBuildingPhase()){
            newPhase.setListener(new BuildingController(this, (BuildingPhaseData) newPhase));
        }
        else if (newPhase.isCheckPhase()){
            newPhase.setListener(new CheckController(this, (CheckPhaseData) newPhase));
        }
        else if (newPhase.isAdventurePhase()){
            newPhase.setListener(new AdventureController(this, (AdventurePhaseData) newPhase));
        }
        else if (newPhase.isEndPhase()){
            newPhase.setListener(new EndController(this, (EndPhaseData) newPhase));
        }
        else {
            throw new RuntimeException("Unknown phase type: " + newPhase.getClass().getName());
        }
    }
}
