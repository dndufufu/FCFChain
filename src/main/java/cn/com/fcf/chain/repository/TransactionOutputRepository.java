package cn.com.fcf.chain.repository;

import cn.com.fcf.chain.domain.TransactionOutput;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the TransactionOutput entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TransactionOutputRepository extends JpaRepository<TransactionOutput, Long> {}
