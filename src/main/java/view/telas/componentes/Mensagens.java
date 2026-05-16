package view.telas.componentes;

import javax.swing.JOptionPane;

import java.awt.Component;

public class Mensagens {

    private Mensagens() {
    }

    public static void sucesso(Component parent, String mensagem) {
        JOptionPane.showMessageDialog(parent, mensagem,
                "Sucesso", JOptionPane.INFORMATION_MESSAGE);
    }

    public static void aviso(Component parent, String mensagem) {
        JOptionPane.showMessageDialog(parent, mensagem,
                "Atencao", JOptionPane.WARNING_MESSAGE);
    }

    public static void info(Component parent, String mensagem, String titulo) {
        JOptionPane.showMessageDialog(parent, mensagem,
                titulo, JOptionPane.INFORMATION_MESSAGE);
    }

    public static void erro(Component parent, String titulo, Exception ex) {
        Throwable causa = ex;
        while (causa.getCause() != null && causa.getCause() != causa) {
            causa = causa.getCause();
        }
        String detalhe = causa.getMessage() != null ? causa.getMessage() : causa.getClass().getSimpleName();
        JOptionPane.showMessageDialog(parent,
                titulo + ":\n\n" + detalhe,
                "Erro", JOptionPane.ERROR_MESSAGE);
    }

    public static void erro(Component parent, String mensagem) {
        JOptionPane.showMessageDialog(parent, mensagem,
                "Erro", JOptionPane.ERROR_MESSAGE);
    }

    public static boolean confirmar(Component parent, String mensagem) {
        int op = JOptionPane.showConfirmDialog(parent, mensagem,
                "Confirmar", JOptionPane.YES_NO_OPTION);
        return op == JOptionPane.YES_OPTION;
    }
}
