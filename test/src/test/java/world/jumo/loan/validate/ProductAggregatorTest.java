/*
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * ProductAggregatorTest.java
 * Copyright 2018 Medical EDI Services (PTY) Ltd. All rights reserved.
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 */

package world.jumo.loan.validate;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;

import org.junit.jupiter.api.Test;

/**
 * Tests the ProductAggregator
 */
@SuppressWarnings("static-method")
public class ProductAggregatorTest {

    class Thing implements Product<Double> {

        Double amount;
        String value;

        public Thing(String value, Double amount) {

            this.value = value;
            this.amount = amount;
        }

        @Override
        public Double getAmount() {

            return amount;
        }

        public String getValue() {

            return value;
        }
    }

    /**
     * Test method for {@link world.jumo.loan.validate.ProductAggregator#ProductAggregator(java.util.function.Function, java.util.List)}.
     */
    @Test
    void testProductAggregator() {

        ProductAggregator<Thing, String, Double> pa = new ProductAggregator<>(t -> t.getValue(), Arrays.asList(TestSum.class));
        assertTrue(pa.get().isEmpty());
        assertEquals("a", pa.converter.apply(new Thing("a", Double.valueOf(1))));
        assertEquals(TestSum.class, pa.aggregates.get(0));
    }

    /**
     * Test method for {@link world.jumo.loan.validate.ProductAggregator#put(world.jumo.loan.validate.Product)}.
     */
    @Test
    void testPut() {

        ProductAggregator<Thing, String, Double> pa = new ProductAggregator<>(t -> t.getValue(), Arrays.asList(TestSum.class));
        pa.put(new Thing("a", Double.valueOf(1)));
        assertEquals(Double.valueOf(1), pa.get().get("a").get(0), 0.0005);
        pa.put(new Thing("a", Double.valueOf(1)));
        assertEquals(Double.valueOf(2), pa.get().get("a").get(0), 0.0005);
        pa.put(new Thing("a", Double.valueOf(1.1)));
        assertEquals(Double.valueOf(3.1), pa.get().get("a").get(0), 0.0005);
        pa.put(new Thing("a", Double.valueOf( -1)));
        assertEquals(Double.valueOf(2.1), pa.get().get("a").get(0), 0.0005);
    }

}
