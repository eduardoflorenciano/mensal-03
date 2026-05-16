package iniciadorSistema;

import config.EntityManagerConfiguracao;
import config.FlyWayConfiguracao;
import entity.Usuario;
import jakarta.persistence.EntityManager;
import service.auth.AuthService;
import service.automovel.AutomovelService;
import service.cliente.ClienteService;
import service.ordemservico.OrdemServicoService;
import service.produto.ProdutoService;
import service.servico.ServicoService;
import service.usuario.GerenciadorUsuarioService;
import view.telas.TelaLogin;
import view.telas.TelaPrincipal;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class IniciarSistemaSwing {

    public void iniciar() {

        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception ignorada) {}

        if (!testarConexao()) return;

        try {
            FlyWayConfiguracao.migrate();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "Erro ao rodar Flyway: " + e.getMessage(),
                    "Aviso", JOptionPane.WARNING_MESSAGE);
        }

        final ClienteService clienteService           = new ClienteService();
        final AutomovelService automovelService       = new AutomovelService();
        final ProdutoService produtoService           = new ProdutoService();
        final ServicoService servicoService           = new ServicoService();
        final OrdemServicoService ordemServicoService = new OrdemServicoService();
        final GerenciadorUsuarioService usuarioService = new GerenciadorUsuarioService();
        final AuthService authService                 = new AuthService();

        SwingUtilities.invokeLater(() -> {

            Usuario logado = TelaLogin.exibir(authService);
            if (logado == null) {
                System.exit(0);
                return;
            }

            TelaPrincipal telaPrincipal = new TelaPrincipal(
                    clienteService,
                    automovelService,
                    produtoService,
                    servicoService,
                    ordemServicoService,
                    usuarioService,
                    logado);
            telaPrincipal.setVisible(true);
        });
    }

    private boolean testarConexao() {
        EntityManager em = null;
        try {
            em = EntityManagerConfiguracao.getEntityManager();
            em.createNativeQuery("SELECT 1").getSingleResult();
            return true;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "Erro ao conectar no banco: " + e.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
            return false;
        } finally {
            if (em != null && em.isOpen()) em.close();
        }
    }
}
