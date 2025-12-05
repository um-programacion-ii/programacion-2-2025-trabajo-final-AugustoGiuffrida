package com.um.programacion2.trabajo_final.service;

import com.um.programacion2.trabajo_final.service.dto.ConfirmarCompraDTO;
import com.um.programacion2.trabajo_final.service.dto.SolicitudBloqueoDTO;
import com.um.programacion2.trabajo_final.service.dto.VentaDTO;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.um.programacion2.trabajo_final.domain.Venta}.
 */
public interface VentaService {

    void bloquearAsientos(String login, SolicitudBloqueoDTO solicitudDTO);

    /**
     * Realiza la compra coordinando sesión, cátedra y base de datos local.
     */
    VentaDTO realizarCompra(String login, ConfirmarCompraDTO compraDTO);

    /**
     * Save a venta.
     *
     * @param ventaDTO the entity to save.
     * @return the persisted entity.
     */
    VentaDTO save(VentaDTO ventaDTO);

    /**
     * Updates a venta.
     *
     * @param ventaDTO the entity to update.
     * @return the persisted entity.
     */
    VentaDTO update(VentaDTO ventaDTO);

    /**
     * Partially updates a venta.
     *
     * @param ventaDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<VentaDTO> partialUpdate(VentaDTO ventaDTO);

    /**
     * Get all the ventas.
     *
     * @return the list of entities.
     */
    List<VentaDTO> findAll();

    /**
     * Get all the ventas with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<VentaDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" venta.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<VentaDTO> findOne(Long id);

    /**
     * Delete the "id" venta.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
