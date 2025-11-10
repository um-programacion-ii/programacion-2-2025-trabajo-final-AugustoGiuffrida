package com.um.programacion2.trabajo_final.service.impl;

import com.um.programacion2.trabajo_final.domain.AsientoVendido;
import com.um.programacion2.trabajo_final.repository.AsientoVendidoRepository;
import com.um.programacion2.trabajo_final.service.AsientoVendidoService;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.um.programacion2.trabajo_final.domain.AsientoVendido}.
 */
@Service
@Transactional
public class AsientoVendidoServiceImpl implements AsientoVendidoService {

    private static final Logger LOG = LoggerFactory.getLogger(AsientoVendidoServiceImpl.class);

    private final AsientoVendidoRepository asientoVendidoRepository;

    public AsientoVendidoServiceImpl(AsientoVendidoRepository asientoVendidoRepository) {
        this.asientoVendidoRepository = asientoVendidoRepository;
    }

    @Override
    public AsientoVendido save(AsientoVendido asientoVendido) {
        LOG.debug("Request to save AsientoVendido : {}", asientoVendido);
        return asientoVendidoRepository.save(asientoVendido);
    }

    @Override
    public AsientoVendido update(AsientoVendido asientoVendido) {
        LOG.debug("Request to update AsientoVendido : {}", asientoVendido);
        return asientoVendidoRepository.save(asientoVendido);
    }

    @Override
    public Optional<AsientoVendido> partialUpdate(AsientoVendido asientoVendido) {
        LOG.debug("Request to partially update AsientoVendido : {}", asientoVendido);

        return asientoVendidoRepository
            .findById(asientoVendido.getId())
            .map(existingAsientoVendido -> {
                if (asientoVendido.getFila() != null) {
                    existingAsientoVendido.setFila(asientoVendido.getFila());
                }
                if (asientoVendido.getColumna() != null) {
                    existingAsientoVendido.setColumna(asientoVendido.getColumna());
                }
                if (asientoVendido.getPersona() != null) {
                    existingAsientoVendido.setPersona(asientoVendido.getPersona());
                }

                return existingAsientoVendido;
            })
            .map(asientoVendidoRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AsientoVendido> findAll() {
        LOG.debug("Request to get all AsientoVendidos");
        return asientoVendidoRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<AsientoVendido> findOne(Long id) {
        LOG.debug("Request to get AsientoVendido : {}", id);
        return asientoVendidoRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete AsientoVendido : {}", id);
        asientoVendidoRepository.deleteById(id);
    }
}
