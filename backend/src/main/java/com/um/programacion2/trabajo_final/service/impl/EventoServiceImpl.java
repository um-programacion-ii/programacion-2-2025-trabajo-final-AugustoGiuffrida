package com.um.programacion2.trabajo_final.service.impl;

import com.um.programacion2.trabajo_final.domain.Evento;
import com.um.programacion2.trabajo_final.repository.EventoRepository;
import com.um.programacion2.trabajo_final.service.EventoService;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.um.programacion2.trabajo_final.domain.Evento}.
 */
@Service
@Transactional
public class EventoServiceImpl implements EventoService {

    private static final Logger LOG = LoggerFactory.getLogger(EventoServiceImpl.class);

    private final EventoRepository eventoRepository;

    public EventoServiceImpl(EventoRepository eventoRepository) {
        this.eventoRepository = eventoRepository;
    }

    @Override
    public Evento save(Evento evento) {
        LOG.debug("Request to save Evento : {}", evento);
        return eventoRepository.save(evento);
    }

    @Override
    public Evento update(Evento evento) {
        LOG.debug("Request to update Evento : {}", evento);
        return eventoRepository.save(evento);
    }

    @Override
    public Optional<Evento> partialUpdate(Evento evento) {
        LOG.debug("Request to partially update Evento : {}", evento);

        return eventoRepository
            .findById(evento.getId())
            .map(existingEvento -> {
                if (evento.getEventoIdCatedra() != null) {
                    existingEvento.setEventoIdCatedra(evento.getEventoIdCatedra());
                }
                if (evento.getTitulo() != null) {
                    existingEvento.setTitulo(evento.getTitulo());
                }
                if (evento.getResumen() != null) {
                    existingEvento.setResumen(evento.getResumen());
                }
                if (evento.getDescripcion() != null) {
                    existingEvento.setDescripcion(evento.getDescripcion());
                }
                if (evento.getFecha() != null) {
                    existingEvento.setFecha(evento.getFecha());
                }
                if (evento.getDireccion() != null) {
                    existingEvento.setDireccion(evento.getDireccion());
                }
                if (evento.getImagen() != null) {
                    existingEvento.setImagen(evento.getImagen());
                }
                if (evento.getFilaAsientos() != null) {
                    existingEvento.setFilaAsientos(evento.getFilaAsientos());
                }
                if (evento.getColumnAsientos() != null) {
                    existingEvento.setColumnAsientos(evento.getColumnAsientos());
                }
                if (evento.getPrecioEntrada() != null) {
                    existingEvento.setPrecioEntrada(evento.getPrecioEntrada());
                }

                return existingEvento;
            })
            .map(eventoRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Evento> findAll() {
        LOG.debug("Request to get all Eventos");
        return eventoRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Evento> findOne(Long id) {
        LOG.debug("Request to get Evento : {}", id);
        return eventoRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete Evento : {}", id);
        eventoRepository.deleteById(id);
    }
}
