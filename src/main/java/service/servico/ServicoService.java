package service.servico;

import entity.Servico;
import repositories.ServicoRepository;

import java.util.List;

public class ServicoService {

    private final ServicoRepository repository;

    public ServicoService() {
        this(new ServicoRepository());
    }

    public ServicoService(ServicoRepository repository) {
        this.repository = repository;
    }

    public void cadastrar(Servico servico) {
        if (servico == null) {
            throw new IllegalArgumentException("Servico nao pode ser nulo.");
        }
        if (servico.getNome() == null || servico.getNome().isBlank()) {
            throw new IllegalArgumentException("Nome do servico e obrigatorio.");
        }
        if (servico.getPreco() <= 0) {
            throw new IllegalArgumentException("Preco do servico deve ser maior que 0.");
        }
        if (servico.getDuracaoMinutos() <= 0) {
            throw new IllegalArgumentException("Duracao do servico deve ser maior que 0.");
        }
        repository.create(servico);
    }

    public List<Servico> listarTodosServicos() {
        return repository.findAll();
    }

    public List<Servico> buscarPorNomeLista(String nome) {
        return repository.findByNome(nome);
    }

    public List<Servico> buscarPorTipoLista(String tipo) {
        return repository.findByTipo(tipo);
    }

    public Servico buscarPorId(Long id) {
        if (id == null) return null;
        return repository.findById(id);
    }

    public void atualizar(Long id, String novoNome, double novoPreco,
                          int novaDuracao, String novoTipo) {
        if (id == null) {
            throw new IllegalArgumentException("ID nao pode ser nulo.");
        }
        Servico servico = repository.findById(id);
        if (servico == null) {
            throw new IllegalArgumentException("Servico com ID " + id + " nao encontrado.");
        }
        if (novoPreco <= 0) {
            throw new IllegalArgumentException("Preco deve ser maior que 0.");
        }
        if (novaDuracao <= 0) {
            throw new IllegalArgumentException("Duracao deve ser maior que 0.");
        }
        servico.setNome(novoNome);
        servico.setPreco(novoPreco);
        servico.setDuracaoMinutos(novaDuracao);
        servico.setTipo(novoTipo);
        repository.update(servico);
    }

    public void remover(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("ID nao pode ser nulo.");
        }
        Servico servico = repository.findById(id);
        if (servico == null) {
            throw new IllegalArgumentException("Servico com ID " + id + " nao encontrado.");
        }
        repository.delete(id);
    }

    public String gerarRelatorio() {
        List<Servico> servicos = repository.findAll();
        if (servicos.isEmpty()) {
            return "Nenhum servico para gerar relatorio.";
        }
        double somaPrecos = 0;
        int somaDuracoes = 0;
        Servico maisCaro = servicos.get(0);
        Servico maisRapido = servicos.get(0);
        for (Servico s : servicos) {
            somaPrecos += s.getPreco();
            somaDuracoes += s.getDuracaoMinutos();
            if (s.getPreco() > maisCaro.getPreco()) maisCaro = s;
            if (s.getDuracaoMinutos() < maisRapido.getDuracaoMinutos()) maisRapido = s;
        }
        double precoMedio = somaPrecos / servicos.size();
        double duracaoMedia = (double) somaDuracoes / servicos.size();
        StringBuilder sb = new StringBuilder();
        sb.append("Total de servicos: ").append(servicos.size()).append('\n');
        sb.append(String.format("Preco medio: R$ %.2f%n", precoMedio));
        sb.append(String.format("Duracao media: %.0f minutos%n", duracaoMedia));
        sb.append("Mais caro: ").append(maisCaro.getNome())
                .append(String.format(" (R$ %.2f)%n", maisCaro.getPreco()));
        sb.append("Mais rapido: ").append(maisRapido.getNome())
                .append(" (").append(maisRapido.getDuracaoMinutos()).append(" min)");
        return sb.toString();
    }
}
