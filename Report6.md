# Part VI: Static Analyzers

## 1. Continuous Integration 
### 1.1 Definition

## 2. PMD
### 2.1 Set up
#### 2.1.1 Download PMD
We followed the instrucitons on [PMD Document](https://pmd.github.io/), and set up pmd on Linux.
```shell
$ cd $HOME
$ wget https://github.com/pmd/pmd/releases/download/pmd_releases%2F7.0.0-rc4/pmd-dist-7.0.0-rc4-bin.zip
$ unzip pmd-dist-7.0.0-rc4-bin.zip
```
#### 2.1.2 Set up PMD maven plugin
In every "pom.xml" file, we added the following configuration.
```xml
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-pmd-plugin</artifactId>
    <version>3.21.2</version> 
    <executions>
        <execution>
            <id>pmd-check</id>
            <phase>validate</phase>
            <goals>
                <goal>check</goal>
            </goals>
        </execution>
    </executions>
    <configuration>
        <failOnViolation>false</failOnViolation>
        <rulesets>
            <ruleset>rulesets/java/basic.xml</ruleset>
            <ruleset>rulesets/java/unusedcode.xml</ruleset>
        </rulesets>
    </configuration>
</plugin>
```
About this configuration:
```xml
<id>: Provides a unique identifier for this execution. Here, it's named pmd-check.
<phase>: Specifies the build phase in which this execution should occur. In this case, it's the validate phase, which runs during the build's validation stage.
<goals>: Lists the goals to be executed in this execution. Here, it contains the check goal of the PMD Plugin.
<failOnViolation>: Determines whether the build should fail if PMD violations are found. Here, it's set to false, indicating that the build should not fail on violations.
<rulesets>: Specifies the PMD rulesets to be used during the analysis. In this example, it includes two rulesets (basic.xml and unusedcode.xml) that define the rules PMD should enforce.
```
__basic.xml__

This ruleset (basic.xml) typically includes fundamental rules that cover basic coding conventions, potential pitfalls, and general best practices. It may include rules related to naming conventions, code complexity, and other essential coding standards.

__unusedcode.xml__

The unusedcode.xml ruleset focuses on identifying and flagging unused code, which may include unused methods, fields, or other code elements. Removing unused code can help reduce code complexity and improve maintainability.

### 2.2 Run PMD Check

Under project path, run:
```shell
mvn pmd:check
```
`pmd.html` could be found at "kafka-ui-api/target/site/pmd.html". We saved the report to "pmd-reports/site/pmd.html".

### 2.3