package com.items.domain.model;

import java.util.Map;

public record ComparisionResult(String bestPriceItemId, double bestPrice, String bestRankedItem,
     double bestRating, Map<String,String> differences) {
    
}
