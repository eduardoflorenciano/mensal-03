package repositories;

import config.EntityManagerConfiguracao;
import entity.OrdemServico;
import entity.Produto;
import entity.Servico;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

import java.util.List;

public class OrdemServicoRepository {

    public OrdemServico create(OrdemServico ordem) {
        EntityManager em = EntityManagerConfiguracao.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            if (ordem.getCliente() != null && ordem.getCliente().getId() != null) {
                ordem.setCliente(em.getReference(entity.Cliente.class, ordem.getCliente().getId()));
            }
            if (ordem.getAutomovel() != null && ordem.getAutomovel().getId() != null) {
                ordem.setAutomovel(em.getReference(entity.Automovel.class, ordem.getAutomovel().getId()));
            }
            em.persist(ordem);
            tx.commit();
            return ordem;
        } catch (RuntimeException e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    public OrdemServico update(OrdemServico ordem) {
        EntityManager em = EntityManagerConfiguracao.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            OrdemServico atualizado = em.merge(ordem);
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
            OrdemServico ordem = em.find(OrdemServico.class, id);
            if (ordem != null) {
                em.remove(ordem);
            }
            tx.commit();
        } catch (RuntimeException e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    public OrdemServico findById(Long id) {
        EntityManager em = EntityManagerConfiguracao.getEntityManager();
        try {
            return em.find(OrdemServico.class, id);
        } finally {
            em.close();
        }
    }

    public List<OrdemServico> findAll() {
        EntityManager em = EntityManagerConfiguracao.getEntityManager();
        try {
            return em.createQuery(
                            "SELECT o FROM OrdemServico o ORDER BY o.id", OrdemServico.class)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    public List<OrdemServico> findPorCliente(Long idCliente) {
        EntityManager em = EntityManagerConfiguracao.getEntityManager();
        try {
            return em.createQuery(
                            "SELECT o FROM OrdemServico o WHERE o.cliente.id = :id ORDER BY o.id",
                            OrdemServico.class)
                    .setParameter("id", idCliente)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    public List<OrdemServico> findPorAutomovel(Long idAutomovel) {
        EntityManager em = EntityManagerConfiguracao.getEntityManager();
        try {
            return em.createQuery(
                            "SELECT o FROM OrdemServico o WHERE o.automovel.id = :id ORDER BY o.id",
                            OrdemServico.class)
                    .setParameter("id", idAutomovel)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    public OrdemServico adicionarServico(Long idOrdem, Long idServico) {
        EntityManager em = EntityManagerConfiguracao.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            OrdemServico ordem = em.find(OrdemServico.class, idOrdem);
            Servico servico = em.find(Servico.class, idServico);
            if (ordem == null || servico == null) {
                throw new IllegalArgumentException("Ordem ou servico nao encontrado.");
            }
            ordem.getServicos().add(servico);
            em.merge(ordem);
            tx.commit();
            return ordem;
        } catch (RuntimeException e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    public OrdemServico adicionarProduto(Long idOrdem, Long idProduto) {
        EntityManager em = EntityManagerConfiguracao.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            OrdemServico ordem = em.find(OrdemServico.class, idOrdem);
            Produto produto = em.find(Produto.class, idProduto);
            if (ordem == null || produto == null) {
                throw new IllegalArgumentException("Ordem ou produto nao encontrado.");
            }
            ordem.getProdutos().add(produto);
            em.merge(ordem);
            tx.commit();
            return ordem;
        } catch (RuntimeException e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    public long contarOsAbertasPorCliente(Long clienteId) {
        EntityManager em = EntityManagerConfiguracao.getEntityManager();
        try {
            return em.createQuery(
                            "SELECT COUNT(o) FROM OrdemServico o WHERE o.cliente.id = :cid AND o.status NOT IN ('FINALIZADA','CANCELADA')",
                            Long.class)
                    .setParameter("cid", clienteId)
                    .getSingleResult();
        } finally {
            em.close();
        }
    }

    public long contarOsAbertasPorAutomovel(Long automovelId) {
        EntityManager em = EntityManagerConfiguracao.getEntityManager();
        try {
            return em.createQuery(
                            "SELECT COUNT(o) FROM OrdemServico o WHERE o.automovel.id = :aid AND o.status NOT IN ('FINALIZADA','CANCELADA')",
                            Long.class)
                    .setParameter("aid", automovelId)
                    .getSingleResult();
        } finally {
            em.close();
        }
    }

    public List<OrdemServico> findAbertasPorCliente(Long clienteId) {
        EntityManager em = EntityManagerConfiguracao.getEntityManager();
        try {
            return em.createQuery(
                            "SELECT o FROM OrdemServico o WHERE o.cliente.id = :cid AND o.status NOT IN ('FINALIZADA','CANCELADA') ORDER BY o.id",
                            OrdemServico.class)
                    .setParameter("cid", clienteId)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    public OrdemServico finalizar(Long idOrdem) {
        EntityManager em = EntityManagerConfiguracao.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            OrdemServico ordem = em.find(OrdemServico.class, idOrdem);
            if (ordem == null) throw new IllegalArgumentException("Ordem nao encontrada.");
            if (!ordem.getServicos().isEmpty()) {
                ordem.finalizarOrdem();
            } else {
                throw new IllegalArgumentException("Nao e possivel finalizar uma OS sem servicos vinculados.");
            }
            em.merge(ordem);
            tx.commit();
            return ordem;
        } catch (RuntimeException e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    public OrdemServico cancelar(Long idOrdem) {
        EntityManager em = EntityManagerConfiguracao.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            OrdemServico ordem = em.find(OrdemServico.class, idOrdem);
            if (ordem == null) throw new IllegalArgumentException("Ordem nao encontrada.");
            if ("FINALIZADA".equals(ordem.getStatus())) {
                throw new IllegalArgumentException("Nao e possivel cancelar uma OS ja finalizada.");
            }
            if ("CANCELADA".equals(ordem.getStatus())) {
                throw new IllegalArgumentException("Esta OS ja esta cancelada.");
            }
            ordem.cancelarOrdem();
            em.merge(ordem);
            tx.commit();
            return ordem;
        } catch (RuntimeException e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        } finally {
            em.close();
        }
    }
}
