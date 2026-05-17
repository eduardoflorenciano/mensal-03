package repositories;

import config.EntityManagerConfiguracao;
import entity.Produto;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;

import java.util.List;

public class ProdutoRepository {

    public Produto create(Produto produto) {
        EntityManager em = EntityManagerConfiguracao.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(produto);
            tx.commit();
            return produto;
        } catch (RuntimeException e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    public Produto update(Produto produto) {
        EntityManager em = EntityManagerConfiguracao.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            Produto atualizado = em.merge(produto);
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
            Produto produto = em.find(Produto.class, id);
            if (produto != null) {
                em.remove(produto);
            }
            tx.commit();
        } catch (RuntimeException e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    public Produto findById(Long id) {
        EntityManager em = EntityManagerConfiguracao.getEntityManager();
        try {
            return em.find(Produto.class, id);
        } finally {
            em.close();
        }
    }

    public List<Produto> findAll() {
        EntityManager em = EntityManagerConfiguracao.getEntityManager();
        try {
            return em.createQuery(
                            "SELECT p FROM Produto p ORDER BY p.id", Produto.class)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    public List<Produto> findByNome(String nome) {
        EntityManager em = EntityManagerConfiguracao.getEntityManager();
        try {
            TypedQuery<Produto> q = em.createQuery(
                    "SELECT p FROM Produto p WHERE LOWER(p.nome) LIKE :nome ORDER BY p.nome",
                    Produto.class);
            q.setParameter("nome", "%" + (nome == null ? "" : nome.toLowerCase()) + "%");
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public List<Produto> findByCategoria(String categoria) {
        EntityManager em = EntityManagerConfiguracao.getEntityManager();
        try {
            TypedQuery<Produto> q = em.createQuery(
                    "SELECT p FROM Produto p WHERE LOWER(p.categoria) LIKE :cat ORDER BY p.nome",
                    Produto.class);
            q.setParameter("cat", "%" + (categoria == null ? "" : categoria.toLowerCase()) + "%");
            return q.getResultList();
        } finally {
            em.close();
        }
    }
}
