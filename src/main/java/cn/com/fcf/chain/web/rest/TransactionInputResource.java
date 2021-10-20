package cn.com.fcf.chain.web.rest;

import cn.com.fcf.chain.domain.TransactionInput;
import cn.com.fcf.chain.repository.TransactionInputRepository;
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
 * REST controller for managing {@link cn.com.fcf.chain.domain.TransactionInput}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class TransactionInputResource {

    private final Logger log = LoggerFactory.getLogger(TransactionInputResource.class);

    private static final String ENTITY_NAME = "transactionInput";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TransactionInputRepository transactionInputRepository;

    public TransactionInputResource(TransactionInputRepository transactionInputRepository) {
        this.transactionInputRepository = transactionInputRepository;
    }

    /**
     * {@code POST  /transaction-inputs} : Create a new transactionInput.
     *
     * @param transactionInput the transactionInput to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new transactionInput, or with status {@code 400 (Bad Request)} if the transactionInput has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/transaction-inputs")
    public ResponseEntity<TransactionInput> createTransactionInput(@Valid @RequestBody TransactionInput transactionInput)
        throws URISyntaxException {
        log.debug("REST request to save TransactionInput : {}", transactionInput);
        if (transactionInput.getId() != null) {
            throw new BadRequestAlertException("A new transactionInput cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TransactionInput result = transactionInputRepository.save(transactionInput);
        return ResponseEntity
            .created(new URI("/api/transaction-inputs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /transaction-inputs/:id} : Updates an existing transactionInput.
     *
     * @param id the id of the transactionInput to save.
     * @param transactionInput the transactionInput to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated transactionInput,
     * or with status {@code 400 (Bad Request)} if the transactionInput is not valid,
     * or with status {@code 500 (Internal Server Error)} if the transactionInput couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/transaction-inputs/{id}")
    public ResponseEntity<TransactionInput> updateTransactionInput(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody TransactionInput transactionInput
    ) throws URISyntaxException {
        log.debug("REST request to update TransactionInput : {}, {}", id, transactionInput);
        if (transactionInput.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, transactionInput.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!transactionInputRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        TransactionInput result = transactionInputRepository.save(transactionInput);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, transactionInput.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /transaction-inputs/:id} : Partial updates given fields of an existing transactionInput, field will ignore if it is null
     *
     * @param id the id of the transactionInput to save.
     * @param transactionInput the transactionInput to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated transactionInput,
     * or with status {@code 400 (Bad Request)} if the transactionInput is not valid,
     * or with status {@code 404 (Not Found)} if the transactionInput is not found,
     * or with status {@code 500 (Internal Server Error)} if the transactionInput couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/transaction-inputs/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TransactionInput> partialUpdateTransactionInput(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody TransactionInput transactionInput
    ) throws URISyntaxException {
        log.debug("REST request to partial update TransactionInput partially : {}, {}", id, transactionInput);
        if (transactionInput.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, transactionInput.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!transactionInputRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TransactionInput> result = transactionInputRepository
            .findById(transactionInput.getId())
            .map(
                existingTransactionInput -> {
                    if (transactionInput.getTransactionOutputId() != null) {
                        existingTransactionInput.setTransactionOutputId(transactionInput.getTransactionOutputId());
                    }
                    if (transactionInput.getuTXO() != null) {
                        existingTransactionInput.setuTXO(transactionInput.getuTXO());
                    }

                    return existingTransactionInput;
                }
            )
            .map(transactionInputRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, transactionInput.getId().toString())
        );
    }

    /**
     * {@code GET  /transaction-inputs} : get all the transactionInputs.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of transactionInputs in body.
     */
    @GetMapping("/transaction-inputs")
    public ResponseEntity<List<TransactionInput>> getAllTransactionInputs(Pageable pageable) {
        log.debug("REST request to get a page of TransactionInputs");
        Page<TransactionInput> page = transactionInputRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /transaction-inputs/:id} : get the "id" transactionInput.
     *
     * @param id the id of the transactionInput to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the transactionInput, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/transaction-inputs/{id}")
    public ResponseEntity<TransactionInput> getTransactionInput(@PathVariable Long id) {
        log.debug("REST request to get TransactionInput : {}", id);
        Optional<TransactionInput> transactionInput = transactionInputRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(transactionInput);
    }

    /**
     * {@code DELETE  /transaction-inputs/:id} : delete the "id" transactionInput.
     *
     * @param id the id of the transactionInput to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/transaction-inputs/{id}")
    public ResponseEntity<Void> deleteTransactionInput(@PathVariable Long id) {
        log.debug("REST request to delete TransactionInput : {}", id);
        transactionInputRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
