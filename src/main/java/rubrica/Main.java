package rubrica; // o rubrica.application

import rubrica.controller.RubricaController;
import rubrica.model.Persona;
import rubrica.model.Rubrica;
import rubrica.persistance.PersonaDAO;
import rubrica.service.PersonaService;
import rubrica.view.VistaPrincipale;

import javax.swing.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        String jdbcURL = "jdbc:mariadb://localhost:3306/rubrica_db";
        String username = "root";
        String password = "newpassword";

        try {
            Connection connection = DriverManager.getConnection(jdbcURL, username, password);
            System.out.println("Connesso a MariaDB con successo!");


            // Crea una persona da aggiungere
            Persona persona = new Persona("Gloria", "Fiammengo",
                    "Via Moglianese 338, Peseggia", "+39 3491861346", 27, 0);

            PersonaDAO personaDAO = new PersonaDAO(connection);
            PersonaService personaService = new PersonaService(personaDAO); // Inizializza il servizio
            SwingUtilities.invokeLater(() -> {
                VistaPrincipale vista = new VistaPrincipale(personaService);
                vista.getModelloLista().addElement(persona);
                RubricaController controller = new RubricaController(personaService, vista);
                vista.setVisible(true);
            });
        } catch (SQLException e) {
            System.out.println("Errore nella connessione a MariaDB.");
            e.printStackTrace();
        }



    }
}
