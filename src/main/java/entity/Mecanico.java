package entity;

import jakarta.persistence.*;

@Entity
@DiscriminatorValue("MECANICO")
public class Mecanico extends Usuario {

    public Mecanico() { super(); }

    public Mecanico(String nome, String email, String senha) {
        super(nome, email, senha);
    }

    @Override
    public String exibirPerfil() {
        return "[ MECANICO ] " + getNome() + " | " + getEmail();
    }
}
