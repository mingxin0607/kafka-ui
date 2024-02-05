

### Lines of code
Counted by `cloc` tool on Linux.


```
---------------------------------------------------------------------------------------
Language                             files          blank        comment           code
---------------------------------------------------------------------------------------
TypeScript                            6481          42985         203840         325842
JSON                                  1810            107              0         248555
Markdown                              1330          61984              0         142141
Java                                   416           5195            693          33570
XML                                     94             23             50          25261
Python                                  49           4370           7926          23036
YAML                                   198           1615            243          19008
HTML                                    14           4659          11214          18380
CSS                                     64            281             54           8626
Bourne Shell                           128            487            207           2919
Maven                                    5             44             10           1303
Sass                                     5            129              0            684
ANTLR Grammar                            1             86              3            532
Windows Module Definition                5             83              0            451
Lisp                                     2             42             38            258
C#                                       1             55              9            186
DOS Batch                                3             36              0            156
SVG                                      6              1              2            131
make                                     6             44             31            130
PHP                                      1             13             19            124
Protocol Buffers                         6             19              1             82
Bourne Again Shell                       3             13              1             51
TOML                                     1              5              0             36
Dockerfile                               3             12              6             24
SQL                                      1              2              0             22
C++                                      2             12             19             20
Nix                                      1              1              0             19
CoffeeScript                             1              1              0              0
---------------------------------------------------------------------------------------
SUM:                                 27946         396566         820149        4112984
---------------------------------------------------------------------------------------
```


## Functional testing and partition testing

### Functional Testing

Functional Testing is a type of software testing that validates the software system against the functional requirements/specifications. The purpose of Functional tests is to test each function of the software application, by providing appropriate input, verifying the output against the Functional requirements.

Functional testing mainly involves black box testing and it is not concerned about the source code of the application. This testing checks User Interface, APIs, Database, Security, Client/Server communication and other functionality of the Application Under Test. The testing can be done either manually or using automation.

#### Purpose of functional testing
- Ensuring Correct Functionality

Systematic functional testing is essential to ensure that the software functions correctly according to its specifications and requirements. This helps in delivering a product that meets user expectations.

- Comprehensive Coverage

Systematic functional testing aims to provide comprehensive coverage of the entire application. This includes testing individual units, ensuring proper integration, and validating the system as a whole.

- Building User Confidence

Thorough functional testing builds confidence among users and stakeholders, assuring them that the software not only works but works reliably under various conditions.


### Partition Testing

Partition Testing is a software testing technique that divides the input data of a software unit into partitions of equivalent data from which test cases can be derived. In principle, test cases are designed to cover each partition at least once. This technique tries to define test cases that uncover classes of errors, thereby reducing the total number of test cases that must be developed. An advantage of this approach is reduction in the time required for testing software due to lesser number of test cases. 

#### Purpose of partition testing
- Efficient Testing

Partition testing allows for efficient testing by dividing the input space into equivalence classes. This reduces the number of test cases needed while ensuring that each class is adequately represented.

- Identifying Critical Input Scenarios

Not all possible inputs need to be tested individually. Partition testing helps identify critical input scenarios, allowing testers to focus on representative values that are likely to reveal defects.

- Identifying Defects Associated with Input Classes

Defects often cluster around specific input classes. Partition testing aids in identifying and addressing issues associated with different input partitions, contributing to improved software reliability.

### The feature for partition testing

We chose the class `Int32Serde` implemented at "kafka-ui-api/src/main/java/com/provectus/kafka/ui/serdes/builtin/Int32Serde.java".
It deals with serialization and deserialization of a 32-bit integer.
It implemented interface `BuiltInSerde` at "kafka-ui-api/src/main/java/com/provectus/kafka/ui/serdes/BuiltInSerde.java", which extends another interface `Serde` at "kafka-ui-serde-api/src/main/java/com/provectus/kafka/ui/serde/api/Serde.java".

### New partition tests
The input space of this feature is 32-bit integers in the form of string.
We partitioned the input space into 4 parts based on the nature of integers: zero, positive integer[1, 2147483647], negative integer[-2147483648, -1], invalid input (including all integers beyond the scope [-2147483648, 2147483647] and all invalid string that does not represent an integer).

We chose number "1234" and "2147483647" to represent the positive integer partition, number "-2147483648" to represent the negative partition and chose "null" and "2147483648" to represent invalid input. 
We included both boundaries and random representative values to cover all partitions and make sure the feature is stable at boundaries.
For valid input, after serialization and deserialization, the value should be the same. For invalid input, an exception should be thrown.

New test cases and their documents were included in "kafka-ui-api/src/test/java/com/provectus/kafka/ui/serdes/builtin/Int32SerdeTest.java". To run all JUnit tests, use the command "mvn clean test".