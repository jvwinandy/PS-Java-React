package br.com.banco.bankTransfer;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;

@Entity
@Table(name = "transferencia")
public final class BankTransfer {
    private @Id Long id;
    private Timestamp dataTransferencia;
    private Double valor;
    private String tipo;
    private String nomeOperadorTransacao;
    private int contaId;

    public Long getId() {
        return id;
    }

    public String getTipo() {
        return tipo;
    }

    public Timestamp getDataTransferencia() {
        return dataTransferencia;
    }

    public Double getValor() {
        return valor;
    }

    public String getNomeOperadorTransacao() {
        return nomeOperadorTransacao;
    }

    public int getContaId() {
        return contaId;
    }
}

