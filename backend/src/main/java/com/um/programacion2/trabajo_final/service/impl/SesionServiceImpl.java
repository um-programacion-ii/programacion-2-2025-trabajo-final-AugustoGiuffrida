package com.um.programacion2.trabajo_final.service.impl;

import com.um.programacion2.trabajo_final.service.SesionService;
import com.um.programacion2.trabajo_final.service.dto.SesionVentaDTO;
import org.springframework.transaction.annotation.Transactional;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
@Transactional
public class SesionServiceImpl implements SesionService {

    private final Logger log = LoggerFactory.getLogger(SesionServiceImpl.class);
    private final RedissonClient redissonClient;

    public SesionServiceImpl(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
    }

    private String getClaveRedis(String login) {
        return "sesion_usuario:" + login;
    }

    @Override
    public void guardarSesion(String login, SesionVentaDTO sesion) {
        log.debug("Guardando sesión en Redis local para usuario: {}", login);
        RBucket<SesionVentaDTO> bucket = redissonClient.getBucket(getClaveRedis(login));
        bucket.set(sesion);
        // Darle un tiempo de vida (30 min)
        bucket.expire(30, TimeUnit.MINUTES);
    }

    @Override
    public Optional<SesionVentaDTO> obtenerSesion(String login) {
        log.debug("Recuperando sesión de Redis local para usuario: {}", login);
        RBucket<SesionVentaDTO> bucket = redissonClient.getBucket(getClaveRedis(login));
        return Optional.ofNullable(bucket.get());
    }

    @Override
    public void borrarSesion(String login) {
        log.debug("Borrando sesión de Redis local para usuario: {}", login);
        RBucket<SesionVentaDTO> bucket = redissonClient.getBucket(getClaveRedis(login));
        bucket.delete();
    }
}
