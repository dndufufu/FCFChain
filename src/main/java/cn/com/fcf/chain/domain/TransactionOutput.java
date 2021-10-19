package cn.com.fcf.chain.domain;

import java.io.Serializable;
import javax.persistence.*;
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
    private Long id;

    @Column(name = "recipient")
    private String recipient;

    @Column(name = "value")
    private Double value;

    @Column(name = "parent_transaction_id")
    private String parentTransactionId;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TransactionOutput id(Long id) {
        this.id = id;
        return this;
    }

    public String getRecipient() {
        return this.recipient;
    }

    public TransactionOutput recipient(String recipient) {
        this.recipient = recipient;
        return this;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public Double getValue() {
        return this.value;
    }

    public TransactionOutput value(Double value) {
        this.value = value;
        return this;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public String getParentTransactionId() {
        return this.parentTransactionId;
    }

    public TransactionOutput parentTransactionId(String parentTransactionId) {
        this.parentTransactionId = parentTransactionId;
        return this;
    }

    public void setParentTransactionId(String parentTransactionId) {
        this.parentTransactionId = parentTransactionId;
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
