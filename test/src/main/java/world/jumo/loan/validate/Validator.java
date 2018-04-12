/*
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * Validator.java
 * Copyright 2018 Medical EDI Services (PTY) Ltd. All rights reserved.
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 */

package world.jumo.loan.validate;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The Validator reads a file of {@link Loan}s from a CSV file, groups by Network, Product and Month, calculates the sum and count of the records and then
 * outputs a file of the group as well as the two aggregates - also in CSV format.
 */
public class Validator {

    private static final Logger LOG = Logger.getLogger(Validator.class.getName());

    /**
     * All aggregate classes have to return the same type.
     * 
     * @return a list of aggregating classes
     */
    static List<Class<? extends Aggregate<BigDecimal>>> getAggregates() {

        return Arrays.asList(Sum.class, Count.class);
    }

    /**
     * To change the "group by" clause, create a new class with fields from {@link Loan}. Change this method to convert a {@link Loan} to the new class. Return
     * the {@link Function} from this method.
     * 
     * @return the function that extracts the values from the {@link Loan} and turns it into a {@link NetworkProductMonthGroup}
     */
    static Function<Loan, NetworkProductMonthGroup> getConverter() {

        return (t) -> new NetworkProductMonthGroup(t.getNetwork(), t.getProduct(), t.getDate().getMonth());
    }

    /**
     * Main program entry point. Reads a file "Lonas.csv" if the name is not supplied as the first argument. Writes a file "Output.csv" if the name is not
     * supplied as the second argument. Input file has the format of MSISDN,'Network Name','Date in yyyy-MMM-dd','Product name',amount. Output file has the
     * format 'Network Name','Product','Month as MMM',total,count.
     * 
     * @param args - args[0]: input filename, args[1]: output filename
     */
    public static void main(String[] args) {

        final Path in = Paths.get(args.length > 0 ? args[0] : "./Loans.csv");
        final Path out = Paths.get(args.length > 1 ? args[1] : "./Output.csv");
        Validator v = new Validator();
        try (BufferedReader reader = Files.newBufferedReader(in, StandardCharsets.UTF_8);
            BufferedWriter writer = Files.newBufferedWriter(out, StandardCharsets.UTF_8, StandardOpenOption.CREATE, StandardOpenOption.WRITE,
                StandardOpenOption.TRUNCATE_EXISTING)) {
            v.read(reader);
            v.write(writer);
        }
        catch (IOException e) {
            LOG.log(Level.SEVERE, e, () -> String.format("Failed to aggreate from file %s to %s", in, out));
        }
    }

    /**
     * Convert an aggregation result into a CSV string.
     * 
     * @param group - the key representing the "group by" clause
     * @param agg - the list of aggregate values
     * @return a CSV string listing the values of the key and then the values of the aggregates
     */
    static String marshallResult(NetworkProductMonthGroup group, List<BigDecimal> agg) {

        final StringBuilder b = new StringBuilder();
        b.append(String.format("%s,%s,'%.3s'", group.getNetwork(), group.getProduct(), group.getMonth()));
        for (BigDecimal r : agg) {
            b.append(",").append(r);
        }
        return b.toString();
    }

    /**
     * Convert a CSV string to a {@link Loan}
     * 
     * @param csv - comma separated fields representing a {@link Loan}
     * @return a newly created {@link Loan} object
     */
    static Loan unmarshallLoan(String csv) {

        final String[] fields = csv.split(",");
        return new Loan(fields[0], fields[1], LocalDate.parse(fields[2], DateTimeFormatter.ofPattern("''dd-MMM-yyyy''")), fields[3], new BigDecimal(fields[4]));
    }

    private ProductAggregator<Loan, NetworkProductMonthGroup, BigDecimal> aggregator;

    /**
     * the Constructor creates a class of ProductAggregator
     */
    public Validator() {

        this.aggregator = new ProductAggregator<>(getConverter(), getAggregates());
    }

    /**
     * Read the Strings from a {@link BufferedReader}, convert them to {@link Loan}s and add them to the aggregator.
     * 
     * @param reader - containing a {@link Loan}s in string format
     * @throws IOException when the file cannot be read
     */
    public void read(BufferedReader reader) throws IOException {

        // read the header
        String line = reader.readLine();
        line = reader.readLine();
        while (line != null) {
            aggregator.put(unmarshallLoan(line));
            line = reader.readLine();
        }
    }

    /**
     * Write the aggregation results to a {@link BufferedWriter}
     * 
     * @param writer - will contain the aggregation results in string format
     * @throws IOException when the file cannot be written
     */
    public void write(BufferedWriter writer) throws IOException {

        writer.write("Network,Product,Month,Sum,Count");
        writer.newLine();
        for (Map.Entry<NetworkProductMonthGroup, List<BigDecimal>> e : aggregator.get().entrySet()) {
            writer.write(Validator.marshallResult(e.getKey(), e.getValue()));
            writer.newLine();
        }
    }
}
