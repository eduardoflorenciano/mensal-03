package view.telas.componentes;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;

public class EstiloApp {

    public static final Color AZUL_PRIMARIO = new Color(0x1E3A8A);
    public static final Color AZUL_CLARO    = new Color(0x3B82F6);
    public static final Color VERDE         = new Color(0x16A34A);
    public static final Color VERMELHO      = new Color(0xDC2626);
    public static final Color AMARELO       = new Color(0xCA8A04);
    public static final Color CINZA_FUNDO   = new Color(0xF3F4F6);
    public static final Color CINZA_BORDA   = new Color(0xD1D5DB);
    public static final Color BRANCO        = Color.WHITE;
    public static final Color PRETO_TEXTO   = new Color(0x111827);

    public static final Font FONTE_TITULO   = new Font("SansSerif", Font.BOLD, 22);
    public static final Font FONTE_SUBTITULO = new Font("SansSerif", Font.BOLD, 16);
    public static final Font FONTE_NORMAL   = new Font("SansSerif", Font.PLAIN, 14);
    public static final Font FONTE_BOTAO    = new Font("SansSerif", Font.BOLD, 14);

    private EstiloApp() {
    }

    public static JButton criarBotao(String texto, Color fundo) {
        JButton botao = new JButton(texto);
        botao.setBackground(fundo);
        botao.setForeground(BRANCO);
        botao.setFont(FONTE_BOTAO);
        botao.setFocusPainted(false);
        botao.setOpaque(true);
        botao.setBorderPainted(false);
        botao.setContentAreaFilled(true);
        botao.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        botao.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(fundo.darker(), 1),
                new EmptyBorder(8, 16, 8, 16)));
        return botao;
    }

    public static JButton criarBotaoPrimario(String texto) {
        return criarBotao(texto, AZUL_PRIMARIO);
    }

    public static JButton criarBotaoSucesso(String texto) {
        return criarBotao(texto, VERDE);
    }

    public static JButton criarBotaoPerigo(String texto) {
        return criarBotao(texto, VERMELHO);
    }

    public static JButton criarBotaoSecundario(String texto) {
        return criarBotao(texto, AMARELO);
    }

    public static JLabel criarTitulo(String texto) {
        JLabel label = new JLabel(texto);
        label.setFont(FONTE_TITULO);
        label.setForeground(AZUL_PRIMARIO);
        return label;
    }

    public static JLabel criarSubtitulo(String texto) {
        JLabel label = new JLabel(texto);
        label.setFont(FONTE_SUBTITULO);
        label.setForeground(PRETO_TEXTO);
        return label;
    }

    public static JLabel criarLabel(String texto) {
        JLabel label = new JLabel(texto);
        label.setFont(FONTE_NORMAL);
        label.setForeground(PRETO_TEXTO);
        return label;
    }

    public static JTextField criarCampoTexto() {
        JTextField campo = new JTextField();
        campo.setFont(FONTE_NORMAL);
        campo.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(CINZA_BORDA),
                new EmptyBorder(6, 8, 6, 8)));
        return campo;
    }

    public static Border bordaPainel(String titulo) {
        return BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(
                        BorderFactory.createLineBorder(CINZA_BORDA),
                        titulo,
                        0, 0,
                        FONTE_SUBTITULO,
                        AZUL_PRIMARIO),
                new EmptyBorder(8, 8, 8, 8));
    }
}
