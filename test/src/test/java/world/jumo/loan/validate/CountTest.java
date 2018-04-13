/*
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * CountTest.java
 * Copyright 2018 Medical EDI Services (PTY) Ltd. All rights reserved.
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 */

package world.jumo.loan.validate;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;

/**
 * Tests the Count class
 */
@SuppressWarnings("static-method")
class CountTest {

    /**
     * Test method for {@link world.jumo.loan.validate.Count#Count()}.
     */
    @Test
    void testCount() {

        Count c = new Count();
        assertEquals(BigDecimal.ZERO, c.get());
    }

    /**
     * Test method for {@link world.jumo.loan.validate.Count#get()}.
     */
    @Test
    void testGet() {

        Count c = new Count();
        c.put(new BigDecimal("123.456"));
        assertEquals(BigDecimal.ONE, c.get());
        c.put(new BigDecimal("123.456"));
        assertEquals(new BigDecimal(2), c.get());
        c.put(new BigDecimal("-123.456"));
        assertEquals(new BigDecimal(3), c.get());
        c.put(new BigDecimal("0"));
        assertEquals(new BigDecimal(4), c.get());
    }

}
