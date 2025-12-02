package com.items.infraestructure.entities;

import com.items.domain.model.Item;
import com.items.domain.model.Specification;

import java.math.BigDecimal;


public class ItemEntity {
    private String id;
    private String name;
    private String imageUrl;
    private String description;
    private BigDecimal price;
    private Double rating;
    private Specification specification;


    public ItemEntity() {}

    public ItemEntity(String id,
                      String name,
                      String imageUrl,
                      String description,
                      BigDecimal price,
                      Double rating,
                      Specification specification
                      ) {
        this.id = id;
        this.name = name;
        this.imageUrl = imageUrl;
        this.description = description;
        this.price = price;
        this.rating = rating;  
        this.specification = specification;   
    }

    public static ItemEntity fromDomain(Item item) {
        if (item == null) return null;
        return new ItemEntity(
                item.id(),
                item.name(),
                item.imageUrl(),
                item.description(),
                item.price(),
                item.rating(),
                item.specification()
               
        );
    }

    public Item toDomain() {
        return new Item(
                this.id,
                this.name,
                this.imageUrl,
                this.description,
                this.price,
                this.rating,
                this.specification
                           
        );
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public Specification getSpecification(){
        return this.specification;
    }

    public void setSpecification(Specification specification){
        this.specification = specification;
    }
   
}
