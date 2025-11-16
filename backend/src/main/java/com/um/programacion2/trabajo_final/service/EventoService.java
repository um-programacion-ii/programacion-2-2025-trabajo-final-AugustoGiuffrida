package com.um.programacion2.trabajo_final.service;

import com.um.programacion2.trabajo_final.domain.Evento;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.um.programacion2.trabajo_final.domain.Evento}.
 */
public interface EventoService {
    /**
     * Save a evento.
     *
     * @param evento the entity to save.
     * @return the persisted entity.
     */
    Evento save(Evento evento);

    /**
     * Updates a evento.
     *
     * @param evento the entity to update.
     * @return the persisted entity.
     */
    Evento update(Evento evento);

    /**
     * Partially updates a evento.
     *
     * @param evento the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Evento> partialUpdate(Evento evento);

    /**
     * Get all the eventos.
     *
     * @return the list of entities.
     */
    List<Evento> findAll();

    /**
     * Get the "id" evento.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Evento> findOne(Long id);

    /**
     * Delete the "id" evento.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
