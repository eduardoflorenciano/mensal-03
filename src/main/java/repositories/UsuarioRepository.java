package repositories;

import config.EntityManagerConfiguracao;
import entity.Usuario;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

import java.util.List;

public class UsuarioRepository {

    public void create(Usuario usuario) {
        EntityManager em = EntityManagerConfiguracao.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(usuario);
            tx.commit();
        } catch (RuntimeException e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    public void update(Usuario usuario) {
        EntityManager em = EntityManagerConfiguracao.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(usuario);
            tx.commit();
        } catch (RuntimeException e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    public void delete(Long id) {
        EntityManager em = EntityManagerConfiguracao.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            Usuario u = em.find(Usuario.class, id);
            if (u != null) em.remove(u);
            tx.commit();
        } catch (RuntimeException e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    public Usuario findById(Long id) {
        EntityManager em = EntityManagerConfiguracao.getEntityManager();
        try {
            return em.find(Usuario.class, id);
        } finally {
            em.close();
        }
    }

    public Usuario findByEmail(String email) {
        EntityManager em = EntityManagerConfiguracao.getEntityManager();
        try {
            List<Usuario> result = em.createQuery(
                            "FROM Usuario u WHERE LOWER(u.email) = LOWER(:email)", Usuario.class)
                    .setParameter("email", email)
                    .getResultList();
            return result.isEmpty() ? null : result.get(0);
        } finally {
            em.close();
        }
    }

    public List<Usuario> findAll() {
        EntityManager em = EntityManagerConfiguracao.getEntityManager();
        try {
            return em.createQuery("FROM Usuario u ORDER BY u.id", Usuario.class).getResultList();
        } finally {
            em.close();
        }
    }

    public boolean emailJaExiste(String email) {
        EntityManager em = EntityManagerConfiguracao.getEntityManager();
        try {
            Long count = em.createQuery(
                            "SELECT COUNT(u) FROM Usuario u WHERE LOWER(u.email) = LOWER(:email)", Long.class)
                    .setParameter("email", email)
                    .getSingleResult();
            return count > 0;
        } finally {
            em.close();
        }
    }

    public boolean emailJaExisteParaOutro(String email, Long idAtual) {
        EntityManager em = EntityManagerConfiguracao.getEntityManager();
        try {
            Long count = em.createQuery(
                            "SELECT COUNT(u) FROM Usuario u WHERE LOWER(u.email) = LOWER(:email) AND u.id <> :id", Long.class)
                    .setParameter("email", email)
                    .setParameter("id", idAtual)
                    .getSingleResult();
            return count > 0;
        } finally {
            em.close();
        }
    }

    public long contarUsuarios() {
        EntityManager em = EntityManagerConfiguracao.getEntityManager();
        try {
            return em.createQuery("SELECT COUNT(u) FROM Usuario u", Long.class).getSingleResult();
        } finally {
            em.close();
        }
    }
}
