package rubrica.controller;

import rubrica.model.Persona;
import rubrica.model.Rubrica;
import rubrica.view.EditorPersona;
import rubrica.view.VistaPrincipale;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class RubricaController {
    private Rubrica rubrica;
    private VistaPrincipale view;

    public RubricaController(Rubrica rubrica, VistaPrincipale view) {
        this.rubrica = rubrica;
        this.view = view;

        this.view.getListaPersone().addMouseListener(new MouseAdapter() {
            // Variabile per tenere traccia dell'ultimo elemento selezionato
            int lastSelectedIndex = -1;

            @Override
            public void mouseClicked(MouseEvent e) {
                JList<Persona> list = view.getListaPersone(); // Ottieni la JList corretta
                int index = list.locationToIndex(e.getPoint()); // Ottieni l'indice dell'elemento cliccato

                if (index != -1 && index == lastSelectedIndex) {
                    // Deseleziona l'elemento se è lo stesso selezionato precedentemente
                    list.clearSelection();
                    lastSelectedIndex = -1; // Resetta l'ultimo indice selezionato
                } else {
                    // Aggiorna l'ultimo indice selezionato
                    lastSelectedIndex = list.getSelectedIndex();
                }
            }
        });
        // Associa azioni ai bottoni
        this.view.getBottoneAggiungi().addActionListener(new NuovaPersonaListener());
        this.view.getBottoneModifica().addActionListener(new ModificaPersonaListener());
        this.view.getBottoneElimina().addActionListener(new EliminaPersonaListener());
    }


    // Listener per aggiungere una nuova persona
    class NuovaPersonaListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            // Apre l'editor con i campi vuoti
            EditorPersona editor = new EditorPersona();
            editor.setVisible(true);

// Listener per il bottone "Salva"
            editor.getSalvaButton().addActionListener(event -> {
                // Recupera i dati inseriti nell'editor
                String nome = editor.getNome();
                String cognome = editor.getCognome();
                String indirizzo = editor.getIndirizzo();
                String telefono = editor.getTelefono();
                String etaString = editor.getEta();  // L'età è ancora una stringa in questa fase

                // Verifica se i campi sono vuoti
                if (nome.isEmpty() || cognome.isEmpty() || indirizzo.isEmpty() || telefono.isEmpty() || etaString.isEmpty()) {
                    JOptionPane.showMessageDialog(editor, "Tutti i campi devono essere compilati.", "Errore", JOptionPane.ERROR_MESSAGE);
                    return; // Interrompe il salvataggio
                }

                // Verifica se l'età è un numero valido
                int eta;
                try {
                    eta = Integer.parseInt(etaString);  // Conversione da stringa a intero
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(editor, "L'età deve essere un numero valido.", "Errore", JOptionPane.ERROR_MESSAGE);
                    return; // Interrompe il salvataggio
                }

                if (!isValidPhoneNumber(telefono)) {
                    JOptionPane.showMessageDialog(editor, "Il numero di telefono non è valido. Può contenere solo cifre e opzionalmente un segno '+' all'inizio.", "Errore", JOptionPane.ERROR_MESSAGE);
                    return; // Interrompe il salvataggio
                }

                // Crea una nuova persona
                Persona nuovaPersona = new Persona(nome, cognome, indirizzo, telefono, eta);  // Passa l'età come intero

                // Aggiunge la persona alla rubrica
                rubrica.aggiungiPersona(nuovaPersona);

                // Aggiorna la vista principale
                view.getModelloLista().addElement(nuovaPersona);

                // Chiude l'editor
                editor.dispose();
            });


            // Listener per il bottone "Annulla"
            editor.getAnnullaButton().addActionListener(event -> {
                // Chiude la finestra senza salvare
                editor.dispose();
            });
        }
    }

    // Listener per modificare una persona esistente
    class ModificaPersonaListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            // Ottieni l'indice dell'elemento selezionato nella JList
            int selectedIndex = view.getListaPersone().getSelectedIndex();

            // Controlla se un elemento è stato selezionato
            if (selectedIndex != -1) {
                // Persona selezionata
                Persona personaSelezionata = view.getModelloLista().getElementAt(selectedIndex);
                Persona personaDaModificare = rubrica.cercaPersonaPerID(personaSelezionata.getID());

                // Apri l'editor con i dati della persona selezionata
                EditorPersona editor = new EditorPersona(personaDaModificare);
                editor.setVisible(true);

                // Listener per "Salva" modifiche
                editor.getSalvaButton().addActionListener(event -> {
                    // Verifica se i campi sono vuoti
                    if (editor.getNome().isEmpty() || editor.getCognome().isEmpty() || editor.getIndirizzo().isEmpty() || editor.getTelefono().isEmpty() || editor.getEta().isEmpty()) {
                        JOptionPane.showMessageDialog(editor, "Tutti i campi devono essere compilati.", "Errore", JOptionPane.ERROR_MESSAGE);
                        return; // Interrompi il salvataggio
                    }

                    // Verifica se l'età è un numero valido
                    int eta;
                    try {
                        eta = Integer.parseInt(editor.getEta());
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(editor, "L'età deve essere un numero valido.", "Errore", JOptionPane.ERROR_MESSAGE);
                        return; // Interrompi il salvataggio
                    }

                    if (!isValidPhoneNumber(editor.getTelefono())) {
                        JOptionPane.showMessageDialog(editor, "Il numero di telefono non è valido. Può contenere solo cifre e opzionalmente un segno '+'.", "Errore", JOptionPane.ERROR_MESSAGE);
                        return; // Interrompe il salvataggio
                    }

                    // Aggiorna i dati della persona, incluso il campo eta come int
                    personaSelezionata.setNome(editor.getNome());
                    personaSelezionata.setCognome(editor.getCognome());
                    personaSelezionata.setIndirizzo(editor.getIndirizzo());
                    personaSelezionata.setTelefono(editor.getTelefono());
                    personaSelezionata.setEta(eta);  // Salva l'età come intero

                    // Aggiorna la rubrica
                    rubrica.modificaPersona(personaSelezionata);

                    // Aggiorna la vista principale
                    view.getModelloLista().set(selectedIndex, personaSelezionata);

                    // Chiude l'editor
                    editor.dispose();
                });


                // Listener per "Annulla"
                editor.getAnnullaButton().addActionListener(event -> {
                        // Chiude l'editor senza salvare
                        editor.dispose();
                });

            } else {
                // Nessuna persona selezionata
                JOptionPane.showMessageDialog(view, "Seleziona una persona da modificare.");
            }
        }
    }


    // Listener per eliminare una persona esistente
    class EliminaPersonaListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            int selectedIndex = view.getListaPersone().getSelectedIndex();
            if (selectedIndex != -1) {
                // Persona selezionata
                Persona personaSelezionata = view.getModelloLista().getElementAt(selectedIndex);
                int conferma = JOptionPane.showConfirmDialog(view, "Eliminare la persona " + personaSelezionata.getNome() + " " + personaSelezionata.getCognome() + "?", "Conferma Eliminazione", JOptionPane.YES_NO_OPTION);

                if (conferma == JOptionPane.YES_OPTION) {
                    // Rimuove la persona dalla rubrica
                    rubrica.rimuoviPersona(personaSelezionata);

                    // Aggiorna la vista principale
                    view.getModelloLista().remove(selectedIndex);
                }
            } else {
                // Nessuna persona selezionata
                JOptionPane.showMessageDialog(view, "Seleziona una persona da eliminare.");
            }
        }
    }

    // Metodo per validare il numero di telefono
    private boolean isValidPhoneNumber(String telefono) {
        // Consenti solo numeri, il simbolo "+" (facoltativo all'inizio), e spazi o trattini
        String regex = "^[+]?\\d{1,3}?[-.\\s]?\\d{1,14}(?:x\\d{1,4})?$";
        return telefono.matches(regex);
    }
}
