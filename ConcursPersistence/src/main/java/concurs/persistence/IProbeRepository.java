package concurs.persistence;


import concurs.model.Proba;

public interface IProbeRepository extends IRepository<Integer, Proba> {

    public Proba findByNumeVarsta(String nume,int varsta);

}
