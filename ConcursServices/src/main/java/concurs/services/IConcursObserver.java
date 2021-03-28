package concurs.services;

import concurs.model.Inregistrare;

public interface IConcursObserver {

    void participantSalvat(Inregistrare inregistrare) throws ConcursException;
}
