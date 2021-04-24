package concurs.model;

import javax.persistence.Entity;
import javax.persistence.Table;


public class Copil extends Entitate<Integer> {

    private String nume;
    private int varsta;

    public Copil(String nume, int varsta) {
        this.nume = nume;
        this.varsta = varsta;
    }

    public String getNume() {
        return nume;
    }

    public void setNume(String nume) {
        this.nume = nume;
    }

    public int getVarsta() {
        return varsta;
    }

    public void setVarsta(int varsta) {
        this.varsta = varsta;
    }

    @Override
    public String toString(){
        return nume+" "+varsta+" ani";
    }
}
