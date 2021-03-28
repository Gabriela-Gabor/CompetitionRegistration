package concurs.model;

public class Utilizator extends Entitate<String>{

    private String numeUtilizator;
    private String parola;

    public Utilizator(String numeUtilizator, String parola) {
        this.numeUtilizator = numeUtilizator;
        this.parola = parola;
    }

    public String getNumeUtilizator() {
        return numeUtilizator;
    }

    public String getParola() {
        return parola;
    }

    public void setNumeUtilizator(String numeUtilizator) {
        this.numeUtilizator = numeUtilizator;
    }

    public void setParola(String parola) {
        this.parola = parola;
    }

    @Override
    public String toString()
    {
        return numeUtilizator+" "+parola;
    }
}
