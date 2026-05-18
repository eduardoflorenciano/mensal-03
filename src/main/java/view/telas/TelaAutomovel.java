package view.telas;

import entity.Automovel;
import entity.Cliente;
import service.automovel.AutomovelService;
import service.cliente.ClienteService;
import view.telas.componentes.EstiloApp;
import view.telas.componentes.Mensagens;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
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

public class TelaAutomovel extends JFrame {

    private final AutomovelService automovelService;
    private final ClienteService clienteService;

    private JTextField campoModelo;
    private JTextField campoPlaca;
    private JTextField campoAno;

    private JTable tabelaDisponiveis;
    private DefaultTableModel modeloDisponiveis;
    private JTable tabelaVinculados;
    private DefaultTableModel modeloVinculados;

    private JComboBox<ItemCliente> seletorCliente;

    private Long automovelSelecionadoId;
    private boolean selecaoEhDisponivel;

    public TelaAutomovel(AutomovelService automovelService, ClienteService clienteService) {
        this.automovelService = automovelService;
        this.clienteService = clienteService;

        configurarJanela();
        montarConteudo();
        atualizarComboClientes();
        atualizarTabelas();
    }

    private void configurarJanela() {
        setTitle("Gerenciar Automoveis");
        setSize(900, 620);
        setMinimumSize(new Dimension(820, 540));
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

        JLabel titulo = new JLabel("Cadastro de Automoveis");
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
        painel.add(criarPainelTabelas(), BorderLayout.CENTER);
        return painel;
    }

