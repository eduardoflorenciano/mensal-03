package service.automovel;

import entity.Automovel;
import entity.Cliente;
import repositories.AutomovelRepository;

import java.util.List;

public class AutomovelService {

    private final AutomovelRepository repository;

    public AutomovelService() {
        this(new AutomovelRepository());
    }

    public AutomovelService(AutomovelRepository repository) {
        this.repository = repository;
    }

    public void cadastrar(Automovel automovel) {
        if (automovel == null) {
            throw new IllegalArgumentException("Automovel nao pode ser nulo.");
        }
        if (automovel.getModelo() == null || automovel.getModelo().isBlank()) {
            throw new IllegalArgumentException("Modelo do automovel e obrigatorio.");
        }
        if (automovel.getPlaca() == null || automovel.getPlaca().isBlank()) {
            throw new IllegalArgumentException("Placa do automovel e obrigatoria.");
        }
        repository.create(automovel);
    }

    public void atualizar(Automovel automovelAtualizado) {
        if (automovelAtualizado == null || automovelAtualizado.getId() == null) {
            throw new IllegalArgumentException("Automovel invalido para atualizacao.");
        }
        Automovel existente = repository.findById(automovelAtualizado.getId());
        if (existente == null) {
            throw new IllegalArgumentException(
                    "Automovel com ID " + automovelAtualizado.getId() + " nao encontrado.");
        }
        existente.setModelo(automovelAtualizado.getModelo());
        existente.setPlaca(automovelAtualizado.getPlaca());
        existente.setAnoFabricacao(automovelAtualizado.getAnoFabricacao());
        repository.update(existente);
    }

    public void remover(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("ID nao pode ser nulo.");
        }
        Automovel automovel = repository.findById(id);
        if (automovel == null) {
            throw new IllegalArgumentException("Automovel com ID " + id + " nao encontrado.");
        }
        repository.delete(id);
    }

    public Automovel buscarPorId(Long id) {
        if (id == null) return null;
        return repository.findById(id);
    }

    public List<Automovel> listarCopiaLista() {
        return repository.findSemCliente();
    }

    public List<Automovel> listarAutomoveisVinculadosDoCliente(Cliente cliente) {
        return repository.findPorCliente(cliente);
    }

    public void vincularAoCliente(Long idAutomovel, Long idCliente) {
        if (idAutomovel == null || idCliente == null) {
            throw new IllegalArgumentException("IDs nao podem ser nulos.");
        }
        repository.vincular(idAutomovel, idCliente);
    }

    public void desvincular(Long idAutomovel) {
        if (idAutomovel == null) {
            throw new IllegalArgumentException("ID nao pode ser nulo.");
        }
        repository.desvincular(idAutomovel);
    }
}
