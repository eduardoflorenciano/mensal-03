package view.telas;

import entity.Automovel;
import entity.Cliente;
import entity.OrdemServico;
import entity.Produto;
import entity.Servico;
import service.automovel.AutomovelService;
import service.cliente.ClienteService;
import service.ordemservico.OrdemServicoService;
import service.produto.ProdutoService;
import service.servico.ServicoService;
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
import javax.swing.ListSelectionModel;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.util.List;

public class TelaOrdemServico extends JFrame {

    private final OrdemServicoService ordemServicoService;
    private final ClienteService clienteService;
    private final AutomovelService automovelService;
    private final ServicoService servicoService;
    private final ProdutoService produtoService;

    private JComboBox<ItemCliente> seletorCliente;
    private JComboBox<ItemAutomovel> seletorAutomovel;

    private JTable tabelaOrdens;
    private DefaultTableModel modeloOrdens;

    private JTable tabelaServicosOs;
    private DefaultTableModel modeloServicosOs;
    private JTable tabelaProdutosOs;
    private DefaultTableModel modeloProdutosOs;

    private JComboBox<ItemServico> seletorServico;
    private JComboBox<ItemProduto> seletorProduto;

    private JLabel labelTotalOs;
    private JLabel labelStatusOs;

    private Long ordemSelecionadaId;

    public TelaOrdemServico(OrdemServicoService ordemServicoService,
                            ClienteService clienteService,
                            AutomovelService automovelService,
                            ServicoService servicoService,
                            ProdutoService produtoService) {
        this.ordemServicoService = ordemServicoService;
        this.clienteService = clienteService;
        this.automovelService = automovelService;
        this.servicoService = servicoService;
        this.produtoService = produtoService;

        configurarJanela();
        montarConteudo();
        recarregarCombos();
        atualizarTabelaOrdens();
    }

    private void configurarJanela() {
        setTitle("Gerenciar Ordens de Servico");
        setSize(1024, 720);
        setMinimumSize(new Dimension(960, 640));
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
        JLabel titulo = new JLabel("Ordens de Servico");
        titulo.setFont(EstiloApp.FONTE_TITULO);
        titulo.setForeground(EstiloApp.BRANCO);
        painel.add(titulo, BorderLayout.WEST);
        return painel;
    }

    private JPanel criarPainelCentro() {
        JPanel painel = new JPanel(new GridLayout(1, 2, 8, 8));
        painel.setBackground(EstiloApp.CINZA_FUNDO);
        painel.setBorder(new EmptyBorder(12, 16, 12, 16));

        painel.add(criarPainelEsquerda());
        painel.add(criarPainelDireita());
        return painel;
    }

