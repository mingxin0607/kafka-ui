
---

# Part III: Structural (White Box) Testing

## 1. Structural Testing

Structural testing, also known as white-box testing, is a testing methodology that is based on the internal paths, structure, and implementation of the software. Unlike black-box testing, which focuses on the external functionality and user interface, structural testing dives into the "white" or transparent box, which is the code itself.

### Importance of Structural Testing

Structural testing is critical for several reasons:

1. **Comprehensive Coverage:** It ensures that all the possible paths within the codebase are executed at least once. This is crucial for uncovering hidden bugs that may not be immediately apparent through functional testing.

2. **Logic Verification:** By examining the logical flow of the application, structural testing verifies the correctness of the code logic, which is vital for the application to behave as expected.

3. **Early Detection of Defects:** Since structural testing involves testing the code during the development phase, defects can be detected early, reducing the cost and effort required to fix them later.

4. **Optimization Opportunities:** It provides insights into the performance and optimization opportunities for the code, as testers can see which parts of the code are executed frequently and which are not.

5. **Security Assessments:** Structural testing can uncover security vulnerabilities within the code, such as buffer overflows or logic flaws that could be exploited.

### Example of Structural Testing

Consider a simple Java method that calculates the factorial of a number:

```java
public int factorial(int number) {
    int result = 1;
    for (int factor = 2; factor <= number; factor++) {
        result *= factor;
    }
    return result;
}
A structural test case for this method would not only test it with different input values (like a black-box test) but also ensure that the loop inside the method is executed the correct number of times for each input. For instance, input 5 should cause the loop to execute five times, multiplying the result by 2, 3, 4, and 5.   





## 2. Structural Testing with Coverage Tool

### 2.1 Use `Jacoco` in maven project
### 2.1.1 Setup

To use `Jacoco` coverage tool in a maven project, we could add the following dependency in each "pom.xml" (We have already done so to our project).
```xml
<build>
    <plugins>
        <!-- ... other plugins ... -->
        <plugin>
            <groupId>org.jacoco</groupId>
            <artifactId>jacoco-maven-plugin</artifactId>
            <version>0.8.7</version> <!-- Use the latest version available -->
            <executions>
                <execution>
                    <id>prepare-agent</id>
                    <goals>
                        <goal>prepare-agent</goal>
                    </goals>
                </execution>
                <execution>
                    <id>report</id>
                    <goals>
                        <goal>report</goal>
                    </goals>
                </execution>
            </executions>
        </plugin>
        <!-- ... other plugins ... -->
    </plugins>
</build>
```
#### 2.1.2 Build & Run Tests

1. To run tests with Jacoco and generate a report in html format, run the following command under folder "kafka-ui":
```bash
mvn clean test jacoco:report
```
2. You could find the report "index.html" under path "kafka-ui-api/target/site/jacoco".
Open "index.html" in a web browser to read the report.

#### 2.1.3 Troubleshooting
1. If you use Linux System as a non-root user, please remember to add "sudo" before each command.
2. If you get stuck while downloading `jacoco-maven-plugin`, try running:
```bash
rm -rf ~/.m2/repository/org/jacoco/jacoco-maven-plugin/0.8.7/
```
Then run:
```bash
mvn clean test jacoco:report
```

### 2.2 Analysis of Original Coverage
Original Report is under folder "jacoco-reports/report-20240217".

## 3. New Test Cases Based on Test Report

### 3.1 Overview 
We added new test cases for three Test classes:

`kafka-ui-api/src/test/java/com/provectus/kafka/ui/KafkaTopicCreateTests.java`

`kafka-ui-api/src/test/java/com/provectus/kafka/ui/controller/ApplicationConfigControllerTest.java`

`kafka-ui-api/src/test/java/com/provectus/kafka/ui/service/ksql/response/ResponseParserTest.java`

Through these new test cases, we reduced total missed lines of code from 8288 to 8236.

New coverage report could be found at "jacoco-reports/report-20240218".

Here is the comparison on the final line between the old report and new report.

__Old Report__
| Class                                               | Instructions Missed | Coverage | Lines Missed | 
|-----------------------------------------------------|---------------------|----------|-----------------|
| Total                                               | 47,036 of 82,073    | 42%      | 8,288 of 14,678   | 

__New Report__
| Class                                               | Instructions Missed | Coverage | Lines Missed |  
|-----------------------------------------------------|---------------------|----------|-----------------|
| Total                                               | 46,822 of 82,073    | 42%      | 8,236 of 14,678  | 


### 3.2 New Test Cases
#### 3.2.1 KafkaTopicCreateTests.java

We added these new test cases:
```Java
/*
 * Checks if a 400 status code is returned when an invalid replication factor is provided.
*/
  @Test
  void shouldReturn400IfInvalidReplicationFactor() {
    TopicCreationDTO topicCreation = new TopicCreationDTO()
            .replicationFactor(0)  // Invalid replication factor
            .partitions(3)
            .name(UUID.randomUUID().toString());

    webTestClient.post()
            .uri("/api/clusters/{clusterName}/topics", LOCAL)
            .bodyValue(topicCreation)
            .exchange()
            .expectStatus()
            .isBadRequest();
  }

