package rubrica; // o rubrica.application

import rubrica.controller.RubricaController;
import rubrica.model.Persona;
import rubrica.model.Rubrica;
import rubrica.view.VistaPrincipale;

import javax.swing.*;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        Rubrica rubrica = new Rubrica();
        rubrica.aggiungiPersona(new Persona("Gloria", "Fiammengo",
                "Via Moglianese 338, Peseggia", "+39 3491861346", 27));
        SwingUtilities.invokeLater(() -> {
            VistaPrincipale vista = new VistaPrincipale(rubrica);
            vista.aggiornaLista(rubrica);
            RubricaController controller = new RubricaController(rubrica, vista);

            vista.setVisible(true);
        });
    }
}
