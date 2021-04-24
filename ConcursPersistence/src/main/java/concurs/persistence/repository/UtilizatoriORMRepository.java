package concurs.persistence.repository;

import concurs.model.Utilizator;
import concurs.persistence.IRepository;
import concurs.persistence.IUtilizatoriRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;


public class UtilizatoriORMRepository implements IUtilizatoriRepository {

    private JdbcUtils dbUtils;

    public UtilizatoriORMRepository(Properties props) {

        dbUtils = new JdbcUtils(props);
        initialize();
    }

    @Override
    public Utilizator findOne(String s) {
        List<Utilizator> utilizatori = null;
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = null;
            try {
                tx = session.beginTransaction();
                utilizatori = session.createQuery("FROM Utilizator WHERE NumeUtilizator = :S", Utilizator.class).setParameter("S", s).list();
                System.out.println(utilizatori);
                tx.commit();
            } catch (RuntimeException ex) {
                if (tx != null) {
                    tx.rollback();
                    ;
                }
            }
        }
            return utilizatori.get(0);


    }

    @Override
    public Iterable<Utilizator> findAll() {
        List<Utilizator> utilizatori = new ArrayList<>();
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = null;
            try {
                tx = session.beginTransaction();
                utilizatori = session.createQuery("from Utilizator", Utilizator.class).list();


                tx.commit();
            } catch (RuntimeException ex) {
                if (tx != null) {
                    tx.rollback();
                    ;
                }
            }
        }
        return utilizatori;
    }

    @Override
    public void save(Utilizator entity) {

    }

    @Override
    public void delete(String s) {

    }

    @Override
    public void update(Utilizator entity) {

    }

    static SessionFactory sessionFactory;

    static void initialize() {
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure() // configures settings from hibernate.cfg.xml
                .build();
        try {
            sessionFactory = new MetadataSources( registry ).buildMetadata().buildSessionFactory();
        }
        catch (Exception e) {
            System.err.println("Exceptie "+e);
            StandardServiceRegistryBuilder.destroy( registry );
        }
    }

    static void close() {
        if (sessionFactory != null)
            sessionFactory.close();
    }
}


