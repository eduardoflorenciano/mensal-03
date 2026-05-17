package entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "produto")
public class Produto extends Item {

    @Column(name = "quantidade_estoque")
    private int quantidadeEstoque;

    @Column(name = "categoria")
    private String categoria;

    @Column(name = "marca")
    private String marca;

    public Produto() {
        super();
    }

    public Produto(String nome, double preco, int quantidadeEstoque, String categoria, String marca) {
        super(nome, preco);
        this.quantidadeEstoque = quantidadeEstoque;
        this.categoria = categoria;
        this.marca = marca;
    }

    public int getQuantidadeEstoque() {
        return quantidadeEstoque;
    }

    public void setQuantidadeEstoque(int quantidadeEstoque) {
        this.quantidadeEstoque = quantidadeEstoque;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    @Override
    public String exibirDetalhes() {
        return String.format(
                "[PRODUTO] ID: %d | Nome: %s %s | Categoria: %s | Preco: R$ %.2f | Estoque: %d unidades",
                getId(), getMarca() != null ? getMarca() : "", getNome(), categoria, getPreco(), quantidadeEstoque
        );
    }
}
