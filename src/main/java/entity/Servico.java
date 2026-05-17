package entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "servico")
public class Servico extends Item {

    @Column(name = "duracao_minutos")
    private int duracaoMinutos;

    @Column(name = "tipo")
    private String tipo;

    public Servico() {
        super();
    }

    public Servico(String nome, double preco, int duracaoMinutos, String tipo) {
        super(nome, preco);
        this.duracaoMinutos = duracaoMinutos;
        this.tipo = tipo;
    }

    public int getDuracaoMinutos() {
        return duracaoMinutos;
    }

    public void setDuracaoMinutos(int duracaoMinutos) {
        this.duracaoMinutos = duracaoMinutos;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    @Override
    public String exibirDetalhes() {
        return String.format(
                "[SERVICO] ID: %d | Nome: %s | Tipo: %s | Preco: R$ %.2f | Duracao: %d min",
                getId(), getNome(), tipo, getPreco(), duracaoMinutos
        );
    }
}
