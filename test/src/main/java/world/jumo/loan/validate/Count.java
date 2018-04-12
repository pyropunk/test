/*
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * Count.java
 * Copyright 2018 Medical EDI Services (PTY) Ltd. All rights reserved.
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 */

package world.jumo.loan.validate;

import java.math.BigDecimal;

/**
 * The Count class defines an {@link Aggregate} that count BigDecimal values without looking at the actual value.
 */
public class Count implements Aggregate<BigDecimal> {

    BigDecimal result = BigDecimal.ZERO;

    public Count() {

        super();
    }

    @Override
    public void put(BigDecimal value) {

        result = result.add(BigDecimal.ONE);
    }

    @Override
    public BigDecimal get() {

        return result;
    }

}
