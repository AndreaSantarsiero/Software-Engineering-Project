# Starting the Java Application (JAR)

This project can be executed in **three different modes**: **GUI**, **CLI**, and **Server**. The mode is selected using command-line flags passed when launching the JAR.

---

## 🔧 Available Modes

### 1. GUI (default graphical mode)
Launches the graphical user interface.  
Optionally, you can pass the server IP and port as parameters to connect directly:

**Valid examples:**   
java -jar GC11-1.0-SNAPSHOT-jar-with-dependencies.jar  
java -jar GC11-1.0-SNAPSHOT-jar-with-dependencies.jar 192.168.1.100  
java -jar GC11-1.0-SNAPSHOT-jar-with-dependencies.jar 192.168.1.100 1099

> If no arguments are provided, the application will start in GUI mode using default connection settings.

---

### 2. CLI (command-line mode)
Starts the client in text-based mode. Use the `-cli` flag, optionally followed by:
- Just the server IP address
- The server IP address and port
- No parameters (uses default IP and port)

**Valid examples:**  
java -jar GC11-1.0-SNAPSHOT-jar-with-dependencies.jar -cli  
java -jar GC11-1.0-SNAPSHOT-jar-with-dependencies.jar 192.168.1.100 -cli
java -jar GC11-1.0-SNAPSHOT-jar-with-dependencies.jar -cli 192.168.1.100 1099

> If nothing is specified after `-cli`, the app will use default values for IP and port.

---

### 3. Server
Starts the application in server mode.
Optionally, you can pass the RMI port and the Socket port as parameters:

**Valid examples:**  
java -jar GC11-1.0-SNAPSHOT-jar-with-dependencies.jar -s
java -jar GC11-1.0-SNAPSHOT-jar-with-dependencies.jar -s 1099 1234

---

## ℹ️ Supported Argument Summary

| Command                   | Mode          | IP         | Port      |
|---------------------------|---------------|------------|-----------|
| -cli                      | CLI           | Default    | Default   |
| 192.168.1.100 -cli        | CLI           | Custom     | Default   |
| -cli 192.168.1.100 1099   | CLI           | Custom     | Custom    |
| -s                        | Server        | Default    | Default   |
| -s 1099 1234              | Server        | Custom     | Custom    |
| (no arguments)            | GUI (default) | Default    | Default   |
| 192.168.1.100             | GUI (default) | Custom     | Default   |

---

## 📌 Notes
- The JAR must be run using Java 17 or later (adjust based on your requirements).
- The `-cli` flag can be placed before or after the ip, but not between ip and port!
- If provided, the IP and port must be valid and reachable from the client machine.

---

## 👤 Authors
- [Andrea Santarsiero](https://github.com/AndreaSantarsiero)
- [Luca Sartori](https://github.com/Luca-Sartori)
- [Andrea Pianini](https://github.com/AndreaPianini)
- [Lorenzo Stani](https://github.com/lorenzostani) 
