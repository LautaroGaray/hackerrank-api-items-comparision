package com.items.domain.model;

import org.junit.jupiter.api.Test;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ComparisionResultTest {

    @Test
    void shouldCreateComparisionResultWithAllFields() {
        String bestPriceId = "id1";
        double bestPrice = 100.0;
        String bestRatedId = "id2";
        double bestRating = 4.8;
        Map<String, String> differences = Map.of("priceDiff", "50.0", "ratingDiff", "0.6");

        ComparisionResult result = new ComparisionResult(bestPriceId, bestPrice, bestRatedId, bestRating, differences);

        assertEquals(bestPriceId, result.bestPriceItemId());
        assertEquals(bestPrice, result.bestPrice());
        assertEquals(bestRatedId, result.bestRankedItem());
        assertEquals(bestRating, result.bestRating());
        assertEquals(differences, result.differences());
    }

    @Test
    void shouldSupportRecordEquality() {
        Map<String, String> diffs = Map.of("key", "value");
        ComparisionResult result1 = new ComparisionResult("id1", 100.0, "id2", 4.5, diffs);
        ComparisionResult result2 = new ComparisionResult("id1", 100.0, "id2", 4.5, diffs);

        assertEquals(result1, result2);
    }

    @Test
    void shouldGenerateCorrectHashCode() {
        Map<String, String> diffs = Map.of("key", "value");
        ComparisionResult result1 = new ComparisionResult("id1", 100.0, "id2", 4.5, diffs);
        ComparisionResult result2 = new ComparisionResult("id1", 100.0, "id2", 4.5, diffs);

        assertEquals(result1.hashCode(), result2.hashCode());
    }
}