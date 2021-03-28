package concurs.persistence;


import concurs.model.Copil;

public interface ICopiiRepository extends IRepository<Integer, Copil> {

    public Copil findByNumeVarsta(String nume,int varsta);

}
