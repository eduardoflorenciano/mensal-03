package view.telas;

import entity.Servico;
import service.servico.ServicoService;
import view.telas.componentes.EstiloApp;
import view.telas.componentes.Mensagens;

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

public class TelaServico extends JFrame {

    private final ServicoService servicoService;

    private JTextField campoNome;
    private JTextField campoTipo;
    private JTextField campoDuracao;
    private JTextField campoPreco;
    private JTextField campoBuscaNome;

    private JTable tabela;
    private DefaultTableModel modeloTabela;

    private Long servicoSelecionadoId;

    public TelaServico(ServicoService servicoService) {
        this.servicoService = servicoService;

        configurarJanela();
        montarConteudo();
        atualizarTabela(servicoService.listarTodosServicos());
    }

    private void configurarJanela() {
        setTitle("Gerenciar Servicos");
        setSize(880, 580);
        setMinimumSize(new Dimension(780, 500));
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
        JLabel titulo = new JLabel("Cadastro de Servicos");
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
        painel.setBorder(EstiloApp.bordaPainel("Dados do Servico"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        campoNome = EstiloApp.criarCampoTexto();
        campoTipo = EstiloApp.criarCampoTexto();
        campoDuracao = EstiloApp.criarCampoTexto();
        campoPreco = EstiloApp.criarCampoTexto();

        gbc.gridx = 0; gbc.gridy = 0;
        painel.add(EstiloApp.criarLabel("Nome:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1;
        painel.add(campoNome, gbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0;
        painel.add(EstiloApp.criarLabel("Tipo (Troca, Revisao...):"), gbc);
        gbc.gridx = 1; gbc.weightx = 1;
        painel.add(campoTipo, gbc);

        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0;
        painel.add(EstiloApp.criarLabel("Duracao (min):"), gbc);
        gbc.gridx = 1; gbc.weightx = 1;
        painel.add(campoDuracao, gbc);

        gbc.gridx = 0; gbc.gridy = 3; gbc.weightx = 0;
        painel.add(EstiloApp.criarLabel("Preco (R$):"), gbc);
        gbc.gridx = 1; gbc.weightx = 1;
        painel.add(campoPreco, gbc);

        return painel;
    }

    private JPanel criarPainelTabela() {
        JPanel painel = new JPanel(new BorderLayout(4, 4));
        painel.setBackground(EstiloApp.CINZA_FUNDO);

        JPanel painelBusca = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 0));
        painelBusca.setBackground(EstiloApp.CINZA_FUNDO);
        campoBuscaNome = EstiloApp.criarCampoTexto();
        campoBuscaNome.setPreferredSize(new Dimension(220, 28));
        JButton botaoBuscarNome = EstiloApp.criarBotaoSecundario("Buscar por nome");
        JButton botaoBuscarTipo = EstiloApp.criarBotaoSecundario("Buscar por tipo");
        JButton botaoLimparBusca = EstiloApp.criarBotao("Mostrar todos", EstiloApp.AZUL_CLARO);
        JButton botaoRelatorio = EstiloApp.criarBotao("Relatorio", EstiloApp.AZUL_PRIMARIO);
        botaoBuscarNome.addActionListener(this::buscarPorNome);
        botaoBuscarTipo.addActionListener(this::buscarPorTipo);
        botaoLimparBusca.addActionListener(e -> atualizarTabela(servicoService.listarTodosServicos()));
        botaoRelatorio.addActionListener(this::exibirRelatorio);
        painelBusca.add(EstiloApp.criarLabel("Texto:"));
        painelBusca.add(campoBuscaNome);
        painelBusca.add(botaoBuscarNome);
        painelBusca.add(botaoBuscarTipo);
        painelBusca.add(botaoLimparBusca);
        painelBusca.add(botaoRelatorio);

        modeloTabela = new DefaultTableModel(
                new Object[]{"ID", "Nome", "Tipo", "Duracao (min)", "Preco (R$)"}, 0) {
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

        JButton botaoLimpar = EstiloApp.criarBotao("Limpar Campos", EstiloApp.AZUL_CLARO);
        JButton botaoCadastrar = EstiloApp.criarBotaoSucesso("Cadastrar");
        JButton botaoAtualizar = EstiloApp.criarBotaoSecundario("Atualizar");
        JButton botaoRemover = EstiloApp.criarBotaoPerigo("Remover");
        JButton botaoFechar = EstiloApp.criarBotao("Fechar", EstiloApp.PRETO_TEXTO);

        botaoLimpar.addActionListener(e -> limparFormulario());
        botaoCadastrar.addActionListener(this::cadastrarServico);
        botaoAtualizar.addActionListener(this::atualizarServico);
        botaoRemover.addActionListener(this::removerServico);
        botaoFechar.addActionListener(e -> dispose());

        painel.add(botaoLimpar);
        painel.add(botaoCadastrar);
        painel.add(botaoAtualizar);
        painel.add(botaoRemover);
        painel.add(botaoFechar);
        return painel;
    }

    private void cadastrarServico(ActionEvent evento) {
        DadosFormulario dados = lerFormulario();
        if (dados == null) {
            return;
        }
        try {
            Servico novo = new Servico(dados.nome, dados.preco, dados.duracao, dados.tipo);
            servicoService.cadastrar(novo);
            Mensagens.sucesso(this, "Servico cadastrado com sucesso!");
            limparFormulario();
            atualizarTabela(servicoService.listarTodosServicos());
        } catch (RuntimeException ex) {
            Mensagens.erro(this, "Erro ao cadastrar servico", ex);
        }
    }

    private void atualizarServico(ActionEvent evento) {
        if (servicoSelecionadoId == null) {
            Mensagens.aviso(this, "Selecione um servico para atualizar.");
            return;
        }
        DadosFormulario dados = lerFormulario();
        if (dados == null) {
            return;
        }
        try {
            servicoService.atualizar(servicoSelecionadoId, dados.nome, dados.preco,
                    dados.duracao, dados.tipo);
            Mensagens.sucesso(this, "Servico atualizado com sucesso!");
            limparFormulario();
            atualizarTabela(servicoService.listarTodosServicos());
        } catch (RuntimeException ex) {
            Mensagens.erro(this, "Erro ao atualizar servico", ex);
        }
    }

    private void removerServico(ActionEvent evento) {
        if (servicoSelecionadoId == null) {
            Mensagens.aviso(this, "Selecione um servico para remover.");
            return;
        }
        if (Mensagens.confirmar(this, "Tem certeza que deseja remover este servico?")) {
            try {
                servicoService.remover(servicoSelecionadoId);
                Mensagens.sucesso(this, "Servico removido com sucesso!");
                limparFormulario();
                atualizarTabela(servicoService.listarTodosServicos());
            } catch (RuntimeException ex) {
                Mensagens.erro(this, "Erro ao remover servico", ex);
            }
        }
    }

    private void buscarPorNome(ActionEvent evento) {
        String texto = campoBuscaNome.getText().trim();
        if (texto.isEmpty()) {
            atualizarTabela(servicoService.listarTodosServicos());
            return;
        }
        List<Servico> encontrados = servicoService.buscarPorNomeLista(texto);
        if (encontrados.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Nenhum servico encontrado com esse nome.",
                    "Busca", JOptionPane.INFORMATION_MESSAGE);
        }
        atualizarTabela(encontrados);
    }

    private void buscarPorTipo(ActionEvent evento) {
        String texto = campoBuscaNome.getText().trim();
        if (texto.isEmpty()) {
            atualizarTabela(servicoService.listarTodosServicos());
            return;
        }
        List<Servico> encontrados = servicoService.buscarPorTipoLista(texto);
        if (encontrados.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Nenhum servico encontrado com esse tipo.",
                    "Busca", JOptionPane.INFORMATION_MESSAGE);
        }
        atualizarTabela(encontrados);
    }

    private void exibirRelatorio(ActionEvent evento) {
        String relatorio = servicoService.gerarRelatorio();
        JOptionPane.showMessageDialog(this,
                relatorio,
                "Relatorio de Servicos",
                JOptionPane.INFORMATION_MESSAGE);
    }

    private DadosFormulario lerFormulario() {
        String nome = campoNome.getText().trim();
        String tipo = campoTipo.getText().trim();
        String duracaoTexto = campoDuracao.getText().trim();
        String precoTexto = campoPreco.getText().trim();

        if (nome.isEmpty() || tipo.isEmpty() || duracaoTexto.isEmpty() || precoTexto.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Preencha todos os campos obrigatorios.",
                    "Validacao", JOptionPane.WARNING_MESSAGE);
            return null;
        }

        int duracao;
        double preco;
        try {
            duracao = Integer.parseInt(duracaoTexto);
            preco = Double.parseDouble(precoTexto.replace(",", "."));
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                    "Duracao deve ser inteira e preco deve ser numerico.",
                    "Validacao", JOptionPane.WARNING_MESSAGE);
            return null;
        }
        if (duracao <= 0) {
            JOptionPane.showMessageDialog(this,
                    "Duracao deve ser maior que 0.",
                    "Validacao", JOptionPane.WARNING_MESSAGE);
            return null;
        }
        if (preco <= 0) {
            JOptionPane.showMessageDialog(this,
                    "Preco deve ser maior que 0.",
                    "Validacao", JOptionPane.WARNING_MESSAGE);
            return null;
        }
        return new DadosFormulario(nome, tipo, duracao, preco);
    }

