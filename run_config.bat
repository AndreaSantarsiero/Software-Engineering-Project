@echo off

:: Set UTF-8 encoding
chcp 65001

:: Start server
start cmd /k "java -jar target\GC11-1.0-SNAPSHOT-jar-with-dependencies.jar -server"

:: Start first CLI client
start cmd /k "java -jar target\GC11-1.0-SNAPSHOT-jar-with-dependencies.jar -cli"

:: Start second CLI client
start cmd /k "java -jar target\GC11-1.0-SNAPSHOT-jar-with-dependencies.jar -cli"
