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
        return personaDAO.recuperaTutteLePersone().stream()
                .anyMatch(p -> p.getNome().equals(persona.getNome()) &&
                        p.getCognome().equals(persona.getCognome()) &&
                        p.getID() != persona.getID());
    }

    private boolean isDuplicatoModifica(Persona persona) {
        return personaDAO.recuperaTutteLePersone().stream()
                .anyMatch(p -> p.getNome().equals(persona.getNome()) &&
                        p.getCognome().equals(persona.getCognome()) &&
                        p.getTelefono().equals(persona.getTelefono()) &&
                        p.getID() != persona.getID());
    }
}
