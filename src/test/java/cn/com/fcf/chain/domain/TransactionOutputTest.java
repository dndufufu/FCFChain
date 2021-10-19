package cn.com.fcf.chain.domain;

import static org.assertj.core.api.Assertions.assertThat;

import cn.com.fcf.chain.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TransactionOutputTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TransactionOutput.class);
        TransactionOutput transactionOutput1 = new TransactionOutput();
        transactionOutput1.setId(1L);
        TransactionOutput transactionOutput2 = new TransactionOutput();
        transactionOutput2.setId(transactionOutput1.getId());
        assertThat(transactionOutput1).isEqualTo(transactionOutput2);
        transactionOutput2.setId(2L);
        assertThat(transactionOutput1).isNotEqualTo(transactionOutput2);
        transactionOutput1.setId(null);
        assertThat(transactionOutput1).isNotEqualTo(transactionOutput2);
    }
}
