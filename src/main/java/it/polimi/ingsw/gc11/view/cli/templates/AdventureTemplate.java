package it.polimi.ingsw.gc11.view.cli.templates;

import it.polimi.ingsw.gc11.model.Player;
import it.polimi.ingsw.gc11.model.shipboard.ShipBoard;
import it.polimi.ingsw.gc11.view.AdventurePhaseData;
import it.polimi.ingsw.gc11.view.cli.controllers.AdventureController;
import it.polimi.ingsw.gc11.view.cli.utils.FlightBoardCLI;
import it.polimi.ingsw.gc11.view.cli.utils.ShipCardCLI;
import org.fusesource.jansi.Ansi;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



public class AdventureTemplate extends CLITemplate {

    private final AdventureController controller;
    private static final int rowCount = 11;
    private static final List<List<String>> mainMenu = List.of(
            List.of("┌┬┐┌─┐┬┌─┌─┐  ┌┐┌┌─┐┬ ┬  ┌─┐┌┬┐┬  ┬┌─┐┌┐┌┌┬┐┬ ┬┬─┐┌─┐  ┌─┐┌─┐┬─┐┌┬┐",
                    " │ ├─┤├┴┐├┤   │││├┤ │││  ├─┤ ││└┐┌┘├┤ │││ │ │ │├┬┘├┤   │  ├─┤├┬┘ ││",
                    " ┴ ┴ ┴┴ ┴└─┘  ┘└┘└─┘└┴┘  ┴ ┴─┴┘ └┘ └─┘┘└┘ ┴ └─┘┴└─└─┘  └─┘┴ ┴┴└──┴┘"),
            List.of("┌─┐┌─┐┌─┐┌─┐┌─┐┌┬┐  ┌─┐┌┬┐┬  ┬┌─┐┌┐┌┌┬┐┬ ┬┬─┐┌─┐  ┌─┐┌─┐┬─┐┌┬┐",
                    "├─┤│  │  ├┤ ├─┘ │   ├─┤ ││└┐┌┘├┤ │││ │ │ │├┬┘├┤   │  ├─┤├┬┘ ││",
                    "┴ ┴└─┘└─┘└─┘┴   ┴   ┴ ┴─┴┘ └┘ └─┘┘└┘ ┴ └─┘┴└─└─┘  └─┘┴ ┴┴└──┴┘"),
            List.of("┌┬┐┌─┐┌─┐┬  ┬┌┐┌┌─┐  ┌─┐┌┬┐┬  ┬┌─┐┌┐┌┌┬┐┬ ┬┬─┐┌─┐  ┌─┐┌─┐┬─┐┌┬┐",
                    " ││├┤ │  │  ││││├┤   ├─┤ ││└┐┌┘├┤ │││ │ │ │├┬┘├┤   │  ├─┤├┬┘ ││",
                    "─┴┘└─┘└─┘┴─┘┴┘└┘└─┘  ┴ ┴─┴┘ └┘ └─┘┘└┘ ┴ └─┘┴└─└─┘  └─┘┴ ┴┴└──┴┘"),
            List.of("┬─┐┌─┐┌─┐┌─┐┬ ┬  ┬┌─┐  ┌─┐┌┬┐┬  ┬┌─┐┌┐┌┌┬┐┬ ┬┬─┐┌─┐  ┌─┐┌─┐┬─┐┌┬┐",
                    "├┬┘├┤ └─┐│ ││ └┐┌┘├┤   ├─┤ ││└┐┌┘├┤ │││ │ │ │├┬┘├┤   │  ├─┤├┬┘ ││",
                    "┴└─└─┘└─┘└─┘┴─┘└┘ └─┘  ┴ ┴─┴┘ └┘ └─┘┘└┘ ┴ └─┘┴└─└─┘  └─┘┴ ┴┴└──┴┘"),
            List.of("┌─┐┌─┐┌─┐  ┌─┐┌┐┌┌─┐┌┬┐┬┌─┐┌─┐  ┌─┐┬ ┬┬┌─┐",
                    "└─┐├┤ ├┤   ├┤ │││├┤ ││││├┤ └─┐  └─┐├─┤│├─┘",
                    "└─┘└─┘└─┘  └─┘┘└┘└─┘┴ ┴┴└─┘└─┘  └─┘┴ ┴┴┴  "),
            List.of("┌─┐┌┐ ┌─┐┬─┐┌┬┐  ┌─┐┬  ┬┌─┐┬ ┬┌┬┐",
                    "├─┤├┴┐│ │├┬┘ │   ├┤ │  ││ ┬├─┤ │ ",
                    "┴ ┴└─┘└─┘┴└─ ┴   └  ┴─┘┴└─┘┴ ┴ ┴ ")
    );
    private static final List<List<String>> actionMenu = List.of(
            List.of("┌─┐┌─┐┌┐┌┌┬┐  ┌─┐┬┬─┐┌─┐  ┌─┐┌─┐┬ ┬┌─┐┬─┐",
                    "└─┐├┤ │││ ││  ├┤ │├┬┘├┤   ├─┘│ ││││├┤ ├┬┘",
                    "└─┘└─┘┘└┘─┴┘  └  ┴┴└─└─┘  ┴  └─┘└┴┘└─┘┴└─"),
            List.of("┌─┐┌─┐┌┐┌┌┬┐  ┌─┐┌┐┌┌─┐┬┌┐┌┌─┐  ┌─┐┌─┐┬ ┬┌─┐┬─┐",
                    "└─┐├┤ │││ ││  ├┤ ││││ ┬││││├┤   ├─┘│ ││││├┤ ├┬┘",
                    "└─┘└─┘┘└┘─┴┘  └─┘┘└┘└─┘┴┘└┘└─┘  ┴  └─┘└┴┘└─┘┴└─"),
            List.of("┌─┐┌─┐┌┐┌┌┬┐  ┌─┐┬─┐┌─┐┬ ┬  ┌┬┐┌─┐┌┬┐┌┐ ┌─┐┬─┐┌─┐",
                    "└─┐├┤ │││ ││  │  ├┬┘├┤ │││  │││├┤ │││├┴┐├┤ ├┬┘└─┐",
                    "└─┘└─┘┘└┘─┴┘  └─┘┴└─└─┘└┴┘  ┴ ┴└─┘┴ ┴└─┘└─┘┴└─└─┘"),
            List.of("┌─┐┌─┐┌┐┌┌┬┐  ┌┐ ┌─┐┌┬┐┌┬┐┌─┐┬─┐┬┌─┐┌─┐",
                    "└─┐├┤ │││ ││  ├┴┐├─┤ │  │ ├┤ ├┬┘│├┤ └─┐",
                    "└─┘└─┘┘└┘─┴┘  └─┘┴ ┴ ┴  ┴ └─┘┴└─┴└─┘└─┘"),
            List.of("┬  ┌─┐┌─┐┌┬┐  ┌┐┌┌─┐┬ ┬  ┌┬┐┌─┐┌┬┐┌─┐┬─┐┬┌─┐┬  ┌─┐",
                    "│  │ │├─┤ ││  │││├┤ │││  │││├─┤ │ ├┤ ├┬┘│├─┤│  └─┐",
                    "┴─┘└─┘┴ ┴─┴┘  ┘└┘└─┘└┴┘  ┴ ┴┴ ┴ ┴ └─┘┴└─┴┴ ┴┴─┘└─┘"),
            List.of("┌─┐┌─┐┌┐┌┌┬┐  ┌─┐┬ ┬┌─┐┌┬┐  ┌┬┐┌─┐┌─┐┌─┐┌┐┌┌─┐┌─┐",
                    "└─┐├┤ │││ ││  └─┐├─┤│ │ │    ││├┤ ├┤ ├┤ │││└─┐├┤ ",
                    "└─┘└─┘┘└┘─┴┘  └─┘┴ ┴└─┘ ┴   ─┴┘└─┘└  └─┘┘└┘└─┘└─┘"),
            List.of("┌─┐┌─┐┌┐┌┌┬┐  ┌┬┐┌─┐┌┬┐┌─┐┌─┐┬─┐  ┌┬┐┌─┐┌─┐┌─┐┌┐┌┌─┐┌─┐",
                    "└─┐├┤ │││ ││  │││├┤  │ ├┤ │ │├┬┘   ││├┤ ├┤ ├┤ │││└─┐├┤ ",
                    "└─┘└─┘┘└┘─┴┘  ┴ ┴└─┘ ┴ └─┘└─┘┴└─  ─┴┘└─┘└  └─┘┘└┘└─┘└─┘"),
            List.of("┌─┐┌┬┐┬  ┬┌─┐┌┐┌┌─┐┌─┐┌┬┐  ┌┬┐┌─┐┌┐┌┬ ┬",
                    "├─┤ ││└┐┌┘├─┤││││  ├┤  ││  │││├┤ ││││ │",
                    "┴ ┴─┴┘ └┘ ┴ ┴┘└┘└─┘└─┘─┴┘  ┴ ┴└─┘┘└┘└─┘"),
            List.of("┌┐ ┌─┐┌─┐┬┌─  ┌┬┐┌─┐  ┌─┐┬─┐┌─┐┬  ┬┬┌─┐┬ ┬┌─┐  ┌┬┐┌─┐┌┐┌┬ ┬",
                    "├┴┐├─┤│  ├┴┐   │ │ │  ├─┘├┬┘├┤ └┐┌┘││ ││ │└─┐  │││├┤ ││││ │",
                    "└─┘┴ ┴└─┘┴ ┴   ┴ └─┘  ┴  ┴└─└─┘ └┘ ┴└─┘└─┘└─┘  ┴ ┴└─┘┘└┘└─┘")
    );

