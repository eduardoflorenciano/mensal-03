package service.auth;

import at.favre.lib.crypto.bcrypt.BCrypt;
import entity.Admin;
import entity.Usuario;
import repositories.UsuarioRepository;

public class AuthService {

    private final UsuarioRepository usuarioRepository;

    public AuthService() {
        this.usuarioRepository = new UsuarioRepository();
        criarAdminPadraoSeNecessario();
    }

    private void criarAdminPadraoSeNecessario() {
        try {
            if (usuarioRepository.contarUsuarios() == 0) {
                String senhaHash = BCrypt.withDefaults().hashToString(12, "admin123".toCharArray());
                Admin adminPadrao = new Admin("Administrador", "admin@oficina.com", senhaHash);
                usuarioRepository.create(adminPadrao);
            }
        } catch (Exception e) {
            System.out.println("Aviso: não foi possível verificar admin padrão: " + e.getMessage());
        }
    }

    public Usuario login(String email, String senha) {
        try {
            Usuario usuario = usuarioRepository.findByEmail(email);
            if (usuario != null) {
                BCrypt.Result resultado = BCrypt.verifyer().verify(senha.toCharArray(), usuario.getSenha());
                if (resultado.verified) return usuario;
            }
            return null;
        } catch (Exception e) {
            System.out.println("Erro no login: " + e.getMessage());
            return null;
        }
    }
}
