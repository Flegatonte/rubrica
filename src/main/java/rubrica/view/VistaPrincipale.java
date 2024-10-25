package rubrica.view;

import rubrica.model.Persona;
import rubrica.service.PersonaService; // Assicurati di importare il servizio

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Vector;

public class VistaPrincipale extends JFrame {
    private final JTextField campoRicerca;
    private final JButton bottoneRicerca;
    private final JButton bottoneAggiungi;
    private final JButton bottoneModifica;
    private final JButton bottoneElimina;
    private final JTable tabellaPersone;
    private final DefaultTableModel modelloTabella;
    private final PersonaService personaService; // Campo per il servizio


    public VistaPrincipale(PersonaService personaService) {
        this.personaService = personaService; // Inizializza il servizio

        setTitle("Rubrica");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        modelloTabella = new DefaultTableModel(new Object[]{"Nome", "Cognome", "Indirizzo", "Telefono", "Età", "ID"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Rende tutte le celle non modificabili
            }
        };        tabellaPersone = new JTable(modelloTabella);
        JScrollPane scrollPane = new JScrollPane(tabellaPersone);

        // Nasconde i campi che non ci interessa visualizzare
        tabellaPersone.getColumnModel().getColumn(2).setMinWidth(0);
        tabellaPersone.getColumnModel().getColumn(2).setMaxWidth(0);
        tabellaPersone.getColumnModel().getColumn(2).setPreferredWidth(0);

        tabellaPersone.getColumnModel().getColumn(4).setMinWidth(0);
        tabellaPersone.getColumnModel().getColumn(4).setMaxWidth(0);
        tabellaPersone.getColumnModel().getColumn(4).setPreferredWidth(0);

        tabellaPersone.getColumnModel().getColumn(5).setMinWidth(0);
        tabellaPersone.getColumnModel().getColumn(5).setMaxWidth(0);
        tabellaPersone.getColumnModel().getColumn(5).setPreferredWidth(0);

        campoRicerca = new JTextField(15);
        bottoneRicerca = new JButton("Cerca");
        bottoneAggiungi = new JButton("Aggiungi");
        bottoneModifica = new JButton("Modifica");
        bottoneElimina = new JButton("Elimina");

        // Pannello per la ricerca
        JPanel pannelloRicerca = new JPanel();
        pannelloRicerca.add(campoRicerca);
        pannelloRicerca.add(bottoneRicerca);

        // Pannello dei pulsanti
        JPanel pannelloBottoni = new JPanel();
        pannelloBottoni.add(bottoneAggiungi);
        pannelloBottoni.add(bottoneModifica);
        pannelloBottoni.add(bottoneElimina);

        add(pannelloRicerca, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(pannelloBottoni, BorderLayout.SOUTH);

        // Carica le persone dal database al caricamento della finestra
        caricaPersoneDalDatabase();
    }

    private void caricaPersoneDalDatabase() {
        Iterable<Persona> persone = personaService.recuperaTutteLePersone();
        aggiornaTabella(persone);
    }

    public void aggiornaVista() {
        caricaPersoneDalDatabase();
    }

    public void aggiornaTabella(Iterable<Persona> persone) {
        modelloTabella.setRowCount(0); // Pulisce la tabella
        for (Persona persona : persone) {
            modelloTabella.addRow(new Object[]{persona.getNome(), persona.getCognome(), persona.getIndirizzo(),
                    persona.getTelefono(), persona.getEta(), persona.getID()});
        }
    }
    public JTextField getCampoRicerca() {
        return campoRicerca;
    }

    public JButton getBottoneRicerca() {
        return bottoneRicerca;
    }

    public JButton getBottoneAggiungi() {
        return bottoneAggiungi;
    }

    public JButton getBottoneModifica() {
        return bottoneModifica;
    }

    public JButton getBottoneElimina() {
        return bottoneElimina;
    }

    public DefaultTableModel getModelloTabella() {
        return modelloTabella;
    }

    public JTable getTabellaPersone() {
        return tabellaPersone;
    }
}
