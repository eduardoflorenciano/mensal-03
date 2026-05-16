package view.telas;

import entity.Usuario;
import service.auth.AuthService;
import view.telas.componentes.EstiloApp;
import view.telas.componentes.Mensagens;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class TelaLogin extends JDialog {

    private final AuthService authService;
    private Usuario usuarioLogado = null;
    private int tentativas = 3;

    private JTextField campoEmail;
    private JPasswordField campoSenha;
    private JLabel labelInfo;

    public TelaLogin(AuthService authService) {
        this.authService = authService;
        configurarJanela();
        montarConteudo();
    }

    private void configurarJanela() {
        setTitle("Auto Center Silva - Login");
        setSize(420, 300);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        setModal(true);
        getContentPane().setBackground(EstiloApp.CINZA_FUNDO);
    }

    private void montarConteudo() {
        setLayout(new BorderLayout());
        add(criarCabecalho(), BorderLayout.NORTH);
        add(criarFormulario(), BorderLayout.CENTER);
    }

    private JPanel criarCabecalho() {
        JPanel painel = new JPanel(new BorderLayout());
        painel.setBackground(EstiloApp.AZUL_PRIMARIO);
        painel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel titulo = new JLabel("AUTO CENTER SILVA");
        titulo.setFont(EstiloApp.FONTE_TITULO);
        titulo.setForeground(EstiloApp.BRANCO);
        titulo.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel sub = new JLabel("Acesso ao Sistema");
        sub.setFont(EstiloApp.FONTE_NORMAL);
        sub.setForeground(EstiloApp.BRANCO);
        sub.setHorizontalAlignment(SwingConstants.CENTER);

        painel.add(titulo, BorderLayout.CENTER);
        painel.add(sub, BorderLayout.SOUTH);
        return painel;
    }

    private JPanel criarFormulario() {
        JPanel painel = new JPanel(new GridBagLayout());
        painel.setBackground(EstiloApp.CINZA_FUNDO);
        painel.setBorder(new EmptyBorder(20, 32, 10, 32));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(6, 4, 6, 4);

        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0.3;
        JLabel lblEmail = new JLabel("E-mail:");
        lblEmail.setFont(EstiloApp.FONTE_NORMAL);
        painel.add(lblEmail, gbc);

        gbc.gridx = 1; gbc.weightx = 0.7;
        campoEmail = new JTextField();
        campoEmail.setFont(EstiloApp.FONTE_NORMAL);
        painel.add(campoEmail, gbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0.3;
        JLabel lblSenha = new JLabel("Senha:");
        lblSenha.setFont(EstiloApp.FONTE_NORMAL);
        painel.add(lblSenha, gbc);

        gbc.gridx = 1; gbc.weightx = 0.7;
        campoSenha = new JPasswordField();
        campoSenha.setFont(EstiloApp.FONTE_NORMAL);
        painel.add(campoSenha, gbc);

        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
        labelInfo = new JLabel(" ");
        labelInfo.setFont(new Font("SansSerif", Font.PLAIN, 12));
        labelInfo.setForeground(Color.RED);
        labelInfo.setHorizontalAlignment(SwingConstants.CENTER);
        painel.add(labelInfo, gbc);

        gbc.gridy = 3;
        JButton btnEntrar = EstiloApp.criarBotaoPrimario("Entrar");
        btnEntrar.addActionListener(this::tentarLogin);
        painel.add(btnEntrar, gbc);

        campoSenha.addKeyListener(new KeyAdapter() {
            @Override public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) tentarLogin(null);
            }
        });

        return painel;
    }

    private void tentarLogin(ActionEvent e) {
        String email = campoEmail.getText().trim();
        String senha = new String(campoSenha.getPassword());

        if (email.isEmpty() || senha.isEmpty()) {
            labelInfo.setText("Preencha e-mail e senha.");
            return;
        }

        Usuario usuario = authService.login(email, senha);

        if (usuario != null) {
            usuarioLogado = usuario;
            dispose();
        } else {
            tentativas--;
            campoSenha.setText("");
            if (tentativas > 0) {
                labelInfo.setText("E-mail ou senha incorretos. Tentativas restantes: " + tentativas);
            } else {
                Mensagens.erro(this, "Número de tentativas esgotado. O sistema será encerrado.");
                System.exit(0);
            }
        }
    }

    public static Usuario exibir(AuthService authService) {
        TelaLogin tela = new TelaLogin(authService);
        tela.setVisible(true);
        return tela.usuarioLogado;
    }
}
