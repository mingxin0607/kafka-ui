# Part 5. Testable Design. Mocking

## 1. **Testable Design**

### 1.1 Testable Design Concept

#### 1.1.1The Concept of Testable Design

A good testable design executes automated testing efficiently and economically, which is an important attribute of a software design (Kexugit, 2008). According to the article provided by Microsoft  (Kexugit, 2008), testability primarily involves establishing quick and efficient feedback loops within your development procedure to identify issues in your code effectively.

#### 1.1.2 The Importance of Testable Design

The earlier problems are detected, the less costly they are to fix (Kexugit, 2008). Therefore, an appropriate testable design can help developers to fix the errors quickly.

#### 1.1.3 Goals & Aspects of Testable Design

The ultimate aim of testability is to establish swift feedback loops within your development workflow, facilitating the detection and rectification of flaws in your code (Kexugit, 2008). One of the most challenging aspects of implementing change lies not in the change itself, but in ensuring that the change does not adversely affect other critical functionalities(Elhage, 2016). Below are several aspects of the testability (Kexugit, 2008): 

1) Repeatability: Automated tests must be repeatable, with expected outcomes measurable for known inputs.
2) Ease of Writing: Tests should be straightforward; if setting up inputs for a test requires significant effort, the investment in writing the test may not be worthwhile.
3) Clarity: Tests should be easily understandable and reveral intentions clearly
4) Speed: Slow tests hinder productivity.

More specifically, tests including unit tests, integration tests, and functional tests can help us to achieve a testable design. It is good that the following characteristics could be followed by the code (Elhage, 2016):

1. Preferring pure functions over immutable data structures simplifies testing, as you can create straightforward test cases using input-output pairs and easily apply fuzzing techniques.
2. Utilizing small modules with clearly defined interfaces enables writing black-box tests that focus on testing interface contracts without concerning overly about internal details or the wider system context.
3. Seperating IO operation from pure computation aids in testing by isolating IO, which is generally more complex to test than pure code.
4. Explicitly declaring dependencies enhances testability compared to implicit dependency handling, allowing for easier testing scenarios such as testing with clean databases, multiple threads, or other specific conditions.
### 1.2 Identify & Improve the Testability in Code

In the Kafka-UI project, we try to improve the testability of the code, which makes the code tested more easily. There is a class called `UInt32Serde` (path: `kafka-ui-api/src/main/java/com/provectus/kafka/ui/serdes/builtin/UInt32Serde.java`). This class is about doing the serialization and deserialization for the unsigned integer. The Serialization Method takes a string input representing a 32-bit integer and converts it into a 4-byte array. The deserialization method takes a byte array and converts it back to a string representing an unsigned 32-bit integer.   

We found one example in this class that might make the code not good for being tested. The original method directly uses the `Ints.toByteArray` method to convert an Integer into a byte array as follows:

```java
//original code
@Override
public Serializer serializer(String topic, Target type) {
    return input -> Ints.toByteArray(Integer.parseUnsignedInt(input));
}

```

The `UInt32Serde` class directly utilizes methods from the Guava library (`Ints.toByteArray`) and the Java Standard Library (`Integer.parseUnsignedInt`) for serialization and deserialization of unsigned 32-bit integers.

This may cause some issues: 1) It could be difficult to mock the behavior of external library methods for edge cases or failure scenarios. 2) It could be challenging to test the serialization logic in isolation from the external library's implementation. 


To solve the above issues, we can make the following modifications:

We can create an interface containing the `toByteArray(String input)` method, so we can isolate the code implementation and the library. 

```java
//interface
public interface UInt32Converter {
    byte[] toByteArray(String input);
}

```

And then in the class, we can utilize the instance of the interface `UInt32Converter`  to convert the data as below. Under that circumstance, if in the future we need to alter the conversion logic or use different logic in unit testing, we can simply provide a different implementation of the UInt32Converter.

```java
// implement the interface and create an interface
public class DefaultUInt32Converter implements UInt32Converter {
    @Override
    public byte[] toByteArray(String input) {
        return Ints.toByteArray(Integer.parseUnsignedInt(input));
    }
}

```
```java
// use the interface rather than directly using the library
public class UInt32Serde {
    private final UInt32Converter converter;

    public UInt32Serde(UInt32Converter converter) {
        this.converter = converter;
    }
```

## 2. **Mocking** 







## References

[1]*Design for testability: A vital aspect of the system architect role in safe*. (2023, March 11). Scaled Agile Framework. https://scaledagileframework.com/design-for-testability-a-vital-aspect-of-the-system-architect-role-in-safe/

[2]Elhage, N. (2016, March). *Design for testability*. Made of Bugs. https://blog.nelhage.com/2016/03/design-for-testability/

[3]Kexugit. (2008). *Patterns in practice: Design for testability*. Microsoft Learn: Build skills that open doors in your career. https://learn.microsoft.com/en-us/archive/msdn-magazine/2008/december/patterns-in-practice-design-for-testability

[4]*Software testability*. (2024, February 21). Wikipedia, the free encyclopedia. Retrieved March 3, 2024, from https://en.wikipedia.org/wiki/Software_testability
