package entity;

import jakarta.persistence.*;

@Entity
@DiscriminatorValue("ATENDENTE")
public class Atendente extends Usuario {

    public Atendente() { super(); }

    public Atendente(String nome, String email, String senha) {
        super(nome, email, senha);
    }

    @Override
    public String exibirPerfil() {
        return "[ ATENDENTE ] " + getNome() + " | " + getEmail();
    }
}