/*
 * Checks if a 400 status code is returned when an invalid number of partitions is provided
*/
  @Test
  void shouldReturn400IfInvalidPartitions() {
    TopicCreationDTO topicCreation = new TopicCreationDTO()
            .replicationFactor(1)
            .partitions(0)  // Invalid number of partitions
            .name(UUID.randomUUID().toString());

    webTestClient.post()
            .uri("/api/clusters/{clusterName}/topics", LOCAL)
            .bodyValue(topicCreation)
            .exchange()
            .expectStatus()
            .isBadRequest();
  }
```

These test cases mainly improve coverage of class "GlobalErrorWebExceptionHandler".

**Coverage report before new test cases**

| Element                                               | Missed Instructions | Cov. | Missed Lines |
|-------------------------------------------------------|---------------------|------|--------------|
| **Total**                                             | 103 of 299          | 65%  | 24           |
| render(ResponseStatusException, ServerRequest)        | 48                  | 0%   | 11           |
| renderDefault(Throwable, ServerRequest)               | 41                  | 0%   | 10           |
| renderErrorResponse(ServerRequest)                    | 1125                | 69%  | 2            |
| render(WebExchangeBindException, ServerRequest)       | 352                 | 94%  | 1            |
| render(CustomBaseException, ServerRequest)            | 44                  | 100% | 11           |
| extractFieldErrorMsg(FieldError)                      | 20                  | 100% | 1            |
| lambda$render$1(Map.Entry)                            | 17                  | 100% | 4            |
| GlobalErrorWebExceptionHandler(ErrorAttributes...)   | 12                  | 100% | 3            |
| coalesce(Object[])                                   | 8                   | 100% | 1            |
| getRoutingFunction(ErrorAttributes)                   | 5                   | 100% | 1            |
| requestId(ServerRequest)                              | 5                   | 100% | 1            |
| lambda$render$0(FieldError)                           | 5                   | 100% | 1            |
| currentTimestamp()                                    | 3                   | 100% | 1            |



**Coverage report after new test cases**

| Element                                               | Missed Instructions | Cov. | Missed Branches | Cov. | Missed | Cxty | Missed Lines | Missed Methods | Missed Classes |
|-------------------------------------------------------|---------------------|------|-----------------|------|--------|------|--------------|----------------|----------------|
| **Total**                                             | 103 of 299          | 65%  | 3 of 8          | 62%  | 5      | 17   | 24           | 72             | 2              |
| render(WebExchangeBindException, ServerRequest)       | 352                 | 94%  | 11              | 50%  | 1      | 2    | 1            | 20             | 0              |
| render(ResponseStatusException, ServerRequest)        | 48                  | 100% | n/a             | 0    | 1      | 0    | 11           | 0              | 1              |
| render(CustomBaseException, ServerRequest)            | 44                  | 100% | n/a             | 0    | 1      | 0    | 11           | 0              | 1              |
| renderDefault(Throwable, ServerRequest)               | 41                  | 100% | n/a             | 0    | 1      | 0    | 10           | 0              | 1              |
| renderErrorResponse(ServerRequest)                    | 36                  | 100% | 6               | 100% | 0      | 4    | 0            | 8              | 0              |
| extractFieldErrorMsg(FieldError)                      | 20                  | 100% | n/a             | 0    | 1      | 0    | 1            | 0              | 1              |
| lambda$render$1(Map.Entry)                            | 17                  | 100% | n/a             | 0    | 1      | 0    | 4            | 0              | 1              |
| GlobalErrorWebExceptionHandler(ErrorAttributes...)   | 12                  | 100% | n/a             | 0    | 1      | 0    | 3            | 0              | 1              |
| coalesce(Object[])                                   | 8                   | 100% | n/a             | 0    | 1      | 0    | 1            | 0              | 1              |
| getRoutingFunction(ErrorAttributes)                   | 5                   | 100% | n/a             | 0    | 1      | 0    | 1            | 0              | 1              |
| requestId(ServerRequest)                              | 5                   | 100% | n/a             | 0    | 1      | 0    | 1            | 0              | 1              |
| lambda$render$0(FieldError)                           | 5                   | 100% | n/a             | 0    | 1      | 0    | 1            | 0              | 1              |
| currentTimestamp()                                    | 3                   | 100% | n/a             | 0    | 1      | 0    | 1            | 0              | 1              |


#### 3.2.2 ResponseParserTest.java

New test cases:

```java

    // Verifies that the parseSelectHeadersString method correctly parses headers with different data types.
  @Test
  void parsesSelectHeaderWithDifferentDataTypes() {
    assertThat(ResponseParser.parseSelectHeadersString("col1 INT, col2 STRING, col3 DOUBLE"))
        .containsExactly("col1 INT", "col2 STRING", "col3 DOUBLE");

    assertThat(ResponseParser.parseSelectHeadersString("name STRING, age INT, active BOOLEAN"))
        .containsExactly("name STRING", "age INT", "active BOOLEAN");
  }

    // Verifies that the parseSelectHeadersString method correctly parses headers with different nested structures.
  @Test
  void parsesSelectHeaderWithDifferentStructures() {
    assertThat(ResponseParser.parseSelectHeadersString(
        "`struct1` STRUCT<`nested1` STRING, `nested2` INT>, `struct2` STRUCT<`nested3` DOUBLE>"))
        .containsExactly(
            "`struct1` STRUCT<`nested1` STRING, `nested2` INT>",
            "`struct2` STRUCT<`nested3` DOUBLE>"
        );

    assertThat(ResponseParser.parseSelectHeadersString(
        "`parent` STRUCT<`child1` INT, `child2` STRING>, `nested` STRUCT<`inner1` DOUBLE>"))
        .containsExactly(
            "`parent` STRUCT<`child1` INT, `child2` STRING>",
            "`nested` STRUCT<`inner1` DOUBLE>"
        );
  }

