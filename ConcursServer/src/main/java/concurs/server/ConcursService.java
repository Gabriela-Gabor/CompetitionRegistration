package concurs.server;

import concurs.model.Copil;
import concurs.model.Inregistrare;
import concurs.model.Proba;
import concurs.model.Utilizator;
import concurs.persistence.ICopiiRepository;
import concurs.persistence.IInregistrariRepository;
import concurs.persistence.IProbeRepository;
import concurs.persistence.IUtilizatoriRepository;
import concurs.services.ConcursException;
import concurs.services.IConcursObserver;
import concurs.services.IConcursService;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ConcursService implements IConcursService {

    private ICopiiRepository repoCopii;
    private IProbeRepository repoProbe;
    private IUtilizatoriRepository repoUtilizatori;
    private IInregistrariRepository repoInregistrari;

    private Map<String,IConcursObserver> loggedUsers;

    public ConcursService(ICopiiRepository repoCopii, IProbeRepository repoProbe, IUtilizatoriRepository repoUtilizatori, IInregistrariRepository repoInregistrari) {
        this.repoCopii = repoCopii;
        this.repoProbe = repoProbe;
        this.repoUtilizatori = repoUtilizatori;
        this.repoInregistrari = repoInregistrari;
        loggedUsers=new ConcurrentHashMap<>();
    }

    public synchronized void login(Utilizator u, IConcursObserver client) throws ConcursException {
        Utilizator utilizator = repoUtilizatori.findOne(u.getNumeUtilizator());
        if(utilizator!=null && utilizator.getParola().equals(u.getParola())){
            if(loggedUsers.get(utilizator.getNumeUtilizator())!=null){
                throw new ConcursException("Utilizatorul este deja logat!");
            }
            loggedUsers.put(utilizator.getNumeUtilizator(),client);
        }else
             throw new ConcursException("Autentificare esuata!");

    }
    public synchronized void logout(Utilizator u, IConcursObserver client) throws ConcursException {
        IConcursObserver localClient=loggedUsers.remove(u.getNumeUtilizator());
        if (localClient==null)
            throw new ConcursException("User "+u.getNumeUtilizator()+" nu este logat");

    }

    public void salveazaCopil(String nume,int varsta)  throws ConcursException
    {
        Copil copil=new Copil(nume,varsta);
        repoCopii.save(copil);
    }

    public void inregistreaza(String nume, int varsta, String proba) throws ConcursException {
        Proba p = repoProbe.findByNumeVarsta(proba, varsta);
        Copil c = repoCopii.findByNumeVarsta(nume, varsta);
        int nrInregistrari;
        if (p!=null && c != null) {
            nrInregistrari = repoInregistrari.findNrInregistrari(c.getId());
            if (nrInregistrari < 2) {
                Inregistrare inregistrare = new Inregistrare(c, p);
                repoInregistrari.save(inregistrare);
                notifyNewParticipanti(inregistrare);

            }
            else throw new ConcursException("Este inscris la doua probe!");
        }
        else throw new ConcursException("Nu s-a gasit copilul/proba");
    }


    public List<Copil> cauta(String proba, int varsta) throws ConcursException {
        Proba p = repoProbe.findByNumeVarsta(proba, varsta);
        List<Copil> copii = repoInregistrari.findCopii(p.getId());
        return copii;
    }

    public List<Proba> findProbe() {
        List<Proba> probe = new ArrayList<>();
        repoProbe.findAll().forEach(x -> probe.add(x));
        return probe;
    }

    public synchronized List<String> findProbeParticipanti()
    {
        List<Proba> probe=findProbe();
        List<String> list=new ArrayList<>();
        int nrParticipanti;
        for(Proba p : probe)
        {
            nrParticipanti=repoInregistrari.findCopii(p.getId()).size();
            list.add(p+"  -  "+nrParticipanti+" participanti");
        }
        return list;
    }

    private final int defaultThreadsNo=5;

    private void notifyNewParticipanti(Inregistrare inregistrare) throws ConcursException {
        Iterable<Utilizator> utilizatori=repoUtilizatori.findAll();

        ExecutorService executor= Executors.newFixedThreadPool(defaultThreadsNo);
        for(Utilizator u:utilizatori){
            IConcursObserver client=loggedUsers.get(u.getId());
            if (client!=null)
                executor.execute(() -> {
                    try {
                        client.participantSalvat(inregistrare.getProba().getId());
                    } catch (ConcursException e) {
                        System.err.println("Error notifying friend " + e);
                    }
                });
        }

        executor.shutdown();
    }
}
