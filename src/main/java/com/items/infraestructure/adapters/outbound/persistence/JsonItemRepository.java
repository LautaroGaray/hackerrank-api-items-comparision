package com.items.infraestructure.adapters.outbound.persistence;

import java.io.File;
import java.util.Optional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.items.domain.model.Item;
import com.items.domain.port.outbound.ItemRepositoryPort;
import com.items.infraestructure.entities.ItemEntity;
import com.items.infraestructure.mapper.ItemEntityMapper;

public class JsonItemRepository  implements ItemRepositoryPort {
    
    private final ObjectMapper mapper = new ObjectMapper();
    private final String baseFolder;
    private final ItemEntityMapper mapperEntity = new ItemEntityMapper();

    public JsonItemRepository(String baseFolder) {
        this.baseFolder = baseFolder;
        new File(baseFolder).mkdirs();
    }

    @Override 
    public Optional<Item> findById(String id){
        try{
            File file = new File(baseFolder + "/" + id + ".json");
            if(!file.exists()){
                return Optional.empty();
            }
            ItemEntity entity = mapper.readValue(file, ItemEntity.class);
            return Optional.of(mapperEntity.toDomain(entity));
        }catch(Exception e){
            throw new RuntimeException("Error reading item with id " + id, e);
        }
    }

    @Override
    public Item save(Item item) {
        try{
            ItemEntity entity = mapperEntity.toEntity(item);

            File file = new File(baseFolder + "/" + item.id() + ".json");
            mapper.writerWithDefaultPrettyPrinter().writeValue(file, entity);
            return item;
        }catch(Exception e){
            throw new RuntimeException("Error saving item with id " + item.id(), e);
        }
    }

    @Override
    public void deleteById(String id) {
        File file = new File(baseFolder + "/" + id + ".json");
        if(file.exists()){
            file.delete();
        }
    }
}
