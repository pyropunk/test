/*
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * Loan.java
 * Copyright 2018 Medical EDI Services (PTY) Ltd. All rights reserved.
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 */

package world.jumo.loan.validate;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * The <code>Loan</code> class defines a loan that was granted.
 */
public class Loan implements Product<BigDecimal> {

    String msisdn;
    String network;
    LocalDate date;
    String product;
    BigDecimal amount;

    public Loan(String msisdn, String network, LocalDate date, String product, BigDecimal amount) {

        super();
        this.msisdn = msisdn;
        this.network = network;
        this.date = date;
        this.product = product;
        this.amount = amount;
    }

    public BigDecimal getAmount() {

        return amount;
    }

    public LocalDate getDate() {

        return date;
    }

    public String getMsisdn() {

        return msisdn;
    }

    public String getNetwork() {

        return network;
    }

    public String getProduct() {

        return product;
    }

    public void setAmount(BigDecimal amount) {

        this.amount = amount;
    }

    public void setDate(LocalDate date) {

        this.date = date;
    }

    public void setMsisdn(String msisdn) {

        this.msisdn = msisdn;
    }

    public void setNetwork(String network) {

        this.network = network;
    }

    public void setProduct(String product) {

        this.product = product;
    }

}
