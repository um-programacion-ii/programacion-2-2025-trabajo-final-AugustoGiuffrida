package com.um.programacion2.trabajo_final.service;

import com.um.programacion2.trabajo_final.service.dto.AsientoVendidoDTO;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.um.programacion2.trabajo_final.domain.AsientoVendido}.
 */
public interface AsientoVendidoService {
    /**
     * Save a asientoVendido.
     *
     * @param asientoVendidoDTO the entity to save.
     * @return the persisted entity.
     */
    AsientoVendidoDTO save(AsientoVendidoDTO asientoVendidoDTO);

    /**
     * Updates a asientoVendido.
     *
     * @param asientoVendidoDTO the entity to update.
     * @return the persisted entity.
     */
    AsientoVendidoDTO update(AsientoVendidoDTO asientoVendidoDTO);

    /**
     * Partially updates a asientoVendido.
     *
     * @param asientoVendidoDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<AsientoVendidoDTO> partialUpdate(AsientoVendidoDTO asientoVendidoDTO);

    /**
     * Get all the asientoVendidos.
     *
     * @return the list of entities.
     */
    List<AsientoVendidoDTO> findAll();

    /**
     * Get the "id" asientoVendido.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<AsientoVendidoDTO> findOne(Long id);

    /**
     * Delete the "id" asientoVendido.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
