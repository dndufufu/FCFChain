package cn.com.fcf.chain.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.security.PublicKey;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Transaction.
 */
@Entity
@Table(name = "transaction")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Transaction implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "hash")
    private String hash;

    @Column(name = "sender")
    private PublicKey sender;

    @Column(name = "recipient")
    private PublicKey recipient;

    @Column(name = "value")
    private Double value;

    @Column(name = "signature")
    private byte[] signature;

    @Column(name = "timestamp")
    private Instant timestamp;

    @Column(name = "status")
    private Boolean status;

    @OneToMany(mappedBy = "transaction")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "transaction" }, allowSetters = true)
    private List<TransactionInput> transactionInputs = new ArrayList<>();

    @OneToMany(mappedBy = "transaction")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "transaction" }, allowSetters = true)
    private List<TransactionOutput> transactionOutputs = new ArrayList<>();

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "transactions" }, allowSetters = true)
    private Block block;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Transaction id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getHash() {
        return this.hash;
    }

    public Transaction hash(String hash) {
        this.setHash(hash);
        return this;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public PublicKey getSender() {
        return this.sender;
    }

    public Transaction sender(PublicKey sender) {
        this.setSender(sender);
        return this;
    }

    public void setSender(PublicKey sender) {
        this.sender = sender;
    }

    public PublicKey getRecipient() {
        return this.recipient;
    }

    public Transaction recipient(PublicKey recipient) {
        this.setRecipient(recipient);
        return this;
    }

    public void setRecipient(PublicKey recipient) {
        this.recipient = recipient;
    }

    public Double getValue() {
        return this.value;
    }

    public Transaction value(Double value) {
        this.setValue(value);
        return this;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public byte[] getSignature() {
        return this.signature;
    }

    public Transaction signature(byte[] signature) {
        this.setSignature(signature);
        return this;
    }

    public void setSignature(byte[] signature) {
        this.signature = signature;
    }

    public Instant getTimestamp() {
        return this.timestamp;
    }

    public Transaction timestamp(Instant timestamp) {
        this.setTimestamp(timestamp);
        return this;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }

    public Boolean getStatus() {
        return this.status;
    }

    public Transaction status(Boolean status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public List<TransactionInput> getTransactionInputs() {
        return this.transactionInputs;
    }

    public void setTransactionInputs(List<TransactionInput> transactionInputs) {
        if (this.transactionInputs != null) {
            this.transactionInputs.forEach(i -> i.setTransaction(null));
        }
        if (transactionInputs != null) {
            transactionInputs.forEach(i -> i.setTransaction(this));
        }
        this.transactionInputs = transactionInputs;
    }

    public Transaction transactionInputs(List<TransactionInput> transactionInputs) {
        this.setTransactionInputs(transactionInputs);
        return this;
    }

    public Transaction addTransactionInput(TransactionInput transactionInput) {
        this.transactionInputs.add(transactionInput);
        transactionInput.setTransaction(this);
        return this;
    }

    public Transaction removeTransactionInput(TransactionInput transactionInput) {
        this.transactionInputs.remove(transactionInput);
        transactionInput.setTransaction(null);
        return this;
    }

    public List<TransactionOutput> getTransactionOutputs() {
        return this.transactionOutputs;
    }

    public void setTransactionOutputs(List<TransactionOutput> transactionOutputs) {
        if (this.transactionOutputs != null) {
            this.transactionOutputs.forEach(i -> i.setTransaction(null));
        }
        if (transactionOutputs != null) {
            transactionOutputs.forEach(i -> i.setTransaction(this));
        }
        this.transactionOutputs = transactionOutputs;
    }

    public Transaction transactionOutputs(List<TransactionOutput> transactionOutputs) {
        this.setTransactionOutputs(transactionOutputs);
        return this;
    }

    public Transaction addTransactionOutput(TransactionOutput transactionOutput) {
        this.transactionOutputs.add(transactionOutput);
        transactionOutput.setTransaction(this);
        return this;
    }

    public Transaction removeTransactionOutput(TransactionOutput transactionOutput) {
        this.transactionOutputs.remove(transactionOutput);
        transactionOutput.setTransaction(null);
        return this;
    }

    public Block getBlock() {
        return this.block;
    }

    public void setBlock(Block block) {
        this.block = block;
    }

    public Transaction block(Block block) {
        this.setBlock(block);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Transaction)) {
            return false;
        }
        return id != null && id.equals(((Transaction) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Transaction{" +
            "id=" + getId() +
            ", hash='" + getHash() + "'" +
            ", sender='" + getSender() + "'" +
            ", recipient='" + getRecipient() + "'" +
            ", value=" + getValue() +
            ", signature='" + getSignature() + "'" +
            ", timestamp='" + getTimestamp() + "'" +
            ", status='" + getStatus() + "'" +
            "}";
    }
}
