package concurs.persistence.repository;


import concurs.model.Utilizator;
import concurs.persistence.IUtilizatoriRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;


public class UtilizatoriRepository implements IUtilizatoriRepository {

    private JdbcUtils dbUtils;


    public UtilizatoriRepository(Properties props) {
        dbUtils=new JdbcUtils(props);
    }

    @Override
    public Utilizator findOne(String s) {

        Connection connection = dbUtils.getConnection();
        try (PreparedStatement preStmt = connection.prepareStatement("SELECT * FROM Utilizatori WHERE NumeUtilizator=?")) {
            preStmt.setString(1, s);
            ResultSet result = preStmt.executeQuery();
            if (result.next()) {
                String numeUtilizator = result.getString("NumeUtilizator");
                String parola = result.getString("Parola");
                Utilizator utilizator = new Utilizator(numeUtilizator, parola);
                utilizator.setId(numeUtilizator);

                return utilizator;
            }
        } catch (SQLException ex) {

            System.out.println("Error DB " + ex);
        }

        return null;

    }

    @Override
    public Iterable<Utilizator> findAll() {

        Connection connection = dbUtils.getConnection();
        List<Utilizator> utilizatori = new ArrayList<>();
        try (PreparedStatement preStmt = connection.prepareStatement("SELECT * FROM Utilizatori")) {
            try (ResultSet result = preStmt.executeQuery()) {

                while (result.next()) {
                    String numeUtilizator = result.getString("NumeUtilizator");
                    String parola = result.getString("Parola");
                    Utilizator utilizator = new Utilizator(numeUtilizator, parola);
                    utilizator.setId(numeUtilizator);
                    utilizatori.add(utilizator);
                }
            }
        } catch (SQLException ex) {

            System.out.println("Error DB " + ex);
        }

        return utilizatori;

    }

    @Override
    public void save(Utilizator entity) {

        Connection connection = dbUtils.getConnection();
        try (PreparedStatement preStmt = connection.prepareStatement("INSERT INTO Utilizatori VALUES (?,?)")) {
            preStmt.setString(1, entity.getNumeUtilizator());
            preStmt.setString(2, entity.getParola());
            int result = preStmt.executeUpdate();

        } catch (SQLException ex) {

            System.out.println("Error DB " + ex);
        }


    }

    @Override
    public void delete(String s) {

        Connection connection = dbUtils.getConnection();
        try (PreparedStatement preStmt = connection.prepareStatement("DELETE FROM Utilizatori WHERE NumeUtilizator=?")) {
            preStmt.setString(1, s);
            int result = preStmt.executeUpdate();
        } catch (SQLException ex) {

            System.out.println("Error DB " + ex);
        }
    }

    @Override
    public void update(Utilizator entity) {

    }
}
