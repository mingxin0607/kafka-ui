package com.provectus.kafka.ui.serdes.builtin;

import static org.assertj.core.api.Assertions.assertThat;

import java.nio.ByteBuffer;
import java.util.UUID;
import com.provectus.kafka.ui.serde.api.DeserializeResult;
import com.provectus.kafka.ui.serde.api.Serde;
import com.provectus.kafka.ui.serdes.PropertyResolverImpl;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;





import com.provectus.kafka.ui.serdes.RecordHeadersImpl;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import org.mockito.Mockito;
import org.springframework.mock.env.MockEnvironment;

class UuidBinarySerdeTest {
  @Test
  void serializerUsesMockedUuidBinaryRepresentation() {
    try (MockedStatic<UUID> mockedUuid = Mockito.mockStatic(UUID.class)) {
      // 创建一个假的UUID实例
      UUID fakeUuid = new UUID(0L, 0L);
      // 当调用UUID.randomUUID()时，返回假的UUID
      mockedUuid.when(UUID::randomUUID).thenReturn(fakeUuid);

      // 这里初始化你的Serde和其他测试设置
      UuidBinarySerde serde = new UuidBinarySerde();
      serde.configure(PropertyResolverImpl.empty(), PropertyResolverImpl.empty(), PropertyResolverImpl.empty());
      var serializer = serde.serializer("anyTopic", Serde.Target.VALUE);

      // 执行序列化
      byte[] bytes = serializer.serialize(fakeUuid.toString());
      var bb = ByteBuffer.wrap(bytes);

      // 验证序列化结果
      assertThat(bb.getLong()).isEqualTo(fakeUuid.getMostSignificantBits());
      assertThat(bb.getLong()).isEqualTo(fakeUuid.getLeastSignificantBits());

      // 确认UUID.randomUUID()至少被调用了一次
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
