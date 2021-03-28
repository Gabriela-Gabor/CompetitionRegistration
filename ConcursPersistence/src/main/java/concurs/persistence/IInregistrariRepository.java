package concurs.persistence;

import concurs.model.Copil;
import concurs.model.Inregistrare;

import java.util.List;

public interface IInregistrariRepository extends IRepository<Integer, Inregistrare> {


    public int findNrInregistrari(int idCopil);

    public List<Copil> findCopii(int idProba);
}
