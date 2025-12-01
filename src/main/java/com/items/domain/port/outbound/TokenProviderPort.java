package com.items.domain.port.outbound;

/**
 * Outbound port for generating and validating tokens.
 * Application code depends on this interface, not on infra implementation.
 */
public interface TokenProviderPort {
    String generateToken(String subject);
    String validateAndGetSubject(String token);
}