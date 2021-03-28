package concurs.persistence.repository;

import concurs.model.Proba;
import concurs.persistence.IProbeRepository;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class ProbeRepository implements IProbeRepository {

    private JdbcUtils dbUtils;


    public ProbeRepository(Properties props) {
        dbUtils=new JdbcUtils(props);
    }

    @Override
    public Proba findOne(Integer integer) {

        Connection connection = dbUtils.getConnection();
        try (PreparedStatement preStmt = connection.prepareStatement("SELECT * FROM  Probe WHERE Id=?")) {
            preStmt.setInt(1, integer);
            ResultSet result = preStmt.executeQuery();
            if (result.next()) {
                int id=result.getInt("Id");
                String denumire = result.getString("Denumire");
                int varstaMinima = result.getInt("VarstaMinima");
                int varstaMaxima = result.getInt("VarstaMaxima");

                Proba proba=new Proba(denumire,varstaMinima,varstaMaxima);
                proba.setId(id);

                return proba;
            }

        } catch (SQLException ex) {

            System.out.println("Error DB " + ex);
        }


        return null;
    }

    @Override
    public Iterable<Proba> findAll() {

        Connection connection = dbUtils.getConnection();
        List<Proba> probe=new ArrayList<>();
        try (PreparedStatement preStmt = connection.prepareStatement("SELECT * FROM  Probe")) {
            try (ResultSet result = preStmt.executeQuery()) {
                while (result.next()) {
                    String denumire = result.getString("Denumire");
                    int varstaMinima = result.getInt("VarstaMinima");
                    int varstaMaxima = result.getInt("VarstaMaxima");
                    int id=result.getInt("Id");
                    Proba proba = new Proba(denumire, varstaMinima, varstaMaxima);
                    proba.setId(id);
                    probe.add(proba);


                }
            }

        } catch (SQLException ex) {

            System.out.println("Error DB " + ex);
        }


        return probe;
    }

    @Override
    public void save(Proba entity) {

        Connection connection = dbUtils.getConnection();
        try (PreparedStatement preStmt = connection.prepareStatement("INSERT INTO Probe(Denumire,VarstaMinima,VarstaMaxima) VALUES (?,?,?)")) {
            preStmt.setString(1, entity.getDenumire());
            preStmt.setInt(2, entity.getVarstaMinima());
            preStmt.setInt(3, entity.getVarstaMaxima());
            int result = preStmt.executeUpdate();

        } catch (SQLException ex) {
             System.out.println("Error DB " + ex);
        }


    }

    @Override
    public void delete(Integer integer) {

        Connection connection = dbUtils.getConnection();
        try (PreparedStatement preStmt = connection.prepareStatement("DELETE FROM Probe WHERE Id=?")) {
            preStmt.setInt(1, integer);
            int result = preStmt.executeUpdate();

        } catch (SQLException ex) {

            System.out.println("Error DB " + ex);
        }
    }

    @Override
    public void update(Proba entity) {

    }

    public Proba findByNumeVarsta(String nume,int varsta)
    {

        Connection connection = dbUtils.getConnection();
        try (PreparedStatement preStmt = connection.prepareStatement("SELECT * FROM  Probe WHERE Denumire=? AND VarstaMinima<=? AND VarstaMaxima>=?")) {
            preStmt.setString(1,nume);
            preStmt.setInt(2,varsta);
            preStmt.setInt(3,varsta);
            ResultSet result = preStmt.executeQuery();
            if (result.next()) {
                int id=result.getInt("Id");
                String denumire = result.getString("Denumire");
                int varstaMinima = result.getInt("VarstaMinima");
                int varstaMaxima = result.getInt("VarstaMaxima");

                Proba proba=new Proba(denumire,varstaMinima,varstaMaxima);
                proba.setId(id);

                return proba;
            }

        } catch (SQLException ex) {

            System.out.println("Error DB " + ex);
        }


        return null;
    }
}
