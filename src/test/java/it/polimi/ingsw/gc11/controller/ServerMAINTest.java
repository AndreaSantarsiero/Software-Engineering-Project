package it.polimi.ingsw.gc11.controller;

import org.junit.jupiter.api.*;
import java.io.*;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;

class ServerMAINTest {
    private static int defaultRmi;
    private static int defaultSocket;

    @BeforeAll
    static void initDefaults() throws Exception {
        try (InputStream input = ServerMAIN.class.getClassLoader()
                .getResourceAsStream("config.properties")) {
            assertNotNull(input, "config.properties not found on classpath");
            Properties prop = new Properties();
            prop.load(input);
            defaultRmi = Integer.parseInt(prop.getProperty("RMIPort"));
            defaultSocket = Integer.parseInt(prop.getProperty("socketPort"));
        }
    }

    private ByteArrayOutputStream outContent;
    private PrintStream originalOut;

    @BeforeEach
    void setUpStreams() {
        originalOut = System.out;
        outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
    }

    @AfterEach
    void restoreStreams() {
        System.setOut(originalOut);
    }

    @Test
    void run_withValidArgs_shouldAcceptProvidedPorts() {
        String[] args = {"1234", "5678"};
        try {
            ServerMAIN.run(args);
        } catch (Exception ignored) {
        }
        String output = outContent.toString();
        // Nessun messaggio di porta invalida
        assertFalse(output.contains("Invalid port number:"),
                "Output should not report any invalid port numbers for valid args");
    }

    @Test
    void run_withInvalidArgs_shouldThrowException() {
        // Negative or out-of-range ports should trigger IllegalArgumentException
        assertThrows(IllegalArgumentException.class,
                () -> ServerMAIN.run(new String[]{"-1", "70000"}),
                "Expected IllegalArgumentException for invalid port values"
        );
    }
}
