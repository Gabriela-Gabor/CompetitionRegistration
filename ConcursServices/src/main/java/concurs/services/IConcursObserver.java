package concurs.services;

import concurs.model.Inregistrare;

public interface IConcursObserver {

    void participantSalvat(int idProba) throws ConcursException;
}
