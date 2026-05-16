package repositories;

import config.EntityManagerConfiguracao;
import entity.Cliente;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;

import java.util.List;

public class ClienteRepository {

    public Cliente create(Cliente cliente) {
        EntityManager em = EntityManagerConfiguracao.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(cliente);
            tx.commit();
            return cliente;
        } catch (RuntimeException e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }

    public Cliente update(Cliente cliente) {
        EntityManager em = EntityManagerConfiguracao.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            Cliente atualizado = em.merge(cliente);
            tx.commit();
            return atualizado;
        } catch (RuntimeException e) {
            if (tx.isActive()) {
                tx.rollback();
            }
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
            Cliente cliente = em.find(Cliente.class, id);
            if (cliente != null) {
                em.remove(cliente);
            }
            tx.commit();
        } catch (RuntimeException e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }

    public Cliente findById(Long id) {
        EntityManager em = EntityManagerConfiguracao.getEntityManager();
        try {
            return em.find(Cliente.class, id);
        } finally {
            em.close();
        }
    }

    public List<Cliente> findAll() {
        EntityManager em = EntityManagerConfiguracao.getEntityManager();
        try {
            TypedQuery<Cliente> query = em.createQuery(
                    "SELECT c FROM Cliente c ORDER BY c.id", Cliente.class);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    public List<Cliente> findByNome(String nome) {
        EntityManager em = EntityManagerConfiguracao.getEntityManager();
        try {
            TypedQuery<Cliente> query = em.createQuery(
                    "SELECT c FROM Cliente c WHERE LOWER(c.nome) LIKE :nome ORDER BY c.nome",
                    Cliente.class);
            query.setParameter("nome", "%" + (nome == null ? "" : nome.toLowerCase()) + "%");
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    public boolean cpfJaExiste(String cpf) {
        EntityManager em = EntityManagerConfiguracao.getEntityManager();
        try {
            Long count = em.createQuery(
                            "SELECT COUNT(c) FROM Cliente c WHERE c.cpf = :cpf", Long.class)
                    .setParameter("cpf", cpf)
                    .getSingleResult();
            return count > 0;
        } finally {
            em.close();
        }
    }

    public boolean cpfJaExisteParaOutro(String cpf, Long idAtual) {
        EntityManager em = EntityManagerConfiguracao.getEntityManager();
        try {
            Long count = em.createQuery(
                            "SELECT COUNT(c) FROM Cliente c WHERE c.cpf = :cpf AND c.id <> :id", Long.class)
                    .setParameter("cpf", cpf)
                    .setParameter("id", idAtual)
                    .getSingleResult();
            return count > 0;
        } finally {
            em.close();
        }
    }
}
