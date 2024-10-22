package rubrica.controller;

import rubrica.model.Persona;
import rubrica.service.PersonaService;
import rubrica.utils.PersonaDuplicataException;
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
    private PersonaService personaService;
    private VistaPrincipale view;

    public RubricaController(PersonaService personaService, VistaPrincipale view) {
        this.personaService = personaService;
        this.view = view;

        this.view.getListaPersone().addMouseListener(new MouseAdapter() {
            int lastSelectedIndex = -1;

            @Override
            public void mouseClicked(MouseEvent e) {
                JList<Persona> list = view.getListaPersone();
                int index = list.locationToIndex(e.getPoint());

                if (index != -1 && index == lastSelectedIndex) {
                    list.clearSelection();
                    lastSelectedIndex = -1;
                } else {
                    lastSelectedIndex = list.getSelectedIndex();
                }
            }
        });

        this.view.getBottoneAggiungi().addActionListener(new NuovaPersonaListener());
        this.view.getBottoneModifica().addActionListener(new ModificaPersonaListener());
        this.view.getBottoneElimina().addActionListener(new EliminaPersonaListener());
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
                    personaService.aggiungiPersona(nuovaPersona);
                } catch (PersonaDuplicataException ex) {
                    throw new RuntimeException(ex);
                }

                view.getModelloLista().addElement(nuovaPersona);
                editor.dispose();
            });

            editor.getAnnullaButton().addActionListener(event -> editor.dispose());
        }
    }

    // Listener per modificare una persona esistente
    class ModificaPersonaListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            int selectedIndex = view.getListaPersone().getSelectedIndex();
            if (selectedIndex != -1) {
                Persona personaSelezionata = view.getModelloLista().getElementAt(selectedIndex);
                System.out.println("id personaSelezionata:" + personaSelezionata.getID());
                Persona personaDaModificare = personaService.cercaPersonaPerID(personaSelezionata.getID());

                System.out.println("id persona da modificare:" + personaDaModificare.getID());
                EditorPersona editor = new EditorPersona(personaDaModificare);
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

                    Persona personaModificata = new Persona(nome, cognome, indirizzo, telefono, eta);
                    try {
                        personaService.modificaPersona(personaModificata);
                    } catch (PersonaDuplicataException ex) {
                        throw new RuntimeException(ex);
                    }

                    view.getModelloLista().setElementAt(personaModificata, selectedIndex);
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
            int selectedIndex = view.getListaPersone().getSelectedIndex();

            // Verifica se è stata selezionata una persona
            if (selectedIndex != -1) {
                Persona personaSelezionata = view.getModelloLista().getElementAt(selectedIndex);
                String nomeCompleto = personaSelezionata.getNome() + " " + personaSelezionata.getCognome();

                // Mostra una finestra di conferma prima di eliminare
                int conferma = JOptionPane.showConfirmDialog(
                        view,
                        "Eliminare la persona " + nomeCompleto + "?",
                        "Conferma Eliminazione",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.WARNING_MESSAGE
                );

                // Se l'utente conferma (preme "Si")
                if (conferma == JOptionPane.YES_OPTION) {
                    System.out.println("Rimozione della persona con ID: " + personaSelezionata.getID());
                    personaService.rimuoviPersona(personaSelezionata);

                    // Aggiorna la lista rimuovendo la persona
                    view.getModelloLista().remove(selectedIndex);
                }
                // Se l'utente preme "No", non accade nulla
            } else {
                // Mostra un messaggio di errore se nessuna persona è selezionata
                JOptionPane.showMessageDialog(
                        view,
                        "Seleziona una persona da eliminare.",
                        "Errore",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        }
    }


    private boolean isValidPhoneNumber(String phoneNumber) {
        return phoneNumber.matches("\\+?[0-9 ]+");
    }
}
