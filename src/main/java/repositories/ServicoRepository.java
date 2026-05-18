package repositories;

import config.EntityManagerConfiguracao;
import entity.Servico;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;

import java.util.List;

public class ServicoRepository {

    public Servico create(Servico servico) {
        EntityManager em = EntityManagerConfiguracao.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(servico);
            tx.commit();
            return servico;
        } catch (RuntimeException e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    public Servico update(Servico servico) {
        EntityManager em = EntityManagerConfiguracao.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            Servico atualizado = em.merge(servico);
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
            Servico servico = em.find(Servico.class, id);
            if (servico != null) {
                em.remove(servico);
            }
            tx.commit();
        } catch (RuntimeException e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    public Servico findById(Long id) {
        EntityManager em = EntityManagerConfiguracao.getEntityManager();
        try {
            return em.find(Servico.class, id);
        } finally {
            em.close();
        }
    }

    public List<Servico> findAll() {
        EntityManager em = EntityManagerConfiguracao.getEntityManager();
        try {
            return em.createQuery(
                            "SELECT s FROM Servico s ORDER BY s.id", Servico.class)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    public List<Servico> findByNome(String nome) {
        EntityManager em = EntityManagerConfiguracao.getEntityManager();
        try {
            TypedQuery<Servico> q = em.createQuery(
                    "SELECT s FROM Servico s WHERE LOWER(s.nome) LIKE :nome ORDER BY s.nome",
                    Servico.class);
            q.setParameter("nome", "%" + (nome == null ? "" : nome.toLowerCase()) + "%");
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public List<Servico> findByTipo(String tipo) {
        EntityManager em = EntityManagerConfiguracao.getEntityManager();
        try {
            TypedQuery<Servico> q = em.createQuery(
                    "SELECT s FROM Servico s WHERE LOWER(s.tipo) LIKE :tipo ORDER BY s.nome",
                    Servico.class);
            q.setParameter("tipo", "%" + (tipo == null ? "" : tipo.toLowerCase()) + "%");
            return q.getResultList();
        } finally {
            em.close();
        }
    }
}
