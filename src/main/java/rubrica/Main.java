package rubrica; // o rubrica.application

import rubrica.controller.RubricaController;
import rubrica.model.Persona;
import rubrica.persistance.PersonaDAO;
import rubrica.service.PersonaService;
import rubrica.utils.PersonaDuplicataException;
import rubrica.view.VistaPrincipale;

import javax.swing.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) {
        String jdbcURL = "jdbc:mariadb://localhost:3306/rubrica_db";
        String username = "root";
        String password = "newpassword";

        try {
            Connection connection = DriverManager.getConnection(jdbcURL, username, password);
            System.out.println("Connesso a MariaDB con successo!");


            PersonaDAO personaDAO = new PersonaDAO(connection);
            PersonaService personaService = new PersonaService(personaDAO); // Inizializza il servizio

            // Crea e visualizza la vista
            SwingUtilities.invokeLater(() -> {
                VistaPrincipale vista = new VistaPrincipale(personaService);

                // Crea il controller
                RubricaController controller = new RubricaController(personaService, vista);
                vista.setVisible(true);
            });

        } catch (SQLException e) {
            System.out.println("Errore nella connessione a MariaDB.");
            e.printStackTrace();
        }
    }
}
