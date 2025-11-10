package com.um.programacion2.trabajo_final.service.impl;

import com.um.programacion2.trabajo_final.domain.Venta;
import com.um.programacion2.trabajo_final.repository.VentaRepository;
import com.um.programacion2.trabajo_final.service.VentaService;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.um.programacion2.trabajo_final.domain.Venta}.
 */
@Service
@Transactional
public class VentaServiceImpl implements VentaService {

    private static final Logger LOG = LoggerFactory.getLogger(VentaServiceImpl.class);

    private final VentaRepository ventaRepository;

    public VentaServiceImpl(VentaRepository ventaRepository) {
        this.ventaRepository = ventaRepository;
    }

    @Override
    public Venta save(Venta venta) {
        LOG.debug("Request to save Venta : {}", venta);
        return ventaRepository.save(venta);
    }

    @Override
    public Venta update(Venta venta) {
        LOG.debug("Request to update Venta : {}", venta);
        return ventaRepository.save(venta);
    }

    @Override
    public Optional<Venta> partialUpdate(Venta venta) {
        LOG.debug("Request to partially update Venta : {}", venta);

        return ventaRepository
            .findById(venta.getId())
            .map(existingVenta -> {
                if (venta.getVentaIdCatedra() != null) {
                    existingVenta.setVentaIdCatedra(venta.getVentaIdCatedra());
                }
                if (venta.getFechaVenta() != null) {
                    existingVenta.setFechaVenta(venta.getFechaVenta());
                }
                if (venta.getPrecioVenta() != null) {
                    existingVenta.setPrecioVenta(venta.getPrecioVenta());
                }
                if (venta.getResultado() != null) {
                    existingVenta.setResultado(venta.getResultado());
                }
                if (venta.getDescripcion() != null) {
                    existingVenta.setDescripcion(venta.getDescripcion());
                }
                if (venta.getEstadoVenta() != null) {
                    existingVenta.setEstadoVenta(venta.getEstadoVenta());
                }

                return existingVenta;
            })
            .map(ventaRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Venta> findAll() {
        LOG.debug("Request to get all Ventas");
        return ventaRepository.findAll();
    }

    public Page<Venta> findAllWithEagerRelationships(Pageable pageable) {
        return ventaRepository.findAllWithEagerRelationships(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Venta> findOne(Long id) {
        LOG.debug("Request to get Venta : {}", id);
        return ventaRepository.findOneWithEagerRelationships(id);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete Venta : {}", id);
        ventaRepository.deleteById(id);
    }
}
