package config;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class EntityManagerConfiguracao {

    private static EntityManagerFactory emf;

    private EntityManagerConfiguracao() {
    }

    private static synchronized EntityManagerFactory getFactory() {
        if (emf == null) {
            SessionFactory sessionFactory = new Configuration()
                    .configure("hibernate.cfg.xml")
                    .buildSessionFactory();
            emf = sessionFactory.unwrap(EntityManagerFactory.class);
        }
        return emf;
    }

    public static EntityManager getEntityManager() {
        return getFactory().createEntityManager();
    }

    public static void fechar() {
        if (emf != null && emf.isOpen()) {
            emf.close();
        }
    }
}
