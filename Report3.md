
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

### Components of Structural Testing

When performing structural testing, several activities are typically involved:

- **Code Coverage Analysis**: This involves using tools to assess which parts of the code are executed during testing. It measures various types of coverage, like line, branch, and method coverage.
- **Test Case Design**: Based on the coverage analysis, test cases are designed to target untested parts of the code to improve the coverage.
- **Test Execution and Monitoring**: The designed test cases are executed, and their execution is monitored to ensure they are covering the intended parts of the code.
- **Results Analysis**: After testing, the results are analyzed to determine the effectiveness of the test cases and to identify areas for further improvement.
  
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
```
A structural test case for this method would not only test it with different input values (like a black-box test) but also ensure that the loop inside the method is executed the correct number of times for each input. For instance, input 5 should cause the loop to execute five times, multiplying the result by 2, 3, 4, and 5.

To have structural testing, the tools we use usually, include JaCoCo, Clover, or Cobertura. Here we choose JaCoCo.

## 2. Structural Testing with Coverage Tool
### 2.1 Use `Jacoco` in Maven project  

#### 2.1.1 Setup

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
The following table is an overview of the test coverage of the project.

| Element | Missed Instructions | Cov. | Missed Branches | Cov. | Missed Cxty | Missed Lines | Missed Methods | Missed Classes |
|---------|---------------------|------|-----------------|------|-------------|--------------|----------------|----------------|
| Total   | 47,036 of 82,073    | 42%  | 5,218 of 6,534  | 20%  | 5,933       | 8,548        | 8,288          | 14,678         |
| ksql    | 16,182 of 5,497     | 25%  | 1,192 of 72     | 5%   | 2,053       | 2,134        | 4,130          | 4,596          |
| com.provectus.kafka.ui.model | 7,087 of 2,449 | 25% | 1,215 of 39 | 3% | 906 | 1,188 | 196 | 547 |
| com.provectus.kafka.ui.controller | 4,642 of 2,031 | 30% | 287 of 53 | 15% | 363 | 509 | 1,200 | 1,649 |
| com.provectus.kafka.ui.config | 2,941 of 751 | 20% | 625 of 31 | 4% | 427 | 564 | 51 | 198 |
| com.provectus.kafka.ui.service | 2,443 of 5,399 | 68% | 181 of 122 | 40% | 292 | 768 | 383 | 1,423 |
| com.provectus.kafka.ui.config.auth | 1,912 | 5% | 255 | 0% | 259 | 269 | 217 | 235 |
| com.provectus.kafka.ui.service.rbac.extractor | 1,110 | 0% | 42 | 0% | 105 | 105 | 261 | 261 |
| com.provectus.kafka.ui.model.rbac | 1,097 of 406 | 27% | 239 | 2% | 169 | 208 | 61 | 141 |
| com.provectus.kafka.ui.util | 1,018 of 1,025 | 50% | 104 of 60 | 36% | 127 | 226 | 184 | 416 |
| com.provectus.kafka.ui.mapper | 922 of 1,376 | 59% | 144 of 92 | 38% | 153 | 239 | 286 | 666 |
| com.provectus.kafka.ui.service.rbac | 905 | 10% | 119 | 4% | 120 | 131 | 225 | 256 |
| com.provectus.kafka.ui.service.metrics | 732 of 761 | 50% | 93 of 59 | 38% | 96 | 159 | 148 | 305 |
| com.provectus.kafka.ui.serdes | 722 of 1,188 | 62% | 81 of 73 | 47% | 85 | 185 | 128 | 366 |
| com.provectus.kafka.ui.util.jsonschema | 717 of 2,430 | 77% | 112 of 177 | 61% | 97 | 293 | 75 | 544 |
| com.provectus.kafka.ui.service.integration.odd | 714 of 715 | 50% | 53 of 17 | 24% | 78 | 136 | 124 | 281 |
| com.provectus.kafka.ui.service.ksql | 698 of 587 | 45% | 11 of 14 | 11% | 97 | 147 | 52 | 171 |
| com.provectus.kafka.ui.model.schemaregistry | 431 | 0% | 76 | 0% | 77 | 77 | 16 | 16 |
| com.provectus.kafka.ui.serdes.builtin | 330 of 2,433 | 88% | 26 of 6 | 63% | 66 | 228 | 87 | 428 |
| com.provectus.kafka.ui.serdes.builtin.sr | 284 of 771 | 73% | 21 of 5 | 54% | 28 | 78 | 42 | 219 |
| com.provectus.kafka.ui.service.ksql.response | 280 of 218 | 43% | 36 of 5 | 40% | 36 | 59 | 69 | 128 |
| com.provectus.kafka.ui.service.integration.odd.schema | 258 of 1,221 | 82% | 58 of 139 | 70% | 58 | 150 | 41 | 343 |
| com.provectus.kafka.ui.exception | 241 of 512 | 67% | - | 66% | 35 | 79 | 76 | 200 |
| com.provectus.kafka.ui.model.connect | 190 | 25% | 38 | 0% | 25 | 37 | 1 | 6 |
| com.provectus.kafka.ui.service.analyze | 91 of 0 | 84% | 22 of 6 | 54% | 28 | 79 | 31 | 187 |
| com.provectus.kafka.ui.config.auth.logout | - | 0% | - | n/a | 9 | 9 | 38 | 38 |
| com.provectus.kafka.ui.emitter | 1,610 | 91% | 228 | 79% | 28 | 164 | 36 | 389 |
| com.provectus.kafka.ui.service.audit | 736 | 84% | 134 | 79% | 19 | 80 | 32 | 189 |
| com.provectus.kafka.ui.service.masking | 193 | 59% | 32 of 18 | 36% | 24 | 42 | 4 | 42 |
| com.provectus.kafka.ui.client | - | 58% | - | n/a | 21 | 44 | 22 | 65 |
| com.provectus.kafka.ui.service.acl | 661 | 86% | 28 | 73% | 19 | 59 | 20 | 173 |
| com.provectus.kafka.ui.model.rbac.permission | 226 | 73% | - | n/a | 14 | 27 | 18 | 63 |
| com.provectus.kafka.ui.service.masking.policies | 484 | 88% | 43 | 84% | 9 | 62 | 11 | 111 |
| com.provectus.kafka.ui.model.rbac.provider | - | 0% | - | n/a | 4 | 4 | 13 | 13 |
| com.provectus.kafka.ui | - | 11% | - | n/a | 2 | 3 | 6 | 7 |



- **Instructions Coverage**: 42% (47,036 of 82,073 instructions missed)
- **Branch Coverage**: 20% (5,218 of 6,534 branches missed)
- **Line Coverage**: 2,978 of 14,678 lines missed (20.3%)
- **Method Coverage**: 207 of 5,113 methods missed (4.04%)

#### 2.2.1 Examples of Untested Methods
**1. KafkaUiApplication Class**
  
| Element                | Missed Instructions | Cov. | Missed Branches | Cov. | Missed | Cxty | Missed Lines | Missed Methods |
|------------------------|---------------------|------|------------------|------|--------|------|--------------|----------------|
| Total                  | 24 of 27            | 11%  | 0 of 0           | n/a  | 2      | 3    | 6            | 7              |
| startApplication(String[]) | 20                | 0%   | n/a              | n/a  | 1      | 1    | 4            | 4              |
| main(String[])         | 4                   | 0%   | n/a              | n/a  | 1      | 1    | 2            | 2              |
| KafkaUiApplication()   | 3                   | 100% | n/a              | n/a  | 0      | 1    | 0            | 1              |

0% coverage indicates that the methods are not tested  

**2. ConsumingStats**

| Element                                  | Missed Instructions | Cov. | Missed Branches | Cov. | Missed | Cxty | Missed Lines | Missed Methods |
|------------------------------------------|---------------------|------|------------------|------|--------|------|--------------|----------------|
| Total                                    | 7 of 92             | 92%  | 0 of 0           | n/a  | 1      | 5    | 2            | 24             |
| incFilterApplyError()                   | 7                   | 0%   | n/a              | n/a  | 1      | 1    | 2            | 1              |
| sendConsumingEvt(FluxSink, PolledRecords) | 35                | 100% | n/a              | n/a  | 0      | 1    | 0            | 1              |
| createConsumingStats()                   | 23                  | 100% | n/a              | n/a  | 0      | 1    | 0            | 1              |
| ConsumingStats()                        | 15                  | 100% | n/a              | n/a  | 0      | 1    | 0            | 1              |
| sendFinishEvent(FluxSink)               | 12                  | 100% | n/a              | n/a  | 0      | 1    | 0            | 1              |

**3. RetryingKafkaConnectClient**
  
| Element                                                     | Missed Instructions | Cov. | Missed Branches | Cov. | Missed | Cxty | Missed Lines | Missed Methods |
|-------------------------------------------------------------|---------------------|------|------------------|------|--------|------|--------------|----------------|
| Total                                                       | 114 of 231          | 50%  | 0 of 0           | n/a  | 21     | 42   | 22           | 51             |
| restartConnector(String, Boolean, Boolean)                 | 7                   | 0%   | n/a              | n/a  | 1      | 1    | 1            | 1              |
| restartConnectorWithHttpInfo(String, Boolean, Boolean)     | 7                   | 0%   | n/a              | n/a  | 1      | 1    | 1            | 1              |
| lambda$conflictCodeRetry$1(RetryBackoffSpec, Retry.RetrySignal) | 7               | 0%   | n/a              | n/a  | 1      | 1    | 2            | 1              |
| getConnectorTaskStatusWithHttpInfo(String, Integer)        | 6                   | 0%   | n/a              | n/a  | 1      | 1    | 1            | 1              |
| restartConnectorTask(String, Integer)                      | 6                   | 0%   | n/a              | n/a  | 1      | 1    | 1            | 1              |
| restartConnectorTaskWithHttpInfo(String, Integer)          | 6                   | 0%   | n/a              | n/a  | 1      | 1    | 1            | 1              |
| setConnectorConfigWithHttpInfo(String, Map)                | 6                   | 0%   | n/a              | n/a  | 1      | 1    | 1            | 1              |
| validateConnectorPluginConfigWithHttpInfo(String, Map)     | 6                   | 0%   | n/a              | n/a  | 1      | 1    | 1            | 1              |
| createConnectorWithHttpInfo(NewConnector)                  | 5                   | 0%   | n/a              | n/a  | 1      | 1    | 1            | 1              |
| deleteConnectorWithHttpInfo(String)                        | 5                   | 0%   | n/a              | n/a  | 1      | 1    | 1            | 1              |
| getConnectorWithHttpInfo(String)                           | 5                   | 0%   | n/a              | n/a  | 1      | 1    | 1            | 1              |
| getConnectorConfigWithHttpInfo(String)                     | 5                   | 0%   | n/a              | n/a  | 1      | 1    | 1            | 1              |
| getConnectorStatusWithHttpInfo(String)                     | 5                   | 0%   | n/a              | n/a  | 1      | 1    | 1            | 1              |
| getConnectorTasksWithHttpInfo(String)                      | 5                   | 0%   | n/a              | n/a  | 1      | 1    | 1            | 1              |
| getConnectorTopicsWithHttpInfo(String)                     | 5                   | 0%   | n/a              | n/a  | 1      | 1    | 1            | 1              |
| getConnectorsWithHttpInfo(String)                          | 5                   | 0%   | n/a              | n/a  | 1      | 1    | 1            | 1              |
| pauseConnector(String)                                     | 5                   | 0%   | n/a              | n/a  | 1      | 1    | 1            | 1              |
| pauseConnectorWithHttpInfo(String)                         | 5                   | 0%   | n/a              | n/a  | 1      | 1    | 1            | 1              |
| resumeConnectorWithHttpInfo(String)                        | 5                   | 0%   | n/a              | n/a  | 1      | 1    | 1            | 1              |
| getConnectorPluginsWithHttpInfo()                          | 4                   | 0%   | n/a              | n/a  | 1      | 1    | 1            | 1              |
| resumeConnector(String)                                    | 4                   | 0%   | n/a              | n/a  | 1      | 1    | 1            | 1              |
| RetryingKafkaConnectClient(ClustersProperties.ConnectCluster, ClustersProperties.TruststoreConfig, DataSize) | 9 | 100% | n/a              | n/a  | 0      | 1    | 0            | 1              |
| conflictCodeRetry()                                        | 8                   | 100% | n/a              | n/a  | 0      | 1    | 0            | 1              |
| withBadRequestErrorHandling(Mono)                          | 8                   | 100% | n/a              | n/a  | 0      | 1    | 0            | 1              |
| static {...}                                               | 7                   | 100% | n/a              | n/a  | 0      | 1    | 0            | 1              |
| setConnectorConfig(String, Map)                            | 6                   | 100% | n/a              | n/a  | 0      | 1    | 0            | 1              |
| getConnectorTaskStatus(String, Integer)                    | 6                   | 100% | n/a              | n/a  | 0      | 1    | 0            | 1              |
| validateConnectorPluginConfig(String, Map)                 | 6                   | 100% | n/a              | n/a  | 0      | 1    | 0            | 1              |
| lambda$withBadRequestErrorHandling$3(WebClientResponseException.InternalServerError) | 6 | 100% | n/a              | n/a  | 0      | 1    | 0            | 1              |
| lambda$withBadRequestErrorHandling$2(WebClientResponseException.BadRequest) | 6 | 100% | n/a              | n/a  | 0      | 1    | 0            | 1              |
| createConnector(NewConnector)                              | 5                   | 100% | n/a              | n/a  | 0      | 1    | 0            | 1              |
| deleteConnector(String)                                    | 5                   | 100% | n/a              | n/a  | 0      | 1    | 0            | 1              |
| getConnector(String)                                       | 5                   | 100% | n/a              | n/a  | 0      | 1    | 0            | 1              |
| getConnectorConfig(String)                                 | 5                   | 100% | n/a              | n/a  | 0      | 1    | 0            | 1              |
| getConnectorStatus(String)                                 | 5                   | 100% | n/a              | n/a  | 0      | 1    | 0            | 1              |
| getConnectorTasks(String)                                  | 5                   | 100% | n/a              | n/a  | 0      | 1    | 0            | 1              |
| getConnectorTopics(String)                                 | 5                   | 100% | n/a              | n/a  | 0      | 1    | 0            | 1              |
| getConnectors(String)                                      | 5                   | 100% | n/a              | n/a  | 0      | 1    | 0            | 1              |
| withRetryOnConflict(Mono)                                  | 4                   | 100% | n/a              | n/a  | 0      | 1    | 0            | 1              |
| withRetryOnConflict(Flux)                                  | 4                   | 100% | n/a              | n/a  | 0      | 1    | 0            | 1              |
| getConnectorPlugins()                                      | 4                   | 100% | n/a              | n/a  | 0      | 1    | 0            | 1              |
| lambda$conflictCodeRetry$0(Throwable)                      | 3                   | 100% | n/a              | n/a  | 0      | 1    | 0            | 1              |


#### 2.2.2 Uncovered/Untested Code
**1. On `KafkaUiApplication` class**
```java
public class KafkaUiApplication {

