package com.items.infraestructure.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OpenApiConfigTest {

    private OpenApiConfig config;

    @BeforeEach
    void setUp() {
        config = new OpenApiConfig();
    }

    @Test
    void itemManagementAPI_shouldReturnOpenAPI() {
        OpenAPI openAPI = config.itemManagementAPI();

        assertNotNull(openAPI);
    }

    @Test
    void itemManagementAPI_shouldHaveInfo() {
        OpenAPI openAPI = config.itemManagementAPI();

        assertNotNull(openAPI.getInfo());
    }

    @Test
    void itemManagementAPI_shouldHaveCorrectTitle() {
        OpenAPI openAPI = config.itemManagementAPI();
        Info info = openAPI.getInfo();

        assertEquals("Item Management API", info.getTitle());
    }

    @Test
    void itemManagementAPI_shouldHaveCorrectDescription() {
        OpenAPI openAPI = config.itemManagementAPI();
        Info info = openAPI.getInfo();

        assertEquals("REST API para gestión de items", info.getDescription());
    }

    @Test
    void itemManagementAPI_shouldHaveCorrectVersion() {
        OpenAPI openAPI = config.itemManagementAPI();
        Info info = openAPI.getInfo();

        assertEquals("v1.0", info.getVersion());
    }

    @Test
    void itemManagementAPI_shouldHaveComponents() {
        OpenAPI openAPI = config.itemManagementAPI();

        assertNotNull(openAPI.getComponents());
    }

    @Test
    void itemManagementAPI_shouldHaveJWTSecurityScheme() {
        OpenAPI openAPI = config.itemManagementAPI();
        Components components = openAPI.getComponents();

        assertNotNull(components.getSecuritySchemes());
        assertTrue(components.getSecuritySchemes().containsKey("JWT"));
    }

    @Test
    void itemManagementAPI_jwtSchemeShouldBeHTTP() {
        OpenAPI openAPI = config.itemManagementAPI();
        SecurityScheme jwtScheme = openAPI.getComponents().getSecuritySchemes().get("JWT");

        assertEquals(SecurityScheme.Type.HTTP, jwtScheme.getType());
    }

    @Test
    void itemManagementAPI_jwtSchemeShouldUseBearer() {
        OpenAPI openAPI = config.itemManagementAPI();
        SecurityScheme jwtScheme = openAPI.getComponents().getSecuritySchemes().get("JWT");

        assertEquals("bearer", jwtScheme.getScheme());
    }

    @Test
    void itemManagementAPI_jwtSchemeShouldHaveBearerFormat() {
        OpenAPI openAPI = config.itemManagementAPI();
        SecurityScheme jwtScheme = openAPI.getComponents().getSecuritySchemes().get("JWT");

        assertEquals("JWT", jwtScheme.getBearerFormat());
    }

    @Test
    void constructor_shouldCreateInstance() {
        OpenApiConfig openApiConfig = new OpenApiConfig();

        assertNotNull(openApiConfig);
    }

    @Test
    void itemManagementAPI_shouldBeCallableMultipleTimes() {
        OpenAPI first = config.itemManagementAPI();
        OpenAPI second = config.itemManagementAPI();

        assertNotNull(first);
        assertNotNull(second);
        assertNotSame(first, second);
    }

    @Test
    void itemManagementAPI_infoShouldNotBeNull() {
        OpenAPI openAPI = config.itemManagementAPI();

        assertNotNull(openAPI.getInfo().getTitle());
        assertNotNull(openAPI.getInfo().getDescription());
        assertNotNull(openAPI.getInfo().getVersion());
    }

    @Test
    void itemManagementAPI_componentsShouldHaveOnlyJWTScheme() {
        OpenAPI openAPI = config.itemManagementAPI();
        Components components = openAPI.getComponents();

        assertEquals(1, components.getSecuritySchemes().size());
    }

    @Test
    void itemManagementAPI_shouldHaveCompleteConfiguration() {
        OpenAPI openAPI = config.itemManagementAPI();

        assertNotNull(openAPI.getInfo());
        assertNotNull(openAPI.getComponents());
        assertNotNull(openAPI.getComponents().getSecuritySchemes());
        assertTrue(openAPI.getComponents().getSecuritySchemes().containsKey("JWT"));
        
        SecurityScheme jwt = openAPI.getComponents().getSecuritySchemes().get("JWT");
        assertEquals(SecurityScheme.Type.HTTP, jwt.getType());
        assertEquals("bearer", jwt.getScheme());
        assertEquals("JWT", jwt.getBearerFormat());
    }
}