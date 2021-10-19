package cn.com.fcf.chain.repository;

import cn.com.fcf.chain.domain.TransactionInput;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the TransactionInput entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TransactionInputRepository extends JpaRepository<TransactionInput, Long> {}
