package com.um.programacion2.trabajo_final.service.impl;

import com.um.programacion2.trabajo_final.domain.Evento;
import com.um.programacion2.trabajo_final.repository.EventoRepository;
import com.um.programacion2.trabajo_final.service.EventoService;
import com.um.programacion2.trabajo_final.service.dto.EventoDTO;
import com.um.programacion2.trabajo_final.service.mapper.EventoMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.um.programacion2.trabajo_final.service.dto.catedra.EventoCatedraDTO;
import java.util.Arrays;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.client.RestTemplate;

/**
 * Service Implementation for managing {@link com.um.programacion2.trabajo_final.domain.Evento}.
 */
@Service
@Transactional
public class EventoServiceImpl implements EventoService {

    private static final Logger LOG = LoggerFactory.getLogger(EventoServiceImpl.class);

    private final EventoRepository eventoRepository;

    private final EventoMapper eventoMapper;

    @Autowired
    private RestTemplate restTemplate;

    @Value("${application.catedra.eventos-url}")
    private String catedraEventosUrl;

    @Value("${application.catedra.forzar-update-url}")
    private String catedraForzarUpdateUrl;

    public EventoServiceImpl(EventoRepository eventoRepository, EventoMapper eventoMapper) {
        this.eventoRepository = eventoRepository;
        this.eventoMapper = eventoMapper;
    }

    @Override
    public void sincronizarEventosCatedra() {
        LOG.info("Iniciando sincronización de eventos desde Cátedra: {}", catedraEventosUrl);

        try {

            LOG.debug("Forzando actualización de eventos en Cátedra: {}", catedraForzarUpdateUrl);
            restTemplate.getForObject(catedraForzarUpdateUrl, String.class);

            LOG.info("Obteniendo eventos desde Cátedra: {}", catedraEventosUrl);
            EventoCatedraDTO[] eventosArray = restTemplate.getForObject(catedraEventosUrl, EventoCatedraDTO[].class);

            if (eventosArray == null || eventosArray.length == 0) {
                LOG.warn("No se recibieron eventos desde Cátedra.");
                return;
            }

            List<EventoCatedraDTO> eventosCatedra = Arrays.asList(eventosArray);
            LOG.info("Se recibieron {} eventos desde Cátedra.", eventosCatedra.size());

            for (EventoCatedraDTO dto : eventosCatedra) {
                Optional<Evento> eventoExistenteOpt = eventoRepository.findByEventoIdCatedra(dto.getId());

                if (eventoExistenteOpt.isPresent()) {
                    LOG.debug("Actualizando evento existente: {}", dto.getTitulo());
                    Evento eventoExistente = eventoExistenteOpt.get();
                    eventoMapper.updateEventoFromCatedraDTO(dto, eventoExistente);
                    eventoRepository.save(eventoExistente);
                } else {
                    LOG.debug("Creando nuevo evento: {}", dto.getTitulo());
                    Evento eventoNuevo = eventoMapper.eventoFromCatedraDTO(dto);
                    eventoRepository.save(eventoNuevo);
                }
            }
            LOG.info("Sincronización de eventos completada.");
        } catch (Exception e) {
            LOG.error("Error durante la sincronización de eventos: {}", e.getMessage(), e);
        }
    }

    @Override
    public EventoDTO save(EventoDTO eventoDTO) {
        LOG.debug("Request to save Evento : {}", eventoDTO);
        Evento evento = eventoMapper.toEntity(eventoDTO);
        evento = eventoRepository.save(evento);
        return eventoMapper.toDto(evento);
    }

    @Override
    public EventoDTO update(EventoDTO eventoDTO) {
        LOG.debug("Request to update Evento : {}", eventoDTO);
        Evento evento = eventoMapper.toEntity(eventoDTO);
        evento = eventoRepository.save(evento);
        return eventoMapper.toDto(evento);
    }

    @Override
    public Optional<EventoDTO> partialUpdate(EventoDTO eventoDTO) {
        LOG.debug("Request to partially update Evento : {}", eventoDTO);

        return eventoRepository
            .findById(eventoDTO.getId())
            .map(existingEvento -> {
                eventoMapper.partialUpdate(existingEvento, eventoDTO);

                return existingEvento;
            })
            .map(eventoRepository::save)
            .map(eventoMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EventoDTO> findAll() {
        LOG.debug("Request to get all Eventos");
        return eventoRepository.findAll().stream().map(eventoMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<EventoDTO> findOne(Long id) {
        LOG.debug("Request to get Evento : {}", id);
        return eventoRepository.findById(id).map(eventoMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete Evento : {}", id);
        eventoRepository.deleteById(id);
    }
}
