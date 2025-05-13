@echo off

:: Set the encoding to UTF-8 for all terminals
chcp 65001

:: Run the first terminal for ServerMAIN
start cmd /k "java -cp target\GC11-1.0-SNAPSHOT-jar-with-dependencies.jar it.polimi.ingsw.gc11.controller.ServerMAIN"

:: Run the second terminal for MainCLI with multiple-client activation key F1
start cmd /k "java -cp target\GC11-1.0-SNAPSHOT-jar-with-dependencies.jar it.polimi.ingsw.gc11.view.cli.MainCLI"

:: Run the third terminal for MainCLI with multiple-client activation key F2
start cmd /k "java -cp target\GC11-1.0-SNAPSHOT-jar-with-dependencies.jar it.polimi.ingsw.gc11.view.cli.MainCLI"

