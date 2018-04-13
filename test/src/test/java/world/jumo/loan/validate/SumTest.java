/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * SumTest.java
 *
 * Copyright 2018 Medical EDI Services (PTY) Ltd. All rights reserved.
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

package world.jumo.loan.validate;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;


/**
 * Test the Sum class
 */
@SuppressWarnings("static-method")
class SumTest {

    /**
     * Test method for {@link world.jumo.loan.validate.Sum#Sum()}.
     */
    @Test
    void testSum() {

        Sum s = new Sum();
        assertEquals(BigDecimal.ZERO, s.get());
    }

    /**
     * Test method for {@link world.jumo.loan.validate.Sum#get()}.
     */
    @Test
    void testGet() {

        Sum s = new Sum();
        s.put(new BigDecimal("123.456"));
        assertEquals(new BigDecimal("123.456"), s.get());
        s.put(new BigDecimal("123.456"));
        assertEquals(new BigDecimal("246.912"), s.get());
        s.put(new BigDecimal("-123.456"));
        assertEquals(new BigDecimal("123.456"), s.get());
        s.put(new BigDecimal("0"));
        assertEquals(new BigDecimal("123.456"), s.get());
        s.put(new BigDecimal("1"));
        assertEquals(new BigDecimal("124.456"), s.get());
    }

}
