package concurs.network.dto;

import java.io.Serializable;

public class InregistrareDTO implements Serializable {

    private String nume;
    private int varsta;
    private String denumire;

    public InregistrareDTO(String nume, int varsta, String denumire) {
        this.nume = nume;
        this.varsta = varsta;
        this.denumire = denumire;
    }

    public String getNume() {
        return nume;
    }

    public int getVarsta() {
        return varsta;
    }

    public String getDenumire() {
        return denumire;
    }
}
