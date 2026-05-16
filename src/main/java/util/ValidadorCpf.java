package util;

public class ValidadorCpf {

    public static boolean validar(String cpf) {
        if (cpf == null) return false;
        cpf = cpf.replaceAll("[^0-9]", "");

        if (cpf.length() != 11) return false;

        boolean todosIguais = true;
        for (int i = 1; i < 11; i++) {
            if (cpf.charAt(i) != cpf.charAt(0)) { todosIguais = false; break; }
        }
        if (todosIguais) return false;

        int soma = 0;
        for (int i = 0; i < 9; i++) soma += (cpf.charAt(i) - '0') * (10 - i);
        int primeiro = 11 - (soma % 11);
        if (primeiro >= 10) primeiro = 0;
        if (primeiro != (cpf.charAt(9) - '0')) return false;

        soma = 0;
        for (int i = 0; i < 10; i++) soma += (cpf.charAt(i) - '0') * (11 - i);
        int segundo = 11 - (soma % 11);
        if (segundo >= 10) segundo = 0;
        return segundo == (cpf.charAt(10) - '0');
    }

    public static String formatar(String cpf) {
        if (cpf == null) return "";
        cpf = cpf.replaceAll("[^0-9]", "");
        if (cpf.length() != 11) return cpf;
        return cpf.substring(0, 3) + "." + cpf.substring(3, 6) + "."
                + cpf.substring(6, 9) + "-" + cpf.substring(9);
    }

    public static String limpar(String cpf) {
        return cpf == null ? "" : cpf.replaceAll("[^0-9]", "");
    }
}
