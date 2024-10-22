package rubrica.persistance;

import rubrica.model.Persona;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PersonaDAO {
    private Connection connection;

    public PersonaDAO(Connection connection) {
        this.connection = connection;
    }

    public Persona cercaPersonaPerID(long id) {
        Persona persona = null;
        String query = "SELECT * FROM persone WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    String nome = resultSet.getString("nome");
                    String cognome = resultSet.getString("cognome");
                    String indirizzo = resultSet.getString("indirizzo");
                    String telefono = resultSet.getString("telefono");
                    int eta = resultSet.getInt("eta");
                    persona = new Persona(nome, cognome, indirizzo, telefono, eta);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return persona;
    }

    public void aggiungiPersona(Persona persona) {
        String query = "INSERT INTO persone (nome, cognome, indirizzo, telefono, eta) VALUES (?, ?, ?, ?, ?)";

        try (
             PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) { // Ottieni le chiavi generate
            statement.setString(1, persona.getNome());
            statement.setString(2, persona.getCognome());
            statement.setString(3, persona.getIndirizzo());
            statement.setString(4, persona.getTelefono());
            statement.setInt(5, persona.getEta());
            statement.executeUpdate();

            // Ottieni l'ID generato
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    long id = generatedKeys.getLong(1);
                    System.out.println("ID generato:" + id);
                    persona.setID(id);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void modificaPersona(Persona persona) {
        String sql = "UPDATE persone SET nome = ?, cognome = ?, indirizzo = ?, telefono = ?, eta = ? WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, persona.getNome());
            statement.setString(2, persona.getCognome());
            statement.setString(3, persona.getIndirizzo());
            statement.setString(4, persona.getTelefono());
            statement.setInt(5, persona.getEta());
            statement.setLong(6, persona.getID());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void rimuoviPersona(Persona persona) {
        String sql = "DELETE FROM persone WHERE id = ?";
        System.out.println(persona.getID());
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, persona.getID());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Persona> recuperaTutteLePersone() {
        List<Persona> persone = new ArrayList<>();
        String sql = "SELECT * FROM persone";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            while (resultSet.next()) {
                Persona persona = new Persona(
                        resultSet.getString("nome"),
                        resultSet.getString("cognome"),
                        resultSet.getString("indirizzo"),
                        resultSet.getString("telefono"),
                        resultSet.getInt("eta")
                );
                persone.add(persona);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return persone;
    }
}