    private static final List<List<String>> advancedActionMenu = List.of(
            List.of("┌┬┐┬ ┬┬─┐┌─┐┬ ┬  ┌┬┐┬┌─┐┌─┐┌─┐",
                    " │ ├─┤├┬┘│ ││││   ││││  ├┤ └─┐",
                    " ┴ ┴ ┴┴└─└─┘└┴┘  ─┴┘┴└─┘└─┘└─┘"),
            List.of("┌─┐┬ ┬┌─┐┌─┐┌─┐┌─┐  ┌─┐┬  ┌─┐┌┐┌┌─┐┌┬┐",
                    "│  ├─┤│ ││ │└─┐├┤   ├─┘│  ├─┤│││├┤  │ ",
                    "└─┘┴ ┴└─┘└─┘└─┘└─┘  ┴  ┴─┘┴ ┴┘└┘└─┘ ┴ "),
            List.of("┌─┐┌─┐┌─┐┌─┐┌─┐┌┬┐  ┬─┐┌─┐┬ ┬┌─┐┬─┐┌┬┐",
                    "├─┤│  │  ├┤ ├─┘ │   ├┬┘├┤ │││├─┤├┬┘ ││",
                    "┴ ┴└─┘└─┘└─┘┴   ┴   ┴└─└─┘└┴┘┴ ┴┴└──┴┘"),
            List.of("┬─┐┌─┐┌─┐┬ ┬┌─┐┌─┐  ┬─┐┌─┐┬ ┬┌─┐┬─┐┌┬┐",
                    "├┬┘├┤ ├┤ │ │└─┐├┤   ├┬┘├┤ │││├─┤├┬┘ ││",
                    "┴└─└─┘└  └─┘└─┘└─┘  ┴└─└─┘└┴┘┴ ┴┴└──┴┘"),
            List.of("┌┐ ┌─┐┌─┐┬┌─  ┌┬┐┌─┐  ┌─┐┬─┐┌─┐┬  ┬┬┌─┐┬ ┬┌─┐  ┌┬┐┌─┐┌┐┌┬ ┬",
                    "├┴┐├─┤│  ├┴┐   │ │ │  ├─┘├┬┘├┤ └┐┌┘││ ││ │└─┐  │││├┤ ││││ │",
                    "└─┘┴ ┴└─┘┴ ┴   ┴ └─┘  ┴  ┴└─└─┘ └┘ ┴└─┘└─┘└─┘  ┴ ┴└─┘┘└┘└─┘")
    );
    private static final List<List<String>> firePowerMenu = List.of(
            List.of("┌─┐┌┬┐┌┬┐  ┌┬┐┌─┐┬ ┬┌┐ ┬  ┌─┐  ┌─┐┌─┐┌┐┌┌┐┌┌─┐┌┐┌",
                    "├─┤ ││ ││   │││ ││ │├┴┐│  ├┤   │  ├─┤│││││││ ││││",
                    "┴ ┴─┴┘─┴┘  ─┴┘└─┘└─┘└─┘┴─┘└─┘  └─┘┴ ┴┘└┘┘└┘└─┘┘└┘"),
            List.of("┌─┐┌┬┐┌┬┐  ┌┐ ┌─┐┌┬┐┌┬┐┌─┐┬─┐┬┌─┐┌─┐",
                    "├─┤ ││ ││  ├┴┐├─┤ │  │ ├┤ ├┬┘│├┤ └─┐",
                    "┴ ┴─┴┘─┴┘  └─┘┴ ┴ ┴  ┴ └─┘┴└─┴└─┘└─┘"),
            List.of("┌─┐┌─┐┌┐┌┌┬┐  ┬─┐┌─┐┌─┐┌─┐┌─┐┌┐┌┌─┐┌─┐",
                    "└─┐├┤ │││ ││  ├┬┘├┤ └─┐├─┘│ ││││└─┐├┤ ",
                    "└─┘└─┘┘└┘─┴┘  ┴└─└─┘└─┘┴  └─┘┘└┘└─┘└─┘"),
            List.of("┬─┐┌─┐┌─┐┌─┐┌┬┐  ┬─┐┌─┐┌─┐┌─┐┌─┐┌┐┌┌─┐┌─┐",
                    "├┬┘├┤ └─┐├┤  │   ├┬┘├┤ └─┐├─┘│ ││││└─┐├┤ ",
                    "┴└─└─┘└─┘└─┘ ┴   ┴└─└─┘└─┘┴  └─┘┘└┘└─┘└─┘"),
            List.of("┌┐ ┌─┐┌─┐┬┌─  ┌┬┐┌─┐  ┌─┐┬─┐┌─┐┬  ┬┬┌─┐┬ ┬┌─┐  ┌┬┐┌─┐┌┐┌┬ ┬",
                    "├┴┐├─┤│  ├┴┐   │ │ │  ├─┘├┬┘├┤ └┐┌┘││ ││ │└─┐  │││├┤ ││││ │",
                    "└─┘┴ ┴└─┘┴ ┴   ┴ └─┘  ┴  ┴└─└─┘ └┘ ┴└─┘└─┘└─┘  ┴ ┴└─┘┘└┘└─┘")
    );
    private static final List<List<String>> enginePowerMenu = List.of(
            List.of("┌─┐┌┬┐┌┬┐  ┌┐ ┌─┐┌┬┐┌┬┐┌─┐┬─┐┬┌─┐┌─┐",
                    "├─┤ ││ ││  ├┴┐├─┤ │  │ ├┤ ├┬┘│├┤ └─┐",
                    "┴ ┴─┴┘─┴┘  └─┘┴ ┴ ┴  ┴ └─┘┴└─┴└─┘└─┘"),
            List.of("┌─┐┌─┐┌┐┌┌┬┐  ┬─┐┌─┐┌─┐┌─┐┌─┐┌┐┌┌─┐┌─┐",
                    "└─┐├┤ │││ ││  ├┬┘├┤ └─┐├─┘│ ││││└─┐├┤ ",
                    "└─┘└─┘┘└┘─┴┘  ┴└─└─┘└─┘┴  └─┘┘└┘└─┘└─┘"),
            List.of("┬─┐┌─┐┌─┐┌─┐┌┬┐  ┬─┐┌─┐┌─┐┌─┐┌─┐┌┐┌┌─┐┌─┐",
                    "├┬┘├┤ └─┐├┤  │   ├┬┘├┤ └─┐├─┘│ ││││└─┐├┤ ",
                    "┴└─└─┘└─┘└─┘ ┴   ┴└─└─┘└─┘┴  └─┘┘└┘└─┘└─┘"),
            List.of("┌┐ ┌─┐┌─┐┬┌─  ┌┬┐┌─┐  ┌─┐┬─┐┌─┐┬  ┬┬┌─┐┬ ┬┌─┐  ┌┬┐┌─┐┌┐┌┬ ┬",
                    "├┴┐├─┤│  ├┴┐   │ │ │  ├─┘├┬┘├┤ └┐┌┘││ ││ │└─┐  │││├┤ ││││ │",
                    "└─┘┴ ┴└─┘┴ ┴   ┴ └─┘  ┴  ┴└─└─┘ └┘ ┴└─┘└─┘└─┘  ┴ ┴└─┘┘└┘└─┘")
    );
    private static final List<List<String>> crewMembersMenu = List.of(
            List.of("┌─┐┌┬┐┌┬┐  ┌─┐┬─┐┌─┐┬ ┬  ┌┬┐┌─┐┌┬┐┌┐ ┌─┐┬─┐┌─┐",
                    "├─┤ ││ ││  │  ├┬┘├┤ │││  │││├┤ │││├┴┐├┤ ├┬┘└─┐",
                    "┴ ┴─┴┘─┴┘  └─┘┴└─└─┘└┴┘  ┴ ┴└─┘┴ ┴└─┘└─┘┴└─└─┘"),
            List.of("┌─┐┌─┐┌┐┌┌┬┐  ┬─┐┌─┐┌─┐┌─┐┌─┐┌┐┌┌─┐┌─┐",
                    "└─┐├┤ │││ ││  ├┬┘├┤ └─┐├─┘│ ││││└─┐├┤ ",
                    "└─┘└─┘┘└┘─┴┘  ┴└─└─┘└─┘┴  └─┘┘└┘└─┘└─┘"),
            List.of("┬─┐┌─┐┌─┐┌─┐┌┬┐  ┬─┐┌─┐┌─┐┌─┐┌─┐┌┐┌┌─┐┌─┐",
                    "├┬┘├┤ └─┐├┤  │   ├┬┘├┤ └─┐├─┘│ ││││└─┐├┤ ",
                    "┴└─└─┘└─┘└─┘ ┴   ┴└─└─┘└─┘┴  └─┘┘└┘└─┘└─┘"),
            List.of("┌┐ ┌─┐┌─┐┬┌─  ┌┬┐┌─┐  ┌─┐┬─┐┌─┐┬  ┬┬┌─┐┬ ┬┌─┐  ┌┬┐┌─┐┌┐┌┬ ┬",
                    "├┴┐├─┤│  ├┴┐   │ │ │  ├─┘├┬┘├┤ └┐┌┘││ ││ │└─┐  │││├┤ ││││ │",
                    "└─┘┴ ┴└─┘┴ ┴   ┴ └─┘  ┴  ┴└─└─┘ └┘ ┴└─┘└─┘└─┘  ┴ ┴└─┘┘└┘└─┘")
    );
    private static final List<List<String>> batteriesMenu = List.of(
            List.of("┌─┐┌┬┐┌┬┐  ┌┐ ┌─┐┌┬┐┌┬┐┌─┐┬─┐┬┌─┐┌─┐",
                    "├─┤ ││ ││  ├┴┐├─┤ │  │ ├┤ ├┬┘│├┤ └─┐",
                    "┴ ┴─┴┘─┴┘  └─┘┴ ┴ ┴  ┴ └─┘┴└─┴└─┘└─┘"),
            List.of("┌─┐┌─┐┌┐┌┌┬┐  ┬─┐┌─┐┌─┐┌─┐┌─┐┌┐┌┌─┐┌─┐",
                    "└─┐├┤ │││ ││  ├┬┘├┤ └─┐├─┘│ ││││└─┐├┤ ",
                    "└─┘└─┘┘└┘─┴┘  ┴└─└─┘└─┘┴  └─┘┘└┘└─┘└─┘"),
            List.of("┬─┐┌─┐┌─┐┌─┐┌┬┐  ┬─┐┌─┐┌─┐┌─┐┌─┐┌┐┌┌─┐┌─┐",
                    "├┬┘├┤ └─┐├┤  │   ├┬┘├┤ └─┐├─┘│ ││││└─┐├┤ ",
                    "┴└─└─┘└─┘└─┘ ┴   ┴└─└─┘└─┘┴  └─┘┘└┘└─┘└─┘"),
            List.of("┌┐ ┌─┐┌─┐┬┌─  ┌┬┐┌─┐  ┌─┐┬─┐┌─┐┬  ┬┬┌─┐┬ ┬┌─┐  ┌┬┐┌─┐┌┐┌┬ ┬",
                    "├┴┐├─┤│  ├┴┐   │ │ │  ├─┘├┬┘├┤ └┐┌┘││ ││ │└─┐  │││├┤ ││││ │",
                    "└─┘┴ ┴└─┘┴ ┴   ┴ └─┘  ┴  ┴└─└─┘ └┘ ┴└─┘└─┘└─┘  ┴ ┴└─┘┘└┘└─┘")
    );
    private static final List<List<String>> loadMaterialsMenu = List.of(
            List.of("┌─┐┬ ┬┌─┐┌─┐  ┌┬┐┌─┐┌┬┐┌─┐┬─┐┬┌─┐┬  ",
                    "└─┐│││├─┤├─┘  │││├─┤ │ ├┤ ├┬┘│├─┤│  ",
                    "└─┘└┴┘┴ ┴┴    ┴ ┴┴ ┴ ┴ └─┘┴└─┴┴ ┴┴─┘"),
            List.of("┌─┐┌─┐┌┐┌┌┬┐  ┬─┐┌─┐┌─┐┌─┐┌─┐┌┐┌┌─┐┌─┐",
                    "└─┐├┤ │││ ││  ├┬┘├┤ └─┐├─┘│ ││││└─┐├┤ ",
                    "└─┘└─┘┘└┘─┴┘  ┴└─└─┘└─┘┴  └─┘┘└┘└─┘└─┘"),
            List.of("┬─┐┌─┐┌─┐┌─┐┌┬┐  ┬─┐┌─┐┌─┐┌─┐┌─┐┌┐┌┌─┐┌─┐",
                    "├┬┘├┤ └─┐├┤  │   ├┬┘├┤ └─┐├─┘│ ││││└─┐├┤ ",
                    "┴└─└─┘└─┘└─┘ ┴   ┴└─└─┘└─┘┴  └─┘┘└┘└─┘└─┘"),
            List.of("┌┐ ┌─┐┌─┐┬┌─  ┌┬┐┌─┐  ┌─┐┬─┐┌─┐┬  ┬┬┌─┐┬ ┬┌─┐  ┌┬┐┌─┐┌┐┌┬ ┬",
                    "├┴┐├─┤│  ├┴┐   │ │ │  ├─┘├┬┘├┤ └┐┌┘││ ││ │└─┐  │││├┤ ││││ │",
                    "└─┘┴ ┴└─┘┴ ┴   ┴ └─┘  ┴  ┴└─└─┘ └┘ ┴└─┘└─┘└─┘  ┴ ┴└─┘┘└┘└─┘")
    );
    private static final List<List<String>> shotDefenseMenu = List.of(
            List.of("┌─┐┌┬┐┌┬┐  ┌┐ ┌─┐┌┬┐┌┬┐┌─┐┬─┐┬┌─┐┌─┐",
                    "├─┤ ││ ││  ├┴┐├─┤ │  │ ├┤ ├┬┘│├┤ └─┐",
                    "┴ ┴─┴┘─┴┘  └─┘┴ ┴ ┴  ┴ └─┘┴└─┴└─┘└─┘"),
            List.of("┌─┐┌─┐┌┐┌┌┬┐  ┬─┐┌─┐┌─┐┌─┐┌─┐┌┐┌┌─┐┌─┐",
                    "└─┐├┤ │││ ││  ├┬┘├┤ └─┐├─┘│ ││││└─┐├┤ ",
                    "└─┘└─┘┘└┘─┴┘  ┴└─└─┘└─┘┴  └─┘┘└┘└─┘└─┘"),
            List.of("┬─┐┌─┐┌─┐┌─┐┌┬┐  ┬─┐┌─┐┌─┐┌─┐┌─┐┌┐┌┌─┐┌─┐",
                    "├┬┘├┤ └─┐├┤  │   ├┬┘├┤ └─┐├─┘│ ││││└─┐├┤ ",
                    "┴└─└─┘└─┘└─┘ ┴   ┴└─└─┘└─┘┴  └─┘┘└┘└─┘└─┘"),
            List.of("┌┐ ┌─┐┌─┐┬┌─  ┌┬┐┌─┐  ┌─┐┬─┐┌─┐┬  ┬┬┌─┐┬ ┬┌─┐  ┌┬┐┌─┐┌┐┌┬ ┬",
                    "├┴┐├─┤│  ├┴┐   │ │ │  ├─┘├┬┘├┤ └┐┌┘││ ││ │└─┐  │││├┤ ││││ │",
                    "└─┘┴ ┴└─┘┴ ┴   ┴ └─┘  ┴  ┴└─└─┘ └┘ ┴└─┘└─┘└─┘  ┴ ┴└─┘┘└┘└─┘")
    );
    private static final List<List<String>> defensiveCannonMenu = List.of(
            List.of("┌─┐┬ ┬┌─┐┌─┐┌─┐┌─┐  ┌┬┐┌─┐┌─┐┌─┐┌┐┌┌─┐┬┬  ┬┌─┐  ┌─┐┌─┐┌┐┌┌┐┌┌─┐┌┐┌",
                    "│  ├─┤│ ││ │└─┐├┤    ││├┤ ├┤ ├┤ │││└─┐│└┐┌┘├┤   │  ├─┤│││││││ ││││",
                    "└─┘┴ ┴└─┘└─┘└─┘└─┘  ─┴┘└─┘└  └─┘┘└┘└─┘┴ └┘ └─┘  └─┘┴ ┴┘└┘┘└┘└─┘┘└┘"),
            List.of("┌─┐┌┬┐┌┬┐  ┌┐ ┌─┐┌┬┐┌┬┐┌─┐┬─┐┬┌─┐┌─┐",
                    "├─┤ ││ ││  ├┴┐├─┤ │  │ ├┤ ├┬┘│├┤ └─┐",
                    "┴ ┴─┴┘─┴┘  └─┘┴ ┴ ┴  ┴ └─┘┴└─┴└─┘└─┘"),
            List.of("┌─┐┌─┐┌┐┌┌┬┐  ┬─┐┌─┐┌─┐┌─┐┌─┐┌┐┌┌─┐┌─┐",
                    "└─┐├┤ │││ ││  ├┬┘├┤ └─┐├─┘│ ││││└─┐├┤ ",
                    "└─┘└─┘┘└┘─┴┘  ┴└─└─┘└─┘┴  └─┘┘└┘└─┘└─┘"),
            List.of("┬─┐┌─┐┌─┐┌─┐┌┬┐  ┬─┐┌─┐┌─┐┌─┐┌─┐┌┐┌┌─┐┌─┐",
                    "├┬┘├┤ └─┐├┤  │   ├┬┘├┤ └─┐├─┘│ ││││└─┐├┤ ",
                    "┴└─└─┘└─┘└─┘ ┴   ┴└─└─┘└─┘┴  └─┘┘└┘└─┘└─┘"),
            List.of("┌┐ ┌─┐┌─┐┬┌─  ┌┬┐┌─┐  ┌─┐┬─┐┌─┐┬  ┬┬┌─┐┬ ┬┌─┐  ┌┬┐┌─┐┌┐┌┬ ┬",
                    "├┴┐├─┤│  ├┴┐   │ │ │  ├─┘├┬┘├┤ └┐┌┘││ ││ │└─┐  │││├┤ ││││ │",
                    "└─┘┴ ┴└─┘┴ ┴   ┴ └─┘  ┴  ┴└─└─┘ └┘ ┴└─┘└─┘└─┘  ┴ ┴└─┘┘└┘└─┘")
    );
    private static final List<List<String>> choosePlanetMenu = List.of(
            List.of("┌─┐┬┬─┐┌─┐┌┬┐",
                    "├┤ │├┬┘└─┐ │ ",
                    "└  ┴┴└─└─┘ ┴ "),
            List.of("┌─┐┌─┐┌─┐┌─┐┌┐┌┌┬┐",
                    "└─┐├┤ │  │ ││││ ││",
                    "└─┘└─┘└─┘└─┘┘└┘─┴┘"),
            List.of("┌┬┐┬ ┬┬┬─┐┌┬┐",
                    " │ ├─┤│├┬┘ ││",
                    " ┴ ┴ ┴┴┴└──┴┘"),
            List.of("┌─┐┌─┐┬ ┬┬─┐┌┬┐┬ ┬",
                    "├┤ │ ││ │├┬┘ │ ├─┤",
                    "└  └─┘└─┘┴└─ ┴ ┴ ┴")
    );