// Verifies that the parseSelectResponse method correctly parses and returns a response table for schema headers.
  @Test
  void parseSelectResponseWithSchemaHeader() {
    // Prepare JSON node with "header" field for schema
    ObjectNode jsonNode = JsonNodeFactory.instance.objectNode();
    jsonNode.set("header", JsonNodeFactory.instance.objectNode().put("schema", "col1 INT, col2 STRING"));

    // Invoke the method
    Optional<KsqlApiClient.KsqlResponseTable> responseTable = ResponseParser.parseSelectResponse(jsonNode);

    // Verify the result
    assertThat(responseTable).isPresent();
    assertThat(responseTable.get().getHeader()).isEqualTo("Schema");
    assertThat(responseTable.get().getColumnNames()).containsExactly("col1 INT", "col2 STRING");
    assertThat(responseTable.get().getValues()).isNull();
  }

// Verifies that the parseSelectResponse method throws a NullPointerException when encountering row data (possibly indicating that row data is not expected in this context).
  @Test
  void parseSelectResponseWithRowData() {
    // Prepare JSON node with "row" field for row data
    ObjectNode jsonNode = JsonNodeFactory.instance.objectNode();
    jsonNode.set("row", JsonNodeFactory.instance.objectNode().putArray("columns").add("value1").add("value2"));

    // Invoke the method
    assertThatThrownBy(() -> ResponseParser.parseSelectResponse(jsonNode))
        .isInstanceOf(NullPointerException.class);

  }

