package cn.com.fcf.chain.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A TransactionInput.
 */
@Entity
@Table(name = "transaction_input")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class TransactionInput implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "transaction_output_id")
    private String transactionOutputId;

    //    @Column(name = "u_txo")
    private TransactionOutput uTXO;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "transactionInputs", "transactionOutputs", "block" }, allowSetters = true)
    private Transaction transaction;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public TransactionInput(String transactionOutputId) {
        this.transactionOutputId = transactionOutputId;
    }

    public Long getId() {
        return this.id;
    }

    public TransactionInput id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTransactionOutputId() {
        return this.transactionOutputId;
    }

    public TransactionInput transactionOutputId(String transactionOutputId) {
        this.setTransactionOutputId(transactionOutputId);
        return this;
    }

    public void setTransactionOutputId(String transactionOutputId) {
        this.transactionOutputId = transactionOutputId;
    }

    @OneToOne
    public TransactionOutput getuTXO() {
        return this.uTXO;
    }

    public TransactionInput uTXO(TransactionOutput uTXO) {
        this.setuTXO(uTXO);
        return this;
    }

    public void setuTXO(TransactionOutput uTXO) {
        this.uTXO = uTXO;
    }

    public Transaction getTransaction() {
        return this.transaction;
    }

    public void setTransaction(Transaction transaction) {
        this.transaction = transaction;
    }

    public TransactionInput transaction(Transaction transaction) {
        this.setTransaction(transaction);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TransactionInput)) {
            return false;
        }
        return id != null && id.equals(((TransactionInput) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TransactionInput{" +
            "id=" + getId() +
            ", transactionOutputId='" + getTransactionOutputId() + "'" +
            ", uTXO='" + getuTXO() + "'" +
            "}";
    }
}
