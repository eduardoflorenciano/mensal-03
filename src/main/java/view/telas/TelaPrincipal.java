package view.telas;

import entity.Admin;
import entity.Mecanico;
import entity.Usuario;
import service.automovel.AutomovelService;
import service.cliente.ClienteService;
import service.ordemservico.OrdemServicoService;
import service.produto.ProdutoService;
import service.servico.ServicoService;
import service.usuario.GerenciadorUsuarioService;
import view.telas.componentes.EstiloApp;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;

public class TelaPrincipal extends JFrame {

    private final ClienteService clienteService;
    private final AutomovelService automovelService;
    private final ProdutoService produtoService;
    private final ServicoService servicoService;
    private final OrdemServicoService ordemServicoService;
    private final GerenciadorUsuarioService usuarioService;
    private final Usuario usuarioLogado;

    public TelaPrincipal(ClienteService clienteService,
                         AutomovelService automovelService,
                         ProdutoService produtoService,
                         ServicoService servicoService,
                         OrdemServicoService ordemServicoService,
                         GerenciadorUsuarioService usuarioService,
                         Usuario usuarioLogado) {
        this.clienteService = clienteService;
        this.automovelService = automovelService;
        this.produtoService = produtoService;
        this.servicoService = servicoService;
        this.ordemServicoService = ordemServicoService;
        this.usuarioService = usuarioService;
        this.usuarioLogado = usuarioLogado;

        configurarJanela();
        montarConteudo();
    }

    private void configurarJanela() {
        setTitle("Auto Center Silva - Sistema da Oficina");
        setSize(720, 540);
        setMinimumSize(new Dimension(640, 480));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setBackground(EstiloApp.CINZA_FUNDO);
    }

    private void montarConteudo() {
        setLayout(new BorderLayout());
        add(criarCabecalho(), BorderLayout.NORTH);
        add(criarPainelBotoes(), BorderLayout.CENTER);
        add(criarRodape(), BorderLayout.SOUTH);
    }

    private JPanel criarCabecalho() {
        JPanel cabecalho = new JPanel(new BorderLayout());
        cabecalho.setBackground(EstiloApp.AZUL_PRIMARIO);
        cabecalho.setBorder(new EmptyBorder(18, 20, 18, 20));

        JLabel titulo = new JLabel("AUTO CENTER SILVA");
        titulo.setFont(EstiloApp.FONTE_TITULO);
        titulo.setForeground(EstiloApp.BRANCO);
        titulo.setHorizontalAlignment(SwingConstants.CENTER);

        String perfil = usuarioLogado != null ? usuarioLogado.exibirPerfil() : "";
        JLabel lblPerfil = new JLabel("Logado como: " + perfil);
        lblPerfil.setFont(EstiloApp.FONTE_NORMAL);
        lblPerfil.setForeground(EstiloApp.BRANCO);
        lblPerfil.setHorizontalAlignment(SwingConstants.CENTER);

        cabecalho.add(titulo, BorderLayout.CENTER);
        cabecalho.add(lblPerfil, BorderLayout.SOUTH);
        return cabecalho;
    }

    private JPanel criarPainelBotoes() {
        boolean isAdmin = usuarioLogado instanceof Admin;
        boolean isMecanico = usuarioLogado instanceof Mecanico;

        JButton botaoOrdens = EstiloApp.criarBotaoPrimario("Ordens de Serviço");
        JButton botaoSair   = EstiloApp.criarBotaoPerigo("Sair");
        botaoOrdens.addActionListener(this::abrirTelaOrdens);
        botaoSair.addActionListener(this::sairDoSistema);

        if (isMecanico) {
            JPanel painel = new JPanel(new GridLayout(2, 1, 16, 16));
            painel.setBorder(new EmptyBorder(24, 120, 24, 120));
            painel.setBackground(EstiloApp.CINZA_FUNDO);
            painel.add(botaoOrdens);
            painel.add(botaoSair);
            return painel;
        }

        int linhas = isAdmin ? 4 : 3;
        JPanel painel = new JPanel(new GridLayout(linhas, 2, 16, 16));
        painel.setBorder(new EmptyBorder(24, 32, 24, 32));
        painel.setBackground(EstiloApp.CINZA_FUNDO);

        JButton botaoClientes   = EstiloApp.criarBotaoPrimario("Gerenciar Clientes");
        JButton botaoAutomoveis = EstiloApp.criarBotaoPrimario("Gerenciar Automóveis");
        JButton botaoProdutos   = EstiloApp.criarBotaoPrimario("Gerenciar Produtos");
        JButton botaoServicos   = EstiloApp.criarBotaoPrimario("Gerenciar Serviços");

        botaoClientes.addActionListener(this::abrirTelaClientes);
        botaoAutomoveis.addActionListener(this::abrirTelaAutomoveis);
        botaoProdutos.addActionListener(this::abrirTelaProdutos);
        botaoServicos.addActionListener(this::abrirTelaServicos);

        painel.add(botaoClientes);
        painel.add(botaoAutomoveis);
        painel.add(botaoProdutos);
        painel.add(botaoServicos);
        painel.add(botaoOrdens);

        if (isAdmin) {
            JButton botaoUsuarios = EstiloApp.criarBotaoSecundario("Gerenciar Usuários");
            botaoUsuarios.addActionListener(this::abrirTelaUsuarios);
            painel.add(botaoUsuarios);
        }

        painel.add(botaoSair);
        return painel;
    }

    private JPanel criarRodape() {
        JPanel rodape = new JPanel();
        rodape.setBackground(EstiloApp.CINZA_FUNDO);
        rodape.setBorder(new EmptyBorder(8, 16, 16, 16));
        JLabel info = new JLabel("Selecione uma opção acima para começar");
        info.setFont(EstiloApp.FONTE_NORMAL);
        info.setForeground(EstiloApp.PRETO_TEXTO);
        rodape.add(info);
        return rodape;
    }

    private void abrirTelaClientes(ActionEvent e) {
        TelaCliente tela = new TelaCliente(clienteService);
        tela.setLocationRelativeTo(this);
        tela.setVisible(true);
    }

    private void abrirTelaAutomoveis(ActionEvent e) {
        TelaAutomovel tela = new TelaAutomovel(automovelService, clienteService);
        tela.setLocationRelativeTo(this);
        tela.setVisible(true);
    }

    private void abrirTelaProdutos(ActionEvent e) {
        TelaProduto tela = new TelaProduto(produtoService);
        tela.setLocationRelativeTo(this);
        tela.setVisible(true);
    }

    private void abrirTelaServicos(ActionEvent e) {
        TelaServico tela = new TelaServico(servicoService);
        tela.setLocationRelativeTo(this);
        tela.setVisible(true);
    }

    private void abrirTelaOrdens(ActionEvent e) {
        TelaOrdemServico tela = new TelaOrdemServico(
                ordemServicoService, clienteService, automovelService,
                servicoService, produtoService);
        tela.setLocationRelativeTo(this);
        tela.setVisible(true);
    }

    private void abrirTelaUsuarios(ActionEvent e) {
        TelaUsuario tela = new TelaUsuario(usuarioService, usuarioLogado.getId());
        tela.setLocationRelativeTo(this);
        tela.setVisible(true);
    }

    private void sairDoSistema(ActionEvent e) {
        dispose();
        System.exit(0);
    }
}
