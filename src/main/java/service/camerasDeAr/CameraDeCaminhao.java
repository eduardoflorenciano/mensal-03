package service.camerasDeAr;

import interfaces.CamerasDeAr;

import java.util.ArrayList;
import java.util.Scanner;

public class CameraDeCaminhao implements CamerasDeAr {
    static Scanner scanner = new Scanner(System.in);

    public ArrayList<String> listarModelos() {
        ArrayList<String> lista = new ArrayList<>();
        lista.add("Camara de ar 900-20");
        lista.add("Camara de ar 1000-20");
        lista.add("Camara de ar 275/80 R22.5");
        lista.add("Camara de ar 295/80 R22.5");
        return lista;
    }

    @Override
    public String escolherCamera() {
        System.out.println("\n===== Camera de Ar Caminhão =====");
        ArrayList<String> lista = listarModelos();

        for (int i = 0; i < lista.size(); i++) {
            System.out.println((i + 1) + " - " + lista.get(i));
        }
        int escolha = lerInteiro("Escolha a Camera de Ar: ");

        if (escolha >= 1 && escolha <= lista.size()) {
            return lista.get(escolha - 1);
        } else {
            System.out.println("Opção inválida!");
            return escolherCamera();
        }
    }

    static int lerInteiro(String mensagem) {
        while (true) {
            System.out.print(mensagem);
            try {
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Entrada invalida! Digite apenas numeros inteiros.");
            }
        }
    }
}
