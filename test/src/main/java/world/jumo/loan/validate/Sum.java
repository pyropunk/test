/*
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * Sum.java
 * Copyright 2018 Medical EDI Services (PTY) Ltd. All rights reserved.
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 */

package world.jumo.loan.validate;

import java.math.BigDecimal;

/**
 * The Sum class defines an {@link Aggregate} that sums BigDecimal values.
 */
public class Sum implements Aggregate<BigDecimal> {

    BigDecimal result = BigDecimal.ZERO;

    public Sum() {

        super();
    }

    public void put(BigDecimal value) {

        result = result.add(value);
        
    }

    public BigDecimal get() {

        return result;
    }

}
