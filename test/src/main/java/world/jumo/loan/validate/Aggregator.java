/*
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * Aggregator.java
 * Copyright 2018 Medical EDI Services (PTY) Ltd. All rights reserved.
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 */

package world.jumo.loan.validate;

import java.util.List;
import java.util.Map;

/**
 * The <code>Aggregator</code> interface is used to aggregate over a list of values.
 * 
 * @param <T> the type of object being aggregated
 * @param <K> the type for the key of the aggregation operation. Similar to a group by clause
 * @param <R> the result type
 */
public interface Aggregator<T, K, R> {

    /**
     * @return a map of grouped values with all the aggregates
     */
    Map<K, List<R>> get();

    /**
     * The put method is used to add a value to the aggregator
     * 
     * @param value
     */
    void put(T value);
}
