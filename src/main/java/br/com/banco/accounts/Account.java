package br.com.banco.accounts;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "conta")
public final class Account {
    private @Id Long idConta;
    private String nomeResponsavel;

    public String getNomeResponsavel() {
        return nomeResponsavel;
    }

    public Long getIdConta() {
        return idConta;
    }
}
