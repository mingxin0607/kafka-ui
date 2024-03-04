package com.provectus.kafka.ui.serdes.builtin;

import com.provectus.kafka.ui.serde.api.DeserializeResult;
import com.provectus.kafka.ui.serde.api.PropertyResolver;
import com.provectus.kafka.ui.serde.api.RecordHeaders;
import com.provectus.kafka.ui.serde.api.SchemaDescription;
import com.provectus.kafka.ui.serdes.BuiltInSerde;
import io.confluent.kafka.schemaregistry.avro.AvroSchemaUtils;
import java.util.Map;
import java.util.Optional;
import lombok.SneakyThrows;
import org.apache.avro.file.DataFileReader;
import org.apache.avro.file.SeekableByteArrayInput;
import org.apache.avro.generic.GenericDatumReader;

public class AvroEmbeddedSerde implements BuiltInSerde {

  public static String name() {
    return "Avro (Embedded)";
  }

  @Override
  public Optional<String> getDescription() {
    return Optional.empty();
  }

  @Override
  public Optional<SchemaDescription> getSchema(String topic, Target type) {
    return Optional.empty();
  }

  @Override
  public boolean canDeserialize(String topic, Target type) {
    return true;
  }

  @Override
  public boolean canSerialize(String topic, Target type) {
    return false;
  }

  @Override
  public Serializer serializer(String topic, Target type) {
    throw new IllegalStateException();
  }

  @Override
  public Deserializer deserializer(String topic, Target type) {
    //This code snippet implements a Deserializer for Avro data. It overrides the deserializer method to return an anonymous Deserializer class.
    // This class's deserialize method attempts to read Avro data from a byte array using DataFileReader and GenericDatumReader.
    // If no data is found (indicating only headers are present), it returns a DeserializeResult with null data.
    // Otherwise, it converts the Avro object to a JSON string using AvroSchemaUtils.toJson and returns a DeserializeResult containing the JSON string.
    // This process is used for converting Avro-encoded Kafka record data into a human-readable JSON format.
    return new Deserializer() {
      @SneakyThrows
      @Override
      public DeserializeResult deserialize(RecordHeaders headers, byte[] data) {
        try (var reader = new DataFileReader<>(new SeekableByteArrayInput(data), new GenericDatumReader<>())) {
          if (!reader.hasNext()) {
            // this is very strange situation, when only header present in payload
            // returning null in this case
            return new DeserializeResult(null, DeserializeResult.Type.JSON, Map.of());
          }
          Object avroObj = reader.next();
          String jsonValue = new String(AvroSchemaUtils.toJson(avroObj));
          //AvroSchemaUtils是 Confluence 的 Kafka Avro Serializer 库的一部分，外部依赖项。
          // 提供了使用 Avro 架构和数据的方法，例如将 Avro 对象转换为 JSON，
          return new DeserializeResult(jsonValue, DeserializeResult.Type.JSON, Map.of());
        }
      }
    };
  }
}
