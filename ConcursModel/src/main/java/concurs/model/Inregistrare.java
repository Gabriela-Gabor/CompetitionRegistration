package concurs.model;

public class Inregistrare extends Entitate<Integer>{

    private Copil copil;
    private Proba proba;

    public Inregistrare(Copil copil, Proba proba) {
        this.copil = copil;
        this.proba = proba;
    }

    public Copil getCopil() {
        return copil;
    }

    public void setCopil(Copil copil) {
        this.copil = copil;
    }

    public Proba getProba() {
        return proba;
    }

    public void setProba(Proba proba) {
        this.proba = proba;
    }

    @Override
    public String toString() {
        return copil.getNume() + " " + proba.getDenumire()+" "+proba.getVarstaMinima()+"-"+proba.getVarstaMaxima();

    }
}
