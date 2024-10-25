package rubrica;

import rubrica.controller.RubricaController;
import rubrica.persistance.PersonaDAO;
import rubrica.service.PersonaService;
import rubrica.view.VistaPrincipale;

import javax.swing.*;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class Main {
    public static void main(String[] args) {
        Properties properties = new Properties();

        // Carica le proprietà dal file di configurazione
        try (InputStream input = Main.class.getClassLoader().getResourceAsStream("config.properties")) {
            if (input == null) {
                System.out.println("Spiacente, impossibile trovare il file di configurazione.");
                return;
            }
            properties.load(input);
        } catch (IOException e) {
            System.out.println("Errore nel caricamento del file di configurazione: " + e.getMessage());
            e.printStackTrace();
            return;
        }

        String jdbcURL = properties.getProperty("jdbc.url");
        String username = properties.getProperty("jdbc.username");
        String password = properties.getProperty("jdbc.password");

        Connection connection = null;
        try {
            connection = DriverManager.getConnection(jdbcURL, username, password);
            System.out.println("Connesso a MariaDB con successo!");

            // Inizializza DAO e Servizio
            PersonaDAO personaDAO = new PersonaDAO(connection);
            PersonaService personaService = new PersonaService(personaDAO);

            // Crea e visualizza la vista
            Connection finalConnection = connection;
            SwingUtilities.invokeLater(() -> {
                VistaPrincipale vista = new VistaPrincipale(personaService);
                RubricaController controller = new RubricaController(personaService, vista);
                vista.setVisible(true);

                // Aggiungi un hook per chiudere la connessione quando l'app si chiude
                Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                    try {
                        if (finalConnection != null && !finalConnection.isClosed()) {
                            finalConnection.close();
                            System.out.println("Connessione a MariaDB chiusa.");
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }));
            });

        } catch (SQLException e) {
            // Messaggio di errore più dettagliato
            JOptionPane.showMessageDialog(null, "Errore nella connessione a MariaDB: " + e.getMessage(),
                    "Errore di Connessione", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            System.exit(1); // Interrompi l'applicazione in caso di errore di connessione
        }
    }
}
