package com.um.programacion2.trabajo_final;

import com.um.programacion2.trabajo_final.config.AsyncSyncConfiguration;
import com.um.programacion2.trabajo_final.config.EmbeddedRedis;
import com.um.programacion2.trabajo_final.config.EmbeddedSQL;
import com.um.programacion2.trabajo_final.config.JacksonConfiguration;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Base composite annotation for integration tests.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@SpringBootTest(classes = { BackendApp.class, JacksonConfiguration.class, AsyncSyncConfiguration.class })
@EmbeddedRedis
@EmbeddedSQL
public @interface IntegrationTest {
}
