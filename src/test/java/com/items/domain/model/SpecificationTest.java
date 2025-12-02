package com.items.domain.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class SpecificationTest {

    @Test
    void shouldCreateSpecificationWithAllFields() {
        Specification spec = new Specification(
            "Dell",
            "XPS 15",
            "Silver",
            2.5,
            "357 x 235 x 18 mm",
            "Aluminum",
            24
        );

        assertEquals("Dell", spec.brand());
        assertEquals("XPS 15", spec.model());
        assertEquals("Silver", spec.color());
        assertEquals(2.5, spec.weight());
        assertEquals("357 x 235 x 18 mm", spec.dimensions());
        assertEquals("Aluminum", spec.material());
        assertEquals(24, spec.warrantyMonths());
    }

    @Test
    void shouldCreateSpecificationWithNullFields() {
        Specification spec = new Specification(null, null, null, null, null, null, null);

        assertNull(spec.brand());
        assertNull(spec.model());
        assertNull(spec.color());
        assertNull(spec.weight());
        assertNull(spec.dimensions());
        assertNull(spec.material());
        assertNull(spec.warrantyMonths());
    }

    @Test
    void shouldSupportEquality() {
        Specification spec1 = new Specification("HP", "Pavilion", "Black", 2.0, "300x200x15", "Plastic", 12);
        Specification spec2 = new Specification("HP", "Pavilion", "Black", 2.0, "300x200x15", "Plastic", 12);

        assertEquals(spec1, spec2);
        assertEquals(spec1.hashCode(), spec2.hashCode());
    }

    @Test
    void shouldNotBeEqualWithDifferentValues() {
        Specification spec1 = new Specification("HP", "Pavilion", "Black", 2.0, "300x200x15", "Plastic", 12);
        Specification spec2 = new Specification("Dell", "XPS", "Silver", 2.5, "350x250x18", "Aluminum", 24);

        assertNotEquals(spec1, spec2);
    }

    @Test
    void shouldHaveProperToString() {
        Specification spec = new Specification("Lenovo", "ThinkPad", "Black", 1.8, "320x220x16", "Carbon", 36);
        String toString = spec.toString();

        assertTrue(toString.contains("Lenovo"));
        assertTrue(toString.contains("ThinkPad"));
        assertTrue(toString.contains("Black"));
    }
}