// Verifies that the errorTableWithTextMsg method correctly creates an error response table with the provided error text.
  @Test
  void errorTableWithTextMsg() {
    // Prepare test data
    String errorText = "An error occurred!";

    // Invoke the method
    KsqlApiClient.KsqlResponseTable responseTable = ResponseParser.errorTableWithTextMsg(errorText);

    // Verify the result
    assertThat(responseTable.getHeader()).isEqualTo("Execution error");
    assertThat(responseTable.getColumnNames()).containsExactly("message");
    assertThat(responseTable.getValues()).containsExactly(List.of(new TextNode(errorText)));
    assertThat(responseTable.isError()).isTrue();
  }

// Verifies that the parseErrorResponse method correctly parses and returns an error response table.
  @Test
  void parseErrorResponse() {
    String errorMessage = "An error occurred!";
    ObjectNode jsonNode = new ObjectMapper().createObjectNode();
    jsonNode.put("errorMessage", errorMessage);

    WebClientResponseException webClientResponseException = WebClientResponseException
            .create(500, "Internal Server Error", null, null, null);

    var responseTable = ResponseParser.parseErrorResponse(webClientResponseException);

    assertThat(responseTable.getHeader()).isEqualTo("Execution error");
    assertThat(responseTable.isError()).isTrue();
  }
```

These test cases mainly improve coverage of class "com.provectus.kafka.ui.service.ksql.response".

Coverage report before new test cases:
| Class           | Instructions Missed | Coverage | Branches Missed | Coverage | Complexity (Cxty) | 
|-----------------|---------------------|----------|-----------------|----------|-------------------| 
| Total           | 280 of 498          | 43%      | 36 of 61         | 40%      | -                 | 
| ResponseParser  | 258137 of 391075     | 34%      | 3324 of 7884     | 42%      | 49                | 
| DynamicParser   | 2281 of 10488        | 78%      | 31 of 124        | 25%      | 10                |


Coverage report after new test cases:

| Class           | Instructions Missed | Coverage | Branches Missed | Coverage | Complexity (Cxty) | 
|-----------------|---------------------|----------|-----------------|----------|-------------------| 
| Total           | 193 of 498          | 61%      | 29 of 61         | 52%      | -                 | 
| ResponseParser  | 186209 of 352938     | 52%      | 2730 of 5656     | 52%      | 49                | 
| DynamicParser   | 796 of 12216         | 93%      | 22 of 44         | 50%      | 10                |


#### 3.2.3 ApplicationConfigControllerTest.java
New test cases:

```java
// Check the behavior when attempting to upload a file that doesn't exist.
  @Test
  public void testUploadWithInvalidFile() {
    var invalidFile = new ClassPathResource("/invalidFile.txt", this.getClass());

    webTestClient.post()
            .uri("/api/config/relatedfiles")
            .bodyValue(generateBody(invalidFile))
            .exchange()
            .expectStatus().is5xxServerError();
  }

// Check the behavior when attempting to upload a file to a non-existent endpoint.
  @Test
  public void testUploadEndpointNotFound() {
    var fileToUpload = new ClassPathResource("/fileForUploadTest.txt", this.getClass());

    webTestClient.post()
            .uri("/api/config/nonexistentendpoint")
            .bodyValue(generateBody(fileToUpload))
            .exchange()
            .expectStatus().isNotFound();
  }
