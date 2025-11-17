package com.um.programacion2.trabajo_final.service.impl;

import com.um.programacion2.trabajo_final.domain.Venta;
import com.um.programacion2.trabajo_final.repository.VentaRepository;
import com.um.programacion2.trabajo_final.service.VentaService;
import com.um.programacion2.trabajo_final.service.dto.VentaDTO;
import com.um.programacion2.trabajo_final.service.mapper.VentaMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
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

    private final VentaMapper ventaMapper;

    public VentaServiceImpl(VentaRepository ventaRepository, VentaMapper ventaMapper) {
        this.ventaRepository = ventaRepository;
        this.ventaMapper = ventaMapper;
    }

    @Override
    public VentaDTO save(VentaDTO ventaDTO) {
        LOG.debug("Request to save Venta : {}", ventaDTO);
        Venta venta = ventaMapper.toEntity(ventaDTO);
        venta = ventaRepository.save(venta);
        return ventaMapper.toDto(venta);
    }

    @Override
    public VentaDTO update(VentaDTO ventaDTO) {
        LOG.debug("Request to update Venta : {}", ventaDTO);
        Venta venta = ventaMapper.toEntity(ventaDTO);
        venta = ventaRepository.save(venta);
        return ventaMapper.toDto(venta);
    }

    @Override
    public Optional<VentaDTO> partialUpdate(VentaDTO ventaDTO) {
        LOG.debug("Request to partially update Venta : {}", ventaDTO);

        return ventaRepository
            .findById(ventaDTO.getId())
            .map(existingVenta -> {
                ventaMapper.partialUpdate(existingVenta, ventaDTO);

                return existingVenta;
            })
            .map(ventaRepository::save)
            .map(ventaMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<VentaDTO> findAll() {
        LOG.debug("Request to get all Ventas");
        return ventaRepository.findAll().stream().map(ventaMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    public Page<VentaDTO> findAllWithEagerRelationships(Pageable pageable) {
        return ventaRepository.findAllWithEagerRelationships(pageable).map(ventaMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<VentaDTO> findOne(Long id) {
        LOG.debug("Request to get Venta : {}", id);
        return ventaRepository.findOneWithEagerRelationships(id).map(ventaMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete Venta : {}", id);
        ventaRepository.deleteById(id);
    }
}
