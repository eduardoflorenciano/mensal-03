package view.telas;

import entity.Produto;
import service.camerasDeAr.CameraDeCaminhao;
import service.camerasDeAr.CameraDeCaminhonete;
import service.camerasDeAr.CameraDeCarro;
import service.produto.ProdutoService;
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
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.util.List;

public class TelaProduto extends JFrame {

    private static final String TIPO_PNEU = "Pneu";
    private static final String TIPO_CAMERA = "Camera de Ar";
    private static final String TIPO_QUIMICO = "Quimico";

    private static final String[] MARCAS_PNEU = {
            "Bridgestone", "Michelin", "Pirelli", "Linglong", "Xbri", "Outros"
    };
    private static final String[] CATEGORIAS_PNEU = {
            "Pneu de carro", "Pneu de caminhonete", "Pneu de caminhao"
    };
    private static final String[] CATEGORIAS_CAMERA = {
            "Camera de carro", "Camera de caminhonete", "Camera de caminhao"
    };

    private static final String[] MEDIDAS_PNEU_CARRO = {
            "185/65 R15", "195/55 R15", "195/60 R15", "205/55 R16",
            "205/60 R16", "215/55 R17", "225/50 R17"
    };
    private static final String[] MEDIDAS_PNEU_CAMINHONETE = {
            "195/75 R16", "205/70 R15", "215/75 R16", "225/70 R16", "265/70 R16"
    };
    private static final String[] MEDIDAS_PNEU_CAMINHAO = {
            "215/75 R17.5", "235/75 R17.5", "275/80 R22.5", "295/80 R22.5", "1000 R20"
    };

    private final ProdutoService produtoService;

    private JComboBox<String> seletorTipo;
    private CardLayout cardLayout;
    private JPanel painelCards;

    private JComboBox<String> seletorMarca;
    private JTextField campoMarcaOutros;
    private JComboBox<String> seletorCategoriaPneu;
    private JComboBox<String> seletorMedidaPneu;

    private JComboBox<String> seletorCategoriaCamera;
    private JComboBox<String> seletorModeloCamera;

    private JTextField campoNomeQuimico;

    private JTextField campoPreco;
    private JTextField campoEstoque;
    private JTextField campoBusca;

    private JTable tabela;
    private DefaultTableModel modeloTabela;

    private Long produtoSelecionadoId;

    public TelaProduto(ProdutoService produtoService) {
        this.produtoService = produtoService;

        configurarJanela();
        montarConteudo();
        atualizarTabela(produtoService.listarTodosProdutos());
    }

