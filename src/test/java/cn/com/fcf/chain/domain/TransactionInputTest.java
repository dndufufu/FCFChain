package cn.com.fcf.chain.domain;

import static org.assertj.core.api.Assertions.assertThat;

import cn.com.fcf.chain.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TransactionInputTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TransactionInput.class);
        TransactionInput transactionInput1 = new TransactionInput();
        transactionInput1.setId(1L);
        TransactionInput transactionInput2 = new TransactionInput();
        transactionInput2.setId(transactionInput1.getId());
        assertThat(transactionInput1).isEqualTo(transactionInput2);
        transactionInput2.setId(2L);
        assertThat(transactionInput1).isNotEqualTo(transactionInput2);
        transactionInput1.setId(null);
        assertThat(transactionInput1).isNotEqualTo(transactionInput2);
    }
}
