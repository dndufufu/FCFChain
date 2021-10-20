package cn.com.fcf.chain.domain;

import cn.com.fcf.chain.util.StringUtil;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.security.PublicKey;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A TransactionOutput.
 */
@Entity
@Table(name = "transaction_output")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class TransactionOutput implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "transaction_output_id")
    private String transactionOutputId;

    @Column(name = "recipient")
    private PublicKey recipient;

    @Column(name = "value")
    private Double value;

    @Column(name = "parent_transaction_id")
    private String parentTransactionId;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "transactionInputs", "transactionOutputs", "block" }, allowSetters = true)
    private Transaction transaction;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public TransactionOutput(PublicKey recipient, Double value, String parentTransactionId) {
        this.recipient = recipient;
        this.value = value;
        this.parentTransactionId = parentTransactionId;
        this.transactionOutputId =
            StringUtil.applySha256(StringUtil.getStringFromKey(recipient) + Double.toString(value) + parentTransactionId);
    }

    public TransactionOutput() {}

    public boolean isMine(PublicKey publicKey) {
        return (publicKey == recipient);
    }

    public String getTransactionOutputId() {
        return this.transactionOutputId;
    }

    public TransactionOutput TransactionOutputId(String transactionOutputId) {
        this.setTransactionOutputId(transactionOutputId);
        return this;
    }

    public void setTransactionOutputId(String transactionOutputId) {
        this.transactionOutputId = transactionOutputId;
    }

    public Long getId() {
        return this.id;
    }

    public TransactionOutput id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public PublicKey getRecipient() {
        return this.recipient;
    }

    public TransactionOutput recipient(PublicKey recipient) {
        this.setRecipient(recipient);
        return this;
    }

    public void setRecipient(PublicKey recipient) {
        this.recipient = recipient;
    }

    public Double getValue() {
        return this.value;
    }

    public TransactionOutput value(Double value) {
        this.setValue(value);
        return this;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public String getParentTransactionId() {
        return this.parentTransactionId;
    }

    public TransactionOutput parentTransactionId(String parentTransactionId) {
        this.setParentTransactionId(parentTransactionId);
        return this;
    }

    public void setParentTransactionId(String parentTransactionId) {
        this.parentTransactionId = parentTransactionId;
    }

    public Transaction getTransaction() {
        return this.transaction;
    }

    public void setTransaction(Transaction transaction) {
        this.transaction = transaction;
    }

    public TransactionOutput transaction(Transaction transaction) {
        this.setTransaction(transaction);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TransactionOutput)) {
            return false;
        }
        return id != null && id.equals(((TransactionOutput) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TransactionOutput{" +
            "id=" + getId() +
            ", recipient='" + getRecipient() + "'" +
            ", value=" + getValue() +
            ", parentTransactionId='" + getParentTransactionId() + "'" +
            "}";
    }
}
