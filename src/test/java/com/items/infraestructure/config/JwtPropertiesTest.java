package com.items.infraestructure.config;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class JwtPropertiesTest {

    @Test
    void shouldSetAndGetSecret() {
        JwtProperties properties = new JwtProperties();
        String secret = "mySecretKey123456789012345678901234567890";

        properties.setSecret(secret);

        assertEquals(secret, properties.getSecret());
    }

    @Test
    void shouldSetAndGetExpirationMs() {
        JwtProperties properties = new JwtProperties();
        long expiration = 7200000L;

        properties.setExpirationMs(expiration);

        assertEquals(expiration, properties.getExpirationMs());
    }

    @Test
    void shouldHaveDefaultExpirationMs() {
        JwtProperties properties = new JwtProperties();

        assertEquals(3600000L, properties.getExpirationMs());
    }

    @Test
    void shouldHandleNullSecret() {
        JwtProperties properties = new JwtProperties();

        properties.setSecret(null);

        assertNull(properties.getSecret());
    }

    @Test
    void shouldHandleZeroExpiration() {
        JwtProperties properties = new JwtProperties();

        properties.setExpirationMs(0L);

        assertEquals(0L, properties.getExpirationMs());
    }

    @Test
    void shouldHandleNegativeExpiration() {
        JwtProperties properties = new JwtProperties();

        properties.setExpirationMs(-1000L);

        assertEquals(-1000L, properties.getExpirationMs());
    }

    @Test
    void shouldSetMultipleTimes() {
        JwtProperties properties = new JwtProperties();

        properties.setSecret("first");
        properties.setSecret("second");
        properties.setExpirationMs(1000L);
        properties.setExpirationMs(2000L);

        assertEquals("second", properties.getSecret());
        assertEquals(2000L, properties.getExpirationMs());
    }

    @Test
    void shouldHandleEmptySecret() {
        JwtProperties properties = new JwtProperties();

        properties.setSecret("");

        assertEquals("", properties.getSecret());
    }

    @Test
    void shouldHandleLongExpiration() {
        JwtProperties properties = new JwtProperties();
        long longExpiration = Long.MAX_VALUE;

        properties.setExpirationMs(longExpiration);

        assertEquals(longExpiration, properties.getExpirationMs());
    }
}