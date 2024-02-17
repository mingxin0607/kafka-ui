
---

# Part III: Structural (White Box) Testing

## 1. Structural Testing


## 2. Structural Testing with Coverage Tool

### 2.1 Use `Jacoco` in maven project
#### 2.1.1 Setup
To use `Jacoco` coverage tool in a maven project, we could add the following dependency in each "pom.xml" (We already done so).
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

### 3.2 New Test Cases


## Reference

