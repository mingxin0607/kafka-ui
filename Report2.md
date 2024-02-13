
---

# Part IV: Functional Testing and Finite State Machines

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


- **Test Case Generation**

    Finite models serve as a basis for generating test cases. Testers can derive test scenarios directly from the states, inputs, and transitions defined in the model, ensuring a structured and systematic approach to test case creation.

- **Boundary Value Testing**

    FSMs help identify boundary conditions and edge cases by visualizing the transitions between states. Testers can design test cases to cover these boundaries, revealing potential issues related to extreme input values or system limits.

- **Error State Identification**

    Finite models explicitly define states, including error states. This aids in the identification and testing of error-handling mechanisms within the system. Testers can design test cases to force the system into error states to validate proper error handling.

- **Model-Based Testing**

    FSMs serve as a foundation for model-based testing, where test cases are generated automatically from the model. Automated tools can use the FSM to generate a set of test scenarios, reducing the manual effort required for test case design.

- **Documentation and Communication**

    FSMs offer a visual and intuitive way to document system behavior. Testers can share the model with stakeholders, including developers and product managers, to communicate complex interactions and state transitions in a more accessible manner.


## 3. The Example Feature as Finite State Machine

We chose the class `Int64Serde` implemented at "kafka-ui-api/src/main/java/com/provectus/kafka/ui/serdes/builtin/Int64Serde.java".
It deals with serialization and deserialization of a 64-bit integer.

It implemented interface `BuiltInSerde` at "kafka-ui-api/src/main/java/com/provectus/kafka/ui/serdes/BuiltInSerde.java", which extends another interface `Serde` at "kafka-ui-serde-api/src/main/java/com/provectus/kafka/ui/serde/api/Serde.java".

## 4. Description of our functional model and diagram

The FSM diagram below appears to model the lifecycle of a `Serde` instance in the Kafka UI context, as defined in the `serde.java` file. Here is a detailed description of the functional model and how it operates based on the information from the FSM and the `Serde` interface:
- **Uninitialized State**:
    - The system starts with the `Serde` instances not yet created or initialized.
  
- **Create Serde Instance Transition**:
    - The transition from "Uninitialized" to "Initialized" is triggered by the system upon application startup, where the Kafka UI scans configurations and instantiates `Serde` instances using their default, non-argument constructors.
  
- **Initialized State**:
    - The `Serde` instances are now in memory but have not been configured with any properties or settings.
  
- **Invoke Configure Method Transition**:
    - This transition occurs when the `configure` method is called on a `Serde` instance. The method sets up the internal state of the `Serde` using configuration properties.
  
- **Configured State**:
    - The `Serde` instance is now fully set up and ready to perform serialization and deserialization operations on Kafka topic keys and values.

- **Serialize/Deserialize Transitions**:
    - From the "Configured" state, the `Serde` can perform two main operations:
        - Serialization: When a data transfer or persistence request occurs, the `Serde` serializes the given input into a format suitable for Kafka, typically a byte array.
        - Deserialization: Upon receiving a data record from Kafka, the `Serde` deserializes the byte array back into the appropriate data structure for use in the application.

- **Serializing/Deserializing States**:
    - These are transient states where the actual work of converting data formats is done. These states reflect ongoing operations. Once serialization or deserialization is complete, the Serde instance transitions back to the `Configured` state, ready for further operations. This cyclical transition allows the `Serde` instance to be used repeatedly during the application's runtime without the need for reinitialization or reconfiguration unless the underlying topic configuration or schema has changed.

- **Closing Transition**:
    - This transition occurs when the application is shutting down or when the `Serde` instance is no longer needed. The `close()` method is called, which is supposed to clean up any resources used by the `Serde`.

- **Closed State**:
    - The final state indicates that the `Serde` instance has been closed and is no longer available for operations.

This FSM is a high-level representation of the operational model of a `Serde` instance within the Kafka UI application. It captures the lifecycle from instance creation, through configuration and operation, to eventual shutdown.

![Fig 4-4-1 FSM](Fig/Untitled%202.png)

Fig 4-4-1 FSM

## 5. Write Test Cases
## Overview

The `Int64Serde` class is responsible for the serialization and deserialization of 64-bit integers. The tests cover:

- Serialization of valid long values.
- Deserialization of valid byte arrays.
- Exception handling for invalid serialization input.
- Exception handling for invalid deserialization input.

## Test Cases

### `testSerializeValidLong`

Verifies that a valid long value is correctly serialized into a byte array matching the expected format.

### `testDeserializeValidBytes`

Ensures that a valid byte array is correctly deserialized back into the original long value, represented as a string.

### `testSerializeInvalidInputThrowsException`

Confirms that attempting to serialize a non-numeric value throws a `NumberFormatException`, as expected.

### `testDeserializeInvalidBytesThrowsException`

Checks that deserializing a byte array that does not represent a valid long value throws an `IllegalArgumentException`.

## Reference

[1]: [Finite-state machine - Wikipedia](https://en.wikipedia.org/wiki/Finite-state_machine)

[2]: [Finite State Machines | Brilliant Math & Science Wiki](https://brilliant.org/wiki/finite-state-machines/)

