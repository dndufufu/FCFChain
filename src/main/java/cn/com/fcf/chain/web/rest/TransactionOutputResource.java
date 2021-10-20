package cn.com.fcf.chain.web.rest;

import cn.com.fcf.chain.domain.TransactionOutput;
import cn.com.fcf.chain.repository.TransactionOutputRepository;
import cn.com.fcf.chain.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link cn.com.fcf.chain.domain.TransactionOutput}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class TransactionOutputResource {

    private final Logger log = LoggerFactory.getLogger(TransactionOutputResource.class);

    private static final String ENTITY_NAME = "transactionOutput";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TransactionOutputRepository transactionOutputRepository;

    public TransactionOutputResource(TransactionOutputRepository transactionOutputRepository) {
        this.transactionOutputRepository = transactionOutputRepository;
    }

    /**
     * {@code POST  /transaction-outputs} : Create a new transactionOutput.
     *
     * @param transactionOutput the transactionOutput to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new transactionOutput, or with status {@code 400 (Bad Request)} if the transactionOutput has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/transaction-outputs")
    public ResponseEntity<TransactionOutput> createTransactionOutput(@Valid @RequestBody TransactionOutput transactionOutput)
        throws URISyntaxException {
        log.debug("REST request to save TransactionOutput : {}", transactionOutput);
        if (transactionOutput.getId() != null) {
            throw new BadRequestAlertException("A new transactionOutput cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TransactionOutput result = transactionOutputRepository.save(transactionOutput);
        return ResponseEntity
            .created(new URI("/api/transaction-outputs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /transaction-outputs/:id} : Updates an existing transactionOutput.
     *
     * @param id the id of the transactionOutput to save.
     * @param transactionOutput the transactionOutput to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated transactionOutput,
     * or with status {@code 400 (Bad Request)} if the transactionOutput is not valid,
     * or with status {@code 500 (Internal Server Error)} if the transactionOutput couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/transaction-outputs/{id}")
    public ResponseEntity<TransactionOutput> updateTransactionOutput(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody TransactionOutput transactionOutput
    ) throws URISyntaxException {
        log.debug("REST request to update TransactionOutput : {}, {}", id, transactionOutput);
        if (transactionOutput.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, transactionOutput.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!transactionOutputRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        TransactionOutput result = transactionOutputRepository.save(transactionOutput);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, transactionOutput.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /transaction-outputs/:id} : Partial updates given fields of an existing transactionOutput, field will ignore if it is null
     *
     * @param id the id of the transactionOutput to save.
     * @param transactionOutput the transactionOutput to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated transactionOutput,
     * or with status {@code 400 (Bad Request)} if the transactionOutput is not valid,
     * or with status {@code 404 (Not Found)} if the transactionOutput is not found,
     * or with status {@code 500 (Internal Server Error)} if the transactionOutput couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/transaction-outputs/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TransactionOutput> partialUpdateTransactionOutput(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody TransactionOutput transactionOutput
    ) throws URISyntaxException {
        log.debug("REST request to partial update TransactionOutput partially : {}, {}", id, transactionOutput);
        if (transactionOutput.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, transactionOutput.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!transactionOutputRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TransactionOutput> result = transactionOutputRepository
            .findById(transactionOutput.getId())
            .map(
                existingTransactionOutput -> {
                    if (transactionOutput.getRecipient() != null) {
                        existingTransactionOutput.setRecipient(transactionOutput.getRecipient());
                    }
                    if (transactionOutput.getValue() != null) {
                        existingTransactionOutput.setValue(transactionOutput.getValue());
                    }
                    if (transactionOutput.getParentTransactionId() != null) {
                        existingTransactionOutput.setParentTransactionId(transactionOutput.getParentTransactionId());
                    }

                    return existingTransactionOutput;
                }
            )
            .map(transactionOutputRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, transactionOutput.getId().toString())
        );
    }

    /**
     * {@code GET  /transaction-outputs} : get all the transactionOutputs.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of transactionOutputs in body.
     */
    @GetMapping("/transaction-outputs")
    public ResponseEntity<List<TransactionOutput>> getAllTransactionOutputs(Pageable pageable) {
        log.debug("REST request to get a page of TransactionOutputs");
        Page<TransactionOutput> page = transactionOutputRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /transaction-outputs/:id} : get the "id" transactionOutput.
     *
     * @param id the id of the transactionOutput to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the transactionOutput, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/transaction-outputs/{id}")
    public ResponseEntity<TransactionOutput> getTransactionOutput(@PathVariable Long id) {
        log.debug("REST request to get TransactionOutput : {}", id);
        Optional<TransactionOutput> transactionOutput = transactionOutputRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(transactionOutput);
    }

    /**
     * {@code DELETE  /transaction-outputs/:id} : delete the "id" transactionOutput.
     *
     * @param id the id of the transactionOutput to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/transaction-outputs/{id}")
    public ResponseEntity<Void> deleteTransactionOutput(@PathVariable Long id) {
        log.debug("REST request to delete TransactionOutput : {}", id);
        transactionOutputRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
