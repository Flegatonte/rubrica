package rubrica.model;
import java.util.ArrayList;
import java.util.List;

public class Rubrica {
    private List<Persona> rubrica;

    public Rubrica() {
        this.rubrica = new ArrayList<>();
    }

    public Rubrica(List<Persona> rubrica) {
        this.rubrica = rubrica;
    }

    public List<Persona> getRubrica() {
        return rubrica;
    }

    public void setRubrica(List<Persona> rubrica) {
        this.rubrica = rubrica;
    }

    public void aggiungiPersona(Persona persona) {
        rubrica.add(persona);
    }

    public Persona cercaPersonaPerID(long id) {
        for (Persona p : rubrica) {
            if (p.getID() == id) {
                return p;
            }
        }
        return null;
    }

    public void modificaPersona(Persona personaModificata) {
        for (Persona persona : rubrica) {
            if (persona.getID() == personaModificata.getID()) {
                persona.setNome(personaModificata.getNome());
                persona.setCognome(personaModificata.getCognome());
                persona.setIndirizzo(personaModificata.getIndirizzo());
                persona.setTelefono(personaModificata.getTelefono());
                persona.setEta(personaModificata.getEta());
                break;
            }
        }
    }

    public void rimuoviPersona(Persona personaDaEliminare) {
        rubrica.removeIf(p -> p.getID() == (personaDaEliminare.getID()));
    }

    public void stampaPersone() {
        for (Persona p : rubrica) {
            System.out.println(p);
        }
    }
}
