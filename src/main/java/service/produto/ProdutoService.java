package service.produto;

import entity.Produto;
import repositories.ProdutoRepository;

import java.util.List;

public class ProdutoService {

    private final ProdutoRepository repository;

    public ProdutoService() {
        this(new ProdutoRepository());
    }

    public ProdutoService(ProdutoRepository repository) {
        this.repository = repository;
    }

    public void cadastrar(Produto produto) {
        if (produto == null) {
            throw new IllegalArgumentException("Produto nao pode ser nulo.");
        }
        if (produto.getNome() == null || produto.getNome().isBlank()) {
            throw new IllegalArgumentException("Nome do produto e obrigatorio.");
        }
        if (produto.getPreco() <= 0) {
            throw new IllegalArgumentException("Preco do produto deve ser maior que 0.");
        }
        if (produto.getQuantidadeEstoque() < 0) {
            throw new IllegalArgumentException("Estoque do produto nao pode ser negativo.");
        }
        repository.create(produto);
    }

    public List<Produto> listarTodosProdutos() {
        return repository.findAll();
    }

    public List<Produto> buscarPorNomeLista(String nome) {
        return repository.findByNome(nome);
    }

    public List<Produto> buscarPorCategoriaLista(String categoria) {
        return repository.findByCategoria(categoria);
    }

    public Produto buscarPorId(Long id) {
        if (id == null) return null;
        return repository.findById(id);
    }

    public void atualizar(Long id, String novoNome, double novoPreco,
                          int novoEstoque, String novaCategoria, String novaMarca) {
        if (id == null) {
            throw new IllegalArgumentException("ID nao pode ser nulo.");
        }
        Produto produto = repository.findById(id);
        if (produto == null) {
            throw new IllegalArgumentException("Produto com ID " + id + " nao encontrado.");
        }
        if (novoPreco <= 0) {
            throw new IllegalArgumentException("Preco deve ser maior que 0.");
        }
        if (novoEstoque < 0) {
            throw new IllegalArgumentException("Estoque nao pode ser negativo.");
        }
        produto.setNome(novoNome);
        produto.setPreco(novoPreco);
        produto.setQuantidadeEstoque(novoEstoque);
        produto.setCategoria(novaCategoria);
        produto.setMarca(novaMarca);
        repository.update(produto);
    }

    public void remover(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("ID nao pode ser nulo.");
        }
        Produto produto = repository.findById(id);
        if (produto == null) {
            throw new IllegalArgumentException("Produto com ID " + id + " nao encontrado.");
        }
        repository.delete(id);
    }

    public String gerarRelatorio() {
        List<Produto> produtos = repository.findAll();
        if (produtos.isEmpty()) {
            return "Nenhum produto para gerar relatorio.";
        }
        double valorTotalEstoque = 0;
        int totalUnidades = 0;
        Produto maisCaro = produtos.get(0);
        Produto maisBarato = produtos.get(0);
        for (Produto p : produtos) {
            valorTotalEstoque += p.getPreco() * p.getQuantidadeEstoque();
            totalUnidades += p.getQuantidadeEstoque();
            if (p.getPreco() > maisCaro.getPreco()) maisCaro = p;
            if (p.getPreco() < maisBarato.getPreco()) maisBarato = p;
        }
        double precoMedio = totalUnidades > 0 ? valorTotalEstoque / totalUnidades : 0;
        StringBuilder sb = new StringBuilder();
        sb.append("Tipos de produto: ").append(produtos.size()).append('\n');
        sb.append("Total em estoque: ").append(totalUnidades).append('\n');
        sb.append(String.format("Valor total em estoque: R$ %.2f%n", valorTotalEstoque));
        sb.append(String.format("Preco medio ponderado: R$ %.2f%n", precoMedio));
        sb.append("Mais caro: ").append(maisCaro.getNome())
                .append(String.format(" (R$ %.2f)%n", maisCaro.getPreco()));
        sb.append("Mais barato: ").append(maisBarato.getNome())
                .append(String.format(" (R$ %.2f)", maisBarato.getPreco()));
        return sb.toString();
    }
}
