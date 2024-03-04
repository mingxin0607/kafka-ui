# Part 5. Testable Design. Mocking

## 1. **Testable Design**

### 1.1 Testable Design Concept

#### 1.1.1The Concept of Testable Design

A good testable design executes the automated testing in an efficient and economic way, which is an important attribute of a software design (Kexugit, 2008). According to the article provided by the Microsoft  (Kexugit, 2008) , testability primarily involves establishing quick and efficient feedback loops within your developemnt procedure to identify issues iin your code effectively.

#### 1.1.2 The Importance of Testable Design

The earlier problems are detected, the less costly they are to fix (Kexugit, 2008). Therefore, an appropriate testable design can help developers to fix the errors in a quick manner.

#### 1.1.3 Goals & Aspects of Testable Design

The ultrimate aim of testability is to establish swift feedback loops within your development workflow, facilitating the detecton and recitification of flaws in your code (Kexugit, 2008). One most challenging aspect of implementing changes lies not in the change itself, but in ensuring that the change does not adversely affect other critical functionalities(Elhage, 2016). Below is several aspects of the testability (Kexugit, 2008): 

1) Repeatability: Automated tests must be repeatable, with expected outcomes measurable for known inputs.
2) Ease of Writing: Tests should be straightforward to write; if setting up inputs for a test requires significant effort, the investment in writing the test may not be worthwhile.
3) Clarity: Tests shouold be easily understandable and reveral intentions clearly
4) Speed: Slow tests hinder productivity.

More specifically, tests including unit tests, integration tests and functional tests can help us to acheive the testable desiign. It is good that the following chracteristics could be followed by the code (Elhage, 2016):

1. Preferrring pure functions over immutable data structuressimplifies testing, as you can create straightforward test cases using input-output pairs and easily apply fuzzing techniques.
2. Utilizing small modules with clearly defined interfaces enables writing black-box tests that focus on testing interface contracts without concerning overly about internal details or the wider system context.
3. Seperating IO operation frorm pure computatoion aids in testing by isolating IO, ehich is generally more complex to test thn pure code.
4. Explicitly declaring dependencies enhances testability compard to implicit dependency handling, allowing for easier testing scenarios such as testing with clean databases, multiple threadsm or other specific conditions.

## 2. **Mocking** 







## References

[1]*Design for testability: A vital aspect of the system architect role in safe*. (2023, March 11). Scaled Agile Framework. https://scaledagileframework.com/design-for-testability-a-vital-aspect-of-the-system-architect-role-in-safe/

[2]Elhage, N. (2016, March). *Design for testability*. Made of Bugs. https://blog.nelhage.com/2016/03/design-for-testability/

[3]Kexugit. (2008). *Patterns in practice: Design for testability*. Microsoft Learn: Build skills that open doors in your career. https://learn.microsoft.com/en-us/archive/msdn-magazine/2008/december/patterns-in-practice-design-for-testability

[4]*Software testability*. (2024, February 21). Wikipedia, the free encyclopedia. Retrieved March 3, 2024, from https://en.wikipedia.org/wiki/Software_testability
