package service.usuario;

import at.favre.lib.crypto.bcrypt.BCrypt;
import entity.Admin;
import entity.Atendente;
import entity.Mecanico;
import entity.Usuario;
import repositories.UsuarioRepository;

import java.util.List;

public class GerenciadorUsuarioService {

    private final UsuarioRepository repository;

    public GerenciadorUsuarioService() {
        this.repository = new UsuarioRepository();
    }

    public List<Usuario> listarTodos() {
        return repository.findAll();
    }

    public Usuario buscarPorId(Long id) {
        return repository.findById(id);
    }

    public void cadastrar(String nome, String email, String senha, int perfil) {
        if (nome == null || nome.isBlank()) throw new IllegalArgumentException("Nome obrigatório.");
        if (email == null || email.isBlank()) throw new IllegalArgumentException("E-mail obrigatório.");
        if (senha == null || senha.isBlank()) throw new IllegalArgumentException("Senha obrigatória.");
        if (repository.emailJaExiste(email)) throw new IllegalArgumentException("E-mail já cadastrado.");

        String hash = BCrypt.withDefaults().hashToString(12, senha.toCharArray());
        Usuario novo = switch (perfil) {
            case 1 -> new Admin(nome, email, hash);
            case 2 -> new Atendente(nome, email, hash);
            case 3 -> new Mecanico(nome, email, hash);
            default -> throw new IllegalArgumentException("Perfil inválido.");
        };
        repository.create(novo);
    }

    public void atualizar(Long id, String novoNome, String novoEmail, String novaSenha, Long idLogado) {
        Usuario usuario = repository.findById(id);
        if (usuario == null) throw new IllegalArgumentException("Usuário não encontrado.");
        if (!novoEmail.equalsIgnoreCase(usuario.getEmail()) && repository.emailJaExisteParaOutro(novoEmail, id)) {
            throw new IllegalArgumentException("E-mail já está em uso por outro usuário.");
        }
        usuario.setNome(novoNome);
        usuario.setEmail(novoEmail);
        if (novaSenha != null && !novaSenha.isBlank()) {
            usuario.setSenha(BCrypt.withDefaults().hashToString(12, novaSenha.toCharArray()));
        }
        repository.update(usuario);
    }

    public void remover(Long id, Long idLogado) {
        if (id.equals(idLogado)) {
            throw new IllegalArgumentException("Você não pode remover sua própria conta enquanto está logado.");
        }
        Usuario usuario = repository.findById(id);
        if (usuario == null) throw new IllegalArgumentException("Usuário não encontrado.");
        repository.delete(id);
    }
}
