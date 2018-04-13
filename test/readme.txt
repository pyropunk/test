Task

Given the below CSV from our accounting department calculate the aggregate loans by the tuple of (Network, Product, Month) 
with the total currency amounts and counts and output into a file CSV file Output.csv

Loans.csv
MSISDN,Network,Date,Product,Amount
27729554427,'Network 1','12-Mar-2016','Loan Product 1',1000.00
27722342551,'Network 2','16-Mar-2016','Loan Product 1',1122.00
27725544272,'Network 3','17-Mar-2016','Loan Product 2',2084.00
27725326345,'Network 1','18-Mar-2016','Loan Product 2',3098.00
27729234533,'Network 2','01-Apr-2016','Loan Product 1',5671.00
27723453455,'Network 3','12-Apr-2016','Loan Product 3',1928.00
27725678534,'Network 2','15-Apr-2016','Loan Product 3',1747.00
27729554427,'Network 1','16-Apr-2016','Loan Product 2',1801.00

Solution considerations

1) The expectation is a file called Output.csv with a line detailing the totals by the tuple of (Network, Product, Month) 
summing the amount and count of loans.

2) A readme.txt detailing the project, usage and assumptions youʼve made as well as the choices around your language, plugins 
and 3rd party libraries.

3) Follow proper coding conventions, object orientated design.

4) Detail performance and scaling considerations.

5) Implement this as you would in a production environment.


Solution:

I chose Java 1.8 with Maven to build my project. The only 3rd party library that I was jacoco to check the unit test coverage.

Assumptions I made:
1) the date will be in dd-MMM-yyyy format
2) the file is always well-formed 

I coded two solutions, Validator.java is written in an OO style and Streams.java is written in a functional style and uses java 1.8 streams.  

For the OO solution I created a generic aggregation mechanism.
Aggregate is the interface for all aggregation classes
Sum and Count implement Aggregate.
Product is the interface for all products. 
Loan is the only implementing class at the moment.
NetworkProductMonthGroup is class that represents the group by clause and a Function copies the values from the Loan POJO.
Aggregator is an interface that defines a put function that receives a type and a get function that outputs keys mapped to a list of aggregates.
ProductAggregator is the class that aggregates over Products as they are put into the ProductAggregator.
The get method of ProductAggregator loops over the result map and converts the internal representation from and Aggregate<Type> to the Type.
This has a small performance impact if the list of grouped values is long.
Validator then creates all the necessary Functions, Arrays and (un)marshalling functions.
I chose to code the marshalling and unmarshalling in the main class instead of in a Factory because the format is file dependent.
The main method reads 2 parameters of the file names (and defaults the given names),
reads the data line by line and adds them to the Aggregator,
then writes the output file by reading the result from the Aggregator.
The OO solution iterates over the string sin the file only once, but there are two loops that iterate over the result map.
If the result list is large, this double loop will have a performance impact.

For the streams solution I used the groupingBy clause to insert the Loan objects
into a TreeMap that uses a Comparator on the 3 fields of the Loan.
I created an internal class SumCount to aggregate the Sum and the Count at the same time.
Afterwards the TeeMap is streamed to file using a conversion function.
The Streams only iterates over the input once and over the output once.
This solution should therefore be faster than the OO solution, but it is not as maintainable.

To compile:
mvn clean install

To run:
java -cp target/validate-0.0.1-SNAPSHOT.jar world.jumo.loan.validate.Validator ./Loans.csv ./Output.csv

 