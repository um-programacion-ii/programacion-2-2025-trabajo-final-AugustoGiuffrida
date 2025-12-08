package com.um.programacion2.trabajo_final.service;

import com.um.programacion2.trabajo_final.service.dto.VentaDTO;

/**
 * Service Interface para gestionar el reintento de ventas pendientes.
 */
public interface VentaRetryService {

    /**
     * Intenta reconciliar una venta manualmente.
     *
     * @param ventaId el ID de la venta local.
     * @return el VentaDTO actualizado.
     */
    VentaDTO reconciliarVentaManual(Long ventaId);
}
