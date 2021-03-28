package concurs.persistence.repository;

import concurs.model.Copil;
import concurs.persistence.ICopiiRepository;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class CopiiRepository implements ICopiiRepository {

    private JdbcUtils dbUtils;


    public CopiiRepository(Properties props) {
        dbUtils=new JdbcUtils(props);
    };


    @Override
    public Copil findOne(Integer integer) {

        Connection connection = dbUtils.getConnection();
        try (PreparedStatement preStmt = connection.prepareStatement("SELECT * FROM Copii WHERE Id=?")) {
            preStmt.setInt(1, integer);
            ResultSet result = preStmt.executeQuery();
            if (result.next()) {
                int id = result.getInt("Id");
                String nume = result.getString("Nume");
                int varsta = result.getInt("Varsta");

                Copil copil = new Copil(nume, varsta);
                copil.setId(id);

                return copil;
            }
        } catch (SQLException ex) {

            System.out.println("Error DB " + ex);
        }

        return null;
    }

    @Override
    public Iterable<Copil> findAll() {

        Connection connection = dbUtils.getConnection();
        List<Copil> copii = new ArrayList<>();
        try (PreparedStatement preStmt = connection.prepareStatement("SELECT * FROM Copii")) {
            try (ResultSet result = preStmt.executeQuery()) {
                while (result.next()) {
                    int id = result.getInt("Id");
                    String nume = result.getString("Nume");
                    int varsta = result.getInt("Varsta");

                    Copil copil = new Copil(nume, varsta);
                    copil.setId(id);
                    copii.add(copil);
                }
            }
        } catch (SQLException ex) {

            System.out.println("Error DB " + ex);
        }

        return copii;
    }

    @Override
    public void save(Copil entity) {

        Connection connection = dbUtils.getConnection();
        try (PreparedStatement preStmt = connection.prepareStatement("INSERT INTO Copii(Nume,Varsta) VALUES (?,?)")) {
            preStmt.setString(1, entity.getNume());
            preStmt.setInt(2, entity.getVarsta());
            int result = preStmt.executeUpdate();
        } catch (SQLException ex) {

            System.out.println("Error DB " + ex);
        }


    }

    @Override
    public void delete(Integer integer) {

        Connection connection = dbUtils.getConnection();
        try (PreparedStatement preStmt = connection.prepareStatement("DELETE FROM Copii WHERE Id=?")) {
            preStmt.setInt(1, integer);
            int result = preStmt.executeUpdate();
        } catch (SQLException ex) {

            System.out.println("Error DB " + ex);
        }


    }

    @Override
    public void update(Copil entity) {

    }

    public Copil findByNumeVarsta(String nume,int varsta)
    {

        Connection connection = dbUtils.getConnection();
        try (PreparedStatement preStmt = connection.prepareStatement("SELECT * FROM Copii WHERE Nume=? AND Varsta=?")) {
            preStmt.setString(1,nume);
            preStmt.setInt(2, varsta);
            ResultSet result = preStmt.executeQuery();
            if (result.next()) {
                int id = result.getInt("Id");
                String nume2 = result.getString("Nume");
                int varsta2 = result.getInt("Varsta");

                Copil copil = new Copil(nume2, varsta2);
                copil.setId(id);

                return copil;
            }
        } catch (SQLException ex) {

            System.out.println("Error DB " + ex);
        }

        return null;
    }
}