    private JPanel criarPainelFormulario() {
        JPanel painel = new JPanel(new GridBagLayout());
        painel.setBackground(EstiloApp.BRANCO);
        painel.setBorder(EstiloApp.bordaPainel("Dados do Automovel"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        campoModelo = EstiloApp.criarCampoTexto();
        campoPlaca = EstiloApp.criarCampoTexto();
        campoAno = EstiloApp.criarCampoTexto();

        gbc.gridx = 0; gbc.gridy = 0;
        painel.add(EstiloApp.criarLabel("Modelo:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1;
        painel.add(campoModelo, gbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0;
        painel.add(EstiloApp.criarLabel("Placa:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1;
        painel.add(campoPlaca, gbc);

        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0;
        painel.add(EstiloApp.criarLabel("Ano de Fabricacao:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1;
        painel.add(campoAno, gbc);

        return painel;
    }

    private JPanel criarPainelTabelas() {
        JPanel painel = new JPanel(new java.awt.GridLayout(1, 2, 8, 8));
        painel.setBackground(EstiloApp.CINZA_FUNDO);

        painel.add(criarPainelTabelaDisponiveis());
        painel.add(criarPainelTabelaVinculados());

        return painel;
    }

    private JPanel criarPainelTabelaDisponiveis() {
        JPanel painel = new JPanel(new BorderLayout(4, 4));
        painel.setBackground(EstiloApp.BRANCO);
        painel.setBorder(EstiloApp.bordaPainel("Disponiveis (sem cliente)"));

        modeloDisponiveis = new DefaultTableModel(
                new Object[]{"ID", "Modelo", "Placa", "Ano"}, 0) {
            @Override
            public boolean isCellEditable(int linha, int coluna) {
                return false;
            }
        };
        tabelaDisponiveis = new JTable(modeloDisponiveis);
        tabelaDisponiveis.setRowHeight(22);
        tabelaDisponiveis.setFont(EstiloApp.FONTE_NORMAL);
        tabelaDisponiveis.getTableHeader().setFont(EstiloApp.FONTE_SUBTITULO);
        tabelaDisponiveis.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabelaDisponiveis.getSelectionModel().addListSelectionListener(e -> {
            int linha = tabelaDisponiveis.getSelectedRow();
            if (linha >= 0) {
                tabelaVinculados.clearSelection();
                automovelSelecionadoId = ((Number) modeloDisponiveis.getValueAt(linha, 0)).longValue();
                selecaoEhDisponivel = true;
                campoModelo.setText((String) modeloDisponiveis.getValueAt(linha, 1));
                campoPlaca.setText((String) modeloDisponiveis.getValueAt(linha, 2));
                campoAno.setText(String.valueOf(modeloDisponiveis.getValueAt(linha, 3)));
            }
        });

        painel.add(new JScrollPane(tabelaDisponiveis), BorderLayout.CENTER);
        return painel;
    }

    private JPanel criarPainelTabelaVinculados() {
        JPanel painel = new JPanel(new BorderLayout(4, 4));
        painel.setBackground(EstiloApp.BRANCO);
        painel.setBorder(EstiloApp.bordaPainel("Vinculados a Cliente"));

        modeloVinculados = new DefaultTableModel(
                new Object[]{"ID", "Modelo", "Placa", "Ano", "Cliente"}, 0) {
            @Override
            public boolean isCellEditable(int linha, int coluna) {
                return false;
            }
        };
        tabelaVinculados = new JTable(modeloVinculados);
        tabelaVinculados.setRowHeight(22);
        tabelaVinculados.setFont(EstiloApp.FONTE_NORMAL);
        tabelaVinculados.getTableHeader().setFont(EstiloApp.FONTE_SUBTITULO);
        tabelaVinculados.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabelaVinculados.getSelectionModel().addListSelectionListener(e -> {
            int linha = tabelaVinculados.getSelectedRow();
            if (linha >= 0) {
                tabelaDisponiveis.clearSelection();
                automovelSelecionadoId = ((Number) modeloVinculados.getValueAt(linha, 0)).longValue();
                selecaoEhDisponivel = false;
                campoModelo.setText((String) modeloVinculados.getValueAt(linha, 1));
                campoPlaca.setText((String) modeloVinculados.getValueAt(linha, 2));
                campoAno.setText(String.valueOf(modeloVinculados.getValueAt(linha, 3)));
            }
        });

        painel.add(new JScrollPane(tabelaVinculados), BorderLayout.CENTER);
        return painel;
    }

    private JPanel criarPainelInferior() {
        JPanel painel = new JPanel(new BorderLayout(0, 8));
        painel.setBackground(EstiloApp.CINZA_FUNDO);
        painel.setBorder(new EmptyBorder(0, 16, 12, 16));

        JPanel painelVinculo = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 4));
        painelVinculo.setBackground(EstiloApp.CINZA_FUNDO);
        seletorCliente = new JComboBox<>();
        seletorCliente.setPreferredSize(new Dimension(240, 28));
        seletorCliente.setFont(EstiloApp.FONTE_NORMAL);
        JButton botaoVincular = EstiloApp.criarBotaoSecundario("Vincular ao Cliente");
        botaoVincular.addActionListener(this::vincularAoCliente);
        JButton botaoDesvincular = EstiloApp.criarBotao("Desvincular", EstiloApp.AZUL_CLARO);
        botaoDesvincular.addActionListener(this::desvincularDoCliente);
        painelVinculo.add(EstiloApp.criarLabel("Cliente:"));
        painelVinculo.add(seletorCliente);
        painelVinculo.add(botaoVincular);
        painelVinculo.add(botaoDesvincular);

        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 4));
        painelBotoes.setBackground(EstiloApp.CINZA_FUNDO);
        JButton botaoLimpar = EstiloApp.criarBotao("Limpar Campos", EstiloApp.AZUL_CLARO);
        JButton botaoCadastrar = EstiloApp.criarBotaoSucesso("Cadastrar");
        JButton botaoAtualizar = EstiloApp.criarBotaoSecundario("Atualizar");
        JButton botaoFechar = EstiloApp.criarBotao("Fechar", EstiloApp.PRETO_TEXTO);
        botaoLimpar.addActionListener(e -> limparFormulario());
        botaoCadastrar.addActionListener(this::cadastrarAutomovel);
        botaoAtualizar.addActionListener(this::atualizarAutomovel);
        botaoFechar.addActionListener(e -> dispose());
        painelBotoes.add(botaoLimpar);
        painelBotoes.add(botaoCadastrar);
        painelBotoes.add(botaoAtualizar);
        painelBotoes.add(botaoFechar);

        painel.add(painelVinculo, BorderLayout.NORTH);
        painel.add(painelBotoes, BorderLayout.SOUTH);
        return painel;
    }

    private void cadastrarAutomovel(ActionEvent evento) {
        String modelo = campoModelo.getText().trim();
        String placa = campoPlaca.getText().trim();
        String anoTexto = campoAno.getText().trim();

        if (modelo.isEmpty() || placa.isEmpty() || anoTexto.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Preencha todos os campos obrigatorios.",
                    "Validacao", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int ano;
        try {
            ano = Integer.parseInt(anoTexto);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                    "Ano deve ser um numero inteiro.",
                    "Validacao", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (ano < 1900 || ano > 2100) {
            JOptionPane.showMessageDialog(this,
                    "Ano de fabricacao invalido.",
                    "Validacao", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            Automovel automovel = new Automovel(modelo, placa, ano);
            automovelService.cadastrar(automovel);
            Mensagens.sucesso(this, "Automovel cadastrado com sucesso!");
            limparFormulario();
            atualizarTabelas();
        } catch (RuntimeException ex) {
            Mensagens.erro(this, "Erro ao cadastrar automovel", ex);
        }
    }

    private void atualizarAutomovel(ActionEvent evento) {
        if (automovelSelecionadoId == null) {
            JOptionPane.showMessageDialog(this,
                    "Selecione um automovel na tabela.",
                    "Atencao", JOptionPane.WARNING_MESSAGE);
            return;
        }
        String modelo = campoModelo.getText().trim();
        String placa = campoPlaca.getText().trim();
        String anoTexto = campoAno.getText().trim();
        if (modelo.isEmpty() || placa.isEmpty() || anoTexto.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Preencha todos os campos obrigatorios.",
                    "Validacao", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int ano;
        try {
            ano = Integer.parseInt(anoTexto);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                    "Ano deve ser um numero inteiro.",
                    "Validacao", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            Automovel atualizado = new Automovel(modelo, placa, ano);
            atualizado.setId(automovelSelecionadoId);
            automovelService.atualizar(atualizado);
            Mensagens.sucesso(this, "Automovel atualizado com sucesso!");
            limparFormulario();
            atualizarTabelas();
        } catch (RuntimeException ex) {
            Mensagens.erro(this, "Erro ao atualizar automovel", ex);
        }
    }

    private void vincularAoCliente(ActionEvent evento) {
        if (automovelSelecionadoId == null || !selecaoEhDisponivel) {
            Mensagens.aviso(this,
                    "Selecione um automovel DISPONIVEL (lado esquerdo) para vincular.");
            return;
        }
        ItemCliente itemCliente = (ItemCliente) seletorCliente.getSelectedItem();
        if (itemCliente == null) {
            Mensagens.aviso(this,
                    "Nenhum cliente cadastrado. Cadastre um cliente primeiro.");
            return;
        }
        try {
            automovelService.vincularAoCliente(automovelSelecionadoId, itemCliente.getId());
            Mensagens.sucesso(this,
                    "Automovel vinculado com sucesso ao cliente!");
            limparFormulario();
            atualizarTabelas();
        } catch (RuntimeException ex) {
            Mensagens.erro(this, "Erro ao vincular automovel", ex);
        }
    }

    private void desvincularDoCliente(ActionEvent evento) {
        if (automovelSelecionadoId == null || selecaoEhDisponivel) {
            Mensagens.aviso(this,
                    "Selecione um automovel VINCULADO (lado direito) para desvincular.");
            return;
        }
        try {
            automovelService.desvincular(automovelSelecionadoId);
            Mensagens.sucesso(this, "Automovel desvinculado com sucesso!");
            limparFormulario();
            atualizarTabelas();
        } catch (RuntimeException ex) {
            Mensagens.erro(this, "Erro ao desvincular automovel", ex);
        }
    }

    private void atualizarComboClientes() {
        List<Cliente> clientes = clienteService.listarTodosClientes();
        DefaultComboBoxModel<ItemCliente> modelo = new DefaultComboBoxModel<>();
        for (Cliente c : clientes) {
            modelo.addElement(new ItemCliente(c.getId(), c.getNome()));
        }
        seletorCliente.setModel(modelo);
    }

    private void atualizarTabelas() {
        atualizarComboClientes();
        modeloDisponiveis.setRowCount(0);
        for (Automovel a : automovelService.listarCopiaLista()) {
            modeloDisponiveis.addRow(new Object[]{
                    a.getId(), a.getModelo(), a.getPlaca(), a.getAnoFabricacao()
            });
        }
        modeloVinculados.setRowCount(0);
        for (Cliente c : clienteService.listarTodosClientes()) {
            for (Automovel a : automovelService.listarAutomoveisVinculadosDoCliente(c)) {
                modeloVinculados.addRow(new Object[]{
                        a.getId(), a.getModelo(), a.getPlaca(), a.getAnoFabricacao(),
                        c.getNome()
                });
            }
        }
    }

    private void limparFormulario() {
        automovelSelecionadoId = null;
        selecaoEhDisponivel = false;
        campoModelo.setText("");
        campoPlaca.setText("");
        campoAno.setText("");
        tabelaDisponiveis.clearSelection();
        tabelaVinculados.clearSelection();
    }

    private static class ItemCliente {
        private final Long id;
        private final String nome;

        ItemCliente(Long id, String nome) {
            this.id = id;
            this.nome = nome;
        }

        Long getId() {
            return id;
        }

        @Override
        public String toString() {
            return "[" + id + "] " + nome;
        }
    }
}
