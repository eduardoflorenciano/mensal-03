package entity;

import jakarta.persistence.*;

@Entity
@DiscriminatorValue("ADMIN")
public class Admin extends Usuario {

    public Admin() { super(); }

    public Admin(String nome, String email, String senha) {
        super(nome, email, senha);
    }

    @Override
    public String exibirPerfil() {
        return "[ ADMIN ] " + getNome() + " | " + getEmail();
    }
}
