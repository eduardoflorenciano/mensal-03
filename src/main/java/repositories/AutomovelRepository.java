package repositories;

import config.EntityManagerConfiguracao;
import entity.Automovel;
import entity.Cliente;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;

import java.util.List;

public class AutomovelRepository {

    public Automovel create(Automovel automovel) {
        EntityManager em = EntityManagerConfiguracao.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(automovel);
            tx.commit();
            return automovel;
        } catch (RuntimeException e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    public Automovel update(Automovel automovel) {
        EntityManager em = EntityManagerConfiguracao.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            Automovel atualizado = em.merge(automovel);
            tx.commit();
            return atualizado;
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
            Automovel automovel = em.find(Automovel.class, id);
            if (automovel != null) {
                em.remove(automovel);
            }
            tx.commit();
        } catch (RuntimeException e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    public Automovel findById(Long id) {
        EntityManager em = EntityManagerConfiguracao.getEntityManager();
        try {
            return em.find(Automovel.class, id);
        } finally {
            em.close();
        }
    }

    public List<Automovel> findAll() {
        EntityManager em = EntityManagerConfiguracao.getEntityManager();
        try {
            return em.createQuery(
                            "SELECT a FROM Automovel a ORDER BY a.id", Automovel.class)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    public List<Automovel> findSemCliente() {
        EntityManager em = EntityManagerConfiguracao.getEntityManager();
        try {
            return em.createQuery(
                            "SELECT a FROM Automovel a WHERE a.cliente IS NULL ORDER BY a.id",
                            Automovel.class)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    public List<Automovel> findComCliente() {
        EntityManager em = EntityManagerConfiguracao.getEntityManager();
        try {
            return em.createQuery(
                            "SELECT a FROM Automovel a WHERE a.cliente IS NOT NULL ORDER BY a.id",
                            Automovel.class)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    public List<Automovel> findPorCliente(Cliente cliente) {
        if (cliente == null || cliente.getId() == null) {
            return java.util.Collections.emptyList();
        }
        EntityManager em = EntityManagerConfiguracao.getEntityManager();
        try {
            TypedQuery<Automovel> query = em.createQuery(
                    "SELECT a FROM Automovel a WHERE a.cliente.id = :id ORDER BY a.id",
                    Automovel.class);
            query.setParameter("id", cliente.getId());
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    public void vincular(Long idAutomovel, Long idCliente) {
        EntityManager em = EntityManagerConfiguracao.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            Automovel automovel = em.find(Automovel.class, idAutomovel);
            Cliente cliente = em.find(Cliente.class, idCliente);
            if (automovel == null || cliente == null) {
                throw new IllegalArgumentException("Automovel ou cliente inexistente.");
            }
            automovel.setCliente(cliente);
            em.merge(automovel);
            tx.commit();
        } catch (RuntimeException e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    public void desvincular(Long idAutomovel) {
        EntityManager em = EntityManagerConfiguracao.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            Automovel automovel = em.find(Automovel.class, idAutomovel);
            if (automovel != null) {
                automovel.setCliente(null);
                em.merge(automovel);
            }
            tx.commit();
        } catch (RuntimeException e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        } finally {
            em.close();
        }
    }
}
