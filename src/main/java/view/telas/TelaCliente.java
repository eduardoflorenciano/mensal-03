package view.telas;

import entity.Cliente;
import service.cliente.ClienteService;
import view.telas.componentes.EstiloApp;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.util.List;

public class TelaCliente extends JFrame {

    private final ClienteService clienteService;

    private JTextField campoNome;
    private JTextField campoCpf;
    private JTextField campoTelefone;
    private JTextField campoBusca;

    private JTable tabela;
    private DefaultTableModel modeloTabela;

    private Long clienteSelecionadoId;

    public TelaCliente(ClienteService clienteService) {
        this.clienteService = clienteService;

        configurarJanela();
        montarConteudo();
        atualizarTabela(clienteService.listarTodosClientes());
    }

    private void configurarJanela() {
        setTitle("Gerenciar Clientes");
        setSize(820, 560);
        setMinimumSize(new Dimension(720, 480));
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        getContentPane().setBackground(EstiloApp.CINZA_FUNDO);
    }

    private void montarConteudo() {
        setLayout(new BorderLayout(8, 8));

        add(criarPainelTopo(), BorderLayout.NORTH);
        add(criarPainelCentro(), BorderLayout.CENTER);
        add(criarPainelInferior(), BorderLayout.SOUTH);
    }

    private JPanel criarPainelTopo() {
        JPanel painel = new JPanel(new BorderLayout());
        painel.setBackground(EstiloApp.AZUL_PRIMARIO);
        painel.setBorder(new EmptyBorder(12, 16, 12, 16));

        JLabel titulo = new JLabel("Cadastro de Clientes");
        titulo.setFont(EstiloApp.FONTE_TITULO);
        titulo.setForeground(EstiloApp.BRANCO);
        painel.add(titulo, BorderLayout.WEST);

        return painel;
    }

    private JPanel criarPainelCentro() {
        JPanel painel = new JPanel(new BorderLayout(8, 8));
        painel.setBackground(EstiloApp.CINZA_FUNDO);
        painel.setBorder(new EmptyBorder(12, 16, 12, 16));

        painel.add(criarPainelFormulario(), BorderLayout.NORTH);
        painel.add(criarPainelTabela(), BorderLayout.CENTER);

        return painel;
    }

