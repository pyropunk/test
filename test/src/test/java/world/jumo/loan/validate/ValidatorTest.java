/*
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * ValidatorTest.java
 * Copyright 2018 Medical EDI Services (PTY) Ltd. All rights reserved.
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 */

package world.jumo.loan.validate;

import static org.junit.Assert.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.math.BigDecimal;
import java.nio.CharBuffer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.Test;

/**
 *
 */
@SuppressWarnings("static-method")
class ValidatorTest {

    /**
     * Test method for {@link world.jumo.loan.validate.Validator#getAggregates()}.
     */
    @Test
    void testGetAggregates() {

        assertEquals(2, Validator.getAggregates().size());
    }

    /**
     * Test method for {@link world.jumo.loan.validate.Validator#getConverter()}.
     */
    @Test
    void testGetConverter() {

        assertEquals(new NetworkProductMonthGroup("a", "b", Month.AUGUST),
            Validator.getConverter().apply(new Loan("1", "a", LocalDate.of(1900, 8, 1), "b", new BigDecimal("100.0"))));
    }

    /**
     * Test method for {@link world.jumo.loan.validate.Validator#main(java.lang.String[])}.
     * @throws IOException 
     */
    @Test
    void testMain() throws IOException {

        Files.deleteIfExists(Paths.get("./target/Output.csv"));
        assertFalse(Files.exists(Paths.get("./target/Output.csv")));
        Validator.main(new String[] {
            "./src/test/resources/Loans.csv", "./target/Output.csv"
        });
        assertTrue(Files.exists(Paths.get("./target/Output.csv")));

        // cover logging
        Validator.main(new String[] {
            "./src/test/resources/EX.csv", "./target/WHY.csv"
        });

        // cover no args
        Validator.main(new String[0] );
    }


    /**
     * Test method for {@link world.jumo.loan.validate.Validator#marshallResult(world.jumo.loan.validate.NetworkProductMonthGroup, java.util.List)}.
     */
    @Test
    void testMarshallResult() {

        assertEquals("a,b,'AUG'", Validator.marshallResult(new NetworkProductMonthGroup("a", "b", Month.AUGUST), Arrays.asList()));
        assertEquals("a,b,'JAN',1", Validator.marshallResult(new NetworkProductMonthGroup("a", "b", Month.JANUARY), Arrays.asList(BigDecimal.ONE)));
        assertEquals("a,b,'AUG',0", Validator.marshallResult(new NetworkProductMonthGroup("a", "b", Month.AUGUST), Arrays.asList(BigDecimal.ZERO)));
        assertEquals("a,b,'AUG',1,0,1",
            Validator.marshallResult(new NetworkProductMonthGroup("a", "b", Month.AUGUST), Arrays.asList(BigDecimal.ONE, BigDecimal.ZERO, BigDecimal.ONE)));
    }

    /**
     * Test method for {@link world.jumo.loan.validate.Validator#unmarshallLoan(java.lang.String)}.
     */
    @Test
    void testUnmarshallLoan() {

        assertEquals("1", Validator.unmarshallLoan("1,a,'01-AUG-1900',b,100.0").getMsisdn());
        assertEquals("a", Validator.unmarshallLoan("1,a,'01-AUG-1900',b,100.0").getNetwork());
        assertEquals(LocalDate.of(1900, 8, 1), Validator.unmarshallLoan("1,a,'01-AUG-1900',b,100.0").getDate());
        assertEquals("b", Validator.unmarshallLoan("1,a,'01-AUG-1900',b,100.0").getProduct());
        assertEquals(new BigDecimal("100.0"), Validator.unmarshallLoan("1,a,'01-AUG-1900',b,100.0").getAmount());
    }

    /**
     * Test method for {@link world.jumo.loan.validate.Validator#Validator()}.
     */
    @Test
    void testValidator() {

        Validator v = new Validator();
        assertNotNull(v.aggregator);
    }

    /**
     * Test method for {@link world.jumo.loan.validate.Validator#read(java.io.BufferedReader)}.
     * 
     * @throws IOException
     */
    @Test
    void testRead() throws IOException {

        StringBuilder b = new StringBuilder();
        b.append("headers").append(System.lineSeparator());
        b.append("1,a,'01-AUG-1900',b,100.0").append(System.lineSeparator());
        b.append("1,b,'01-AUG-1900',b,100.0").append(System.lineSeparator());
        b.append("1,a,'01-AUG-1900',c,100.0").append(System.lineSeparator());
        b.append("1,a,'01-AUG-1900',b,100.0").append(System.lineSeparator());
        Validator v = new Validator();
        try (ByteArrayInputStream a = new ByteArrayInputStream(b.toString().getBytes());
            Reader r = new InputStreamReader(a);
            BufferedReader br = new BufferedReader(r)) {
            v.read(br);
            assertEquals(3, v.aggregator.get().size());
        }

        try (ByteArrayOutputStream a = new ByteArrayOutputStream();
            Writer w = new OutputStreamWriter(a);
            BufferedWriter bw = new BufferedWriter(w)) {
            v.write(bw);
            bw.flush();
            assertThat(a.toString(), CoreMatchers.containsString("a,b,'AUG',200.0,2"));
            assertThat(a.toString(), CoreMatchers.containsString("a,c,'AUG',100.0,1"));
            assertThat(a.toString(), CoreMatchers.containsString("b,b,'AUG',100.0,1"));
        }
    }

}
