/*
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * Product.java
 * Copyright 2018 Medical EDI Services (PTY) Ltd. All rights reserved.
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 */

package world.jumo.loan.validate;

/**
 * A <code>Product</code> is an item that has a value of a type R.
 * 
 * @param <T> the type for the amount of the product. usually numeric
 */
public interface Product<T> {

    /**
     * @return the amount of the product
     */
    T getAmount();
}
