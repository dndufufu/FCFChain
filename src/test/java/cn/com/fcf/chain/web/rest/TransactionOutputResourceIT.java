package cn.com.fcf.chain.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import cn.com.fcf.chain.IntegrationTest;
import cn.com.fcf.chain.domain.TransactionOutput;
import cn.com.fcf.chain.repository.TransactionOutputRepository;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link TransactionOutputResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TransactionOutputResourceIT {

    private static final String DEFAULT_RECIPIENT = "AAAAAAAAAA";
    private static final String UPDATED_RECIPIENT = "BBBBBBBBBB";

    private static final Double DEFAULT_VALUE = 1D;
    private static final Double UPDATED_VALUE = 2D;

    private static final String DEFAULT_PARENT_TRANSACTION_ID = "AAAAAAAAAA";
    private static final String UPDATED_PARENT_TRANSACTION_ID = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/transaction-outputs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TransactionOutputRepository transactionOutputRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTransactionOutputMockMvc;

    private TransactionOutput transactionOutput;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TransactionOutput createEntity(EntityManager em) {
        TransactionOutput transactionOutput = new TransactionOutput()
            .recipient(DEFAULT_RECIPIENT)
            .value(DEFAULT_VALUE)
            .parentTransactionId(DEFAULT_PARENT_TRANSACTION_ID);
        return transactionOutput;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TransactionOutput createUpdatedEntity(EntityManager em) {
        TransactionOutput transactionOutput = new TransactionOutput()
            .recipient(UPDATED_RECIPIENT)
            .value(UPDATED_VALUE)
            .parentTransactionId(UPDATED_PARENT_TRANSACTION_ID);
        return transactionOutput;
    }

    @BeforeEach
    public void initTest() {
        transactionOutput = createEntity(em);
    }

    @Test
    @Transactional
    void createTransactionOutput() throws Exception {
        int databaseSizeBeforeCreate = transactionOutputRepository.findAll().size();
        // Create the TransactionOutput
        restTransactionOutputMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(transactionOutput))
            )
            .andExpect(status().isCreated());

        // Validate the TransactionOutput in the database
        List<TransactionOutput> transactionOutputList = transactionOutputRepository.findAll();
        assertThat(transactionOutputList).hasSize(databaseSizeBeforeCreate + 1);
        TransactionOutput testTransactionOutput = transactionOutputList.get(transactionOutputList.size() - 1);
        assertThat(testTransactionOutput.getRecipient()).isEqualTo(DEFAULT_RECIPIENT);
        assertThat(testTransactionOutput.getValue()).isEqualTo(DEFAULT_VALUE);
        assertThat(testTransactionOutput.getParentTransactionId()).isEqualTo(DEFAULT_PARENT_TRANSACTION_ID);
    }

    @Test
    @Transactional
    void createTransactionOutputWithExistingId() throws Exception {
        // Create the TransactionOutput with an existing ID
        transactionOutput.setId(1L);

        int databaseSizeBeforeCreate = transactionOutputRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTransactionOutputMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(transactionOutput))
            )
            .andExpect(status().isBadRequest());

        // Validate the TransactionOutput in the database
        List<TransactionOutput> transactionOutputList = transactionOutputRepository.findAll();
        assertThat(transactionOutputList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllTransactionOutputs() throws Exception {
        // Initialize the database
        transactionOutputRepository.saveAndFlush(transactionOutput);

        // Get all the transactionOutputList
        restTransactionOutputMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(transactionOutput.getId().intValue())))
            .andExpect(jsonPath("$.[*].recipient").value(hasItem(DEFAULT_RECIPIENT)))
            .andExpect(jsonPath("$.[*].value").value(hasItem(DEFAULT_VALUE.doubleValue())))
            .andExpect(jsonPath("$.[*].parentTransactionId").value(hasItem(DEFAULT_PARENT_TRANSACTION_ID)));
    }

    @Test
    @Transactional
    void getTransactionOutput() throws Exception {
        // Initialize the database
        transactionOutputRepository.saveAndFlush(transactionOutput);

        // Get the transactionOutput
        restTransactionOutputMockMvc
            .perform(get(ENTITY_API_URL_ID, transactionOutput.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(transactionOutput.getId().intValue()))
            .andExpect(jsonPath("$.recipient").value(DEFAULT_RECIPIENT))
            .andExpect(jsonPath("$.value").value(DEFAULT_VALUE.doubleValue()))
            .andExpect(jsonPath("$.parentTransactionId").value(DEFAULT_PARENT_TRANSACTION_ID));
    }

    @Test
    @Transactional
    void getNonExistingTransactionOutput() throws Exception {
        // Get the transactionOutput
        restTransactionOutputMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewTransactionOutput() throws Exception {
        // Initialize the database
        transactionOutputRepository.saveAndFlush(transactionOutput);

        int databaseSizeBeforeUpdate = transactionOutputRepository.findAll().size();

        // Update the transactionOutput
        TransactionOutput updatedTransactionOutput = transactionOutputRepository.findById(transactionOutput.getId()).get();
        // Disconnect from session so that the updates on updatedTransactionOutput are not directly saved in db
        em.detach(updatedTransactionOutput);
        updatedTransactionOutput.recipient(UPDATED_RECIPIENT).value(UPDATED_VALUE).parentTransactionId(UPDATED_PARENT_TRANSACTION_ID);

        restTransactionOutputMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedTransactionOutput.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedTransactionOutput))
            )
            .andExpect(status().isOk());

        // Validate the TransactionOutput in the database
        List<TransactionOutput> transactionOutputList = transactionOutputRepository.findAll();
        assertThat(transactionOutputList).hasSize(databaseSizeBeforeUpdate);
        TransactionOutput testTransactionOutput = transactionOutputList.get(transactionOutputList.size() - 1);
        assertThat(testTransactionOutput.getRecipient()).isEqualTo(UPDATED_RECIPIENT);
        assertThat(testTransactionOutput.getValue()).isEqualTo(UPDATED_VALUE);
        assertThat(testTransactionOutput.getParentTransactionId()).isEqualTo(UPDATED_PARENT_TRANSACTION_ID);
    }

    @Test
    @Transactional
    void putNonExistingTransactionOutput() throws Exception {
        int databaseSizeBeforeUpdate = transactionOutputRepository.findAll().size();
        transactionOutput.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTransactionOutputMockMvc
            .perform(
                put(ENTITY_API_URL_ID, transactionOutput.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(transactionOutput))
            )
            .andExpect(status().isBadRequest());

        // Validate the TransactionOutput in the database
        List<TransactionOutput> transactionOutputList = transactionOutputRepository.findAll();
        assertThat(transactionOutputList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTransactionOutput() throws Exception {
        int databaseSizeBeforeUpdate = transactionOutputRepository.findAll().size();
        transactionOutput.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTransactionOutputMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(transactionOutput))
            )
            .andExpect(status().isBadRequest());

        // Validate the TransactionOutput in the database
        List<TransactionOutput> transactionOutputList = transactionOutputRepository.findAll();
        assertThat(transactionOutputList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTransactionOutput() throws Exception {
        int databaseSizeBeforeUpdate = transactionOutputRepository.findAll().size();
        transactionOutput.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTransactionOutputMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(transactionOutput))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TransactionOutput in the database
        List<TransactionOutput> transactionOutputList = transactionOutputRepository.findAll();
        assertThat(transactionOutputList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTransactionOutputWithPatch() throws Exception {
        // Initialize the database
        transactionOutputRepository.saveAndFlush(transactionOutput);

        int databaseSizeBeforeUpdate = transactionOutputRepository.findAll().size();

        // Update the transactionOutput using partial update
        TransactionOutput partialUpdatedTransactionOutput = new TransactionOutput();
        partialUpdatedTransactionOutput.setId(transactionOutput.getId());

        partialUpdatedTransactionOutput.value(UPDATED_VALUE);

        restTransactionOutputMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTransactionOutput.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTransactionOutput))
            )
            .andExpect(status().isOk());

        // Validate the TransactionOutput in the database
        List<TransactionOutput> transactionOutputList = transactionOutputRepository.findAll();
        assertThat(transactionOutputList).hasSize(databaseSizeBeforeUpdate);
        TransactionOutput testTransactionOutput = transactionOutputList.get(transactionOutputList.size() - 1);
        assertThat(testTransactionOutput.getRecipient()).isEqualTo(DEFAULT_RECIPIENT);
        assertThat(testTransactionOutput.getValue()).isEqualTo(UPDATED_VALUE);
        assertThat(testTransactionOutput.getParentTransactionId()).isEqualTo(DEFAULT_PARENT_TRANSACTION_ID);
    }

    @Test
    @Transactional
    void fullUpdateTransactionOutputWithPatch() throws Exception {
        // Initialize the database
        transactionOutputRepository.saveAndFlush(transactionOutput);

        int databaseSizeBeforeUpdate = transactionOutputRepository.findAll().size();

        // Update the transactionOutput using partial update
        TransactionOutput partialUpdatedTransactionOutput = new TransactionOutput();
        partialUpdatedTransactionOutput.setId(transactionOutput.getId());

        partialUpdatedTransactionOutput
            .recipient(UPDATED_RECIPIENT)
            .value(UPDATED_VALUE)
            .parentTransactionId(UPDATED_PARENT_TRANSACTION_ID);

        restTransactionOutputMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTransactionOutput.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTransactionOutput))
            )
            .andExpect(status().isOk());

        // Validate the TransactionOutput in the database
        List<TransactionOutput> transactionOutputList = transactionOutputRepository.findAll();
        assertThat(transactionOutputList).hasSize(databaseSizeBeforeUpdate);
        TransactionOutput testTransactionOutput = transactionOutputList.get(transactionOutputList.size() - 1);
        assertThat(testTransactionOutput.getRecipient()).isEqualTo(UPDATED_RECIPIENT);
        assertThat(testTransactionOutput.getValue()).isEqualTo(UPDATED_VALUE);
        assertThat(testTransactionOutput.getParentTransactionId()).isEqualTo(UPDATED_PARENT_TRANSACTION_ID);
    }

    @Test
    @Transactional
    void patchNonExistingTransactionOutput() throws Exception {
        int databaseSizeBeforeUpdate = transactionOutputRepository.findAll().size();
        transactionOutput.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTransactionOutputMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, transactionOutput.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(transactionOutput))
            )
            .andExpect(status().isBadRequest());

        // Validate the TransactionOutput in the database
        List<TransactionOutput> transactionOutputList = transactionOutputRepository.findAll();
        assertThat(transactionOutputList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTransactionOutput() throws Exception {
        int databaseSizeBeforeUpdate = transactionOutputRepository.findAll().size();
        transactionOutput.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTransactionOutputMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(transactionOutput))
            )
            .andExpect(status().isBadRequest());

        // Validate the TransactionOutput in the database
        List<TransactionOutput> transactionOutputList = transactionOutputRepository.findAll();
        assertThat(transactionOutputList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTransactionOutput() throws Exception {
        int databaseSizeBeforeUpdate = transactionOutputRepository.findAll().size();
        transactionOutput.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTransactionOutputMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(transactionOutput))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TransactionOutput in the database
        List<TransactionOutput> transactionOutputList = transactionOutputRepository.findAll();
        assertThat(transactionOutputList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTransactionOutput() throws Exception {
        // Initialize the database
        transactionOutputRepository.saveAndFlush(transactionOutput);

        int databaseSizeBeforeDelete = transactionOutputRepository.findAll().size();

        // Delete the transactionOutput
        restTransactionOutputMockMvc
            .perform(delete(ENTITY_API_URL_ID, transactionOutput.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<TransactionOutput> transactionOutputList = transactionOutputRepository.findAll();
        assertThat(transactionOutputList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
