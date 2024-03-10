# Part VI: Static Analyzers

## 1. Static Analysis Tools

### 1.1 Definition

### 1.2 Goals

### 1.3 purposes

### 1.4 How to Use



## 2. PMD (Tool 1)

### 2.1 About PMD

PMD is a static source code analysis tool that identifies various issues in source code by applying a set of rules. It helps developers improve code quality by detecting potential bugs, code smells, and other issues. PMD supports multiple programming languages, including Java, JavaScript, XML, and more. 

__Features__

- Rule-Based Analysis: PMD analyzes source code based on a predefined set of rules. Each rule corresponds to a specific pattern or coding practice.

- Multiple Language Support: While originally designed for Java, PMD has expanded to support several languages, including JavaScript, XML, Apex, and others.
- Customizable Rulesets: Users can customize rulesets to include or exclude specific rules based on their project's requirements.
- Command-Line and IDE Integration: PMD can be run from the command line or integrated into popular Integrated Development Environments (IDEs) such as Eclipse, IntelliJ IDEA, and others.

### 2.2 Set up

#### 2.2.1 Download PMD

We followed the instrucitons on [PMD Document](https://pmd.github.io/), and set up pmd on Linux.

```shell
$ cd $HOME
$ wget https://github.com/pmd/pmd/releases/download/pmd_releases%2F7.0.0-rc4/pmd-dist-7.0.0-rc4-bin.zip
$ unzip pmd-dist-7.0.0-rc4-bin.zip
```

#### 2.2.2 PMD maven plugin

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
            <ruleset>category/java/errorprone.xml</ruleset>
            <ruleset>category/java/codestyle.xml</ruleset>
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

Fundamental rules that cover basic coding conventions, potential pitfalls, and general best practices. 

__unusedcode.xml__

Focuses on identifying and flagging unused code, which may include unused methods, fields, or other code elements. Removing unused code can help reduce code complexity and improve maintainability.

__errorprone.xml__

Targets potential bugs and error-prone constructs in the code.

__codestyle.xml__

Enforces coding style conventions to maintain consistent code formatting.

### 2.3 Run PMD Check

Under project path, run:

```shell
mvn pmd:check
```

`pmd.html` could be found at "kafka-ui-api/target/site/pmd.html". We saved the report to "pmd-reports/site/pmd.html".

### 2.4 PMD Report

#### 2.4.1 Priority 1 Warnings

`FinalParameterInAbstractMethod` in `LogoutSuccessHandler.java`

This warning suggests that the parameters in abstract methods should not be declared as final. In Java, abstract methods cannot have implementation details in their declaration, and marking parameters as final is redundant. Therefore, removing the final modifier from these parameters is recommended, though it would not cause real problem.

`ReturnEmptyCollectionRatherThanNull` in `MessagesController.java`

This warning advises returning an empty collection (Collections.emptyList()) instead of null. It helps to avoid potential NullPointerExceptions and is a good practice for enhancing code robustness.

`VariableNamingConventions`, `FieldNamingConventions` in `MessageFilters.java`

The variable GROOVY_ENGINE violates naming conventions. It should either be final or start with a lowercase character. To address this, consider making the variable final (static final GROOVY_ENGINE).

`FieldNamingConventions`, `VariableNamingConventions` in `Provider.java`

Similar to the previous case, the static fields violate naming conventions. They should either be final or follow the appropriate naming conventions for readability and maintainability.

`ConstructorCallsOverridableMethod` in `WebClientConfigurator.java`

This warning indicates that a potentially overridable method is called during object construction. It can lead to unexpected behavior, as subclass methods may not be fully initialized at the time of the call. Consider refactoring the code to avoid calling overridable methods during object construction.

### 2.4.2 Priority 3 Warnings

These are some example priority 3 warnings. They are mostly about code style and could help with code readability and maintainability.

`MethodArgumentCouldBeFinal` in `KafkaUiApplication.java` and `RetryingKafkaConnectClient.java`

PMD suggests that the method argument args could be declared as final. This is actually not a real problem to the project.

`CommentDefaultAccessModifier`, `DefaultPackage`, `LongVariable` in `ClustersProperties.java`

These warnings suggest code quality improvements. Adding comments for default access modifiers, avoiding default package usage, and considering shorter variable names contribute to better code readability and maintainability.


`AtLeastOneConstructor`, `CompareObjectsWithEquals` in `CorsGlobalConfiguration.java`

Ensuring at least one constructor is a good practice. Comparing objects with equals instead of direct reference comparison is more appropriate for object equality.



## 3. SpotBugs (Tool 2)

### 3.1 About SpotBugs

SpotBugs is a powerful static analysis tool used to identify potential bugs in Java code. It works by analyzing Java bytecode to detect various types of bugs, such as null pointer dereferences, concurrency issues, and resource leaks. By examining the structure and behavior of Java programs, SpotBugs can help developers identify and address potential issues before they manifest as runtime errors or system failures. SpotBugs is an essential tool for Java developers looking to improve the quality and reliability of their code.

**Features**

- Rule-Based Analysis: SpotBugs utilizes rule-based analysis to identify common coding errors and potential issues in Java source code.

- Customizable Configuration: Users can customize SpotBugs' configuration by enabling/disabling rules and defining custom rulesets to tailor the analysis to their project's requirements.

- Command-Line and IDE Integration: SpotBugs seamlessly integrates with popular IDEs and can be executed from the command line, facilitating its incorporation into various development workflows.

### 3.2 Set Up & Run SpotBugs

#### 3.2.1 Using the SpotBugs GUI

**Installation:**

1. **Extracting the Distribution:**

   - Download the SpotBugs binary distribution in either gzipped tar format or zip format.

     [SpotBugs Download Link](https://spotbugs.readthedocs.io/en/stable/installing.html)

   - Extract the downloaded distribution into a directory of your choice.

     For gzipped tar format:

     ```
     $ gunzip -c spotbugs-4.8.3.tgz | tar xvf -
     ```

     For zip format:

     ```
     $ unzip spotbugs-4.8.3.zip
     ```

**Running:**

2. **Executing SpotBugs:**

   - To start the SpotBugs GUI & Enter the directory:

     ```shell
     cd /usr/local/spotbugs-4.8.3
     ```

   - To run SpotBugs directly via command line:

     ```shell
     java -jar lib/spotbugs.jar -gui
     ```

#### 3.2.2 Using the SpotBugs Maven Plugin

**Step 1: Configure SpotBugs Maven Plugin**

Add the following configuration to the `pom.xml` file of the project:

```xml
<plugin>
    <groupId>com.github.spotbugs</groupId>
    <artifactId>spotbugs-maven-plugin</artifactId>
    <version>4.2.3</version>
    <configuration>
        <effort>Max</effort>
        <threshold>High</threshold>
        <excludeFilterFile>${session.executionRootDirectory}/spotbugs-exclude.xml</excludeFilterFile>
        <outputDirectory>${project.build.directory}/spotbugs</outputDirectory>
        <outputFormat>html</outputFormat>
    </configuration>
</plugin>
```

This configuration specifies the version of the SpotBugs Maven plugin, analysis effort, threshold, path to the exclude filter file, output directory, and output format.

**Step 2: Add SpotBugs Maven Plugin Execution Goal**

Add the following configuration to the `pom.xml` file to execute SpotBugs check during the build process:

```xml
<plugin>
    <groupId>com.github.spotbugs</groupId>
    <artifactId>spotbugs-maven-plugin</artifactId>
    <executions>
        <execution>
            <goals>
                <goal>check</goal>
            </goals>
            <phase>verify</phase>
        </execution>
    </executions>
</plugin>
```

This configuration specifies to execute SpotBugs check during the `verify` phase of the build lifecycle using the `check` goal of the SpotBugs Maven plugin.

**Step 3: Create SpotBugs Exclude Filter File**

Create a file named `spotbugs-exclude.xml` in the root directory of your project and add the following content:

```xml
<FindBugsFilter>
    <Match>
        <Class name="~.*\.generated\..*"/>
    </Match>
    <Match>
        <Bug pattern="RCN_REDUNDANT_NULLCHECK_OF_NONNULL_VALUE"/>
    </Match>
</FindBugsFilter>
```

This configuration specifies the classes and bug patterns to be excluded.

**Step 4: Challenges May Encounter**

1. **Java Version Issue**: Ensure that Java version is 17.
2. **File Not Found Issue**: Specify the correct file path in the configuration.
3. **Note pom.xml Commands**: Ensure correctness of configurations in the `pom.xml` file.
4. **Memory Insufficiency Issue**: Set sufficient memory for Maven. Use a script like the following:

```bash
#!/bin/bash
export MAVEN_OPTS="-Xmx1024m"
mvn "$@"
```

**Step5: CMD Line**

Run the following command:

```shell
mvn clean verify
```

or

```shell
mvn com.github.spotbugs:spotbugs-maven-plugin:4.2.3:spotbugs
```



### 3.3 SpotBugs Report

#### 3.3.1 Result Table

After using the spot bugs plugin, two types of reports is gotten. One is in `HTML` format and another one is in `XML` format. The report is under the `spot-bugs-report`folder under the `kafka-ui`directory.

The following data is generated from the module `kafka-ui-api`.

| Metrics                  | Total |
| ------------------------ | ----- |
| Packages analyzed        | 25    |
| High Priority Warnings   | 17    |
| Medium Priority Warnings | 277   |
| Low Priority Warnings    | 63    |
| Total Warnings           | 357   |

**Analysis:**

- Warnings were generated for 25 packages, and the majority of warnings fall into the medium priority category, with 277 instances reported.
- There are also significant numbers of low priority warnings (63) and high priority warnings (17), suggesting various issues identified within the codebase.

| Warning Type                          | Number |
| ------------------------------------- | ------ |
| Bad practice Warnings                 | 4      |
| Correctness Warnings                  | 1      |
| Experimental Warnings                 | 1      |
| Internationalization Warnings         | 32     |
| Malicious code vulnerability Warnings | 224    |
| Performance Warnings                  | 27     |
| Dodgy code Warnings                   | 68     |
| Total Warnings                        | 357    |

**Analysis:**

- The most prevalent warning type is "Malicious code vulnerability," with a total of 224 instances reported. This suggests potential security risks within the codebase that need to be addressed.
- The presence of warnings across multiple categories indicates various aspects of the codebase that may require attention and improvement to enhance code quality and maintainability.



#### 3.3.2 Warning Details
In addition to providing warnings, detailed information is available for each warning, often indicated by abbreviations. Here are explanations for some of these abbreviations:  

`EI (May expose internal representation by returning reference to mutable object)`: Indicates that there's a possibility of exposing the internal structure of an object by returning a reference to a mutable (changeable) object.

`EI2 (May expose internal representation by incorporating reference to a mutable object)`: Suggests that the internal representation of an object might be exposed by including a reference to a mutable object within it.

`SIC (Could be refactored into a named static inner class)`: Suggests that the class could potentially be refactored into a named static inner class.

`SS (Unread field: should this field be static?)`: Indicates that a field is unread and raises a question of whether it should be static.

#### Bad Practice Warnings

`Boxed Value Reboxing (BX_UNBOXING_IMMEDIATELY_REBOXED):`
A boxed value is unboxed and then immediately reboxed.

`Constructor Exception (CT_CONSTRUCTOR_THROW):`
Be wary of letting constructors throw exceptions. Classes that throw exceptions in their constructors are vulnerable to Finalizer attacks.

`Duplicate Switch Clauses (DB_DUPLICATE_SWITCH_CLAUSES):`
Method uses the same code for two switch clauses. This could indicate a case of duplicate code or a coding mistake.

#### Correctness Warnings

`Dead Local Store (DLS_DEAD_LOCAL_STORE):`
This instruction assigns a value to a local variable, but the value is not read or used in any subsequent instruction. Often, this indicates an error because the value computed is never used.

`Redundant Nullcheck (RCN_REDUNDANT_NULLCHECK_OF_NONNULL_VALUE):`
This method contains a redundant check of a known non-null value against the constant null.

`Uncalled Private Method (UPM_UNCALLED_PRIVATE_METHOD):`
This private method is never called. Consider removing it if it's not intended for reflection usage.

#### Experimental Warnings

`Should Be Static (SS_SHOULD_BE_STATIC):`
Unread field: should this field be static? Consider making the field static since it's initialized to a compile-time static value.

#### Internationalization Warnings

`Reliance on Default Encoding (DM_DEFAULT_ENCODING):`
Found a call to a method that will perform a byte to String (or String to byte) conversion, assuming that the default platform encoding is suitable. This may cause application behavior to vary between platforms.

`Consider Using Locale Parameterized Version (DM_CONVERT_CASE):`
A String is being converted to upper or lowercase using the platform's default encoding. Consider using String.toUpperCase(Locale l) or String.toLowerCase(Locale l) versions instead.

#### Malicious Code Vulnerability Warnings

`Classloader Creation Inside doPrivileged (DP_CREATE_CLASSLOADER_INSIDE_DO_PRIVILEGED):`
Classloaders should only be created inside a doPrivileged block for security reasons.

`System.exit Invocation (DM_EXIT):`
Invoking System.exit shuts down the entire Java virtual machine. Consider throwing a RuntimeException instead to avoid abrupt shutdowns.

#### Performance Warnings

`Unread Public/Protected Field (URF_UNREAD_PUBLIC_OR_PROTECTED_FIELD):`
This field is never read. Consider removing it if not intended for external usage.

`Inefficient Map Iterator Usage (WMI_WRONG_MAP_ITERATOR):`
This method accesses the value of a Map entry using a key that was retrieved from a keySet iterator. It's more efficient to use an iterator on the entrySet of the map.


## 4. Contrast between PMD and SpotBugs





## References
_Introduction — spotbugs 4.8.3 documentation._ (n.d.). SpotBugs manual — spotbugs 4.8.3 documentation. https://spotbugs.readthedocs.io/en/latest/introduction.html

