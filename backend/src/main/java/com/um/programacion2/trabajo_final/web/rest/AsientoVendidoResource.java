package com.um.programacion2.trabajo_final.web.rest;

import com.um.programacion2.trabajo_final.domain.AsientoVendido;
import com.um.programacion2.trabajo_final.repository.AsientoVendidoRepository;
import com.um.programacion2.trabajo_final.service.AsientoVendidoService;
import com.um.programacion2.trabajo_final.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.um.programacion2.trabajo_final.domain.AsientoVendido}.
 */
@RestController
@RequestMapping("/api/asiento-vendidos")
public class AsientoVendidoResource {

    private static final Logger LOG = LoggerFactory.getLogger(AsientoVendidoResource.class);

    private static final String ENTITY_NAME = "asientoVendido";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AsientoVendidoService asientoVendidoService;

    private final AsientoVendidoRepository asientoVendidoRepository;

    public AsientoVendidoResource(AsientoVendidoService asientoVendidoService, AsientoVendidoRepository asientoVendidoRepository) {
        this.asientoVendidoService = asientoVendidoService;
        this.asientoVendidoRepository = asientoVendidoRepository;
    }

    /**
     * {@code POST  /asiento-vendidos} : Create a new asientoVendido.
     *
     * @param asientoVendido the asientoVendido to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new asientoVendido, or with status {@code 400 (Bad Request)} if the asientoVendido has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<AsientoVendido> createAsientoVendido(@Valid @RequestBody AsientoVendido asientoVendido)
        throws URISyntaxException {
        LOG.debug("REST request to save AsientoVendido : {}", asientoVendido);
        if (asientoVendido.getId() != null) {
            throw new BadRequestAlertException("A new asientoVendido cannot already have an ID", ENTITY_NAME, "idexists");
        }
        asientoVendido = asientoVendidoService.save(asientoVendido);
        return ResponseEntity.created(new URI("/api/asiento-vendidos/" + asientoVendido.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, asientoVendido.getId().toString()))
            .body(asientoVendido);
    }

    /**
     * {@code PUT  /asiento-vendidos/:id} : Updates an existing asientoVendido.
     *
     * @param id the id of the asientoVendido to save.
     * @param asientoVendido the asientoVendido to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated asientoVendido,
     * or with status {@code 400 (Bad Request)} if the asientoVendido is not valid,
     * or with status {@code 500 (Internal Server Error)} if the asientoVendido couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<AsientoVendido> updateAsientoVendido(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody AsientoVendido asientoVendido
    ) throws URISyntaxException {
        LOG.debug("REST request to update AsientoVendido : {}, {}", id, asientoVendido);
        if (asientoVendido.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, asientoVendido.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!asientoVendidoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        asientoVendido = asientoVendidoService.update(asientoVendido);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, asientoVendido.getId().toString()))
            .body(asientoVendido);
    }

    /**
     * {@code PATCH  /asiento-vendidos/:id} : Partial updates given fields of an existing asientoVendido, field will ignore if it is null
     *
     * @param id the id of the asientoVendido to save.
     * @param asientoVendido the asientoVendido to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated asientoVendido,
     * or with status {@code 400 (Bad Request)} if the asientoVendido is not valid,
     * or with status {@code 404 (Not Found)} if the asientoVendido is not found,
     * or with status {@code 500 (Internal Server Error)} if the asientoVendido couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<AsientoVendido> partialUpdateAsientoVendido(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody AsientoVendido asientoVendido
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update AsientoVendido partially : {}, {}", id, asientoVendido);
        if (asientoVendido.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, asientoVendido.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!asientoVendidoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AsientoVendido> result = asientoVendidoService.partialUpdate(asientoVendido);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, asientoVendido.getId().toString())
        );
    }

    /**
     * {@code GET  /asiento-vendidos} : get all the asientoVendidos.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of asientoVendidos in body.
     */
    @GetMapping("")
    public List<AsientoVendido> getAllAsientoVendidos() {
        LOG.debug("REST request to get all AsientoVendidos");
        return asientoVendidoService.findAll();
    }

    /**
     * {@code GET  /asiento-vendidos/:id} : get the "id" asientoVendido.
     *
     * @param id the id of the asientoVendido to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the asientoVendido, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<AsientoVendido> getAsientoVendido(@PathVariable("id") Long id) {
        LOG.debug("REST request to get AsientoVendido : {}", id);
        Optional<AsientoVendido> asientoVendido = asientoVendidoService.findOne(id);
        return ResponseUtil.wrapOrNotFound(asientoVendido);
    }

    /**
     * {@code DELETE  /asiento-vendidos/:id} : delete the "id" asientoVendido.
     *
     * @param id the id of the asientoVendido to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAsientoVendido(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete AsientoVendido : {}", id);
        asientoVendidoService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
