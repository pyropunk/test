/*
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * ProductAggregator.java
 * Copyright 2018 Medical EDI Services (PTY) Ltd. All rights reserved.
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 */

package world.jumo.loan.validate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * The <code>ProductAggregator</code> receives individual {@link Product} subclasses and aggregates them using a {@link Product} to Key converter.
 * A list of {@link Aggregate} classes is supplied will be instantiated and populated with the relevant aggregate value.
 * 
 * @param <T> the type of {@link Product}
 * @param <K> the type of the key - effectively a group-by clause that contains a subset of the fields in the {@link Product}
 * @param <R> the numeric type of the aggregation result
 */
public class ProductAggregator<T extends Product<R>, K, R> implements Aggregator<T, K, R> {

    Function<T, K> converter;
    List<Class<? extends Aggregate<R>>> aggregates;

    Map<K, List<Aggregate<R>>> grouped = new HashMap<>();

    /**
     * The Constructor.<br>
     * <br>
     * <b>To use:</b>
     * 
     * <pre>
     * class Thing extends Product&lt;Double&gt; {
     * 
     *     Double amount;
     *     String value;
     * 
     *     &#64;Override
     *     Double getAmount() {
     * 
     *         return amount;
     *     }
     * 
     *     String getValue() {
     * 
     *         return value;
     *     }
     * }
     * 
     * class Sum extends Aggregate&lt;Double&gt; {
     * 
     *     Double sum = Double.valueOf(0.0);
     * 
     *     &#64;Override
     *     void put(Double d) {
     * 
     *         sum = Double.valueOf(sum.doubleValue() + d.doubleValue());
     *     }
     * 
     *     &#64;Override
     *     Double get() {
     * 
     *         return sum;
     *     }
     * }
     * 
     * List<Thing> things = Arrays.asList(new Thing(), new Thing());
     * ProductAggregator pa = new ProductAggregator<>(t -> t.getValue(), Arrays.asList(Sum.class));
     * for (Thing t : things) {
     *     pa.add(t);
     * }
     * Map<String, List<Double>> aggregated = pa.get();
     * </pre>
     * 
     * @param converter a function that converts a T to a K
     * @param aggregates a List of Aggregate classes
     */
    public ProductAggregator(Function<T, K> converter, List<Class<? extends Aggregate<R>>> aggregates) {

        this.converter = converter;
        this.aggregates = aggregates;
    }

    @Override
    public Map<K, List<R>> get() {

        final Map<K, List<R>> results = new HashMap<>();

        for (Map.Entry<K, List<Aggregate<R>>> entry : grouped.entrySet()) {

            final List<R> r = new ArrayList<>();
            for (Aggregate<R> a : entry.getValue()) {
                r.add(a.get());
            }
            results.put(entry.getKey(), r);
        }
        return results;
    }

    @Override
    public void put(T value) {

        try {

            final K key = converter.apply(value);

            if ( !grouped.containsKey(key)) {

                final List<Aggregate<R>> list = new ArrayList<>();
                for (Class<? extends Aggregate<R>> ac : aggregates) {
                    final Aggregate<R> a = ac.newInstance();
                    a.put(value.getAmount());
                    list.add(a);
                }
                grouped.put(key, list);
            }
            else {
                List<Aggregate<R>> list = grouped.get(key);
                for (Aggregate<R> a : list) {
                    a.put(value.getAmount());
                }
            }
        }
        catch (InstantiationException | IllegalAccessException a) {
            throw new SecurityException(a);
        }
    }
}
