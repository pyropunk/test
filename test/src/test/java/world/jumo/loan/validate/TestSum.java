package world.jumo.loan.validate;

/**
 * Test class for ProductAggregatorTest
 */
public class TestSum implements Aggregate<Double> {

    Double sum = Double.valueOf(0.0);

    public TestSum() {

        super();
    }

    @Override
    public void put(Double d) {

        sum = Double.valueOf(sum.doubleValue() + d.doubleValue());
    }

    @Override
    public Double get() {

        return sum;
    }
}
