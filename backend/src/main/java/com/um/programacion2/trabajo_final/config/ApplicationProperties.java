package com.um.programacion2.trabajo_final.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Properties specific to Backend.
 * <p>
 * Properties are configured in the {@code application.yml} file.
 * See {@link tech.jhipster.config.JHipsterProperties} for a good example.
 */
@ConfigurationProperties(prefix = "application", ignoreUnknownFields = false)
public class ApplicationProperties {

    private final Catedra catedra = new Catedra();

    public Catedra getCatedra() {
        return catedra;
    }

    public static class Catedra {
        private String token;
        private String ventaUrl;
        private String proxyUrl;
        private String eventosUrl;
        private String bloqueoUrl;
        private String forzarUpdateUrl;
        private String listarVentasUrl;

        public String getListarVentasUrl() {
            return listarVentasUrl;
        }

        public void setListarVentasUrl(String listarVentasUrl) {
            this.listarVentasUrl = listarVentasUrl;
        }

        public String getForzarUpdateUrl() {
            return forzarUpdateUrl;
        }

        public void setForzarUpdateUrl(String forzarUpdateUrl) {
            this.forzarUpdateUrl = forzarUpdateUrl;
        }

        public String getBloqueoUrl() {
            return bloqueoUrl;
        }

        public void setBloqueoUrl(String bloqueoUrl) {
            this.bloqueoUrl = bloqueoUrl;
        }

        public String getVentaUrl() {
            return ventaUrl;
        }
        public void setVentaUrl(String ventaUrl) {
            this.ventaUrl = ventaUrl;
        }

        public String getProxyUrl() {
            return proxyUrl;
        }

        public void setProxyUrl(String proxyUrl) {
            this.proxyUrl = proxyUrl;
        }
        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public String getEventosUrl() {
            return eventosUrl;
        }

        public void setEventosUrl(String eventosUrl) {
            this.eventosUrl = eventosUrl;
        }
    }

    private final Liquibase liquibase = new Liquibase();

    // jhipster-needle-application-properties-property

    public Liquibase getLiquibase() {
        return liquibase;
    }

    // jhipster-needle-application-properties-property-getter

    public static class Liquibase {

        private Boolean asyncStart = true;

        public Boolean getAsyncStart() {
            return asyncStart;
        }

        public void setAsyncStart(Boolean asyncStart) {
            this.asyncStart = asyncStart;
        }
    }
    // jhipster-needle-application-properties-property-class
}
