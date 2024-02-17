package com.provectus.kafka.ui.serdes.builtin;

import static org.assertj.core.api.Assertions.assertThat; 
//Introducing JUnit's assertion library for test verification
import static org.junit.jupiter.api.Assertions.assertArrayEquals; 
import static org.junit.jupiter.api.Assertions.assertEquals;  
import static org.junit.jupiter.api.Assertions.assertThrows; 

import com.google.common.primitives.Longs; 
// Longs utility class in Guava library, used to handle conversion between long and byte
import com.google.common.primitives.Longs; 
import com.provectus.kafka.ui.serde.api.DeserializeResult; 
import com.provectus.kafka.ui.serde.api.Serde; 
import com.provectus.kafka.ui.serdes.PropertyResolverImpl; 
import com.provectus.kafka.ui.serdes.RecordHeadersImpl; 
import org.apache.kafka.common.header.internals.RecordHeaders; 
//JUnit's BeforeEach annotation is used to specify the method to be executed before each test method
import org.junit.jupiter.api.BeforeEach; 
//JUnit's Test annotation, used to mark test methods
import org.junit.jupiter.api.Test; 
import org.junit.jupiter.params.ParameterizedTest; 
import org.junit.jupiter.params.provider.EnumSource; 



class Int64SerdeTest {
  private Int64Serde serde; //Test object
  //Initialization method executed before each test method is executed

  @BeforeEach 
  void init() {
    // Instantiate the test object
    serde = new Int64Serde(); 
    //original one
    serde.configure(
        PropertyResolverImpl.empty(),
        PropertyResolverImpl.empty(),
        PropertyResolverImpl.empty()
    ); 
  }

  @Test
  void testSerializeValidLong() {
    String topic = "testSerializeValidLong"; 
    //Define the value to be serialized
    String value = "1234567890"; 
    // expected byte sequence
    byte[] expected = Longs.toByteArray(Long.parseLong(value)); 
    //Perform serialization operation
    byte[] actual = serde.serializer(topic, Serde.Target.VALUE).serialize(value); 
    //Verify serialization results
    assertArrayEquals(expected, actual, 
        "Serialization of a valid long should match expected bytes."); 
  }

  @Test
  void testDeserializeValidBytes() {
    String topic = "testDeserializeValidBytes";
    //Define the value to be deserialized
    long value = 1234567890L; 
    // Convert long value to byte sequence
    byte[] bytes = Longs.toByteArray(value); 
    //Perform deserialization operation
    DeserializeResult result = serde.deserializer(topic, Serde.Target.VALUE).deserialize(null, bytes); 
    //Verify deserialization results
    assertEquals(String.valueOf(value), result.getResult(), 
        "Deserialization should match the original long value as a string."); 
  }

  @Test
  void testSerializeInvalidInputThrowsException() {
    String topic = "testSerializeInvalidInputThrowsExceptio";
    String invalidInput = "not a number"; 
    //Exception thrown when validating invalid input during serialization
    assertThrows(NumberFormatException.class, () -> 
         serde.serializer(topic, Serde.Target.VALUE).serialize(invalidInput), 
        "Serialization of invalid input should throw NumberFormatException."); 
  }

  @Test
  void testDeserializeInvalidBytesThrowsException() {
    String topic = "testDeserializeInvalidBytesThrowsException";
    // invalid: Less than 8 bytes required for a long
    byte[] invalidBytes = new byte[]{1, 2, 3, 4}; 
    //Exception thrown when verifying deserialization of invalid byte sequence
    assertThrows(IllegalArgumentException.class, () 
        -> serde.deserializer(topic, Serde.Target.VALUE).deserialize(null, invalidBytes),
        "Deserialization of invalid bytes should throw IllegalArgumentException."); 
  }

  @ParameterizedTest
  @EnumSource
  void serializeUses8BytesLongRepresentation(Serde.Target type) {
    var serializer = serde.serializer("anyTopic", type);
    byte[] bytes = serializer.serialize("1234"); 
    assertThat(bytes).isEqualTo(Longs.toByteArray(1234)); 
  }

  @ParameterizedTest
  @EnumSource
  void deserializeUses8BytesLongRepresentation(Serde.Target type) {
    var deserializer = serde.deserializer("anyTopic", type); 
    var result = deserializer.deserialize(new RecordHeadersImpl(), Longs.toByteArray(1234)); 
    assertThat(result.getResult()).isEqualTo("1234"); 
    assertThat(result.getType()).isEqualTo(DeserializeResult.Type.JSON); 
  }

}