    private JPanel criarPainelEsquerda() {
        JPanel painel = new JPanel(new BorderLayout(4, 8));
        painel.setBackground(EstiloApp.BRANCO);
        painel.setBorder(EstiloApp.bordaPainel("Criar Ordem de Servico"));

        JPanel painelForm = new JPanel(new GridBagLayout());
        painelForm.setBackground(EstiloApp.BRANCO);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        seletorCliente = new JComboBox<>();
        seletorCliente.setFont(EstiloApp.FONTE_NORMAL);
        seletorCliente.addActionListener(e -> atualizarComboAutomoveis());
        seletorAutomovel = new JComboBox<>();
        seletorAutomovel.setFont(EstiloApp.FONTE_NORMAL);

        gbc.gridx = 0; gbc.gridy = 0;
        painelForm.add(EstiloApp.criarLabel("Cliente:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1;
        painelForm.add(seletorCliente, gbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0;
        painelForm.add(EstiloApp.criarLabel("Automovel:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1;
        painelForm.add(seletorAutomovel, gbc);

        JButton botaoCriar = EstiloApp.criarBotaoSucesso("Criar OS");
        botaoCriar.addActionListener(this::criarOrdem);

        JPanel painelBotao = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        painelBotao.setBackground(EstiloApp.BRANCO);
        painelBotao.add(botaoCriar);

        modeloOrdens = new DefaultTableModel(
                new Object[]{"ID", "Cliente", "Veiculo", "Status", "Total (R$)"}, 0) {
            @Override
            public boolean isCellEditable(int linha, int coluna) {
                return false;
            }
        };
        tabelaOrdens = new JTable(modeloOrdens);
        tabelaOrdens.setRowHeight(22);
        tabelaOrdens.setFont(EstiloApp.FONTE_NORMAL);
        tabelaOrdens.getTableHeader().setFont(EstiloApp.FONTE_SUBTITULO);
        tabelaOrdens.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabelaOrdens.getSelectionModel().addListSelectionListener(e -> selecionarOrdem());

        JPanel painelLista = new JPanel(new BorderLayout(4, 4));
        painelLista.setBackground(EstiloApp.BRANCO);
        painelLista.add(EstiloApp.criarSubtitulo("Ordens cadastradas:"), BorderLayout.NORTH);
        painelLista.add(new JScrollPane(tabelaOrdens), BorderLayout.CENTER);

        JPanel painelNorte = new JPanel(new BorderLayout(4, 4));
        painelNorte.setBackground(EstiloApp.BRANCO);
        painelNorte.add(painelForm, BorderLayout.CENTER);
        painelNorte.add(painelBotao, BorderLayout.SOUTH);

        painel.add(painelNorte, BorderLayout.NORTH);
        painel.add(painelLista, BorderLayout.CENTER);
        return painel;
    }

    private JPanel criarPainelDireita() {
        JPanel painel = new JPanel(new BorderLayout(4, 8));
        painel.setBackground(EstiloApp.BRANCO);
        painel.setBorder(EstiloApp.bordaPainel("Detalhes da Ordem Selecionada"));

        JPanel painelInfo = new JPanel(new GridLayout(2, 1, 4, 4));
        painelInfo.setBackground(EstiloApp.BRANCO);
        labelStatusOs = EstiloApp.criarSubtitulo("Selecione uma ordem...");
        labelTotalOs = EstiloApp.criarLabel("Total: R$ 0,00");
        painelInfo.add(labelStatusOs);
        painelInfo.add(labelTotalOs);

        modeloServicosOs = new DefaultTableModel(
                new Object[]{"ID", "Nome", "Tipo", "Preco (R$)"}, 0) {
            @Override
            public boolean isCellEditable(int linha, int coluna) {
                return false;
            }
        };
        tabelaServicosOs = new JTable(modeloServicosOs);
        tabelaServicosOs.setRowHeight(22);
        tabelaServicosOs.setFont(EstiloApp.FONTE_NORMAL);
        tabelaServicosOs.getTableHeader().setFont(EstiloApp.FONTE_SUBTITULO);

        modeloProdutosOs = new DefaultTableModel(
                new Object[]{"ID", "Nome", "Categoria", "Preco (R$)"}, 0) {
            @Override
            public boolean isCellEditable(int linha, int coluna) {
                return false;
            }
        };
        tabelaProdutosOs = new JTable(modeloProdutosOs);
        tabelaProdutosOs.setRowHeight(22);
        tabelaProdutosOs.setFont(EstiloApp.FONTE_NORMAL);
        tabelaProdutosOs.getTableHeader().setFont(EstiloApp.FONTE_SUBTITULO);

        JPanel painelServicos = new JPanel(new BorderLayout(4, 4));
        painelServicos.setBackground(EstiloApp.BRANCO);
        painelServicos.setBorder(EstiloApp.bordaPainel("Servicos da OS"));
        seletorServico = new JComboBox<>();
        seletorServico.setFont(EstiloApp.FONTE_NORMAL);
        JButton botaoAddServico = EstiloApp.criarBotaoSucesso("Adicionar Servico");
        botaoAddServico.addActionListener(this::adicionarServico);
        JPanel addServicoTop = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 0));
        addServicoTop.setBackground(EstiloApp.BRANCO);
        addServicoTop.add(seletorServico);
        addServicoTop.add(botaoAddServico);
        painelServicos.add(addServicoTop, BorderLayout.NORTH);
        painelServicos.add(new JScrollPane(tabelaServicosOs), BorderLayout.CENTER);

        JPanel painelProdutos = new JPanel(new BorderLayout(4, 4));
        painelProdutos.setBackground(EstiloApp.BRANCO);
        painelProdutos.setBorder(EstiloApp.bordaPainel("Produtos da OS"));
        seletorProduto = new JComboBox<>();
        seletorProduto.setFont(EstiloApp.FONTE_NORMAL);
        JButton botaoAddProduto = EstiloApp.criarBotaoSucesso("Adicionar Produto");
        botaoAddProduto.addActionListener(this::adicionarProduto);
        JPanel addProdutoTop = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 0));
        addProdutoTop.setBackground(EstiloApp.BRANCO);
        addProdutoTop.add(seletorProduto);
        addProdutoTop.add(botaoAddProduto);
        painelProdutos.add(addProdutoTop, BorderLayout.NORTH);
        painelProdutos.add(new JScrollPane(tabelaProdutosOs), BorderLayout.CENTER);

        JPanel painelItens = new JPanel(new GridLayout(2, 1, 4, 8));
        painelItens.setBackground(EstiloApp.BRANCO);
        painelItens.add(painelServicos);
        painelItens.add(painelProdutos);

        painel.add(painelInfo, BorderLayout.NORTH);
        painel.add(painelItens, BorderLayout.CENTER);
        return painel;
    }

    private JPanel criarPainelInferior() {
        JPanel painel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 8));
        painel.setBackground(EstiloApp.CINZA_FUNDO);
        painel.setBorder(new EmptyBorder(0, 16, 12, 16));

