package concurs.model;

public class Proba extends Entitate<Integer>{

    private String denumire;
    private int varstaMinima;
    private int varstaMaxima;

    public Proba(String denumire, int varstaMinima, int varstaMaxima) {
        this.denumire = denumire;
        this.varstaMinima = varstaMinima;
        this.varstaMaxima = varstaMaxima;
    }

    public String getDenumire() {
        return denumire;
    }

    public void setDenumire(String denumire) {
        this.denumire = denumire;
    }

    public int getVarstaMinima() {
        return varstaMinima;
    }

    public void setVarstaMinima(int varstaMinima) {
        this.varstaMinima = varstaMinima;
    }

    public int getVarstaMaxima() {
        return varstaMaxima;
    }

    public void setVarstaMaxima(int varstaMaxima) {
        this.varstaMaxima = varstaMaxima;
    }

    @Override
    public String toString()
    {
        return denumire+" [ "+varstaMinima+" , "+varstaMaxima+" ]";
    }
}
