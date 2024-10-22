package rubrica.view;

import rubrica.model.Persona;
import rubrica.service.PersonaService; // Assicurati di importare il servizio

import javax.swing.*;
import java.awt.*;

public class VistaPrincipale extends JFrame {
    private final JTextField campoRicerca;
    private final JComboBox<String> filtroEta;
    private final JButton bottoneRicerca;
    private final JButton bottoneAggiungi;
    private final JButton bottoneModifica;
    private final JButton bottoneElimina;
    private final JList<Persona> listaPersone;
    private final DefaultListModel<Persona> modelloLista;
    private final PersonaService personaService; // Campo per il servizio

    // Modifica il costruttore per accettare un PersonaService
    public VistaPrincipale(PersonaService personaService) {
        this.personaService = personaService; // Inizializza il servizio

        setTitle("Rubrica");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Centra la finestra
        setLayout(new BorderLayout());

        // Inizializzazione dei componenti
        modelloLista = new DefaultListModel<>();
        listaPersone = new JList<>(modelloLista);
        JScrollPane scrollPane = new JScrollPane(listaPersone);

        campoRicerca = new JTextField(15);
        filtroEta = new JComboBox<>(new String[]{"Tutti", "0-20", "21-40", "41-60", "61+"});
        bottoneRicerca = new JButton("Cerca");
        bottoneAggiungi = new JButton("Aggiungi");
        bottoneModifica = new JButton("Modifica");
        bottoneElimina = new JButton("Elimina");

        // Pannello per la ricerca
        JPanel pannelloRicerca = new JPanel();
        pannelloRicerca.add(campoRicerca);
        pannelloRicerca.add(filtroEta);
        pannelloRicerca.add(bottoneRicerca);

        // Pannello dei pulsanti
        JPanel pannelloBottoni = new JPanel();
        pannelloBottoni.add(bottoneAggiungi);
        pannelloBottoni.add(bottoneModifica);
        pannelloBottoni.add(bottoneElimina);

        // Aggiunta dei pannelli alla finestra
        add(pannelloRicerca, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(pannelloBottoni, BorderLayout.SOUTH);

        // Carica le persone dal database al caricamento della finestra
        caricaPersoneDalDatabase();
    }

    // Metodo per caricare le persone dal database
    private void caricaPersoneDalDatabase() {
        Iterable<Persona> persone = personaService.recuperaTutteLePersone();
        aggiornaLista(persone);
    }

    // Metodo per aggiornare la lista delle persone
    public void aggiornaLista(Iterable<Persona> persone) {
        modelloLista.clear();
        for (Persona persona : persone) {
            modelloLista.addElement(persona);
        }
    }

    // Metodi per ottenere i componenti (getter)
    public JTextField getCampoRicerca() {
        return campoRicerca;
    }

    public JComboBox<String> getFiltroEta() {
        return filtroEta;
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

    public DefaultListModel<Persona> getModelloLista() {
        return modelloLista;
    }

    public JList<Persona> getListaPersone() {
        return listaPersone;
    }
}
