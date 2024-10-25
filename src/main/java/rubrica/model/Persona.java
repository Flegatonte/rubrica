package rubrica.model;

public class Persona {
    private long id; // ID della persona, assegnato dal database
    private String nome;
    private String cognome;
    private String indirizzo;
    private String telefono;
    private int eta;

    // Costruttore senza ID (usato per l'aggiunta)
    public Persona(String nome, String cognome, String indirizzo, String telefono, int eta) {
        this.nome = nome;
        this.cognome = cognome;
        this.indirizzo = indirizzo;
        this.telefono = telefono;
        this.eta = eta;
    }

    // Costruttore con ID (usato per il recupero)
    public Persona(String nome, String cognome, String indirizzo, String telefono, int eta, long id) {
        this.id = id;
        this.nome = nome;
        this.cognome = cognome;
        this.indirizzo = indirizzo;
        this.telefono = telefono;
        this.eta = eta;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Persona)) return false;
        Persona p = (Persona) obj;
        return this.id == p.getID();
    }

    @Override
    public int hashCode() {
        return Long.hashCode(id);
    }

    @Override
    public String toString() {
        return nome + ' ' + cognome + ", " + eta + ", " + indirizzo + ", " + telefono;
    }

    public long getID() {
        return id;
    }

    // Setter per l'ID
    public void setID(long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCognome() {
        return cognome;
    }

    public void setCognome(String cognome) {
        this.cognome = cognome;
    }

    public String getIndirizzo() {
        return indirizzo;
    }

    public void setIndirizzo(String indirizzo) {
        this.indirizzo = indirizzo;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public int getEta() {
        return eta;
    }

    public void setEta(int eta) {
        this.eta = eta;
    }
}
