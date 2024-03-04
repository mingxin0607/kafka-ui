package com.provectus.kafka.ui.serdes.builtin;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.json.JsonMapper;
import com.provectus.kafka.ui.serde.api.DeserializeResult;
import com.provectus.kafka.ui.serde.api.Serde;
import com.provectus.kafka.ui.serdes.PropertyResolverImpl;
import io.confluent.kafka.schemaregistry.avro.AvroSchemaUtils;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import org.apache.avro.Schema;
import org.apache.avro.file.DataFileReader;
import org.apache.avro.file.DataFileWriter;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericDatumWriter;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.io.BinaryEncoder;
import org.apache.avro.io.EncoderFactory;
import org.apache.avro.specific.SpecificDatumWriter;
import org.apache.kafka.common.serialization.Deserializer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class AvroEmbeddedSerdeTest {

//  @Mock
//  private DataFileReader<GenericRecord> dataFileReader;
//
//  private AvroEmbeddedSerde serde;
////创建待测类变量
//
//  @BeforeEach
//  void setUp() {
//    MockitoAnnotations.initMocks(this);
//    serde = new AvroEmbeddedSerde();
//  }
//
//  @Test
//  void deserializeShouldReturnJsonValue() throws IOException {
//    // Arrange: Create a simple Avro schema，将来要作为参照物
//    String schemaString = """
//            {
//              "type": "record",
//              "name": "TestAvroRecord",
//              "fields": [
//                { "name": "field1", "type": "string" },
//                { "name": "field2", "type": "int" }
//              ]
//            }
//            """;
//    Schema schema = new Schema.Parser().parse(schemaString);
////    record.put("field1", "this is test msg");
////    record.put("field2", 100500);
//
//    String jsonRecord = new String(AvroSchemaUtils.toJson(schema));
//    byte[] serializedRecordBytes = serializeAvroWithEmbeddedSchema((GenericRecord) schema);
//
//    var deserializer = avroEmbeddedSerde.deserializer("anyTopic", Serde.Target.KEY);
//    DeserializeResult result = deserializer.deserialize(null, serializedRecordBytes);
//
//
//
//    assertThat(result.getType()).isEqualTo(DeserializeResult.Type.JSON);
//    assertThat(result.getAdditionalProperties()).isEmpty();
//    assertJsonEquals(jsonRecord, result.getResult());

//
//    // Create a GenericRecord with the schema
//    GenericRecord user1 = new GenericData.Record(schema);
//    user1.put("field1", "this is test msg");
//    user1.put("field2", 256);
//
//    // Serialize the GenericRecord to a byte array
//    ByteArrayOutputStream out = new ByteArrayOutputStream();
//    BinaryEncoder encoder = EncoderFactory.get().binaryEncoder(out, null);
//    SpecificDatumWriter<GenericRecord> writer = new SpecificDatumWriter<>(schema);
//    writer.write(user1, encoder);
//    encoder.flush();
//    out.close();
//    byte[] serializedBytes = out.toByteArray();
//
//    // Mock the DataFileReader to return the GenericRecord
//    when(dataFileReader.hasNext()).thenReturn(true).thenReturn(false); // End after one record
//    when(dataFileReader.next()).thenReturn(user1);
//
//    // Act: Deserialize the bytes
//    var deserializer = avroEmbeddedSerde.deserializer("anyTopic", Serde.Target.VALUE);
//    DeserializeResult result = deserializer.deserialize(null, serializedBytes);
//
//    // Assert: Deserialize and verify the result
//    assertEquals("{ \"field1\": \"this is test msg\", \"field2\": 256 }", result.getResult());
//
//    // Verify that DataFileReader methods were called as expected
//    verify(dataFileReader, times(2)).hasNext();
//    verify(dataFileReader).next();
//  }

  private AvroEmbeddedSerde avroEmbeddedSerde;

  @BeforeEach
  void init() {
    avroEmbeddedSerde = new AvroEmbeddedSerde();
    avroEmbeddedSerde.configure(
        PropertyResolverImpl.empty(),
        PropertyResolverImpl.empty(),
        PropertyResolverImpl.empty()
    );
  }

  @ParameterizedTest
  @EnumSource
  void canDeserializeReturnsTrueForAllTargets(Serde.Target target) {
    assertThat(avroEmbeddedSerde.canDeserialize("anyTopic", target))
        .isTrue();
  }

  @ParameterizedTest
  @EnumSource
  void canSerializeReturnsFalseForAllTargets(Serde.Target target) {
    assertThat(avroEmbeddedSerde.canSerialize("anyTopic", target))
        .isFalse();
  }

  @Test
  void deserializerParsesAvroDataWithEmbeddedSchema() throws Exception {
    Schema schema = new Schema.Parser().parse("""
        {
          "type": "record",
          "name": "TestAvroRecord",
          "fields": [
            { "name": "field1", "type": "string" },
            { "name": "field2", "type": "int" }
          ]
        }
        """
    );
    GenericRecord record = new GenericData.Record(schema);
    record.put("field1", "this is test msg");
    record.put("field2", 100500);

    String jsonRecord = new String(AvroSchemaUtils.toJson(record));
    byte[] serializedRecordBytes = serializeAvroWithEmbeddedSchema(record);

    var deserializer = avroEmbeddedSerde.deserializer("anyTopic", Serde.Target.KEY);
    DeserializeResult result = deserializer.deserialize(null, serializedRecordBytes);


    assertThat(result.getType()).isEqualTo(DeserializeResult.Type.JSON);
    assertThat(result.getAdditionalProperties()).isEmpty();
    assertJsonEquals(jsonRecord, result.getResult());
  }

  private void assertJsonEquals(String expected, String actual) throws IOException {
    var mapper = new JsonMapper();
    assertThat(mapper.readTree(actual)).isEqualTo(mapper.readTree(expected));
  }

  private byte[] serializeAvroWithEmbeddedSchema(GenericRecord record) throws IOException {
    try (DataFileWriter<GenericRecord> writer = new DataFileWriter<>(new GenericDatumWriter<>());
         ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
      writer.create(record.getSchema(), baos);
      writer.append(record);
      writer.flush();
      return baos.toByteArray();
    }
  }
}
