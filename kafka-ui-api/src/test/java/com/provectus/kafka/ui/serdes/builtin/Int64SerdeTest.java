package com.provectus.kafka.ui.serdes.builtin;

import static org.assertj.core.api.Assertions.assertThat;

import com.google.common.primitives.Longs;
import com.provectus.kafka.ui.serde.api.DeserializeResult;
import com.provectus.kafka.ui.serde.api.Serde;
import com.provectus.kafka.ui.serdes.PropertyResolverImpl;
import com.provectus.kafka.ui.serdes.RecordHeadersImpl;
import org.apache.kafka.common.header.internals.RecordHeaders;
import org.junit.jupiter.api.BeforeEach; //JUnit's BeforeEach annotation is used to specify the method to be executed before each test method
import org.junit.jupiter.api.Test;//JUnit's Test annotation, used to mark test methods
import static org.junit.jupiter.api.Assertions.*;//Introducing JUnit's assertion library for test verification
import com.google.common.primitives.Longs; //Longs utility class in Guava library, used to handle conversion between long and byte
import com.provectus.kafka.ui.serde.api.*;//Introduce the project Serde interface and result class
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;


class Int64SerdeTest {

  private Int64Serde serde;//Test object

  @BeforeEach //Initialization method executed before each test method is executed
  void init() {
    serde = new Int64Serde();// Instantiate the test object
    serde.configure( //original one
        PropertyResolverImpl.empty(),
        PropertyResolverImpl.empty(),
        PropertyResolverImpl.empty()
    );
  }
  @Test
  void testSerializeValidLong() {
    String topic = "testSerializeValidLong";
    String value = "1234567890";//Define the value to be serialized
    byte[] expected = Longs.toByteArray(Long.parseLong(value));//// expected byte sequence
    byte[] actual = serde.serializer(topic, Serde.Target.VALUE).serialize(value);////Perform serialization operation
    assertArrayEquals(expected, actual, "Serialization of a valid long should match expected bytes.");////Verify serialization results
  }

  @Test
  void testDeserializeValidBytes() {
    String topic = "testDeserializeValidBytes";
    long value = 1234567890L;//Define the value to be deserialized
    byte[] bytes = Longs.toByteArray(value);// Convert long value to byte sequence
    DeserializeResult result = serde.deserializer(topic, Serde.Target.VALUE).deserialize(null, bytes);//Perform deserialization operation
    assertEquals(String.valueOf(value), result.getResult(), "Deserialization should match the original long value as a string.");//Verify deserialization results
  }

  @Test
  void testSerializeInvalidInputThrowsException() {
    String topic = "testSerializeInvalidInputThrowsExceptio";
    String invalidInput = "not a number";
    assertThrows(NumberFormatException.class, () -> serde.serializer(topic, Serde.Target.VALUE).serialize(invalidInput),////Exception thrown when validating invalid input during serialization
      "Serialization of invalid input should throw NumberFormatException.");
  }

  @Test
  void testDeserializeInvalidBytesThrowsException() {
    String topic = "testDeserializeInvalidBytesThrowsException";
    byte[] invalidBytes = new byte[]{1, 2, 3, 4}; // invalid: Less than 8 bytes required for a long
    //Exception thrown when verifying deserialization of invalid byte sequence
    assertThrows(IllegalArgumentException.class, () -> serde.deserializer(topic, Serde.Target.VALUE).deserialize(null, invalidBytes),//
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