    private JPanel criarPainelFormulario() {
        JPanel painel = new JPanel(new GridBagLayout());
        painel.setBackground(EstiloApp.BRANCO);
        painel.setBorder(EstiloApp.bordaPainel("Dados do Cliente"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        campoNome = EstiloApp.criarCampoTexto();
        campoCpf = EstiloApp.criarCampoTexto();
        campoTelefone = EstiloApp.criarCampoTexto();

        gbc.gridx = 0; gbc.gridy = 0;
        painel.add(EstiloApp.criarLabel("Nome:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1;
        painel.add(campoNome, gbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0;
        painel.add(EstiloApp.criarLabel("CPF:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1;
        painel.add(campoCpf, gbc);

        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0;
        painel.add(EstiloApp.criarLabel("Telefone:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1;
        painel.add(campoTelefone, gbc);

        return painel;
    }

    private JPanel criarPainelTabela() {
        JPanel painel = new JPanel(new BorderLayout(4, 4));
        painel.setBackground(EstiloApp.CINZA_FUNDO);

        JPanel painelBusca = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 0));
        painelBusca.setBackground(EstiloApp.CINZA_FUNDO);
        campoBusca = EstiloApp.criarCampoTexto();
        campoBusca.setPreferredSize(new Dimension(220, 28));
        JButton botaoBuscar = EstiloApp.criarBotaoSecundario("Buscar por nome");
        JButton botaoLimparBusca = EstiloApp.criarBotao("Mostrar todos", EstiloApp.AZUL_CLARO);
        botaoBuscar.addActionListener(this::buscarPorNome);
        botaoLimparBusca.addActionListener(e -> atualizarTabela(clienteService.listarTodosClientes()));

        painelBusca.add(EstiloApp.criarLabel("Filtro:"));
        painelBusca.add(campoBusca);
        painelBusca.add(botaoBuscar);
        painelBusca.add(botaoLimparBusca);

        modeloTabela = new DefaultTableModel(
                new Object[]{"ID", "Nome", "CPF", "Telefone", "Qtd. Automoveis"}, 0) {
            @Override
            public boolean isCellEditable(int linha, int coluna) {
                return false;
            }
        };
        tabela = new JTable(modeloTabela);
        tabela.setRowHeight(24);
        tabela.setFont(EstiloApp.FONTE_NORMAL);
        tabela.getTableHeader().setFont(EstiloApp.FONTE_SUBTITULO);
        tabela.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabela.getSelectionModel().addListSelectionListener(e -> preencherFormularioComSelecao());

        painel.add(painelBusca, BorderLayout.NORTH);
        painel.add(new JScrollPane(tabela), BorderLayout.CENTER);
        return painel;
    }

    private JPanel criarPainelInferior() {
        JPanel painel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 8));
        painel.setBackground(EstiloApp.CINZA_FUNDO);
        painel.setBorder(new EmptyBorder(0, 16, 12, 16));

        JButton botaoNovo = EstiloApp.criarBotao("Limpar Campos", EstiloApp.AZUL_CLARO);
        JButton botaoSalvar = EstiloApp.criarBotaoSucesso("Cadastrar");
        JButton botaoAtualizar = EstiloApp.criarBotaoSecundario("Atualizar");
        JButton botaoRemover = EstiloApp.criarBotaoPerigo("Remover");
        JButton botaoFechar = EstiloApp.criarBotao("Fechar", EstiloApp.PRETO_TEXTO);

        botaoNovo.addActionListener(e -> limparFormulario());
        botaoSalvar.addActionListener(this::cadastrarCliente);
        botaoAtualizar.addActionListener(this::atualizarCliente);
        botaoRemover.addActionListener(this::removerCliente);
        botaoFechar.addActionListener(e -> dispose());

        painel.add(botaoNovo);
        painel.add(botaoSalvar);
        painel.add(botaoAtualizar);
        painel.add(botaoRemover);
        painel.add(botaoFechar);
        return painel;
    }

    private void cadastrarCliente(ActionEvent evento) {
        String nome = campoNome.getText().trim();
        String cpf = campoCpf.getText().trim();
        String telefone = campoTelefone.getText().trim();

        if (!validarCampos(nome, cpf, telefone)) {
            return;
        }

        try {
            Cliente cliente = new Cliente(nome, telefone, cpf);
            clienteService.cadastrarCliente(cliente);

            JOptionPane.showMessageDialog(this,
                    "Cliente cadastrado com sucesso!",
                    "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            limparFormulario();
            atualizarTabela(clienteService.listarTodosClientes());
        } catch (RuntimeException ex) {
            mostrarErro("Erro ao cadastrar cliente", ex);
        }
    }

    private void atualizarCliente(ActionEvent evento) {
        if (clienteSelecionadoId == null) {
            JOptionPane.showMessageDialog(this,
                    "Selecione um cliente na tabela para atualizar.",
                    "Atencao", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String nome = campoNome.getText().trim();
        String cpf = campoCpf.getText().trim();
        String telefone = campoTelefone.getText().trim();
        if (!validarCampos(nome, cpf, telefone)) {
            return;
        }

        try {
            Cliente clienteAtualizado = new Cliente(nome, telefone, cpf);
            clienteAtualizado.setId(clienteSelecionadoId);
            clienteService.atualziar(clienteAtualizado);

            JOptionPane.showMessageDialog(this,
                    "Cliente atualizado com sucesso!",
                    "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            limparFormulario();
            atualizarTabela(clienteService.listarTodosClientes());
        } catch (RuntimeException ex) {
            mostrarErro("Erro ao atualizar cliente", ex);
        }
    }

    private void removerCliente(ActionEvent evento) {
        if (clienteSelecionadoId == null) {
            JOptionPane.showMessageDialog(this,
                    "Selecione um cliente na tabela para remover.",
                    "Atencao", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirmar = JOptionPane.showConfirmDialog(this,
                "Tem certeza que deseja remover este cliente?",
                "Confirmar remocao", JOptionPane.YES_NO_OPTION);

        if (confirmar == JOptionPane.YES_OPTION) {
            try {
                clienteService.remover(clienteSelecionadoId);
                JOptionPane.showMessageDialog(this,
                        "Cliente removido com sucesso!",
                        "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                limparFormulario();
                atualizarTabela(clienteService.listarTodosClientes());
            } catch (RuntimeException ex) {
                mostrarErro("Erro ao remover cliente", ex);
            }
        }
    }

    private void buscarPorNome(ActionEvent evento) {
        String nome = campoBusca.getText().trim();
        try {
            if (nome.isEmpty()) {
                atualizarTabela(clienteService.listarTodosClientes());
                return;
            }
            List<Cliente> encontrados = clienteService.buscarPorNomeLista(nome);
            if (encontrados.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Nenhum cliente encontrado com esse nome.",
                        "Busca", JOptionPane.INFORMATION_MESSAGE);
            }
            atualizarTabela(encontrados);
        } catch (RuntimeException ex) {
            mostrarErro("Erro ao buscar clientes", ex);
        }
    }

    private void mostrarErro(String titulo, Exception ex) {
        String detalhe = ex.getMessage() != null ? ex.getMessage() : ex.getClass().getSimpleName();
        JOptionPane.showMessageDialog(this,
                titulo + ":\n\n" + detalhe,
                "Erro", JOptionPane.ERROR_MESSAGE);
    }

    private void preencherFormularioComSelecao() {
        int linha = tabela.getSelectedRow();
        if (linha < 0) {
            return;
        }
        clienteSelecionadoId = ((Number) modeloTabela.getValueAt(linha, 0)).longValue();
        campoNome.setText((String) modeloTabela.getValueAt(linha, 1));
        campoCpf.setText((String) modeloTabela.getValueAt(linha, 2));
        campoTelefone.setText((String) modeloTabela.getValueAt(linha, 3));
    }

    private void limparFormulario() {
        clienteSelecionadoId = null;
        campoNome.setText("");
        campoCpf.setText("");
        campoTelefone.setText("");
        tabela.clearSelection();
    }

    private boolean validarCampos(String nome, String cpf, String telefone) {
        if (nome.isEmpty() || cpf.isEmpty() || telefone.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Preencha todos os campos obrigatorios.",
                    "Validacao", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        if (cpf.length() < 11) {
            JOptionPane.showMessageDialog(this,
                    "CPF deve ter pelo menos 11 caracteres.",
                    "Validacao", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        return true;
    }

    private void atualizarTabela(List<Cliente> clientes) {
        modeloTabela.setRowCount(0);
        for (Cliente c : clientes) {
            modeloTabela.addRow(new Object[]{
                    c.getId(),
                    c.getNome(),
                    c.getCpf(),
                    c.getTelefone(),
                    c.getAutomoveis() != null ? c.getAutomoveis().size() : 0
            });
        }
    }
}
