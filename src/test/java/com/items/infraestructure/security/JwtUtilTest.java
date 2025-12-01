package com.items.infraestructure.security;

import com.items.infraestructure.config.JwtProperties;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.SignatureException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

class JwtUtilTest {

    private JwtUtil jwtUtil;
    private final String TEST_SECRET = "thisismytestsecretkeythathastobeatleast32byteslong"; 
    private final long EXPIRATION_MS = 3600000; 
    private final String TEST_USERNAME = "testUser";

    @BeforeEach
    void setUp() {
        JwtProperties mockProps = Mockito.mock(JwtProperties.class);
        when(mockProps.getSecret()).thenReturn(TEST_SECRET);
        when(mockProps.getExpirationMs()).thenReturn(EXPIRATION_MS);
        jwtUtil = new JwtUtil(mockProps);
    }

   
    @Test  
    void generateToken_shouldGenerateValidTokenWithCorrectSubject() {
        String token = jwtUtil.generateToken(TEST_USERNAME);
        assertThat(token).isNotNull();
        
        String subject = jwtUtil.validateAndGetSubject(token);
        assertThat(subject).isEqualTo(TEST_USERNAME);
    }

    @Test  
    void validateAndGetSubject_validToken_shouldReturnSubject() {
        String token = jwtUtil.generateToken(TEST_USERNAME);
        String subject = jwtUtil.validateAndGetSubject(token);
        assertThat(subject).isEqualTo(TEST_USERNAME);
    }

  
    @Test  
    void validateAndGetSubject_expiredToken_shouldThrowExpiredJwtException() throws InterruptedException {
       
        JwtProperties shortLivedProps = Mockito.mock(JwtProperties.class);
        when(shortLivedProps.getSecret()).thenReturn(TEST_SECRET);
        when(shortLivedProps.getExpirationMs()).thenReturn(10L); 
        JwtUtil shortLivedJwtUtil = new JwtUtil(shortLivedProps);

        String token = shortLivedJwtUtil.generateToken(TEST_USERNAME);
        Thread.sleep(15); 
       
        assertThrows(ExpiredJwtException.class, () -> shortLivedJwtUtil.validateAndGetSubject(token));
    }

    @Test   
    void validateAndGetSubject_tamperedToken_shouldThrowSignatureException() {
      
        String originalToken = jwtUtil.generateToken(TEST_USERNAME);

        JwtProperties differentKeyProps = Mockito.mock(JwtProperties.class);
        when(differentKeyProps.getSecret()).thenReturn("differentkeythathastobeatleast32byteslongforsigning");
        when(differentKeyProps.getExpirationMs()).thenReturn(EXPIRATION_MS);
        JwtUtil maliciousJwtUtil = new JwtUtil(differentKeyProps);
      
        assertThrows(SignatureException.class, () -> maliciousJwtUtil.validateAndGetSubject(originalToken));
    }
}