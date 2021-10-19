package cn.com.fcf.chain.domain;

import java.io.Serializable;
import java.time.Instant;
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
    private Integer nonce;

    @Column(name = "trading_volume")
    private Integer tradingVolume;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Block id(Long id) {
        this.id = id;
        return this;
    }

    public String getHash() {
        return this.hash;
    }

    public Block hash(String hash) {
        this.hash = hash;
        return this;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getPreviousHash() {
        return this.previousHash;
    }

    public Block previousHash(String previousHash) {
        this.previousHash = previousHash;
        return this;
    }

    public void setPreviousHash(String previousHash) {
        this.previousHash = previousHash;
    }

    public String getMerkleRoot() {
        return this.merkleRoot;
    }

    public Block merkleRoot(String merkleRoot) {
        this.merkleRoot = merkleRoot;
        return this;
    }

    public void setMerkleRoot(String merkleRoot) {
        this.merkleRoot = merkleRoot;
    }

    public Instant getTimestamp() {
        return this.timestamp;
    }

    public Block timestamp(Instant timestamp) {
        this.timestamp = timestamp;
        return this;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }

    public Integer getNonce() {
        return this.nonce;
    }

    public Block nonce(Integer nonce) {
        this.nonce = nonce;
        return this;
    }

    public void setNonce(Integer nonce) {
        this.nonce = nonce;
    }

    public Integer getTradingVolume() {
        return this.tradingVolume;
    }

    public Block tradingVolume(Integer tradingVolume) {
        this.tradingVolume = tradingVolume;
        return this;
    }

    public void setTradingVolume(Integer tradingVolume) {
        this.tradingVolume = tradingVolume;
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
