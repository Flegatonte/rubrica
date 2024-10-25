package rubrica.controller;

import rubrica.model.Persona;
import rubrica.service.PersonaService;
import rubrica.utils.PersonaDuplicataException;
import rubrica.view.EditorPersona;
import rubrica.view.VistaPrincipale;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Vector;

public class RubricaController {
    private final PersonaService personaService;
    private final VistaPrincipale view;

    public RubricaController(PersonaService personaService, VistaPrincipale view) {
        this.personaService = personaService;
        this.view = view;

        initializeListeners();
    }

    private void initializeListeners() {
        // Listener per selezionare la riga nella tabella
        this.view.getTabellaPersone().getParent().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = view.getTabellaPersone().rowAtPoint(e.getPoint());

                // Se non viene cliccata nessuna riga, deseleziona tutte le righe
                if (row == -1) {
                    view.getTabellaPersone().clearSelection();
                    view.getTabellaPersone().repaint();
                }
            }
        });

        this.view.getBottoneAggiungi().addActionListener(new NuovaPersonaListener());
        this.view.getBottoneModifica().addActionListener(new ModificaPersonaListener());
        this.view.getBottoneElimina().addActionListener(new EliminaPersonaListener());
        this.view.getBottoneRicerca().addActionListener(new RicercaPersonaListener());
    }

    // Listener per aggiungere una nuova persona
    class NuovaPersonaListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            EditorPersona editor = new EditorPersona();
            editor.setVisible(true);

            editor.getSalvaButton().addActionListener(event -> {
                String nome = editor.getNome();
                String cognome = editor.getCognome();
                String indirizzo = editor.getIndirizzo();
                String telefono = editor.getTelefono();
                String etaString = editor.getEta();

                if (nome.isEmpty() || cognome.isEmpty() || indirizzo.isEmpty() || telefono.isEmpty() || etaString.isEmpty()) {
                    JOptionPane.showMessageDialog(editor, "Tutti i campi devono essere compilati.", "Errore", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                int eta;
                try {
                    eta = Integer.parseInt(etaString);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(editor, "L'età deve essere un numero valido.", "Errore", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (!isValidPhoneNumber(telefono)) {
                    JOptionPane.showMessageDialog(editor, "Il numero di telefono non è valido.", "Errore", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                Persona nuovaPersona = new Persona(nome, cognome, indirizzo, telefono, eta);
                try {
                    // Aggiunge la persona nel database e recupera l'ID generato dal database
                    personaService.aggiungiPersona(nuovaPersona);

                    // Dopo aver recuperato l'ID, aggiunge la persona alla tabella con tutti i dati necessari
                    view.getModelloTabella().addRow(new Object[]{
                            nuovaPersona.getNome(),
                            nuovaPersona.getCognome(),
                            nuovaPersona.getIndirizzo(),
                            nuovaPersona.getTelefono(),
                            nuovaPersona.getEta(),
                            nuovaPersona.getID()
                    });

                } catch (PersonaDuplicataException ex) {
                    JOptionPane.showMessageDialog(editor, "Persona già esistente.", "Errore", JOptionPane.ERROR_MESSAGE);
                }

                editor.dispose();
            });

            editor.getAnnullaButton().addActionListener(event -> editor.dispose());
        }
    }


    // Listener per modificare una persona esistente
    class ModificaPersonaListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            int selectedRow = view.getTabellaPersone().getSelectedRow();
            if (selectedRow != -1) {

                // Crea un oggetto Persona da modificare
                Persona personaDaModificare = getPersonaFromTable(selectedRow);
                EditorPersona editor = new EditorPersona(personaDaModificare);
                editor.setVisible(true);

                editor.getSalvaButton().addActionListener(event -> {
                    // Raccoglie i dati dal form
                    String nuovoNome = editor.getNome();
                    String nuovoCognome = editor.getCognome();
                    String nuovoIndirizzo = editor.getIndirizzo();
                    String nuovoTelefono = editor.getTelefono();
                    String nuovaEtaString = editor.getEta();

                    if (nuovoNome.isEmpty() || nuovoCognome.isEmpty() || nuovoIndirizzo.isEmpty() || nuovoTelefono.isEmpty() || nuovaEtaString.isEmpty()) {
                        JOptionPane.showMessageDialog(editor, "Tutti i campi devono essere compilati.", "Errore", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    int nuovaEta;
                    try {
                        nuovaEta = Integer.parseInt(nuovaEtaString);
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(editor, "L'età deve essere un numero valido.", "Errore", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    if (!isValidPhoneNumber(nuovoTelefono)) {
                        JOptionPane.showMessageDialog(editor, "Il numero di telefono non è valido.", "Errore", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    // Crea una nuova persona con i dati aggiornati
                    Persona personaModificata = new Persona(nuovoNome, nuovoCognome, nuovoIndirizzo, nuovoTelefono,
                            nuovaEta, personaDaModificare.getID());
                    try {
                        personaService.modificaPersona(personaModificata);
                        // Aggiorna il modello della tabella
                        view.getModelloTabella().setValueAt(nuovoNome, selectedRow, 0);
                        view.getModelloTabella().setValueAt(nuovoCognome, selectedRow, 1);
                        view.getModelloTabella().setValueAt(nuovoIndirizzo, selectedRow, 2);
                        view.getModelloTabella().setValueAt(nuovoTelefono, selectedRow, 3);
                        view.getModelloTabella().setValueAt(nuovaEta, selectedRow, 4);
                    } catch (PersonaDuplicataException ex) {
                        JOptionPane.showMessageDialog(editor, "Persona già esistente.", "Errore", JOptionPane.ERROR_MESSAGE);
                    }

                    editor.dispose();
                });

                editor.getAnnullaButton().addActionListener(event -> editor.dispose());
            } else {
                JOptionPane.showMessageDialog(view, "Seleziona una persona da modificare.", "Errore", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // Listener per eliminare una persona
    class EliminaPersonaListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            int selectedRow = view.getTabellaPersone().getSelectedRow();
            if (selectedRow != -1) {
                String nomeCompleto = view.getModelloTabella().getValueAt(selectedRow, 0) + " " +
                        view.getModelloTabella().getValueAt(selectedRow, 1);

                int conferma = JOptionPane.showConfirmDialog(
                        view,
                        "Eliminare la persona " + nomeCompleto + "?",
                        "Conferma Eliminazione",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.WARNING_MESSAGE
                );

                if (conferma == JOptionPane.YES_OPTION) {
                    personaService.rimuoviPersona(getPersonaFromTable(selectedRow));
                    view.aggiornaVista();
                }
            } else {
                JOptionPane.showMessageDialog(view, "Seleziona una persona da eliminare.", "Errore", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // Listener per cercare una persona
    class RicercaPersonaListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String nomeRicerca = view.getCampoRicerca().getText().trim().toLowerCase();

            // Se il campo di ricerca è vuoto, mostra la lista completa
            if (nomeRicerca.isEmpty()) {
                view.aggiornaTabella(new Vector<>(personaService.recuperaTutteLePersone()));
                return;
            }

            Iterable<Persona> persone = personaService.recuperaTutteLePersone();
            Vector<Persona> personeTrovate = new Vector<>(); // Lista per le persone trovate

            // Filtra le persone in base sia al nome che al cognome
            for (Persona persona : persone) {
                String nomeCompleto = (persona.getNome() + " " + persona.getCognome()).toLowerCase();
                if (persona.getNome().toLowerCase().contains(nomeRicerca) ||
                        persona.getCognome().toLowerCase().contains(nomeRicerca) ||
                        nomeCompleto.contains(nomeRicerca)) {
                    personeTrovate.add(persona);
                }
            }

            // Aggiorna la tabella con le persone trovate
            view.aggiornaTabella(personeTrovate);
        }
    }


    private Persona getPersonaFromTable(int selectedRow) {
        String nome = view.getModelloTabella().getValueAt(selectedRow, 0).toString();
        String cognome = view.getModelloTabella().getValueAt(selectedRow, 1).toString();
        String indirizzo = view.getModelloTabella().getValueAt(selectedRow, 2).toString();
        String telefono = view.getModelloTabella().getValueAt(selectedRow, 3).toString();
        int eta = Integer.parseInt(view.getModelloTabella().getValueAt(selectedRow, 4).toString());
        long id = Long.parseLong(view.getModelloTabella().getValueAt(selectedRow, 5).toString());

        return new Persona(nome, cognome, indirizzo, telefono, eta, id);
    }


    private boolean isValidPhoneNumber(String phoneNumber) {
        // Rimuove spazi e trattini, se presenti
        phoneNumber = phoneNumber.replaceAll("\\s+", "").replaceAll("-", "");

        // Controlla che il numero inizi con "+" (facoltativo) o cifre
        if (!phoneNumber.matches("^\\+?[0-9]+$")) {
            return false;
        }

        // Verifica che la lunghezza sia tra 10 e 15 cifre
        int length = phoneNumber.startsWith("+") ? phoneNumber.length() - 1 : phoneNumber.length();
        if (length < 10 || length > 15) {
            return false;
        }

        return true;
    }

}
