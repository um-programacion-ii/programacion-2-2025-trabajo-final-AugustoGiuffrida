package com.um.programacion2.proxy.web.rest;

import com.um.programacion2.proxy.service.CatedraGatewayService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/gateway")
public class GatewayController {

    private final CatedraGatewayService catedraGatewayService;

    @Value("${application.bloquear-asientos-url}")
    private String bloquearAsientosUrl;

    @Value("${application.realizar-venta-url}")
    private String realizarVentasUrl;

    public GatewayController(CatedraGatewayService catedraGatewayService) {
        this.catedraGatewayService = catedraGatewayService;
    }

    @PostMapping("/bloquear-asientos")
    public ResponseEntity<Object> bloquearAsientos(@RequestBody Object request, @RequestHeader HttpHeaders headers) {
        return catedraGatewayService.forwardPost(bloquearAsientosUrl, request, headers);
    }

    @PostMapping("/realizar-venta")
    public ResponseEntity<Object> realizarVenta(@RequestBody Object request, @RequestHeader HttpHeaders headers) {
        return catedraGatewayService.forwardPost(realizarVentasUrl, request, headers);
    }
}