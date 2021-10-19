package cn.com.fcf.chain.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import cn.com.fcf.chain.IntegrationTest;
import cn.com.fcf.chain.domain.TransactionInput;
import cn.com.fcf.chain.repository.TransactionInputRepository;
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
 * Integration tests for the {@link TransactionInputResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TransactionInputResourceIT {

    private static final String DEFAULT_TRANSACTION_OUTPUT_ID = "AAAAAAAAAA";
    private static final String UPDATED_TRANSACTION_OUTPUT_ID = "BBBBBBBBBB";

    private static final String DEFAULT_U_TXO = "AAAAAAAAAA";
    private static final String UPDATED_U_TXO = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/transaction-inputs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TransactionInputRepository transactionInputRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTransactionInputMockMvc;

    private TransactionInput transactionInput;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TransactionInput createEntity(EntityManager em) {
        TransactionInput transactionInput = new TransactionInput().transactionOutputId(DEFAULT_TRANSACTION_OUTPUT_ID).uTXO(DEFAULT_U_TXO);
        return transactionInput;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TransactionInput createUpdatedEntity(EntityManager em) {
        TransactionInput transactionInput = new TransactionInput().transactionOutputId(UPDATED_TRANSACTION_OUTPUT_ID).uTXO(UPDATED_U_TXO);
        return transactionInput;
    }

    @BeforeEach
    public void initTest() {
        transactionInput = createEntity(em);
    }

    @Test
    @Transactional
    void createTransactionInput() throws Exception {
        int databaseSizeBeforeCreate = transactionInputRepository.findAll().size();
        // Create the TransactionInput
        restTransactionInputMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(transactionInput))
            )
            .andExpect(status().isCreated());

        // Validate the TransactionInput in the database
        List<TransactionInput> transactionInputList = transactionInputRepository.findAll();
        assertThat(transactionInputList).hasSize(databaseSizeBeforeCreate + 1);
        TransactionInput testTransactionInput = transactionInputList.get(transactionInputList.size() - 1);
        assertThat(testTransactionInput.getTransactionOutputId()).isEqualTo(DEFAULT_TRANSACTION_OUTPUT_ID);
        assertThat(testTransactionInput.getuTXO()).isEqualTo(DEFAULT_U_TXO);
    }

    @Test
    @Transactional
    void createTransactionInputWithExistingId() throws Exception {
        // Create the TransactionInput with an existing ID
        transactionInput.setId(1L);

        int databaseSizeBeforeCreate = transactionInputRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTransactionInputMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(transactionInput))
            )
            .andExpect(status().isBadRequest());

        // Validate the TransactionInput in the database
        List<TransactionInput> transactionInputList = transactionInputRepository.findAll();
        assertThat(transactionInputList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllTransactionInputs() throws Exception {
        // Initialize the database
        transactionInputRepository.saveAndFlush(transactionInput);

        // Get all the transactionInputList
        restTransactionInputMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(transactionInput.getId().intValue())))
            .andExpect(jsonPath("$.[*].transactionOutputId").value(hasItem(DEFAULT_TRANSACTION_OUTPUT_ID)))
            .andExpect(jsonPath("$.[*].uTXO").value(hasItem(DEFAULT_U_TXO)));
    }

    @Test
    @Transactional
    void getTransactionInput() throws Exception {
        // Initialize the database
        transactionInputRepository.saveAndFlush(transactionInput);

        // Get the transactionInput
        restTransactionInputMockMvc
            .perform(get(ENTITY_API_URL_ID, transactionInput.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(transactionInput.getId().intValue()))
            .andExpect(jsonPath("$.transactionOutputId").value(DEFAULT_TRANSACTION_OUTPUT_ID))
            .andExpect(jsonPath("$.uTXO").value(DEFAULT_U_TXO));
    }

    @Test
    @Transactional
    void getNonExistingTransactionInput() throws Exception {
        // Get the transactionInput
        restTransactionInputMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewTransactionInput() throws Exception {
        // Initialize the database
        transactionInputRepository.saveAndFlush(transactionInput);

        int databaseSizeBeforeUpdate = transactionInputRepository.findAll().size();

        // Update the transactionInput
        TransactionInput updatedTransactionInput = transactionInputRepository.findById(transactionInput.getId()).get();
        // Disconnect from session so that the updates on updatedTransactionInput are not directly saved in db
        em.detach(updatedTransactionInput);
        updatedTransactionInput.transactionOutputId(UPDATED_TRANSACTION_OUTPUT_ID).uTXO(UPDATED_U_TXO);

        restTransactionInputMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedTransactionInput.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedTransactionInput))
            )
            .andExpect(status().isOk());

        // Validate the TransactionInput in the database
        List<TransactionInput> transactionInputList = transactionInputRepository.findAll();
        assertThat(transactionInputList).hasSize(databaseSizeBeforeUpdate);
        TransactionInput testTransactionInput = transactionInputList.get(transactionInputList.size() - 1);
        assertThat(testTransactionInput.getTransactionOutputId()).isEqualTo(UPDATED_TRANSACTION_OUTPUT_ID);
        assertThat(testTransactionInput.getuTXO()).isEqualTo(UPDATED_U_TXO);
    }

    @Test
    @Transactional
    void putNonExistingTransactionInput() throws Exception {
        int databaseSizeBeforeUpdate = transactionInputRepository.findAll().size();
        transactionInput.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTransactionInputMockMvc
            .perform(
                put(ENTITY_API_URL_ID, transactionInput.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(transactionInput))
            )
            .andExpect(status().isBadRequest());

        // Validate the TransactionInput in the database
        List<TransactionInput> transactionInputList = transactionInputRepository.findAll();
        assertThat(transactionInputList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTransactionInput() throws Exception {
        int databaseSizeBeforeUpdate = transactionInputRepository.findAll().size();
        transactionInput.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTransactionInputMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(transactionInput))
            )
            .andExpect(status().isBadRequest());

        // Validate the TransactionInput in the database
        List<TransactionInput> transactionInputList = transactionInputRepository.findAll();
        assertThat(transactionInputList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTransactionInput() throws Exception {
        int databaseSizeBeforeUpdate = transactionInputRepository.findAll().size();
        transactionInput.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTransactionInputMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(transactionInput))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TransactionInput in the database
        List<TransactionInput> transactionInputList = transactionInputRepository.findAll();
        assertThat(transactionInputList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTransactionInputWithPatch() throws Exception {
        // Initialize the database
        transactionInputRepository.saveAndFlush(transactionInput);

        int databaseSizeBeforeUpdate = transactionInputRepository.findAll().size();

        // Update the transactionInput using partial update
        TransactionInput partialUpdatedTransactionInput = new TransactionInput();
        partialUpdatedTransactionInput.setId(transactionInput.getId());

        partialUpdatedTransactionInput.uTXO(UPDATED_U_TXO);

        restTransactionInputMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTransactionInput.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTransactionInput))
            )
            .andExpect(status().isOk());

        // Validate the TransactionInput in the database
        List<TransactionInput> transactionInputList = transactionInputRepository.findAll();
        assertThat(transactionInputList).hasSize(databaseSizeBeforeUpdate);
        TransactionInput testTransactionInput = transactionInputList.get(transactionInputList.size() - 1);
        assertThat(testTransactionInput.getTransactionOutputId()).isEqualTo(DEFAULT_TRANSACTION_OUTPUT_ID);
        assertThat(testTransactionInput.getuTXO()).isEqualTo(UPDATED_U_TXO);
    }

    @Test
    @Transactional
    void fullUpdateTransactionInputWithPatch() throws Exception {
        // Initialize the database
        transactionInputRepository.saveAndFlush(transactionInput);

        int databaseSizeBeforeUpdate = transactionInputRepository.findAll().size();

        // Update the transactionInput using partial update
        TransactionInput partialUpdatedTransactionInput = new TransactionInput();
        partialUpdatedTransactionInput.setId(transactionInput.getId());

        partialUpdatedTransactionInput.transactionOutputId(UPDATED_TRANSACTION_OUTPUT_ID).uTXO(UPDATED_U_TXO);

        restTransactionInputMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTransactionInput.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTransactionInput))
            )
            .andExpect(status().isOk());

        // Validate the TransactionInput in the database
        List<TransactionInput> transactionInputList = transactionInputRepository.findAll();
        assertThat(transactionInputList).hasSize(databaseSizeBeforeUpdate);
        TransactionInput testTransactionInput = transactionInputList.get(transactionInputList.size() - 1);
        assertThat(testTransactionInput.getTransactionOutputId()).isEqualTo(UPDATED_TRANSACTION_OUTPUT_ID);
        assertThat(testTransactionInput.getuTXO()).isEqualTo(UPDATED_U_TXO);
    }

    @Test
    @Transactional
    void patchNonExistingTransactionInput() throws Exception {
        int databaseSizeBeforeUpdate = transactionInputRepository.findAll().size();
        transactionInput.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTransactionInputMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, transactionInput.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(transactionInput))
            )
            .andExpect(status().isBadRequest());

        // Validate the TransactionInput in the database
        List<TransactionInput> transactionInputList = transactionInputRepository.findAll();
        assertThat(transactionInputList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTransactionInput() throws Exception {
        int databaseSizeBeforeUpdate = transactionInputRepository.findAll().size();
        transactionInput.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTransactionInputMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(transactionInput))
            )
            .andExpect(status().isBadRequest());

        // Validate the TransactionInput in the database
        List<TransactionInput> transactionInputList = transactionInputRepository.findAll();
        assertThat(transactionInputList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTransactionInput() throws Exception {
        int databaseSizeBeforeUpdate = transactionInputRepository.findAll().size();
        transactionInput.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTransactionInputMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(transactionInput))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TransactionInput in the database
        List<TransactionInput> transactionInputList = transactionInputRepository.findAll();
        assertThat(transactionInputList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTransactionInput() throws Exception {
        // Initialize the database
        transactionInputRepository.saveAndFlush(transactionInput);

        int databaseSizeBeforeDelete = transactionInputRepository.findAll().size();

        // Delete the transactionInput
        restTransactionInputMockMvc
            .perform(delete(ENTITY_API_URL_ID, transactionInput.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<TransactionInput> transactionInputList = transactionInputRepository.findAll();
        assertThat(transactionInputList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