    private static final List<List<String>> numBatteriesMenu = List.of(
            List.of("┌─┐┌─┐┬─┐┌─┐",
                    "┌─┘├┤ ├┬┘│ │",
                    "└─┘└─┘┴└─└─┘ "),
            List.of("┌─┐┌┐┌┌─┐",
                    "│ ││││├┤ ",
                    "└─┘┘└┘└─┘"),
            List.of("┌┬┐┬ ┬┌─┐",
                    " │ ││││ │",
                    " ┴ └┴┘└─┘"),
            List.of("┌┬┐┬ ┬┬─┐┌─┐┌─┐",
                    " │ ├─┤├┬┘├┤ ├┤ ",
                    " ┴ ┴ ┴┴└─└─┘└─┘")
    );
    private static final List<List<String>> numMembersMenu = List.of(
            List.of("┌─┐┌─┐┬─┐┌─┐",
                    "┌─┘├┤ ├┬┘│ │",
                    "└─┘└─┘┴└─└─┘ "),
            List.of("┌─┐┌┐┌┌─┐",
                    "│ ││││├┤ ",
                    "└─┘┘└┘└─┘"),
            List.of("┌┬┐┬ ┬┌─┐",
                    " │ ││││ │",
                    " ┴ └┴┘└─┘")
    );



