package cn.com.fcf.chain.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import cn.com.fcf.chain.IntegrationTest;
import cn.com.fcf.chain.domain.Block;
import cn.com.fcf.chain.repository.BlockRepository;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
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
 * Integration tests for the {@link BlockResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class BlockResourceIT {

    private static final String DEFAULT_HASH = "AAAAAAAAAA";
    private static final String UPDATED_HASH = "BBBBBBBBBB";

    private static final String DEFAULT_PREVIOUS_HASH = "AAAAAAAAAA";
    private static final String UPDATED_PREVIOUS_HASH = "BBBBBBBBBB";

    private static final String DEFAULT_MERKLE_ROOT = "AAAAAAAAAA";
    private static final String UPDATED_MERKLE_ROOT = "BBBBBBBBBB";

    private static final Instant DEFAULT_TIMESTAMP = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_TIMESTAMP = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Integer DEFAULT_NONCE = 1;
    private static final Integer UPDATED_NONCE = 2;

    private static final Integer DEFAULT_TRADING_VOLUME = 1;
    private static final Integer UPDATED_TRADING_VOLUME = 2;

    private static final String ENTITY_API_URL = "/api/blocks";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private BlockRepository blockRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restBlockMockMvc;

    private Block block;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Block createEntity(EntityManager em) {
        Block block = new Block()
            .hash(DEFAULT_HASH)
            .previousHash(DEFAULT_PREVIOUS_HASH)
            .merkleRoot(DEFAULT_MERKLE_ROOT)
            .timestamp(DEFAULT_TIMESTAMP)
            .nonce(DEFAULT_NONCE)
            .tradingVolume(DEFAULT_TRADING_VOLUME);
        return block;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Block createUpdatedEntity(EntityManager em) {
        Block block = new Block()
            .hash(UPDATED_HASH)
            .previousHash(UPDATED_PREVIOUS_HASH)
            .merkleRoot(UPDATED_MERKLE_ROOT)
            .timestamp(UPDATED_TIMESTAMP)
            .nonce(UPDATED_NONCE)
            .tradingVolume(UPDATED_TRADING_VOLUME);
        return block;
    }

    @BeforeEach
    public void initTest() {
        block = createEntity(em);
    }

    @Test
    @Transactional
    void createBlock() throws Exception {
        int databaseSizeBeforeCreate = blockRepository.findAll().size();
        // Create the Block
        restBlockMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(block)))
            .andExpect(status().isCreated());

        // Validate the Block in the database
        List<Block> blockList = blockRepository.findAll();
        assertThat(blockList).hasSize(databaseSizeBeforeCreate + 1);
        Block testBlock = blockList.get(blockList.size() - 1);
        assertThat(testBlock.getHash()).isEqualTo(DEFAULT_HASH);
        assertThat(testBlock.getPreviousHash()).isEqualTo(DEFAULT_PREVIOUS_HASH);
        assertThat(testBlock.getMerkleRoot()).isEqualTo(DEFAULT_MERKLE_ROOT);
        assertThat(testBlock.getTimestamp()).isEqualTo(DEFAULT_TIMESTAMP);
        assertThat(testBlock.getNonce()).isEqualTo(DEFAULT_NONCE);
        assertThat(testBlock.getTradingVolume()).isEqualTo(DEFAULT_TRADING_VOLUME);
    }

    @Test
    @Transactional
    void createBlockWithExistingId() throws Exception {
        // Create the Block with an existing ID
        block.setId(1L);

        int databaseSizeBeforeCreate = blockRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restBlockMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(block)))
            .andExpect(status().isBadRequest());

        // Validate the Block in the database
        List<Block> blockList = blockRepository.findAll();
        assertThat(blockList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllBlocks() throws Exception {
        // Initialize the database
        blockRepository.saveAndFlush(block);

        // Get all the blockList
        restBlockMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(block.getId().intValue())))
            .andExpect(jsonPath("$.[*].hash").value(hasItem(DEFAULT_HASH)))
            .andExpect(jsonPath("$.[*].previousHash").value(hasItem(DEFAULT_PREVIOUS_HASH)))
            .andExpect(jsonPath("$.[*].merkleRoot").value(hasItem(DEFAULT_MERKLE_ROOT)))
            .andExpect(jsonPath("$.[*].timestamp").value(hasItem(DEFAULT_TIMESTAMP.toString())))
            .andExpect(jsonPath("$.[*].nonce").value(hasItem(DEFAULT_NONCE)))
            .andExpect(jsonPath("$.[*].tradingVolume").value(hasItem(DEFAULT_TRADING_VOLUME)));
    }

    @Test
    @Transactional
    void getBlock() throws Exception {
        // Initialize the database
        blockRepository.saveAndFlush(block);

        // Get the block
        restBlockMockMvc
            .perform(get(ENTITY_API_URL_ID, block.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(block.getId().intValue()))
            .andExpect(jsonPath("$.hash").value(DEFAULT_HASH))
            .andExpect(jsonPath("$.previousHash").value(DEFAULT_PREVIOUS_HASH))
            .andExpect(jsonPath("$.merkleRoot").value(DEFAULT_MERKLE_ROOT))
            .andExpect(jsonPath("$.timestamp").value(DEFAULT_TIMESTAMP.toString()))
            .andExpect(jsonPath("$.nonce").value(DEFAULT_NONCE))
            .andExpect(jsonPath("$.tradingVolume").value(DEFAULT_TRADING_VOLUME));
    }

    @Test
    @Transactional
    void getNonExistingBlock() throws Exception {
        // Get the block
        restBlockMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewBlock() throws Exception {
        // Initialize the database
        blockRepository.saveAndFlush(block);

        int databaseSizeBeforeUpdate = blockRepository.findAll().size();

        // Update the block
        Block updatedBlock = blockRepository.findById(block.getId()).get();
        // Disconnect from session so that the updates on updatedBlock are not directly saved in db
        em.detach(updatedBlock);
        updatedBlock
            .hash(UPDATED_HASH)
            .previousHash(UPDATED_PREVIOUS_HASH)
            .merkleRoot(UPDATED_MERKLE_ROOT)
            .timestamp(UPDATED_TIMESTAMP)
            .nonce(UPDATED_NONCE)
            .tradingVolume(UPDATED_TRADING_VOLUME);

        restBlockMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedBlock.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedBlock))
            )
            .andExpect(status().isOk());

        // Validate the Block in the database
        List<Block> blockList = blockRepository.findAll();
        assertThat(blockList).hasSize(databaseSizeBeforeUpdate);
        Block testBlock = blockList.get(blockList.size() - 1);
        assertThat(testBlock.getHash()).isEqualTo(UPDATED_HASH);
        assertThat(testBlock.getPreviousHash()).isEqualTo(UPDATED_PREVIOUS_HASH);
        assertThat(testBlock.getMerkleRoot()).isEqualTo(UPDATED_MERKLE_ROOT);
        assertThat(testBlock.getTimestamp()).isEqualTo(UPDATED_TIMESTAMP);
        assertThat(testBlock.getNonce()).isEqualTo(UPDATED_NONCE);
        assertThat(testBlock.getTradingVolume()).isEqualTo(UPDATED_TRADING_VOLUME);
    }

    @Test
    @Transactional
    void putNonExistingBlock() throws Exception {
        int databaseSizeBeforeUpdate = blockRepository.findAll().size();
        block.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBlockMockMvc
            .perform(
                put(ENTITY_API_URL_ID, block.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(block))
            )
            .andExpect(status().isBadRequest());

        // Validate the Block in the database
        List<Block> blockList = blockRepository.findAll();
        assertThat(blockList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchBlock() throws Exception {
        int databaseSizeBeforeUpdate = blockRepository.findAll().size();
        block.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBlockMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(block))
            )
            .andExpect(status().isBadRequest());

        // Validate the Block in the database
        List<Block> blockList = blockRepository.findAll();
        assertThat(blockList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamBlock() throws Exception {
        int databaseSizeBeforeUpdate = blockRepository.findAll().size();
        block.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBlockMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(block)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Block in the database
        List<Block> blockList = blockRepository.findAll();
        assertThat(blockList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateBlockWithPatch() throws Exception {
        // Initialize the database
        blockRepository.saveAndFlush(block);

        int databaseSizeBeforeUpdate = blockRepository.findAll().size();

        // Update the block using partial update
        Block partialUpdatedBlock = new Block();
        partialUpdatedBlock.setId(block.getId());

        partialUpdatedBlock.hash(UPDATED_HASH).merkleRoot(UPDATED_MERKLE_ROOT).tradingVolume(UPDATED_TRADING_VOLUME);

        restBlockMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBlock.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedBlock))
            )
            .andExpect(status().isOk());

        // Validate the Block in the database
        List<Block> blockList = blockRepository.findAll();
        assertThat(blockList).hasSize(databaseSizeBeforeUpdate);
        Block testBlock = blockList.get(blockList.size() - 1);
        assertThat(testBlock.getHash()).isEqualTo(UPDATED_HASH);
        assertThat(testBlock.getPreviousHash()).isEqualTo(DEFAULT_PREVIOUS_HASH);
        assertThat(testBlock.getMerkleRoot()).isEqualTo(UPDATED_MERKLE_ROOT);
        assertThat(testBlock.getTimestamp()).isEqualTo(DEFAULT_TIMESTAMP);
        assertThat(testBlock.getNonce()).isEqualTo(DEFAULT_NONCE);
        assertThat(testBlock.getTradingVolume()).isEqualTo(UPDATED_TRADING_VOLUME);
    }

    @Test
    @Transactional
    void fullUpdateBlockWithPatch() throws Exception {
        // Initialize the database
        blockRepository.saveAndFlush(block);

        int databaseSizeBeforeUpdate = blockRepository.findAll().size();

        // Update the block using partial update
        Block partialUpdatedBlock = new Block();
        partialUpdatedBlock.setId(block.getId());

        partialUpdatedBlock
            .hash(UPDATED_HASH)
            .previousHash(UPDATED_PREVIOUS_HASH)
            .merkleRoot(UPDATED_MERKLE_ROOT)
            .timestamp(UPDATED_TIMESTAMP)
            .nonce(UPDATED_NONCE)
            .tradingVolume(UPDATED_TRADING_VOLUME);

        restBlockMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBlock.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedBlock))
            )
            .andExpect(status().isOk());

        // Validate the Block in the database
        List<Block> blockList = blockRepository.findAll();
        assertThat(blockList).hasSize(databaseSizeBeforeUpdate);
        Block testBlock = blockList.get(blockList.size() - 1);
        assertThat(testBlock.getHash()).isEqualTo(UPDATED_HASH);
        assertThat(testBlock.getPreviousHash()).isEqualTo(UPDATED_PREVIOUS_HASH);
        assertThat(testBlock.getMerkleRoot()).isEqualTo(UPDATED_MERKLE_ROOT);
        assertThat(testBlock.getTimestamp()).isEqualTo(UPDATED_TIMESTAMP);
        assertThat(testBlock.getNonce()).isEqualTo(UPDATED_NONCE);
        assertThat(testBlock.getTradingVolume()).isEqualTo(UPDATED_TRADING_VOLUME);
    }

    @Test
    @Transactional
    void patchNonExistingBlock() throws Exception {
        int databaseSizeBeforeUpdate = blockRepository.findAll().size();
        block.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBlockMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, block.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(block))
            )
            .andExpect(status().isBadRequest());

        // Validate the Block in the database
        List<Block> blockList = blockRepository.findAll();
        assertThat(blockList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchBlock() throws Exception {
        int databaseSizeBeforeUpdate = blockRepository.findAll().size();
        block.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBlockMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(block))
            )
            .andExpect(status().isBadRequest());

        // Validate the Block in the database
        List<Block> blockList = blockRepository.findAll();
        assertThat(blockList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamBlock() throws Exception {
        int databaseSizeBeforeUpdate = blockRepository.findAll().size();
        block.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBlockMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(block)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Block in the database
        List<Block> blockList = blockRepository.findAll();
        assertThat(blockList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteBlock() throws Exception {
        // Initialize the database
        blockRepository.saveAndFlush(block);

        int databaseSizeBeforeDelete = blockRepository.findAll().size();

        // Delete the block
        restBlockMockMvc
            .perform(delete(ENTITY_API_URL_ID, block.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Block> blockList = blockRepository.findAll();
        assertThat(blockList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
