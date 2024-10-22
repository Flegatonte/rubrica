package rubrica.service;

import rubrica.persistance.PersonaDAO;
import rubrica.model.Persona;
import rubrica.utils.PersonaDuplicataException;

import java.util.List;

public class PersonaService {
    private PersonaDAO personaDAO;

    public PersonaService(PersonaDAO personaDAO) {
        this.personaDAO = personaDAO;
    }

    public void aggiungiPersona(Persona persona) throws PersonaDuplicataException {
        if (isDuplicato(persona)) {
            throw new PersonaDuplicataException("Persona già esistente!");
        }
        personaDAO.aggiungiPersona(persona);
    }

    public Persona cercaPersonaPerID(long id) {
        return personaDAO.cercaPersonaPerID(id);
    }

    public List<Persona> recuperaTutteLePersone() {
        return personaDAO.recuperaTutteLePersone();
    }

    public void modificaPersona(Persona persona) throws PersonaDuplicataException {
        if (isDuplicatoModifica(persona)) {
            throw new PersonaDuplicataException("Persona già esistente!");
        }
        personaDAO.modificaPersona(persona);
    }

    public void rimuoviPersona(Persona persona) {
        personaDAO.rimuoviPersona(persona);
    }

    private boolean isDuplicato(Persona persona) {
        List<Persona> persone = personaDAO.recuperaTutteLePersone();
        for (Persona p : persone) {
            if (p.getNome().equals(persona.getNome()) &&
                    p.getCognome().equals(persona.getCognome()) &&
                    !(p.getID() == persona.getID())) {
                return true;
            }
        }
        return false;
    }

    private boolean isDuplicatoModifica(Persona persona) {
        List<Persona> persone = personaDAO.recuperaTutteLePersone(); // Ottieni tutte le persone dal database
        for (Persona p : persone) {
            // Controlla se nome, cognome e telefono sono gli stessi e se non è la stessa persona
            if (p.getNome().equals(persona.getNome()) &&
                    p.getCognome().equals(persona.getCognome()) &&
                    p.getTelefono().equals(persona.getTelefono()) &&
                    !(p.getID() == persona.getID())) {
                return true; // Trovato un duplicato
            }
        }
        return false; // Nessun duplicato trovato
    }
}