  public static void main(String[] args) {
    startApplication(args);
  }

  public static ConfigurableApplicationContext startApplication(String[] args) {
    return new SpringApplicationBuilder(KafkaUiApplication.class)
        .initializers(DynamicConfigOperations.dynamicConfigPropertiesInitializer())
        .build()
        .run(args);
  }
}
```

**2. On `ConsumingStats` class**
```java
  void incFilterApplyError() {
    filterApplyErrors++;
  }

  void sendFinishEvent(FluxSink<TopicMessageEventDTO> sink) {
    sink.next(
        new TopicMessageEventDTO()
            .type(TopicMessageEventDTO.TypeEnum.DONE)
            .consuming(createConsumingStats())
    );
  }
```

**3. On `RetryingKafkaConnectClient` class**
```java
  @Override
  public Mono<Void> restartConnector(String connectorName, Boolean includeTasks, Boolean onlyFailed)
      throws WebClientResponseException {
    return withRetryOnConflict(super.restartConnector(connectorName, includeTasks, onlyFailed));
  }

  @Override
  public Mono<ResponseEntity<Void>> restartConnectorWithHttpInfo(String connectorName, Boolean includeTasks,
                                                                 Boolean onlyFailed) throws WebClientResponseException {
    return withRetryOnConflict(super.restartConnectorWithHttpInfo(connectorName, includeTasks, onlyFailed));
  }

  @Override
  public Mono<Void> restartConnectorTask(String connectorName, Integer taskId) throws WebClientResponseException {
    return withRetryOnConflict(super.restartConnectorTask(connectorName, taskId));
  }

  @Override
  public Mono<ResponseEntity<Void>> restartConnectorTaskWithHttpInfo(String connectorName, Integer taskId)
      throws WebClientResponseException {
    return withRetryOnConflict(super.restartConnectorTaskWithHttpInfo(connectorName, taskId));
  }

```
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
[1] _Unstop_. (n.d.). Unstop - Competitions, Quizzes, Hackathons, Scholarships and Internships for Students and Corporates. https://unstop.com/blog/structural-testing

[2] _What is White Box Testing?_. (n.d.). Check Point. https://www.checkpoint.com/cyber-hub/cyber-security/what-is-white-box-testing/#:~:text=White%20box%20testing%20is%20a,gray%20and%20black%20box%20testing

[3] _White box testing - Software engineering- GeeksforGeeks._ (2023, December 27). GeeksforGeeks. https://www.geeksforgeeks.org/software-engineering-white-box-testing/
