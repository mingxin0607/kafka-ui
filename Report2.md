
---

# Part II: Functional Testing and Finite State Machines

## 1. Finite State Machines

A **finite-state machine** (FSM) or finite-state automaton (FSA, plural: automata), finite automaton, or simply a state machine, is a mathematical model of computation. It is __an abstract machine that can be in exactly one of a finite number of states at any given time__. [1](https://en.wikipedia.org/wiki/Finite-state_machine)

The FSM can change from one state to another in response to some inputs; the change from one state to another is called a **transition**.An FSM is defined by __a list of its states, its initial state, and the inputs that trigger each transition__. [1](https://en.wikipedia.org/wiki/Finite-state_machine)

### An Example

A system where particular inputs cause particular changes in state can be represented using finite state machines. 

```
        +---------+     Coin    +----------+
    +-->|         | ----------->|          |---+
Push|   | Locked  |             | Unlocked |   | Coin
    +---|         | <---------- |          |<--+
        +---------+    Push     +----------+
             |
             + Initial State
```

This example describes the various states of a turnstile. Inserting a coin into a turnstile will unlock it, and after the turnstile has been pushed, it locks again. Inserting a coin into an unlocked turnstile, or pushing against a locked turnstile will not change its state. [2](https://brilliant.org/wiki/finite-state-machines/)

## 2. Finite Models in Testing
Finite models, especially FSMs, offer a systematic and structured approach to software testing. 
They provide a visual representation of system behavior, aiding in test case generation, scenario exploration, and the identification of potential issues across different aspects of the system.
They are beneficial in the following ways:


- Test Case Generation

    Finite models serve as a basis for generating test cases. Testers can derive test scenarios directly from the states, inputs, and transitions defined in the model, ensuring a structured and systematic approach to test case creation.

- Boundary Value Testing

    FSMs help identify boundary conditions and edge cases by visualizing the transitions between states. Testers can design test cases to cover these boundaries, revealing potential issues related to extreme input values or system limits.

- Error State Identification

    Finite models explicitly define states, including error states. This aids in the identification and testing of error-handling mechanisms within the system. Testers can design test cases to force the system into error states to validate proper error handling.

- Model-Based Testing

    FSMs serve as a foundation for model-based testing, where test cases are generated automatically from the model. Automated tools can use the FSM to generate a set of test scenarios, reducing the manual effort required for test case design.

- Documentation and Communication

    FSMs offer a visual and intuitive way to document system behavior. Testers can share the model with stakeholders, including developers and product managers, to communicate complex interactions and state transitions in a more accessible manner.


## 3. The Example Feature as Finite State Machine

We chose the class `Int64Serde` implemented at "kafka-ui-api/src/main/java/com/provectus/kafka/ui/serdes/builtin/Int64Serde.java".
It deals with serialization and deserialization of a 64-bit integer.

It implemented interface `BuiltInSerde` at "kafka-ui-api/src/main/java/com/provectus/kafka/ui/serdes/BuiltInSerde.java", which extends another interface `Serde` at "kafka-ui-serde-api/src/main/java/com/provectus/kafka/ui/serde/api/Serde.java".

## 4. Description of our functional model and diagram


## 5. Write Test Cases


## Reference

[1]: [Finite-state machine - Wikipedia](https://en.wikipedia.org/wiki/Finite-state_machine)

[2]: [Finite State Machines | Brilliant Math & Science Wiki](https://brilliant.org/wiki/finite-state-machines/)

