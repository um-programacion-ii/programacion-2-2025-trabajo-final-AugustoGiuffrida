package com.um.programacion2.trabajo_final.service;

import com.um.programacion2.trabajo_final.domain.AsientoVendido;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.um.programacion2.trabajo_final.domain.AsientoVendido}.
 */
public interface AsientoVendidoService {
    /**
     * Save a asientoVendido.
     *
     * @param asientoVendido the entity to save.
     * @return the persisted entity.
     */
    AsientoVendido save(AsientoVendido asientoVendido);

    /**
     * Updates a asientoVendido.
     *
     * @param asientoVendido the entity to update.
     * @return the persisted entity.
     */
    AsientoVendido update(AsientoVendido asientoVendido);

    /**
     * Partially updates a asientoVendido.
     *
     * @param asientoVendido the entity to update partially.
     * @return the persisted entity.
     */
    Optional<AsientoVendido> partialUpdate(AsientoVendido asientoVendido);

    /**
     * Get all the asientoVendidos.
     *
     * @return the list of entities.
     */
    List<AsientoVendido> findAll();

    /**
     * Get the "id" asientoVendido.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<AsientoVendido> findOne(Long id);

    /**
     * Delete the "id" asientoVendido.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
