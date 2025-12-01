package com.items.infraestructure.config;

import com.items.application.service.ItemService;
import com.items.domain.port.outbound.ItemRepositoryPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class BeanConfigTest {

    @Mock
    private ItemRepositoryPort itemRepository;

    private BeanConfig beanConfig;

    @Test
    void itemRepositoryPort_shouldCreateJsonItemRepository() {
        beanConfig = new BeanConfig();

        ItemRepositoryPort repository = beanConfig.itemRepositoryPort();

        assertNotNull(repository);
    }

    @Test
    void itemApplicationService_shouldCreateItemService() {
        beanConfig = new BeanConfig();

        ItemService itemService = beanConfig.itemApplicationService(itemRepository);

        assertNotNull(itemService);
    }

    @Test
    void itemApplicationService_shouldUseProvidedRepository() {
        beanConfig = new BeanConfig();

        ItemService itemService = beanConfig.itemApplicationService(itemRepository);

        assertNotNull(itemService);
    }

    @Test
    void constructor_shouldInitialize() {
        beanConfig = new BeanConfig();

        assertNotNull(beanConfig);
    }
}