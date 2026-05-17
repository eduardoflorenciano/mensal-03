package service.ordemservico;

import entity.OrdemServico;
import entity.Produto;
import repositories.OrdemServicoRepository;
import repositories.ProdutoRepository;

import java.util.List;

public class OrdemServicoService {

    private final OrdemServicoRepository repository;
    private final ProdutoRepository produtoRepository;

    public OrdemServicoService() {
        this(new OrdemServicoRepository(), new ProdutoRepository());
    }

    public OrdemServicoService(OrdemServicoRepository repository, ProdutoRepository produtoRepository) {
        this.repository = repository;
        this.produtoRepository = produtoRepository;
    }

    public void cadastrarOrdem(OrdemServico ordemServico) {
        if (ordemServico == null) throw new IllegalArgumentException("Ordem nao pode ser nula.");
        if (ordemServico.getCliente() == null) throw new IllegalArgumentException("Ordem precisa de um cliente.");
        if (ordemServico.getAutomovel() == null) throw new IllegalArgumentException("Ordem precisa de um automovel.");

        long osAbertas = repository.contarOsAbertasPorAutomovel(ordemServico.getAutomovel().getId());
        if (osAbertas > 0) {
            throw new IllegalArgumentException(
                    "Ja existe uma OS em aberto para este automovel. Finalize ou cancele antes de abrir uma nova.");
        }
        repository.create(ordemServico);
    }

    public OrdemServico adicionarServico(Long idOrdem, Long idServico) {
        OrdemServico os = repository.findById(idOrdem);
        if (os == null) throw new IllegalArgumentException("Ordem nao encontrada.");
        if ("FINALIZADA".equals(os.getStatus()) || "CANCELADA".equals(os.getStatus())) {
            throw new IllegalArgumentException("Nao e possivel editar uma OS com status " + os.getStatus() + ".");
        }
        return repository.adicionarServico(idOrdem, idServico);
    }

    public OrdemServico adicionarProduto(Long idOrdem, Long idProduto) {
        OrdemServico os = repository.findById(idOrdem);
        if (os == null) throw new IllegalArgumentException("Ordem nao encontrada.");
        if ("FINALIZADA".equals(os.getStatus()) || "CANCELADA".equals(os.getStatus())) {
            throw new IllegalArgumentException("Nao e possivel editar uma OS com status " + os.getStatus() + ".");
        }

        Produto produto = produtoRepository.findById(idProduto);
        if (produto == null) throw new IllegalArgumentException("Produto nao encontrado.");
        if (produto.getQuantidadeEstoque() <= 0) {
            throw new IllegalArgumentException("Produto '" + produto.getNome() + "' sem estoque disponivel.");
        }

        produto.setQuantidadeEstoque(produto.getQuantidadeEstoque() - 1);
        produtoRepository.update(produto);
        return repository.adicionarProduto(idOrdem, idProduto);
    }

    public void finalizarOs(Long id) {
        if (id == null) throw new IllegalArgumentException("ID nao pode ser nulo.");
        repository.finalizar(id);
    }

    public void cancelarOs(Long id) {
        if (id == null) throw new IllegalArgumentException("ID nao pode ser nulo.");
        repository.cancelar(id);
    }

    public OrdemServico buscarPorId(Long id) {
        if (id == null) return null;
        return repository.findById(id);
    }

    public List<OrdemServico> listarTodasOrdens() {
        return repository.findAll();
    }

    public void remover(Long id) {
        if (id == null) throw new IllegalArgumentException("ID nao pode ser nulo.");
        OrdemServico ordem = repository.findById(id);
        if (ordem == null) throw new IllegalArgumentException("Ordem com ID " + id + " nao encontrada.");
        repository.delete(id);
    }
}