        JButton botaoFinalizar = EstiloApp.criarBotaoSucesso("Finalizar OS");
        JButton botaoCancelar  = EstiloApp.criarBotaoSecundario("Cancelar OS");
        JButton botaoRemover   = EstiloApp.criarBotaoPerigo("Remover OS");
        JButton botaoFechar    = EstiloApp.criarBotao("Fechar", EstiloApp.PRETO_TEXTO);
        botaoFinalizar.addActionListener(this::finalizarOrdem);
        botaoCancelar.addActionListener(this::cancelarOrdem);
        botaoRemover.addActionListener(this::removerOrdem);
        botaoFechar.addActionListener(e -> dispose());
        painel.add(botaoFinalizar);
        painel.add(botaoCancelar);
        painel.add(botaoRemover);
        painel.add(botaoFechar);
        return painel;
    }

    private void criarOrdem(ActionEvent evento) {
        ItemCliente itemCliente = (ItemCliente) seletorCliente.getSelectedItem();
        ItemAutomovel itemAutomovel = (ItemAutomovel) seletorAutomovel.getSelectedItem();

        if (itemCliente == null) {
            Mensagens.aviso(this, "Nenhum cliente cadastrado. Cadastre um cliente primeiro.");
            return;
        }
        if (itemAutomovel == null) {
            Mensagens.aviso(this,
                    "O cliente nao possui automovel vinculado. " +
                            "Vincule um automovel a este cliente antes de criar a OS.");
            return;
        }

        try {
            Cliente cliente = clienteService.buscarPorId(itemCliente.getId());
            Automovel automovel = null;
            for (Automovel a : cliente.getAutomoveis()) {
                if (a.getId().equals(itemAutomovel.getId())) {
                    automovel = a;
                    break;
                }
            }
            if (automovel == null) {
                Mensagens.aviso(this, "Nao foi possivel localizar o automovel do cliente.");
                return;
            }

            OrdemServico ordem = new OrdemServico(cliente, automovel);
            ordemServicoService.cadastrarOrdem(ordem);
            Mensagens.sucesso(this, "Ordem de Servico criada com sucesso! ID: " + ordem.getId());
            atualizarTabelaOrdens();
        } catch (RuntimeException ex) {
            Mensagens.erro(this, "Erro ao criar ordem de servico", ex);
        }
    }

    private void adicionarServico(ActionEvent evento) {
        if (!validarOrdemSelecionadaAberta("adicionar servico")) {
            return;
        }
        ItemServico item = (ItemServico) seletorServico.getSelectedItem();
        if (item == null) {
            Mensagens.aviso(this, "Nenhum servico cadastrado.");
            return;
        }
        try {
            OrdemServico atualizada = ordemServicoService.adicionarServico(
                    ordemSelecionadaId, item.getId());
            Mensagens.sucesso(this, "Servico adicionado a OS!");
            atualizarTabelaOrdens();
            carregarDetalhesOrdem(atualizada);
        } catch (RuntimeException ex) {
            Mensagens.erro(this, "Erro ao adicionar servico", ex);
        }
    }

    private void adicionarProduto(ActionEvent evento) {
        if (!validarOrdemSelecionadaAberta("adicionar produto")) {
            return;
        }
        ItemProduto item = (ItemProduto) seletorProduto.getSelectedItem();
        if (item == null) {
            Mensagens.aviso(this, "Nenhum produto cadastrado.");
            return;
        }
        try {
            Produto produto = produtoService.buscarPorId(item.getId());
            if (produto == null) {
                Mensagens.aviso(this, "Produto nao encontrado.");
                return;
            }
            if (produto.getQuantidadeEstoque() <= 0) {
                Mensagens.aviso(this, "Produto sem estoque.");
                return;
            }
            OrdemServico atualizada = ordemServicoService.adicionarProduto(
                    ordemSelecionadaId, item.getId());
            Mensagens.sucesso(this, "Produto adicionado a OS!");
            atualizarTabelaOrdens();
            carregarDetalhesOrdem(atualizada);
        } catch (RuntimeException ex) {
            Mensagens.erro(this, "Erro ao adicionar produto", ex);
        }
    }

    private void finalizarOrdem(ActionEvent evento) {
        if (ordemSelecionadaId == null) {
            Mensagens.aviso(this, "Selecione uma ordem para finalizar.");
            return;
        }
        try {
            OrdemServico ordem = ordemServicoService.buscarPorId(ordemSelecionadaId);
            if (ordem == null) {
                return;
            }
            if ("FINALIZADA".equals(ordem.getStatus())) {
                Mensagens.aviso(this, "Esta ordem ja esta finalizada.");
                return;
            }
            ordemServicoService.finalizarOs(ordemSelecionadaId);
            Mensagens.sucesso(this, "Ordem finalizada com sucesso!");
            atualizarTabelaOrdens();
            carregarDetalhesOrdem(ordemServicoService.buscarPorId(ordemSelecionadaId));
        } catch (RuntimeException ex) {
            Mensagens.erro(this, "Erro ao finalizar ordem", ex);
        }
    }

    private void cancelarOrdem(ActionEvent evento) {
        if (ordemSelecionadaId == null) {
            Mensagens.aviso(this, "Selecione uma ordem para cancelar.");
            return;
        }
        if (!Mensagens.confirmar(this, "Confirma o cancelamento da OS #" + ordemSelecionadaId + "?\nO histórico será preservado.")) {
            return;
        }
        try {
            ordemServicoService.cancelarOs(ordemSelecionadaId);
            Mensagens.sucesso(this, "Ordem cancelada com sucesso!");
            atualizarTabelaOrdens();
            carregarDetalhesOrdem(ordemServicoService.buscarPorId(ordemSelecionadaId));
        } catch (RuntimeException ex) {
            Mensagens.erro(this, "Erro ao cancelar ordem", ex);
        }
    }

    private void removerOrdem(ActionEvent evento) {
        if (ordemSelecionadaId == null) {
            Mensagens.aviso(this, "Selecione uma ordem para remover.");
            return;
        }
        if (Mensagens.confirmar(this, "Tem certeza que deseja remover esta ordem de servico?")) {
            try {
                ordemServicoService.remover(ordemSelecionadaId);
                Mensagens.sucesso(this, "Ordem removida com sucesso!");
                ordemSelecionadaId = null;
                labelStatusOs.setText("Selecione uma ordem...");
                labelTotalOs.setText("Total: R$ 0,00");
                modeloServicosOs.setRowCount(0);
                modeloProdutosOs.setRowCount(0);
                atualizarTabelaOrdens();
            } catch (RuntimeException ex) {
                Mensagens.erro(this, "Erro ao remover ordem", ex);
            }
        }
    }

    private boolean validarOrdemSelecionadaAberta(String acao) {
        if (ordemSelecionadaId == null) {
            Mensagens.aviso(this, "Selecione uma ordem para " + acao + ".");
            return false;
        }
        OrdemServico ordem = ordemServicoService.buscarPorId(ordemSelecionadaId);
        if (ordem == null) {
            return false;
        }
        if ("FINALIZADA".equals(ordem.getStatus())) {
            Mensagens.aviso(this,
                    "Nao e possivel " + acao + " em uma ordem finalizada.");
            return false;
        }
        return true;
    }

    private void selecionarOrdem() {
        int linha = tabelaOrdens.getSelectedRow();
        if (linha < 0) {
            return;
        }
        ordemSelecionadaId = ((Number) modeloOrdens.getValueAt(linha, 0)).longValue();
        OrdemServico ordem = ordemServicoService.buscarPorId(ordemSelecionadaId);
        carregarDetalhesOrdem(ordem);
    }

    private void carregarDetalhesOrdem(OrdemServico ordem) {
        if (ordem == null) {
            return;
        }
        labelStatusOs.setText(
                "OS #" + ordem.getId() + " - Cliente: " + ordem.getCliente().getNome()
                        + " - Status: " + ordem.getStatus());
        labelTotalOs.setText(String.format("Total: R$ %.2f", ordem.calcularValorTotal()));

        modeloServicosOs.setRowCount(0);
        for (Servico s : ordem.getServicos()) {
            modeloServicosOs.addRow(new Object[]{
                    s.getId(), s.getNome(), s.getTipo(),
                    String.format("%.2f", s.getPreco())
            });
        }
        modeloProdutosOs.setRowCount(0);
        for (Produto p : ordem.getProdutos()) {
            modeloProdutosOs.addRow(new Object[]{
                    p.getId(), p.getNome(), p.getCategoria(),
                    String.format("%.2f", p.getPreco())
            });
        }
    }

    private void recarregarCombos() {
        DefaultComboBoxModel<ItemCliente> modelo = new DefaultComboBoxModel<>();
        for (Cliente c : clienteService.listarTodosClientes()) {
            modelo.addElement(new ItemCliente(c.getId(), c.getNome()));
        }
        seletorCliente.setModel(modelo);
        atualizarComboAutomoveis();

        DefaultComboBoxModel<ItemServico> modeloS = new DefaultComboBoxModel<>();
        for (Servico s : servicoService.listarTodosServicos()) {
            modeloS.addElement(new ItemServico(s.getId(), s.getNome(), s.getPreco()));
        }
        seletorServico.setModel(modeloS);

        DefaultComboBoxModel<ItemProduto> modeloP = new DefaultComboBoxModel<>();
        for (Produto p : produtoService.listarTodosProdutos()) {
            modeloP.addElement(new ItemProduto(p.getId(), p.getNome(), p.getPreco()));
        }
        seletorProduto.setModel(modeloP);
    }

    private void atualizarComboAutomoveis() {
        DefaultComboBoxModel<ItemAutomovel> modelo = new DefaultComboBoxModel<>();
        ItemCliente itemCliente = (ItemCliente) seletorCliente.getSelectedItem();
        if (itemCliente != null) {
            Cliente cliente = clienteService.buscarPorId(itemCliente.getId());
            if (cliente != null) {
                for (Automovel a : automovelService.listarAutomoveisVinculadosDoCliente(cliente)) {
                    modelo.addElement(new ItemAutomovel(a.getId(), a.getModelo(), a.getPlaca()));
                }
            }
        }
        seletorAutomovel.setModel(modelo);
    }

    private void atualizarTabelaOrdens() {
        recarregarCombos();
        modeloOrdens.setRowCount(0);
        for (OrdemServico os : ordemServicoService.listarTodasOrdens()) {
            modeloOrdens.addRow(new Object[]{
                    os.getId(),
                    os.getCliente() != null ? os.getCliente().getNome() : "",
                    os.getAutomovel() != null ? os.getAutomovel().getModelo() : "",
                    os.getStatus(),
                    String.format("%.2f", os.calcularValorTotal())
            });
        }
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

    private static class ItemAutomovel {
        private final Long id;
        private final String modelo;
        private final String placa;

        ItemAutomovel(Long id, String modelo, String placa) {
            this.id = id;
            this.modelo = modelo;
            this.placa = placa;
        }

        Long getId() {
            return id;
        }

        @Override
        public String toString() {
            return "[" + id + "] " + modelo + " - " + placa;
        }
    }

    private static class ItemServico {
        private final Long id;
        private final String nome;
        private final double preco;

        ItemServico(Long id, String nome, double preco) {
            this.id = id;
            this.nome = nome;
            this.preco = preco;
        }

        Long getId() {
            return id;
        }

        @Override
        public String toString() {
            return String.format("[%d] %s (R$ %.2f)", id, nome, preco);
        }
    }

    private static class ItemProduto {
        private final Long id;
        private final String nome;
        private final double preco;

        ItemProduto(Long id, String nome, double preco) {
            this.id = id;
            this.nome = nome;
            this.preco = preco;
        }

        Long getId() {
            return id;
        }

        @Override
        public String toString() {
            return String.format("[%d] %s (R$ %.2f)", id, nome, preco);
        }
    }
}
