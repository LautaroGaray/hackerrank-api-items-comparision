package com.items.infraestructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.items.application.service.ItemService;
import com.items.domain.port.outbound.ItemRepositoryPort;
import com.items.infraestructure.adapters.outbound.persistence.JsonItemRepository;

@Configuration
public class BeanConfig {

    @Bean
    public ItemRepositoryPort itemRepositoryPort() {
        return new JsonItemRepository("data/items");
    }

    @Bean ItemService itemApplicationService(ItemRepositoryPort itemRepositoryPort){
        return new ItemService(itemRepositoryPort);
    }
    
}
