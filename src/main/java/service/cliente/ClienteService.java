package service.cliente;

import entity.Cliente;
import repositories.ClienteRepository;
import repositories.OrdemServicoRepository;
import util.ValidadorCpf;

import java.util.List;

public class ClienteService {

    private final ClienteRepository repository;
    private final OrdemServicoRepository osRepository;

    public ClienteService() {
        this(new ClienteRepository(), new OrdemServicoRepository());
    }

    public ClienteService(ClienteRepository repository, OrdemServicoRepository osRepository) {
        this.repository = repository;
        this.osRepository = osRepository;
    }

    public void cadastrarCliente(Cliente cliente) {
        if (cliente == null) throw new IllegalArgumentException("Cliente nao pode ser nulo.");
        if (cliente.getNome() == null || cliente.getNome().isBlank())
            throw new IllegalArgumentException("Nome do cliente e obrigatorio.");
        if (cliente.getCpf() == null || cliente.getCpf().isBlank())
            throw new IllegalArgumentException("CPF do cliente e obrigatorio.");

        String cpfLimpo = ValidadorCpf.limpar(cliente.getCpf());
        if (!ValidadorCpf.validar(cpfLimpo))
            throw new IllegalArgumentException("CPF invalido! Verifique os digitos informados.");
        if (repository.cpfJaExiste(cpfLimpo))
            throw new IllegalArgumentException("Ja existe um cliente cadastrado com este CPF.");

        cliente.setCpf(cpfLimpo);
        repository.create(cliente);
    }

    public Cliente buscarPorId(Long id) {
        if (id == null) return null;
        return repository.findById(id);
    }

    public List<Cliente> listarTodosClientes() {
        return repository.findAll();
    }

    public List<Cliente> buscarPorNomeLista(String nome) {
        return repository.findByNome(nome);
    }

    public void atualziar(Cliente clienteAtualizado) {
        if (clienteAtualizado == null || clienteAtualizado.getId() == null)
            throw new IllegalArgumentException("Cliente invalido para atualizacao.");

        Cliente existente = repository.findById(clienteAtualizado.getId());
        if (existente == null)
            throw new IllegalArgumentException("Cliente com ID " + clienteAtualizado.getId() + " nao encontrado.");

        String cpfLimpo = ValidadorCpf.limpar(clienteAtualizado.getCpf());
        if (!ValidadorCpf.validar(cpfLimpo))
            throw new IllegalArgumentException("CPF invalido! Verifique os digitos informados.");
        if (repository.cpfJaExisteParaOutro(cpfLimpo, existente.getId()))
            throw new IllegalArgumentException("Ja existe outro cliente cadastrado com este CPF.");

        existente.setNome(clienteAtualizado.getNome());
        existente.setCpf(cpfLimpo);
        existente.setTelefone(clienteAtualizado.getTelefone());
        repository.update(existente);
    }

    public void remover(Long id) {
        if (id == null) throw new IllegalArgumentException("ID nao pode ser nulo.");

        Cliente existente = repository.findById(id);
        if (existente == null)
            throw new IllegalArgumentException("Cliente com ID " + id + " nao encontrado.");

        long osAbertas = osRepository.contarOsAbertasPorCliente(id);
        if (osAbertas > 0) {
            List<entity.OrdemServico> lista = osRepository.findAbertasPorCliente(id);
            StringBuilder ids = new StringBuilder();
            for (entity.OrdemServico os : lista) {
                if (ids.length() > 0) ids.append(", ");
                ids.append("#").append(os.getId());
            }
            throw new IllegalArgumentException(
                    "Nao e possivel excluir o cliente pois possui OS(s) em aberto: " + ids +
                            ". Finalize ou cancele antes de excluir.");
        }
        repository.delete(id);
    }
}
