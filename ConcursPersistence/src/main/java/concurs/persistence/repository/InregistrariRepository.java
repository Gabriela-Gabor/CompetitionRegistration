package concurs.persistence.repository;


import concurs.model.Copil;
import concurs.model.Inregistrare;
import concurs.model.Proba;
import concurs.persistence.ICopiiRepository;
import concurs.persistence.IInregistrariRepository;
import concurs.persistence.IProbeRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class InregistrariRepository implements IInregistrariRepository {

    private JdbcUtils dbUtils;

    private ICopiiRepository repoCopii;
    private IProbeRepository repoProbe;

    public InregistrariRepository(Properties props, ICopiiRepository repoCopii, IProbeRepository repoProbe) {
        dbUtils = new JdbcUtils(props);
        this.repoCopii = repoCopii;
        this.repoProbe = repoProbe;
    }

    @Override
    public Inregistrare findOne(Integer integer) {

        Connection connection = dbUtils.getConnection();
        try (PreparedStatement preStmt = connection.prepareStatement("SELECT * FROM Inregistrari WHERE Id=?")) {
            preStmt.setInt(1, integer);
            ResultSet result = preStmt.executeQuery();
            if (result.next()) {
                int id = result.getInt("Id");
                int idCopil = result.getInt("Copil");
                int idProba = result.getInt("Proba");

                Copil copil = repoCopii.findOne(idCopil);
                Proba proba = repoProbe.findOne(idProba);
                Inregistrare inregistrare = new Inregistrare(copil, proba);
                inregistrare.setId(id);

                return inregistrare;
            }
        } catch (SQLException ex) {

            System.out.println("Error DB " + ex);
        }

        return null;
    }

    @Override
    public Iterable<Inregistrare> findAll() {

        Connection connection = dbUtils.getConnection();
        List<Inregistrare> inregistrari = new ArrayList<>();
        try (PreparedStatement preStmt = connection.prepareStatement("SELECT * FROM Inregistrari ")) {
            try (ResultSet result = preStmt.executeQuery()) {
                while (result.next()) {

                    int idCopil = result.getInt("Copil");
                    int idProba = result.getInt("Proba");
                    int id = result.getInt("Id");

                    Copil copil = repoCopii.findOne(idCopil);
                    Proba proba = repoProbe.findOne(idProba);
                    Inregistrare inregistrare = new Inregistrare(copil, proba);
                    inregistrare.setId(id);
                    inregistrari.add(inregistrare);
                }
            }
        } catch (SQLException ex) {

            System.out.println("Error DB " + ex);
        }

        return inregistrari;
    }

    @Override
    public void save(Inregistrare entity) {

        Connection connection = dbUtils.getConnection();
        try (PreparedStatement preStmt = connection.prepareStatement("INSERT INTO Inregistrari(Copil,Proba) VALUES (?,?)")) {
            preStmt.setInt(1, entity.getCopil().getId());
            preStmt.setInt(2, entity.getProba().getId());
            int result = preStmt.executeUpdate();
        } catch (SQLException ex) {

            System.out.println("Error DB " + ex);
        }


    }

    @Override
    public void delete(Integer integer) {

        Connection connection = dbUtils.getConnection();
        try (PreparedStatement preStmt = connection.prepareStatement("DELETE FROM Inregistrare WHERE Id=?")) {
            preStmt.setInt(1, integer);
            int result = preStmt.executeUpdate();
        } catch (SQLException ex) {

            System.out.println("Error DB " + ex);
        }


    }

    @Override
    public void update(Inregistrare entity) {

    }

    public int findNrInregistrari(int idCopil) {

        Connection connection = dbUtils.getConnection();
        try (PreparedStatement preStmt = connection.prepareStatement("SELECT COUNT(Id) FROM Inregistrari WHERE Copil=?")) {
            preStmt.setInt(1, idCopil);
            ResultSet result = preStmt.executeQuery();
            if (result.next()) {
                int nr = result.getInt("COUNT(Id)");
                return nr;
            }
        } catch (SQLException ex) {

            System.out.println("Error DB " + ex);
        }

        return 0;
    }

    public List<Copil> findCopii(int idProba) {

        Connection connection = dbUtils.getConnection();
        List<Copil> copii = new ArrayList<>();
        try (PreparedStatement preStmt = connection.prepareStatement("SELECT * FROM Inregistrari WHERE Proba=?")) {
            preStmt.setInt(1, idProba);
            try (ResultSet result = preStmt.executeQuery()) {
                while (result.next()) {
                    int idCopil = result.getInt("Copil");
                    Copil copil = repoCopii.findOne(idCopil);
                    copii.add(copil);
                }
            }
        } catch (SQLException ex) {

            System.out.println("Error DB " + ex);
        }

        return copii;
    }
}

