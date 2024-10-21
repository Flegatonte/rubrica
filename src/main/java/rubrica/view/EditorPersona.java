package rubrica.view;

import rubrica.model.Persona;

import javax.swing.*;
import java.awt.*;

public class EditorPersona extends JFrame {
    private JTextField nomeField;
    private JTextField cognomeField;
    private JTextField indirizzoField;
    private JTextField telefonoField;
    private JTextField etaField;
    private final JButton salvaButton;
    private final JButton annullaButton;

    public EditorPersona() {
        this(null); // Chiamata al costruttore che accetta una persona
    }

    public EditorPersona(Persona persona) {
        setTitle("Editor Persona");
        setSize(500, 400);  // Aumenta la dimensione della finestra
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        // Margine per i bordi
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL; // Allarga i campi

        // Creazione dei campi e delle etichette
        JLabel nomeLabel = new JLabel("Nome:");
        nomeField = new JTextField(25); // Campi più larghi

        JLabel cognomeLabel = new JLabel("Cognome:");
        cognomeField = new JTextField(25);

        JLabel indirizzoLabel = new JLabel("Indirizzo:");
        indirizzoField = new JTextField(25);

        JLabel telefonoLabel = new JLabel("Telefono:");
        telefonoField = new JTextField(25);

        JLabel etaLabel = new JLabel("Età:");
        etaField = new JTextField(25);

        // Precompila i campi se una persona è fornita (caso modifica)
        if (persona != null) {
            nomeField.setText(persona.getNome());
            cognomeField.setText(persona.getCognome());
            indirizzoField.setText(persona.getIndirizzo());
            telefonoField.setText(persona.getTelefono());
            etaField.setText(String.valueOf(persona.getEta()));
        }

        // Creazione dei bottoni
        salvaButton = new JButton("Salva");
        annullaButton = new JButton("Annulla");

        // Aggiunta dei componenti al layout
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(nomeLabel, gbc);

        gbc.gridx = 1;
        add(nomeField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        add(cognomeLabel, gbc);

        gbc.gridx = 1;
        add(cognomeField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        add(indirizzoLabel, gbc);

        gbc.gridx = 1;
        add(indirizzoField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        add(telefonoLabel, gbc);

        gbc.gridx = 1;
        add(telefonoField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        add(etaLabel, gbc);

        gbc.gridx = 1;
        add(etaField, gbc);

        // Aggiunta dei bottoni
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.NONE; // Non estendere i bottoni a tutta la larghezza
        gbc.anchor = GridBagConstraints.CENTER; // Centra i bottoni

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(salvaButton);
        buttonPanel.add(annullaButton);
        add(buttonPanel, gbc);

        setLocationRelativeTo(null); // Centra la finestra sullo schermo
    }

    // Metodi getter per recuperare i valori dai campi
    public String getNome() {
        return nomeField.getText();
    }

    public String getCognome() {
        return cognomeField.getText();
    }

    public String getIndirizzo() {
        return indirizzoField.getText();
    }

    public String getTelefono() {
        return telefonoField.getText();
    }

    public String getEta() {
        return etaField.getText();  // Restituisce il testo inserito, senza convertire direttamente in int
    }

    // Metodi per ottenere i bottoni Salva e Annulla
    public JButton getSalvaButton() {
        return salvaButton;
    }

    public JButton getAnnullaButton() {
        return annullaButton;
    }

    // Metodo per ottenere la persona modificata includendo l'ID
    public Persona getPersonaModificata(long id) {
        try {
            int eta = Integer.parseInt(getEta());
            return new Persona(
                    getNome(),
                    getCognome(),
                    getIndirizzo(),
                    getTelefono(),
                    eta
            );
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Errore: età non valida. Inserisci un numero intero.");
            return null;
        }
    }
}
