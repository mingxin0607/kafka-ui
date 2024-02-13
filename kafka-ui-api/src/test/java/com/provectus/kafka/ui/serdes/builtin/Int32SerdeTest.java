package com.provectus.kafka.ui.serdes.builtin;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.google.common.primitives.Ints;
import com.provectus.kafka.ui.serde.api.DeserializeResult;
import com.provectus.kafka.ui.serde.api.Serde;
import com.provectus.kafka.ui.serdes.PropertyResolverImpl;
import com.provectus.kafka.ui.serdes.RecordHeadersImpl;
import org.apache.kafka.common.header.internals.RecordHeaders;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

/**
 * Unit tests for the Int32Serde class.
 */
class Int32SerdeTest {

  private Int32Serde serde;

  /**
   * Initializes the test environment before each test method.
   */
  @BeforeEach
  void init() {
    // Initialization method to set up the test environment
    serde = new Int32Serde();
    serde.configure(
        PropertyResolverImpl.empty(),
        PropertyResolverImpl.empty(),
        PropertyResolverImpl.empty()
    );
  }

  /**
   * Test for serialization using 4-byte int representation.
   *
   * @param type The Serde target type.
   */
  @ParameterizedTest
  @EnumSource
  void serializeUses4BytesIntRepresentation(Serde.Target type) {
    var serializer = serde.serializer("anyTopic", type);
    byte[] bytes = serializer.serialize("1234");
    assertThat(bytes).isEqualTo(Ints.toByteArray(1234));
  }

  /**
   * Test for deserialization using 4-byte int representation.
   *
   * @param type The Serde target type.
   */
  @ParameterizedTest
  @EnumSource
  void deserializeUses4BytesIntRepresentation(Serde.Target type) {
    var deserializer = serde.deserializer("anyTopic", type);
    var result = deserializer.deserialize(new RecordHeadersImpl(), Ints.toByteArray(1234));
    assertThat(result.getResult()).isEqualTo("1234");
    assertThat(result.getType()).isEqualTo(DeserializeResult.Type.JSON);
  }

  /**
   * Test for serialize and deserialize with maximum 32-bit integer value.
   *
   * @param type The Serde target type.
   */
  @ParameterizedTest
  @EnumSource
  void serializeAndDeserializeInt32Max(Serde.Target type) {
    // Given
    var serializer = serde.serializer("anyTopic", type);
    var deserializer = serde.deserializer("anyTopic", type);

    // When
    byte[] bytes = serializer.serialize("2147483647");
    var result = deserializer.deserialize(new RecordHeadersImpl(), Ints.toByteArray(2147483647));

    // Then
    assertThat(result.getResult()).isEqualTo("2147483647");
    assertThat(result.getType()).isEqualTo(DeserializeResult.Type.JSON);
  }

  /**
   * Test for serialize and deserialize with minimum 32-bit integer value.
   *
   * @param type The Serde target type.
   */
  @ParameterizedTest
  @EnumSource
  void serializeAndDeserializeInt32Min(Serde.Target type) {
    // Given
    var serializer = serde.serializer("anyTopic", type);
    var deserializer = serde.deserializer("anyTopic", type);

    // When
    byte[] bytes = serializer.serialize("-2147483648");
    var result = deserializer.deserialize(new RecordHeadersImpl(), Ints.toByteArray(-2147483648));

    // Then
    assertThat(result.getResult()).isEqualTo("-2147483648");
    assertThat(result.getType()).isEqualTo(DeserializeResult.Type.JSON);
  }

  /**
   * Test for serialize and deserialize with zero value.
   *
   * @param type The Serde target type.
   */
  @ParameterizedTest
  @EnumSource
  void serializeAndDeserializeWithZeroValue(Serde.Target type) {
    // Given
    var serializer = serde.serializer("anyTopic", type);
    var deserializer = serde.deserializer("anyTopic", type);

    // When
    byte[] bytes = serializer.serialize("0");
    var result = deserializer.deserialize(new RecordHeadersImpl(), Ints.toByteArray(0));

    // Then
    assertThat(result.getResult()).isEqualTo("0");
    assertThat(result.getType()).isEqualTo(DeserializeResult.Type.JSON);
  }
  
  /**
   * Test for serialization with null value.
   *
   * @param type The Serde target type.
   */
  @ParameterizedTest
  @EnumSource
  void serializeWithNullValue(Serde.Target type) {

    var serializer = serde.serializer("anyTopic", type);
    assertThrows(Exception.class, () -> serializer.serialize(null));

  }
  
  /**
   * Test for serialization with an integer larger than 4 bytes.
   *
   * @param type The Serde target type.
   */
  @ParameterizedTest
  @EnumSource
  void serializeIntLargerThan4Bytes(Serde.Target type) {

    var serializer = serde.serializer("anyTopic", type);
    assertThrows(Exception.class, () -> serializer.serialize("2147483648"));

  }

}