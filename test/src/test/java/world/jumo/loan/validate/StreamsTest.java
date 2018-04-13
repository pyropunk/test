/*
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * StreamsTest.java
 * Copyright 2018 Medical EDI Services (PTY) Ltd. All rights reserved.
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 */

package world.jumo.loan.validate;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.junit.jupiter.api.Test;

/**
 * Test the Streams class
 */
@SuppressWarnings("static-method")
class StreamsTest {

    /**
     * Test method for {@link world.jumo.loan.validate.Streams#main(java.lang.String[])}.
     * 
     * @throws IOException
     */
    @Test
    void testMain() throws IOException {

        Files.deleteIfExists(Paths.get("./target/Streams.csv"));
        assertFalse(Files.exists(Paths.get("./target/Streams.csv")));
        Streams.main(new String[] {
            "./src/test/resources/Loans.csv", "./target/Streams.csv"
        });
        assertTrue(Files.exists(Paths.get("./target/Streams.csv")));

        // cover logging
        Streams.main(new String[] {
            "./src/test/resources/EX.csv", "./target/WHY.csv"
        });

        // cover no args
        Streams.main(new String[0] );
   }
}
