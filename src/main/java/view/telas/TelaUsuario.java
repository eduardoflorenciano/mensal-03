package view.telas;

import entity.Usuario;
import service.usuario.GerenciadorUsuarioService;
import view.telas.componentes.EstiloApp;
import view.telas.componentes.Mensagens;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class TelaUsuario extends JFrame {

    private final GerenciadorUsuarioService service;
    private final Long idUsuarioLogado;

    private JTable tabela;
    private DefaultTableModel modeloTabela;

    private JTextField campoNome;
    private JTextField campoEmail;
    private JPasswordField campoSenha;
    private JComboBox<String> comboPerfil;

    private Long idSelecionado = null;

    public TelaUsuario(GerenciadorUsuarioService service, Long idUsuarioLogado) {
        this.service = service;
        this.idUsuarioLogado = idUsuarioLogado;
        configurarJanela();
        montarConteudo();
        carregarTabela();
    }

    private void configurarJanela() {
        setTitle("Gerenciar Usuários");
        setSize(800, 560);
        setMinimumSize(new Dimension(700, 480));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        getContentPane().setBackground(EstiloApp.CINZA_FUNDO);
    }

    private void montarConteudo() {
        setLayout(new BorderLayout(8, 8));
        add(criarCabecalho(), BorderLayout.NORTH);
        add(criarPainelCentral(), BorderLayout.CENTER);
        add(criarPainelBotoes(), BorderLayout.SOUTH);
    }

    private JPanel criarCabecalho() {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(EstiloApp.AZUL_PRIMARIO);
        p.setBorder(new EmptyBorder(14, 16, 14, 16));
        JLabel titulo = new JLabel("Gerenciamento de Usuários");
        titulo.setFont(EstiloApp.FONTE_TITULO);
        titulo.setForeground(EstiloApp.BRANCO);
        titulo.setHorizontalAlignment(SwingConstants.CENTER);
        p.add(titulo);
        return p;
    }

    private JSplitPane criarPainelCentral() {
        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, criarFormulario(), criarTabela());
        split.setDividerLocation(300);
        split.setResizeWeight(0.35);
        split.setBorder(new EmptyBorder(8, 8, 4, 8));
        return split;
    }

    private JPanel criarFormulario() {
        JPanel p = new JPanel(new GridBagLayout());
        p.setBackground(EstiloApp.CINZA_FUNDO);
        p.setBorder(EstiloApp.bordaPainel("Dados do Usuário"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 4, 5, 4);

        String[] labels = {"Nome:", "E-mail:", "Senha:", "Perfil:"};
        for (int i = 0; i < labels.length; i++) {
            gbc.gridx = 0; gbc.gridy = i; gbc.weightx = 0.3;
            JLabel lbl = new JLabel(labels[i]);
            lbl.setFont(EstiloApp.FONTE_NORMAL);
            p.add(lbl, gbc);
        }

        gbc.gridx = 1; gbc.weightx = 0.7;

        gbc.gridy = 0; campoNome = new JTextField(); campoNome.setFont(EstiloApp.FONTE_NORMAL); p.add(campoNome, gbc);
        gbc.gridy = 1; campoEmail = new JTextField(); campoEmail.setFont(EstiloApp.FONTE_NORMAL); p.add(campoEmail, gbc);
        gbc.gridy = 2; campoSenha = new JPasswordField(); campoSenha.setFont(EstiloApp.FONTE_NORMAL); p.add(campoSenha, gbc);
        gbc.gridy = 3; comboPerfil = new JComboBox<>(new String[]{"Admin", "Atendente", "Mecânico"});
        comboPerfil.setFont(EstiloApp.FONTE_NORMAL); p.add(comboPerfil, gbc);

        gbc.gridy = 4; gbc.gridx = 0; gbc.gridwidth = 2;
        JLabel dicaSenha = new JLabel("* Senha obrigatória apenas no cadastro");
        dicaSenha.setFont(new Font("SansSerif", Font.ITALIC, 11));
        dicaSenha.setForeground(Color.GRAY);
        p.add(dicaSenha, gbc);

        return p;
    }

    private JScrollPane criarTabela() {
        String[] colunas = {"ID", "Nome", "E-mail", "Perfil"};
        modeloTabela = new DefaultTableModel(colunas, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tabela = new JTable(modeloTabela);
        tabela.setFont(EstiloApp.FONTE_NORMAL);
        tabela.setRowHeight(26);
        tabela.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) selecionarLinha();
        });
        return new JScrollPane(tabela);
    }

    private JPanel criarPainelBotoes() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 8));
        p.setBackground(EstiloApp.CINZA_FUNDO);

        JButton btnCadastrar = EstiloApp.criarBotaoSucesso("Cadastrar");
        JButton btnAtualizar = EstiloApp.criarBotaoPrimario("Atualizar");
        JButton btnRemover   = EstiloApp.criarBotaoPerigo("Remover");
        JButton btnLimpar    = EstiloApp.criarBotaoSecundario("Limpar");
        JButton btnFechar    = EstiloApp.criarBotao("Fechar", EstiloApp.PRETO_TEXTO);

        btnCadastrar.addActionListener(e -> cadastrar());
        btnAtualizar.addActionListener(e -> atualizar());
        btnRemover.addActionListener(e -> remover());
        btnLimpar.addActionListener(e -> limpar());
        btnFechar.addActionListener(e -> dispose());

        p.add(btnCadastrar);
        p.add(btnAtualizar);
        p.add(btnRemover);
        p.add(btnLimpar);
        p.add(btnFechar);
        return p;
    }

    private void carregarTabela() {
        modeloTabela.setRowCount(0);
        try {
            List<Usuario> usuarios = service.listarTodos();
            for (Usuario u : usuarios) {
                String perfil = u.getClass().getSimpleName();
                modeloTabela.addRow(new Object[]{u.getId(), u.getNome(), u.getEmail(), perfil});
            }
        } catch (Exception e) {
            Mensagens.erro(this, "Erro ao carregar usuários: " + e.getMessage());
        }
    }

    private void selecionarLinha() {
        int linha = tabela.getSelectedRow();
        if (linha < 0) return;
        idSelecionado = (Long) modeloTabela.getValueAt(linha, 0);
        campoNome.setText((String) modeloTabela.getValueAt(linha, 1));
        campoEmail.setText((String) modeloTabela.getValueAt(linha, 2));
        campoSenha.setText("");
        String perfil = (String) modeloTabela.getValueAt(linha, 3);
        comboPerfil.setSelectedItem(perfil.equals("Admin") ? "Admin" : perfil.equals("Atendente") ? "Atendente" : "Mecânico");
    }

    private void cadastrar() {
        try {
            String senha = new String(campoSenha.getPassword());
            int perfil = comboPerfil.getSelectedIndex() + 1;
            service.cadastrar(campoNome.getText().trim(), campoEmail.getText().trim(), senha, perfil);
            Mensagens.sucesso(this, "Usuário cadastrado com sucesso!");
            limpar();
            carregarTabela();
        } catch (Exception e) {
            Mensagens.erro(this, e.getMessage());
        }
    }

    private void atualizar() {
        if (idSelecionado == null) { Mensagens.aviso(this, "Selecione um usuário na tabela."); return; }
        try {
            String senha = new String(campoSenha.getPassword());
            service.atualizar(idSelecionado, campoNome.getText().trim(), campoEmail.getText().trim(), senha, idUsuarioLogado);
            Mensagens.sucesso(this, "Usuário atualizado com sucesso!");
            limpar();
            carregarTabela();
        } catch (Exception e) {
            Mensagens.erro(this, e.getMessage());
        }
    }

    private void remover() {
        if (idSelecionado == null) { Mensagens.aviso(this, "Selecione um usuário na tabela."); return; }
        if (!Mensagens.confirmar(this, "Confirma a remoção do usuário ID " + idSelecionado + "?")) return;
        try {
            service.remover(idSelecionado, idUsuarioLogado);
            Mensagens.sucesso(this, "Usuário removido com sucesso!");
            limpar();
            carregarTabela();
        } catch (Exception e) {
            Mensagens.erro(this, e.getMessage());
        }
    }

    private void limpar() {
        idSelecionado = null;
        campoNome.setText("");
        campoEmail.setText("");
        campoSenha.setText("");
        comboPerfil.setSelectedIndex(0);
        tabela.clearSelection();
    }
}
