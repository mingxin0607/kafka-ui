package com.provectus.kafka.ui.serdes.builtin;

import static org.assertj.core.api.Assertions.assertThat;

import com.provectus.kafka.ui.serde.api.DeserializeResult;
import com.provectus.kafka.ui.serde.api.Serde;
import com.provectus.kafka.ui.serdes.PropertyResolverImpl;
import com.provectus.kafka.ui.serdes.RecordHeadersImpl;
import java.nio.ByteBuffer;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.mock.env.MockEnvironment;




class UuidBinarySerdeTest {
  @Test
  void serializerUsesMockedUuidBinaryRepresentation() {
    try (MockedStatic<UUID> mockedUuid = Mockito.mockStatic(UUID.class)) {
      // Use a specific UUID instance for testing
      UUID specificUuid = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");
      // When UUID.randomUUID() is called, return this specific UUID
      mockedUuid.when(UUID::randomUUID).thenReturn(specificUuid);

      // Initialize your Serde and other test setups
      UuidBinarySerde serde = new UuidBinarySerde();
      serde.configure(PropertyResolverImpl.empty(), PropertyResolverImpl.empty(), PropertyResolverImpl.empty());
      var serializer = serde.serializer("anyTopic", Serde.Target.VALUE);

      // Execute serialization
      byte[] bytes = serializer.serialize(specificUuid.toString());
      var bb = ByteBuffer.wrap(bytes);

      // Verify the serialization result
      if (serde.mostSignificantBitsFirst) {
        assertThat(bb.getLong()).isEqualTo(specificUuid.getMostSignificantBits());
        assertThat(bb.getLong()).isEqualTo(specificUuid.getLeastSignificantBits());
      } else {
        assertThat(bb.getLong()).isEqualTo(specificUuid.getLeastSignificantBits());
        assertThat(bb.getLong()).isEqualTo(specificUuid.getMostSignificantBits());
      }

      // Confirm UUID.randomUUID() was called at least once
      mockedUuid.verify(UUID::randomUUID, Mockito.times(1));
    }

  }

  @Nested
  class MsbFirst {

    private UuidBinarySerde serde;

    @BeforeEach
    void init() {
      serde = new UuidBinarySerde();
      serde.configure(
          PropertyResolverImpl.empty(),
          PropertyResolverImpl.empty(),
          PropertyResolverImpl.empty()
      );
    }

    @ParameterizedTest
    @EnumSource
    void serializerUses16bytesUuidBinaryRepresentation(Serde.Target type) {
      var serializer = serde.serializer("anyTopic", type);
      var uuid = UUID.randomUUID();
      byte[] bytes = serializer.serialize(uuid.toString());
      var bb = ByteBuffer.wrap(bytes);
      assertThat(bb.getLong()).isEqualTo(uuid.getMostSignificantBits());
      assertThat(bb.getLong()).isEqualTo(uuid.getLeastSignificantBits());
    }

    @ParameterizedTest
    @EnumSource
    void deserializerUses16bytesUuidBinaryRepresentation(Serde.Target type) {
      var uuid = UUID.randomUUID();
      var bb = ByteBuffer.allocate(16);
      bb.putLong(uuid.getMostSignificantBits());
      bb.putLong(uuid.getLeastSignificantBits());

      var result = serde.deserializer("anyTopic", type).deserialize(new RecordHeadersImpl(), bb.array());
      assertThat(result.getType()).isEqualTo(DeserializeResult.Type.STRING);
      assertThat(result.getAdditionalProperties()).isEmpty();
      assertThat(result.getResult()).isEqualTo(uuid.toString());
    }
  }

  @Nested
  class MsbLast {

    private UuidBinarySerde serde;

    @BeforeEach
    void init() {
      serde = new UuidBinarySerde();
      serde.configure(
          new PropertyResolverImpl(new MockEnvironment().withProperty("mostSignificantBitsFirst", "false")),
          PropertyResolverImpl.empty(),
          PropertyResolverImpl.empty()
      );
    }

    @ParameterizedTest
    @EnumSource
    void serializerUses16bytesUuidBinaryRepresentation(Serde.Target type) {
      var serializer = serde.serializer("anyTopic", type);
      var uuid = UUID.randomUUID();
      byte[] bytes = serializer.serialize(uuid.toString());
      var bb = ByteBuffer.wrap(bytes);
      assertThat(bb.getLong()).isEqualTo(uuid.getLeastSignificantBits());
      assertThat(bb.getLong()).isEqualTo(uuid.getMostSignificantBits());
    }

    @ParameterizedTest
    @EnumSource
    void deserializerUses16bytesUuidBinaryRepresentation(Serde.Target type) {
      var uuid = UUID.randomUUID();
      var bb = ByteBuffer.allocate(16);
      bb.putLong(uuid.getLeastSignificantBits());
      bb.putLong(uuid.getMostSignificantBits());

      var result = serde.deserializer("anyTopic", type).deserialize(new RecordHeadersImpl(), bb.array());
      assertThat(result.getType()).isEqualTo(DeserializeResult.Type.STRING);
      assertThat(result.getAdditionalProperties()).isEmpty();
      assertThat(result.getResult()).isEqualTo(uuid.toString());
    }
  }

}
