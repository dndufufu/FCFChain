package cn.com.fcf.chain.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Block.
 */
@Entity
@Table(name = "block")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Block implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "hash")
    private String hash;

    @Column(name = "previous_hash")
    private String previousHash;

    @Column(name = "merkle_root")
    private String merkleRoot;

    @Column(name = "timestamp")
    private Instant timestamp;

    @Column(name = "nonce")
    private Integer nonce = 0;

    @Column(name = "trading_volume")
    private Integer tradingVolume;

    @OneToMany(mappedBy = "block")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "block" }, allowSetters = true)
    private List<Transaction> transactions = new ArrayList<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Block id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getHash() {
        return this.hash;
    }

    public Block hash(String hash) {
        this.setHash(hash);
        return this;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getPreviousHash() {
        return this.previousHash;
    }

    public Block previousHash(String previousHash) {
        this.setPreviousHash(previousHash);
        return this;
    }

    public void setPreviousHash(String previousHash) {
        this.previousHash = previousHash;
    }

    public String getMerkleRoot() {
        return this.merkleRoot;
    }

    public Block merkleRoot(String merkleRoot) {
        this.setMerkleRoot(merkleRoot);
        return this;
    }

    public void setMerkleRoot(String merkleRoot) {
        this.merkleRoot = merkleRoot;
    }

    public Instant getTimestamp() {
        return this.timestamp;
    }

    public Block timestamp(Instant timestamp) {
        this.setTimestamp(timestamp);
        return this;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }

    public Integer getNonce() {
        return this.nonce;
    }

    public Block nonce(Integer nonce) {
        this.setNonce(nonce);
        return this;
    }

    public void setNonce(Integer nonce) {
        this.nonce = nonce;
    }

    public Integer getTradingVolume() {
        return this.tradingVolume;
    }

    public Block tradingVolume(Integer tradingVolume) {
        this.setTradingVolume(tradingVolume);
        return this;
    }

    public void setTradingVolume(Integer tradingVolume) {
        this.tradingVolume = tradingVolume;
    }

    public List<Transaction> getTransactions() {
        return this.transactions;
    }

    public void setTransactions(List<Transaction> transactions) {
        if (this.transactions != null) {
            this.transactions.forEach(i -> i.setBlock(null));
        }
        if (transactions != null) {
            transactions.forEach(i -> i.setBlock(this));
        }
        this.transactions = transactions;
    }

    public Block transactions(List<Transaction> transactions) {
        this.setTransactions(transactions);
        return this;
    }

    public Block addTransaction(Transaction transaction) {
        this.transactions.add(transaction);
        transaction.setBlock(this);
        return this;
    }

    public Block removeTransaction(Transaction transaction) {
        this.transactions.remove(transaction);
        transaction.setBlock(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Block)) {
            return false;
        }
        return id != null && id.equals(((Block) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Block{" +
            "id=" + getId() +
            ", hash='" + getHash() + "'" +
            ", previousHash='" + getPreviousHash() + "'" +
            ", merkleRoot='" + getMerkleRoot() + "'" +
            ", timestamp='" + getTimestamp() + "'" +
            ", nonce=" + getNonce() +
            ", tradingVolume=" + getTradingVolume() +
            "}";
    }
}
