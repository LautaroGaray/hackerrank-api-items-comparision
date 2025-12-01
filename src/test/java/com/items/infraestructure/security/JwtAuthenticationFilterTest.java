package com.items.infraestructure.security;

import com.items.domain.port.outbound.TokenProviderPort;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JwtAuthenticationFilterTest {

   
    @Mock
    private TokenProviderPort tokenProvider;

    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private FilterChain filterChain;

    @InjectMocks
    private JwtAuthenticationFilter jwtAuthenticationFilter;

 
    @BeforeEach
    void setUp() {
        SecurityContextHolder.clearContext();
    }
   
    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test 
    void doFilterInternal_noAuthorizationHeader_shouldContinueChain() throws ServletException, IOException {
      
        when(request.getHeader("Authorization")).thenReturn(null);
        // WHEN
        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

  
        verify(tokenProvider, never()).validateAndGetSubject(anyString());       
        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();      
        verify(filterChain, times(1)).doFilter(request, response);
    }

  
    @Test   
    void doFilterInternal_wrongAuthorizationFormat_shouldContinueChain() throws ServletException, IOException {
       
        when(request.getHeader("Authorization")).thenReturn("Basic dXNlcm5hbWU6cGFzc3dvcmQ=");
     
        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);
     
        verify(tokenProvider, never()).validateAndGetSubject(anyString());  
        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();     
        verify(filterChain, times(1)).doFilter(request, response);
    }

 
    @Test  
    void doFilterInternal_validToken_shouldSetAuthentication() throws ServletException, IOException {
  
        String validToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9";
        String username = "testUser";
        when(request.getHeader("Authorization")).thenReturn("Bearer " + validToken);       
        when(tokenProvider.validateAndGetSubject(validToken)).thenReturn(username);
       
        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);
    
        verify(tokenProvider, times(1)).validateAndGetSubject(validToken);      
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        assertThat(auth).isNotNull();      
        assertThat(auth.getPrincipal()).isEqualTo(username);    
        assertThat(auth.getAuthorities()).hasSize(1)
                .extracting("authority").contains("ROLE_USER");
      
        verify(filterChain, times(1)).doFilter(request, response);
    }

    @Test   
    void doFilterInternal_invalidToken_shouldClearContext() throws ServletException, IOException {
    
        String invalidToken = "invalid.jwt.token";
        when(request.getHeader("Authorization")).thenReturn("Bearer " + invalidToken);       
        when(tokenProvider.validateAndGetSubject(invalidToken)).thenThrow(new RuntimeException("Token expirado"));

      
        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);
        
        verify(tokenProvider, times(1)).validateAndGetSubject(invalidToken);       
        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
      
        verify(filterChain, times(1)).doFilter(request, response);
    }
}