    private void preencherFormularioComSelecao() {
        int linha = tabela.getSelectedRow();
        if (linha < 0) {
            return;
        }
        servicoSelecionadoId = ((Number) modeloTabela.getValueAt(linha, 0)).longValue();
        campoNome.setText((String) modeloTabela.getValueAt(linha, 1));
        campoTipo.setText((String) modeloTabela.getValueAt(linha, 2));
        campoDuracao.setText(String.valueOf(modeloTabela.getValueAt(linha, 3)));
        campoPreco.setText(String.valueOf(modeloTabela.getValueAt(linha, 4)));
    }

    private void limparFormulario() {
        servicoSelecionadoId = null;
        campoNome.setText("");
        campoTipo.setText("");
        campoDuracao.setText("");
        campoPreco.setText("");
        tabela.clearSelection();
    }

    private void atualizarTabela(List<Servico> servicos) {
        modeloTabela.setRowCount(0);
        for (Servico s : servicos) {
            modeloTabela.addRow(new Object[]{
                    s.getId(), s.getNome(), s.getTipo(),
                    s.getDuracaoMinutos(),
                    String.format("%.2f", s.getPreco())
            });
        }
    }

    private static class DadosFormulario {
        final String nome;
        final String tipo;
        final int duracao;
        final double preco;

        DadosFormulario(String nome, String tipo, int duracao, double preco) {
            this.nome = nome;
            this.tipo = tipo;
            this.duracao = duracao;
            this.preco = preco;
        }
    }
}