    public AdventureTemplate(AdventureController controller) {
        this.controller = controller;
    }



    public void render() {
        if (!controller.isActive()) {
            return;
        }

        AdventurePhaseData data = controller.getPhaseData();
        clearView();
        System.out.println("╔═╗╔╦╗╦  ╦╔═╗╔╗╔╔╦╗╦ ╦╦═╗╔═╗  ╔═╗╦ ╦╔═╗╔═╗╔═╗\n" +
                           "╠═╣ ║║╚╗╔╝║╣ ║║║ ║ ║ ║╠╦╝║╣   ╠═╝╠═╣╠═╣╚═╗║╣ \n" +
                           "╩ ╩═╩╝ ╚╝ ╚═╝╝╚╝ ╩ ╚═╝╩╚═╚═╝  ╩  ╩ ╩╩ ╩╚═╝╚═╝");
        ShipBoard shipBoard = data.getPlayer().getShipBoard();
        List<Player> players = new ArrayList<>();
        players.add(data.getPlayer());
        players.addAll(data.getEnemies().values());
        int menuIndex = 0;
        int flightIndex = 0;


        if(data.getState() == AdventurePhaseData.AdventureState.SHOW_ENEMIES_SHIP){
            Map<String, ShipBoard> enemiesShipBoard = new HashMap<>();
            for(Map.Entry<String, Player> entry : data.getEnemies().entrySet()){
                enemiesShipBoard.put(entry.getKey(), entry.getValue().getShipBoard());
            }

            printEnemiesShipBoard(enemiesShipBoard);
            for (int i = 0; i < pressEnterToContinue.size(); i++) {
                System.out.println(pressEnterToContinue.get(i));
            }
            return;
        }



        for(int y = 0; y < rowCount; y++){
            for (int i = 0; i < ShipCardCLI.cardLength; i++) {

                //printing user shipBoard (reserved components)
                if(y == 0){
                    if(i <= 2) {
                        System.out.print("   ");
                        for (int x = 0; x < shipBoard.getWidth(); x++) {
                            if(x < shipBoard.getWidth()){
                                shipBoardCLI.printInvalidSquare();
                            }
                        }
                        System.out.print("         ");
                    }
                    else if(i == 3){
                        for (int x = 0; x < shipBoard.getWidth(); x++) {
                            if(x < (shipBoard.getWidth() - 2)){
                                shipBoardCLI.printInvalidSquare();
                            }
                            else if(x == (shipBoard.getWidth() - 1)){
                                System.out.print("    Reserved components:                  ");
                            }
                        }
                    }
                    else if(i == 4){
                        System.out.print("   ");
                        for (int x = 0; x < shipBoard.getWidth(); x++) {
                            if(x < (shipBoard.getWidth() - 2)){
                                shipBoardCLI.printInvalidSquare();
                            }
                            else {
                                System.out.print("       " + (x + 3 - shipBoard.getWidth()) + "       ");
                            }
                        }
                        System.out.print("         ");
                    }
                    else {
                        shipBoardCLI.printReservedCards(shipBoard, i-5, -1);
                        System.out.print("      ");
                    }
                }

                else if (y == 1){
                    if(i < 5){
                        shipBoardCLI.printReservedCards(shipBoard, i+2, -1);
                        System.out.print("      ");
                    }
                    else if (i == 5){
                        printEmptyShipLine(shipBoard);
                    }
                    else if (i == 6){
                        shipBoardCLI.printHorizontalCoordinates(shipBoard);
                        System.out.print("      ");
                    }
                }

                //printing user shipBoard (main board)
                else if (y < shipBoard.getLength() + 2){
                    shipBoardCLI.print(shipBoard, y-2, i, controller.getSelectedJ(), controller.getSelectedI());
                    System.out.print("      ");
                }
                else if (y < shipBoard.getLength() + 3 && i < 3){
                    if (i == 0){
                        shipBoardCLI.printHorizontalCoordinates(shipBoard);
                        System.out.print("      ");
                    }
                    else {
                        printEmptyShipLine(shipBoard);
                    }
                }


                //printing menu
                else {
                    if(data.getState() == AdventurePhaseData.AdventureState.CHOOSE_MAIN_MENU){
                        printMenu(shipBoard, menuIndex, mainMenu, controller.getMainMenu());
                    }
                    else if(data.getState() == AdventurePhaseData.AdventureState.CHOOSE_ACTION_MENU){
                        printMenu(shipBoard, menuIndex, actionMenu, controller.getActionMenu());
                    }
                    else if(data.getState() == AdventurePhaseData.AdventureState.CHOOSE_ADVANCED_ACTION_MENU){
                        printMenu(shipBoard, menuIndex, advancedActionMenu, controller.getAdvancedActionMenu());
                    }
                    else if(data.getState() == AdventurePhaseData.AdventureState.FIRE_POWER_MENU || data.getState() == AdventurePhaseData.AdventureState.CHOOSE_DOUBLE_CANNON || data.getState() == AdventurePhaseData.AdventureState.CHOOSE_FIRE_BATTERIES || data.getState() == AdventurePhaseData.AdventureState.FIRE_POWER_SETUP){
                        printMenu(shipBoard, menuIndex, firePowerMenu, controller.getFirePowerMenu());
                    }
                    else if(data.getState() == AdventurePhaseData.AdventureState.ENGINE_POWER_MENU || data.getState() == AdventurePhaseData.AdventureState.CHOOSE_ENGINE_BATTERIES || data.getState() == AdventurePhaseData.AdventureState.ENGINE_POWER_SETUP){
                        printMenu(shipBoard, menuIndex, enginePowerMenu, controller.getEnginePowerMenu());
                    }
                    else if(data.getState() == AdventurePhaseData.AdventureState.CREW_MEMBERS_MENU || data.getState() == AdventurePhaseData.AdventureState.CHOOSE_HOUSING_UNIT || data.getState() == AdventurePhaseData.AdventureState.CREW_MEMBERS_SETUP){
                        printMenu(shipBoard, menuIndex, crewMembersMenu, controller.getCrewMembersMenu());
                    }
                    else if(data.getState() == AdventurePhaseData.AdventureState.BATTERIES_MENU || data.getState() == AdventurePhaseData.AdventureState.CHOOSE_BATTERIES || data.getState() == AdventurePhaseData.AdventureState.BATTERIES_SETUP){
                        printMenu(shipBoard, menuIndex, batteriesMenu, controller.getBatteriesMenu());
                    }
                    else if(data.getState() == AdventurePhaseData.AdventureState.LOAD_MATERIALS_MENU || data.getState() == AdventurePhaseData.AdventureState.CHOOSE_STORAGE || data.getState() == AdventurePhaseData.AdventureState.LOAD_MATERIALS_SETUP){
                        printMenu(shipBoard, menuIndex, loadMaterialsMenu, controller.getLoadMaterialsMenu());
                    }
                    else if(data.getState() == AdventurePhaseData.AdventureState.SHOT_DEFENSE_MENU || data.getState() == AdventurePhaseData.AdventureState.CHOOSE_SHOT_BATTERIES || data.getState() == AdventurePhaseData.AdventureState.SHOT_DEFENSE_SETUP){
                        printMenu(shipBoard, menuIndex, shotDefenseMenu, controller.getShotDefenseMenu());
                    }
                    else if(data.getState() == AdventurePhaseData.AdventureState.DEFENSIVE_CANNON_MENU || data.getState() == AdventurePhaseData.AdventureState.CHOOSE_DEFENSIVE_CANNON || data.getState() == AdventurePhaseData.AdventureState.CHOOSE_DEFENSIVE_BATTERIES || data.getState() == AdventurePhaseData.AdventureState.DEFENSIVE_CANNON_SETUP){
                        printMenu(shipBoard, menuIndex, defensiveCannonMenu, controller.getDefensiveCannonMenu());
                    }
                    else if(data.getState() == AdventurePhaseData.AdventureState.CHOOSE_PLANET_MENU || data.getState() == AdventurePhaseData.AdventureState.CHOOSE_PLANET_SETUP){
                        printMenu(shipBoard, menuIndex, choosePlanetMenu, controller.getChoosePlanetMenu());
                    }
                    else if(data.getState() == AdventurePhaseData.AdventureState.SELECT_FIRE_NUM_BATTERIES  ||
                            data.getState() == AdventurePhaseData.AdventureState.SELECT_ENGINE_NUM_BATTERIES ||
                            data.getState() == AdventurePhaseData.AdventureState.SELECT_NUM_BATTERIES ||
                            data.getState() == AdventurePhaseData.AdventureState.SELECT_DEFENSE_NUM_BATTERIES ||
                            data.getState() == AdventurePhaseData.AdventureState.SELECT_SHOT_NUM_BATTERIES)
                    {
                        printMenu(shipBoard, menuIndex, numBatteriesMenu, controller.getNumBatteries());
                    }
                    else if(data.getState() == AdventurePhaseData.AdventureState.SELECT_NUM_MEMBERS){
                        printMenu(shipBoard, menuIndex, numBatteriesMenu, controller.getNumMembers());
                    }
                    menuIndex++;
                }


                //printing adventure card
                if (y == 0){
                    System.out.print(" ");
                }
                else if (y == 1){
                    System.out.print("                                        ");
                    if(i == 0){
                        System.out.print(" Current adventure:        it's " +data.getCurrentPlayer() + "'s turn to play");
                    }
                    else {
                        adventureCardCLI.print(data.getAdventureCard(), i-1);
                    }
                }
                else if (y == 2){
                    System.out.print("                                        ");
                    adventureCardCLI.print(data.getAdventureCard(), i+6);
                }
                else if (y == 3){
                    System.out.print("                                        ");
                    if(i < 2){
                        adventureCardCLI.print(data.getAdventureCard(), i+13);
                    }
                    if(i == 3){
                        if(data.getGameHint() != null){
                            System.out.print("Hint: " + data.getGameHint());
                        }
                    }
                    if(i == 4){
                        System.out.print("Current state: " + data.getState().toString() + ", active: " + controller.isActive() + ", state new: " + data.isStateNew());
                    }
                    else if(i == 5){
                        System.out.print("NumBatteries: " + controller.getNumBatteries() + ", NumMembers: " + controller.getNumMembers());
                    }
                    else if(i == 6){
                        Player enemy = data.getEnemies().entrySet().iterator().next().getValue();
                        System.out.print("- your position: " + data.getPlayer().getPosition() + ",   " + enemy.getUsername() + " position: " + enemy.getPosition());
                    }
                }
                else if (y == 4 && i < 4){
                    System.out.print(" ");
                }


                //print flight board
                else {
                    System.out.print("                                 ");
                    FlightBoardCLI.print(data.getFlightBoard(), players, flightIndex);
                    flightIndex++;
                }


                System.out.println(Ansi.ansi().reset());
            }
        }


        //printing error messages
        String serverMessage = data.getServerMessage();
        if(serverMessage != null && !serverMessage.isEmpty()) {
            System.out.println(Ansi.ansi().fg(Ansi.Color.RED) + serverMessage.toUpperCase() + Ansi.ansi().reset());
            data.resetServerMessage();
        }
    }



    public int getRowCount(){
        return rowCount;
    }

    public int getMainMenuSize(){
        return mainMenu.size();
    }

    public int getActionMenuSize(){
        return actionMenu.size();
    }

    public int getAdvancedActionMenuSize(){
        return advancedActionMenu.size();
    }

    public int getFirePowerMenuSize(){
        return firePowerMenu.size();
    }

    public int getEnginePowerMenuSize(){
        return enginePowerMenu.size();
    }

    public int getCrewMembersMenuSize(){
        return crewMembersMenu.size();
    }

    public int getBatteriesMenuSize(){
        return batteriesMenu.size();
    }

    public int getLoadMaterialsMenuSize(){
        return loadMaterialsMenu.size();
    }

    public int getShotDefenseMenu(){
        return shotDefenseMenu.size();
    }

    public int getDefensiveCannonMenuSize(){
        return defensiveCannonMenu.size();
    }

    public int getChoosePlanetMenuSize(){
        return choosePlanetMenu.size();
    }

    public int getNumBatteriesMenuSize(){
        return numBatteriesMenu.size();
    }

    public int getNumMembersMenuSize(){
        return numMembersMenu.size();
    }
}
