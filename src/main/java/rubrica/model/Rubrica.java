package rubrica.model;
import java.util.ArrayList;
import java.util.Vector;

public class Rubrica {
    private Vector<Persona> rubrica;

    public Rubrica(Vector<Persona> rubrica) {
        this.rubrica = rubrica;
    }

    public Vector<Persona> getRubrica() {
        return rubrica;
    }

    public void setRubrica(Vector<Persona> rubrica) {
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

    public void modificaPersona(Persona p) {
        rimuoviPersona(p.getID());
        rubrica.add(p);
    }

    public void rimuoviPersona(long id) {
        rubrica.removeIf(p -> p.getID() == (id));
    }

    public void stampaPersone() {
        for (Persona p : rubrica) {
            System.out.println(p);
        }
    }
}
