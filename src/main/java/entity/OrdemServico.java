package entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "ordem_servico")
public class OrdemServico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "data_abertura")
    private LocalDate dataAbertura;

    @Column(name = "data_finalizacao")
    private LocalDate dataFinalizacao;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "automovel_id")
    private Automovel automovel;

    @Column(name = "status")
    private String status;

    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "os_servico",
            joinColumns = @JoinColumn(name = "ordem_servico_id"),
            inverseJoinColumns = @JoinColumn(name = "servico_id"))
    private List<Servico> servicos = new ArrayList<>();

    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "os_produto",
            joinColumns = @JoinColumn(name = "ordem_servico_id"),
            inverseJoinColumns = @JoinColumn(name = "produto_id"))
    private List<Produto> produtos = new ArrayList<>();

    public OrdemServico() {
    }

    public OrdemServico(Cliente cliente, Automovel automovel) {
        this.cliente = cliente;
        this.automovel = automovel;
        this.dataAbertura = LocalDate.now();
        this.status = "ABERTA";
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Automovel getAutomovel() {
        return automovel;
    }

    public void setAutomovel(Automovel automovel) {
        this.automovel = automovel;
    }

    public LocalDate getDataAbertura() {
        return dataAbertura;
    }

    public void setDataAbertura(LocalDate dataAbertura) {
        this.dataAbertura = dataAbertura;
    }

    public LocalDate getDataFinalizacao() {
        return dataFinalizacao;
    }

    public void setDataFinalizacao(LocalDate dataFinalizacao) {
        this.dataFinalizacao = dataFinalizacao;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void finalizarOrdem() {
        this.status = "FINALIZADA";
        this.dataFinalizacao = LocalDate.now();
    }

    public void cancelarOrdem() {
        this.status = "CANCELADA";
        this.dataFinalizacao = LocalDate.now();
    }

    public void atualizarStatus(String novoStatus) {
        this.status = novoStatus;
    }

    public void adicionarServico(Servico servico) {
        this.servicos.add(servico);
    }

    public List<Servico> getServicos() {
        return servicos;
    }

    public void setServicos(List<Servico> servicos) {
        this.servicos = servicos;
    }

    public void adicionarProduto(Produto produto) {
        produtos.add(produto);
    }

    public List<Produto> getProdutos() {
        return produtos;
    }

    public void setProdutos(List<Produto> produtos) {
        this.produtos = produtos;
    }

    public float calcularValorTotal() {
        float total = 0;
        for (Servico s : servicos) {
            total += s.getPreco();
        }
        for (Produto p : produtos) {
            total += p.getPreco();
        }
        return total;
    }

    public String exibirDetalhes() {
        return String.format(
                "[ORDEM] ID: %d | Cliente: %s | Veiculo: %s | Status: %s | Total: R$ %.2f",
                id,
                cliente != null ? cliente.getNome() : "-",
                automovel != null ? automovel.getModelo() : "-",
                status,
                calcularValorTotal()
        );
    }
}
