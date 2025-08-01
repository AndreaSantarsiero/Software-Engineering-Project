# Galaxy Trucker — Software Engineering Project

**Galaxy Trucker** is a software project inspired by the board game of the same name, developed as part of the *Software Engineering* course at Politecnico di Milano. While it replicates the game's mechanics, the main focus was designing and implementing a modular, testable, and networked application in Java.

Final grade: **30/30**.

---

## 🛠️ Technologies & Architecture

### Language & Tooling
- Java 22
- JavaFX for the graphical user interface (GUI)
- JLine for the text-based interface (TUI)
- Maven for dependency and build management
- JUnit for unit and integration testing

### Architecture
- MVC (Model-View-Controller) pattern with a thin client structure
- Clear separation of responsibilities:
  - The **Model** is shared between client and server
  - The **Controller** runs on the server
  - The **View** runs on the client (GUI and TUI are fully interchangeable)

### Networking
- Dual implementation of client-server communication:
  - RMI (Remote Method Invocation)
  - Custom TCP Socket protocol
- The client can dynamically choose the communication protocol at runtime, without affecting game logic or experience

---

## 💻 User Interfaces

### GUI — JavaFX
- Fully interactive visual interface
- Drag & drop support for spaceship construction
- Visual feedback and animations during flight

### TUI — JLine
- Responsive terminal interface
- Keyboard-based navigation
- Suitable for headless environments (e.g., SSH sessions)

---

## ✅ Testing & Code Quality

- Complete test suite, including unit tests, integration tests, and networking tests
- Modular design allows mocking and isolated testing
- Build and test automation handled by Maven and the Surefire plugin

---

## 🚀 Key Features

- Multiplayer support (up to 4 players) over local network
- Dynamic spaceship construction with real constraints
- Automatic event resolution during the flight phase
- Real-time state synchronization between server and clients
- Support for both standard and advanced game modes

---

## 📦 How to Run the Project

Requirements: Java 22+

To build the project, use the Maven `clean install` goal.

To start the server, run the generated JAR file in the `server/target` folder.

To start the client, run the generated JAR file in the `client/target` folder.

At launch, the client will prompt you to select the communication method: RMI or Socket.

---

## 👨‍💻 Authors

- [Andrea Santarsiero](https://github.com/AndreaSantarsiero)  
- [Luca Sartori](https://github.com/Luca-Sartori)  
- [Andrea Pianini](https://github.com/AndreaPianini)  
- [Lorenzo Stani](https://github.com/lorenzostani)

---

## 📄 License

This project is licensed under the **GPL-3.0 License**.
