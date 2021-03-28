package concurs.services;

import concurs.model.Copil;
import concurs.model.Inregistrare;
import concurs.model.Proba;
import concurs.model.Utilizator;

import java.util.ArrayList;
import java.util.List;

public interface IConcursService {

    void login(Utilizator u,IConcursObserver client) throws ConcursException ;

    void logout(Utilizator u,IConcursObserver client) throws ConcursException ;

    void salveazaCopil(String nume, int varsta) throws ConcursException;

    void inregistreaza(String nume, int varsta, String proba) throws ConcursException;

    public List<Copil> cauta(String proba, int varsta)throws ConcursException ;

    public List<Proba> findProbe();

    public List<String> findProbeParticipanti() throws ConcursException;
}
