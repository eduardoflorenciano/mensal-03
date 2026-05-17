package entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "automovel")
public class Automovel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "modelo")
    private String modelo;

    @Column(name = "placa")
    private String placa;

    @Column(name = "ano_fabricacao")
    private int anoFabricacao;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "cliente_id", nullable = true)
    private Cliente cliente;

    public Automovel() {
    }

    public Automovel(String modelo, String placa, int anoFabricacao) {
        this.modelo = modelo;
        this.placa = placa;
        this.anoFabricacao = anoFabricacao;
    }

    public Automovel(Automovel copia) {
        this.modelo = copia.getModelo();
        this.placa = copia.getPlaca();
        this.anoFabricacao = copia.getAnoFabricacao();
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

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public int getAnoFabricacao() {
        return anoFabricacao;
    }

    public void setAnoFabricacao(int anoFabricacao) {
        this.anoFabricacao = anoFabricacao;
    }

    public String exibirDetalhes() {
        return String.format(
                "[Automovel] ID: %d | Modelo: %s | Placa: %s | Data Fabricacao: %d",
                id, modelo, placa, anoFabricacao
        );
    }
}