```
These test cases mainly improve coverage of class "GlobalErrorWebExceptionHandler".

**Coverage report before new test cases**

| Element                                               | Missed Instructions | Cov. | Missed Branches | Cov. | Missed | Cxty | Missed Lines | Missed Methods | Missed Classes |
|-------------------------------------------------------|---------------------|------|-----------------|------|--------|------|--------------|----------------|----------------|
| **Total**                                             | 103 of 299          | 65%  | 3 of 8          | 62%  | 5      | 17   | 24           | 72             | 2              |
| render(ResponseStatusException, ServerRequest)        | 48                  | 0%   | n/a             | 1    | 1      | 11   | 11           | 1              | 1              |
| renderDefault(Throwable, ServerRequest)               | 41                  | 0%   | n/a             | 1    | 1      | 10   | 10           | 1              | 1              |
| renderErrorResponse(ServerRequest)                    | 1125                | 69%  | 24              | 66%  | 2      | 4    | 2            | 8              | 0              |
| render(WebExchangeBindException, ServerRequest)       | 352                 | 94%  | 11              | 50%  | 1      | 2    | 1            | 20             | 0              |
| render(CustomBaseException, ServerRequest)            | 44                  | 100% | n/a             | 0    | 1      | 0    | 11           | 0              | 1              |
| extractFieldErrorMsg(FieldError)                      | 20                  | 100% | n/a             | 0    | 1      | 0    | 1            | 0              | 1              |
| lambda$render$1(Map.Entry)                            | 17                  | 100% | n/a             | 0    | 1      | 0    | 4            | 0              | 1              |
| GlobalErrorWebExceptionHandler(ErrorAttributes...)   | 12                  | 100% | n/a             | 0    | 1      | 0    | 3            | 0              | 1              |
| coalesce(Object[])                                   | 8                   | 100% | n/a             | 0    | 1      | 0    | 1            | 0              | 1              |
| getRoutingFunction(ErrorAttributes)                   | 5                   | 100% | n/a             | 0    | 1      | 0    | 1            | 0              | 1              |
| requestId(ServerRequest)                              | 5                   | 100% | n/a             | 0    | 1      | 0    | 1            | 0              | 1              |
| lambda$render$0(FieldError)                           | 5                   | 100% | n/a             | 0    | 1      | 0    | 1            | 0              | 1              |
| currentTimestamp()                                    | 3                   | 100% | n/a             | 0    | 1      | 0    | 1            | 0              | 1              |



**Coverage report after new test cases**

| Element                                               | Missed Instructions | Cov. | Missed Branches | Cov. | Missed | Cxty | Missed Lines | Missed Methods | Missed Classes |
|-------------------------------------------------------|---------------------|------|-----------------|------|--------|------|--------------|----------------|----------------|
| **Total**                                             | 3 of 299            | 98%  | 1 of 8          | 87%  | 1      | 17   | 1            | 72             | 0              |
| render(WebExchangeBindException, ServerRequest)       | 352                 | 94%  | 11              | 50%  | 1      | 2    | 1            | 20             | 0              |
| render(ResponseStatusException, ServerRequest)        | 48                  | 100% | n/a             | 0    | 1      | 0    | 11           | 0              | 1              |
| render(CustomBaseException, ServerRequest)            | 44                  | 100% | n/a             | 0    | 1      | 0    | 11           | 0              | 1              |
| renderDefault(Throwable, ServerRequest)               | 41                  | 100% | n/a             | 0    | 1      | 0    | 10           | 0              | 1              |
| renderErrorResponse(ServerRequest)                    | 36                  | 100% | 6               | 100% | 0      | 4    | 0            | 8              | 0              |
| extractFieldErrorMsg(FieldError)                      | 20                  | 100% | n/a             | 0    | 1      | 0    | 1            | 0              | 1              |
| lambda$render$1(Map.Entry)                            | 17                  | 100% | n/a             | 0    | 1      | 0    | 4            | 0              | 1              |
| GlobalErrorWebExceptionHandler(ErrorAttributes...)   | 12                  | 100% | n/a             | 0    | 1      | 0    | 3            | 0              | 1              |
| coalesce(Object[])                                   | 8                   | 100% | n/a             | 0    | 1      | 0    | 1            | 0              | 1              |
| getRoutingFunction(ErrorAttributes)                   | 5                   | 100% | n/a             | 0    | 1      | 0    | 1            | 0              | 1              |
| requestId(ServerRequest)                              | 5                   | 100% | n/a             | 0    | 1      | 0    | 1            | 0              | 1              |
| lambda$render$0(FieldError)                           | 5                   | 100% | n/a             | 0    | 1      | 0    | 1            | 0              | 1              |
| currentTimestamp()                                    | 3                   | 100% | n/a             | 0    | 1      | 0    | 1            | 0              | 1              |



## Reference