    private void configurarJanela() {
        setTitle("Gerenciar Produtos");
        setSize(960, 640);
        setMinimumSize(new Dimension(840, 560));
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
        JLabel titulo = new JLabel("Cadastro de Produtos");
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
        JPanel painel = new JPanel(new BorderLayout(8, 8));
        painel.setBackground(EstiloApp.BRANCO);
        painel.setBorder(EstiloApp.bordaPainel("Dados do Produto"));

        JPanel painelTipoBase = new JPanel(new GridBagLayout());
        painelTipoBase.setBackground(EstiloApp.BRANCO);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        seletorTipo = new JComboBox<>(new String[]{TIPO_PNEU, TIPO_CAMERA, TIPO_QUIMICO});
        seletorTipo.setFont(EstiloApp.FONTE_NORMAL);
        seletorTipo.addActionListener(this::trocarTipoSelecionado);

        campoPreco = EstiloApp.criarCampoTexto();
        campoEstoque = EstiloApp.criarCampoTexto();

        gbc.gridx = 0; gbc.gridy = 0;
        painelTipoBase.add(EstiloApp.criarLabel("Tipo de Produto:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1;
        painelTipoBase.add(seletorTipo, gbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0;
        painelTipoBase.add(EstiloApp.criarLabel("Preco (R$):"), gbc);
        gbc.gridx = 1; gbc.weightx = 1;
        painelTipoBase.add(campoPreco, gbc);

        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0;
        painelTipoBase.add(EstiloApp.criarLabel("Estoque:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1;
        painelTipoBase.add(campoEstoque, gbc);

        cardLayout = new CardLayout();
        painelCards = new JPanel(cardLayout);
        painelCards.setBackground(EstiloApp.BRANCO);
        painelCards.add(criarCardPneu(), TIPO_PNEU);
        painelCards.add(criarCardCamera(), TIPO_CAMERA);
        painelCards.add(criarCardQuimico(), TIPO_QUIMICO);

        painel.add(painelTipoBase, BorderLayout.WEST);
        painel.add(painelCards, BorderLayout.CENTER);
        return painel;
    }

    private JPanel criarCardPneu() {
        JPanel painel = new JPanel(new GridBagLayout());
        painel.setBackground(EstiloApp.BRANCO);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        seletorMarca = new JComboBox<>(MARCAS_PNEU);
        seletorMarca.setFont(EstiloApp.FONTE_NORMAL);
        seletorMarca.addActionListener(e -> {
            String selecionada = (String) seletorMarca.getSelectedItem();
            campoMarcaOutros.setEnabled("Outros".equals(selecionada));
        });
        campoMarcaOutros = EstiloApp.criarCampoTexto();
        campoMarcaOutros.setEnabled(false);

        seletorCategoriaPneu = new JComboBox<>(CATEGORIAS_PNEU);
        seletorCategoriaPneu.setFont(EstiloApp.FONTE_NORMAL);
        seletorCategoriaPneu.addActionListener(this::atualizarMedidasPneu);

        seletorMedidaPneu = new JComboBox<>(MEDIDAS_PNEU_CARRO);
        seletorMedidaPneu.setFont(EstiloApp.FONTE_NORMAL);

        gbc.gridx = 0; gbc.gridy = 0;
        painel.add(EstiloApp.criarLabel("Marca:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1;
        painel.add(seletorMarca, gbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0;
        painel.add(EstiloApp.criarLabel("Marca (Outros):"), gbc);
        gbc.gridx = 1; gbc.weightx = 1;
        painel.add(campoMarcaOutros, gbc);

        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0;
        painel.add(EstiloApp.criarLabel("Categoria:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1;
        painel.add(seletorCategoriaPneu, gbc);

        gbc.gridx = 0; gbc.gridy = 3; gbc.weightx = 0;
        painel.add(EstiloApp.criarLabel("Medida:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1;
        painel.add(seletorMedidaPneu, gbc);

        return painel;
    }

    private JPanel criarCardCamera() {
        JPanel painel = new JPanel(new GridBagLayout());
        painel.setBackground(EstiloApp.BRANCO);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        seletorCategoriaCamera = new JComboBox<>(CATEGORIAS_CAMERA);
        seletorCategoriaCamera.setFont(EstiloApp.FONTE_NORMAL);
        seletorCategoriaCamera.addActionListener(this::atualizarModelosCamera);

        seletorModeloCamera = new JComboBox<>(new CameraDeCarro().listarModelos().toArray(new String[0]));
        seletorModeloCamera.setFont(EstiloApp.FONTE_NORMAL);

        gbc.gridx = 0; gbc.gridy = 0;
        painel.add(EstiloApp.criarLabel("Categoria:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1;
        painel.add(seletorCategoriaCamera, gbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0;
        painel.add(EstiloApp.criarLabel("Modelo:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1;
        painel.add(seletorModeloCamera, gbc);

        return painel;
    }

    private JPanel criarCardQuimico() {
        JPanel painel = new JPanel(new GridBagLayout());
        painel.setBackground(EstiloApp.BRANCO);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        campoNomeQuimico = EstiloApp.criarCampoTexto();

        gbc.gridx = 0; gbc.gridy = 0;
        painel.add(EstiloApp.criarLabel("Nome do produto:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1;
        painel.add(campoNomeQuimico, gbc);

        return painel;
    }

    private JPanel criarPainelTabela() {
        JPanel painel = new JPanel(new BorderLayout(4, 4));
        painel.setBackground(EstiloApp.CINZA_FUNDO);

        JPanel painelBusca = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 0));
        painelBusca.setBackground(EstiloApp.CINZA_FUNDO);
        campoBusca = EstiloApp.criarCampoTexto();
        campoBusca.setPreferredSize(new Dimension(220, 28));
        JButton botaoBuscarNome = EstiloApp.criarBotaoSecundario("Buscar por nome");
        JButton botaoBuscarCategoria = EstiloApp.criarBotaoSecundario("Buscar por categoria");
        JButton botaoLimparBusca = EstiloApp.criarBotao("Mostrar todos", EstiloApp.AZUL_CLARO);
        JButton botaoRelatorio = EstiloApp.criarBotao("Relatorio", EstiloApp.AZUL_PRIMARIO);
        botaoBuscarNome.addActionListener(this::buscarPorNome);
        botaoBuscarCategoria.addActionListener(this::buscarPorCategoria);
        botaoLimparBusca.addActionListener(e -> atualizarTabela(produtoService.listarTodosProdutos()));
        botaoRelatorio.addActionListener(this::exibirRelatorio);
        painelBusca.add(EstiloApp.criarLabel("Texto:"));
        painelBusca.add(campoBusca);
        painelBusca.add(botaoBuscarNome);
        painelBusca.add(botaoBuscarCategoria);
        painelBusca.add(botaoLimparBusca);
        painelBusca.add(botaoRelatorio);

        modeloTabela = new DefaultTableModel(
                new Object[]{"ID", "Nome", "Marca", "Categoria", "Preco (R$)", "Estoque"}, 0) {
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
        botaoCadastrar.addActionListener(this::cadastrarProduto);
        botaoAtualizar.addActionListener(this::atualizarProduto);
        botaoRemover.addActionListener(this::removerProduto);
        botaoFechar.addActionListener(e -> dispose());

        painel.add(botaoLimpar);
        painel.add(botaoCadastrar);
        painel.add(botaoAtualizar);
        painel.add(botaoRemover);
        painel.add(botaoFechar);
        return painel;
    }

    private void trocarTipoSelecionado(ActionEvent evento) {
        String tipo = (String) seletorTipo.getSelectedItem();
        cardLayout.show(painelCards, tipo);
    }

    private void atualizarMedidasPneu(ActionEvent evento) {
        String categoria = (String) seletorCategoriaPneu.getSelectedItem();
        String[] medidas;
        if ("Pneu de caminhonete".equals(categoria)) {
            medidas = MEDIDAS_PNEU_CAMINHONETE;
        } else if ("Pneu de caminhao".equals(categoria)) {
            medidas = MEDIDAS_PNEU_CAMINHAO;
        } else {
            medidas = MEDIDAS_PNEU_CARRO;
        }
        seletorMedidaPneu.setModel(new DefaultComboBoxModel<>(medidas));
    }

    private void atualizarModelosCamera(ActionEvent evento) {
        String categoria = (String) seletorCategoriaCamera.getSelectedItem();
        List<String> modelos;
        if ("Camera de caminhonete".equals(categoria)) {
            modelos = new CameraDeCaminhonete().listarModelos();
        } else if ("Camera de caminhao".equals(categoria)) {
            modelos = new CameraDeCaminhao().listarModelos();
        } else {
            modelos = new CameraDeCarro().listarModelos();
        }
        seletorModeloCamera.setModel(new DefaultComboBoxModel<>(modelos.toArray(new String[0])));
    }

    private void cadastrarProduto(ActionEvent evento) {
        DadosProduto dados = lerFormulario();
        if (dados == null) {
            return;
        }
        try {
            Produto novo = new Produto(dados.nome, dados.preco, dados.estoque,
                    dados.categoria, dados.marca);
            produtoService.cadastrar(novo);
            Mensagens.sucesso(this, "Produto cadastrado com sucesso!");
            limparFormulario();
            atualizarTabela(produtoService.listarTodosProdutos());
        } catch (RuntimeException ex) {
            Mensagens.erro(this, "Erro ao cadastrar produto", ex);
        }
    }

    private void atualizarProduto(ActionEvent evento) {
        if (produtoSelecionadoId == null) {
            Mensagens.aviso(this, "Selecione um produto para atualizar.");
            return;
        }
        DadosProduto dados = lerFormulario();
        if (dados == null) {
            return;
        }
        try {
            produtoService.atualizar(produtoSelecionadoId, dados.nome, dados.preco,
                    dados.estoque, dados.categoria, dados.marca);
            Mensagens.sucesso(this, "Produto atualizado com sucesso!");
            limparFormulario();
            atualizarTabela(produtoService.listarTodosProdutos());
        } catch (RuntimeException ex) {
            Mensagens.erro(this, "Erro ao atualizar produto", ex);
        }
    }

    private void removerProduto(ActionEvent evento) {
        if (produtoSelecionadoId == null) {
            Mensagens.aviso(this, "Selecione um produto para remover.");
            return;
        }
        if (Mensagens.confirmar(this, "Tem certeza que deseja remover este produto?")) {
            try {
                produtoService.remover(produtoSelecionadoId);
                Mensagens.sucesso(this, "Produto removido com sucesso!");
                limparFormulario();
                atualizarTabela(produtoService.listarTodosProdutos());
            } catch (RuntimeException ex) {
                Mensagens.erro(this, "Erro ao remover produto", ex);
            }
        }
    }

    private void buscarPorNome(ActionEvent evento) {
        String texto = campoBusca.getText().trim();
        if (texto.isEmpty()) {
            atualizarTabela(produtoService.listarTodosProdutos());
            return;
        }
        List<Produto> encontrados = produtoService.buscarPorNomeLista(texto);
        if (encontrados.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Nenhum produto encontrado com esse nome.",
                    "Busca", JOptionPane.INFORMATION_MESSAGE);
        }
        atualizarTabela(encontrados);
    }

    private void buscarPorCategoria(ActionEvent evento) {
        String texto = campoBusca.getText().trim();
        if (texto.isEmpty()) {
            atualizarTabela(produtoService.listarTodosProdutos());
            return;
        }
        List<Produto> encontrados = produtoService.buscarPorCategoriaLista(texto);
        if (encontrados.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Nenhum produto encontrado com essa categoria.",
                    "Busca", JOptionPane.INFORMATION_MESSAGE);
        }
        atualizarTabela(encontrados);
    }

    private void exibirRelatorio(ActionEvent evento) {
        String relatorio = produtoService.gerarRelatorio();
        JOptionPane.showMessageDialog(this,
                relatorio,
                "Relatorio de Produtos",
                JOptionPane.INFORMATION_MESSAGE);
    }

    private DadosProduto lerFormulario() {
        String tipo = (String) seletorTipo.getSelectedItem();
        String precoTexto = campoPreco.getText().trim();
        String estoqueTexto = campoEstoque.getText().trim();
        if (precoTexto.isEmpty() || estoqueTexto.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Preencha preco e estoque.",
                    "Validacao", JOptionPane.WARNING_MESSAGE);
            return null;
        }
        double preco;
        int estoque;
        try {
            preco = Double.parseDouble(precoTexto.replace(",", "."));
            estoque = Integer.parseInt(estoqueTexto);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                    "Preco deve ser numerico e estoque inteiro.",
                    "Validacao", JOptionPane.WARNING_MESSAGE);
            return null;
        }
        if (preco <= 0) {
            JOptionPane.showMessageDialog(this,
                    "Preco deve ser maior que 0.",
                    "Validacao", JOptionPane.WARNING_MESSAGE);
            return null;
        }
        if (estoque < 0) {
            JOptionPane.showMessageDialog(this,
                    "Estoque nao pode ser negativo.",
                    "Validacao", JOptionPane.WARNING_MESSAGE);
            return null;
        }

        String nome;
        String categoria;
        String marca;

        if (TIPO_PNEU.equals(tipo)) {
            String marcaSelecionada = (String) seletorMarca.getSelectedItem();
            if ("Outros".equals(marcaSelecionada)) {
                marca = campoMarcaOutros.getText().trim();
                if (marca.isEmpty()) {
                    JOptionPane.showMessageDialog(this,
                            "Informe o nome da marca em 'Marca (Outros)'.",
                            "Validacao", JOptionPane.WARNING_MESSAGE);
                    return null;
                }
            } else {
                marca = marcaSelecionada;
            }
            categoria = (String) seletorCategoriaPneu.getSelectedItem();
            nome = (String) seletorMedidaPneu.getSelectedItem();
        } else if (TIPO_CAMERA.equals(tipo)) {
            marca = "";
            categoria = (String) seletorCategoriaCamera.getSelectedItem();
            nome = (String) seletorModeloCamera.getSelectedItem();
        } else {
            marca = "";
            categoria = "Quimicos";
            nome = campoNomeQuimico.getText().trim();
            if (nome.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Informe o nome do produto quimico.",
                        "Validacao", JOptionPane.WARNING_MESSAGE);
                return null;
            }
        }

        return new DadosProduto(nome, categoria, marca, preco, estoque);
    }

    private void preencherFormularioComSelecao() {
        int linha = tabela.getSelectedRow();
        if (linha < 0) {
            return;
        }
        produtoSelecionadoId = ((Number) modeloTabela.getValueAt(linha, 0)).longValue();
        String nome = String.valueOf(modeloTabela.getValueAt(linha, 1));
        String marca = String.valueOf(modeloTabela.getValueAt(linha, 2));
        String categoria = String.valueOf(modeloTabela.getValueAt(linha, 3));
        String precoTexto = String.valueOf(modeloTabela.getValueAt(linha, 4)).replace(",", ".");
        String estoque = String.valueOf(modeloTabela.getValueAt(linha, 5));
        campoPreco.setText(precoTexto);
        campoEstoque.setText(estoque);

        if (categoria != null && categoria.startsWith("Pneu")) {
            seletorTipo.setSelectedItem(TIPO_PNEU);
            selecionarComboOuOutros(seletorMarca, campoMarcaOutros, marca);
            seletorCategoriaPneu.setSelectedItem(categoria);
            atualizarMedidasPneu(null);
            seletorMedidaPneu.setSelectedItem(nome);
        } else if (categoria != null && categoria.startsWith("Camera")) {
            seletorTipo.setSelectedItem(TIPO_CAMERA);
            seletorCategoriaCamera.setSelectedItem(categoria);
            atualizarModelosCamera(null);
            seletorModeloCamera.setSelectedItem(nome);
        } else {
            seletorTipo.setSelectedItem(TIPO_QUIMICO);
            campoNomeQuimico.setText(nome);
        }
    }

    private void selecionarComboOuOutros(JComboBox<String> combo, JTextField campoOutros, String valor) {
        for (int i = 0; i < combo.getItemCount(); i++) {
            if (combo.getItemAt(i).equals(valor)) {
                combo.setSelectedIndex(i);
                campoOutros.setText("");
                campoOutros.setEnabled(false);
                return;
            }
        }
        combo.setSelectedItem("Outros");
        campoOutros.setEnabled(true);
        campoOutros.setText(valor != null ? valor : "");
    }

    private void limparFormulario() {
        produtoSelecionadoId = null;
        campoPreco.setText("");
        campoEstoque.setText("");
        campoMarcaOutros.setText("");
        campoMarcaOutros.setEnabled(false);
        seletorMarca.setSelectedIndex(0);
        seletorCategoriaPneu.setSelectedIndex(0);
        atualizarMedidasPneu(null);
        seletorCategoriaCamera.setSelectedIndex(0);
        atualizarModelosCamera(null);
        campoNomeQuimico.setText("");
        seletorTipo.setSelectedIndex(0);
        tabela.clearSelection();
    }

    private void atualizarTabela(List<Produto> produtos) {
        modeloTabela.setRowCount(0);
        for (Produto p : produtos) {
            modeloTabela.addRow(new Object[]{
                    p.getId(), p.getNome(),
                    p.getMarca() != null ? p.getMarca() : "",
                    p.getCategoria(),
                    String.format("%.2f", p.getPreco()),
                    p.getQuantidadeEstoque()
            });
        }
    }

    private static class DadosProduto {
        final String nome;
        final String categoria;
        final String marca;
        final double preco;
        final int estoque;

        DadosProduto(String nome, String categoria, String marca, double preco, int estoque) {
            this.nome = nome;
            this.categoria = categoria;
            this.marca = marca;
            this.preco = preco;
            this.estoque = estoque;
        }
    }
}
