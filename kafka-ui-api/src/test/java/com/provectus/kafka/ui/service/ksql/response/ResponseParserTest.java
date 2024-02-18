package com.provectus.kafka.ui.service.ksql.response;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TextNode;
import com.google.common.collect.Lists;
import com.provectus.kafka.ui.exception.KsqlApiException;
import com.provectus.kafka.ui.service.ksql.KsqlApiClient;
import com.provectus.kafka.ui.service.ksql.response.ResponseParser;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.WebClientResponseException;

class ResponseParserTest {

  @Test
  void parsesSelectHeaderIntoColumnNames() {
    assertThat(ResponseParser.parseSelectHeadersString("`inQuotes` INT, notInQuotes INT"))
        .containsExactly("`inQuotes` INT", "notInQuotes INT");

    assertThat(ResponseParser.parseSelectHeadersString("`name with comma,` INT, name2 STRING"))
        .containsExactly("`name with comma,` INT", "name2 STRING");

    assertThat(ResponseParser.parseSelectHeadersString(
        "`topLvl` INT, `struct` STRUCT<`nested1` STRING, anotherName STRUCT<nested2 INT>>"))
        .containsExactly(
            "`topLvl` INT",
            "`struct` STRUCT<`nested1` STRING, anotherName STRUCT<nested2 INT>>"
        );
  }

  @Test
  void parsesSelectHeaderWithDifferentDataTypes() {
    assertThat(ResponseParser.parseSelectHeadersString("col1 INT, col2 STRING, col3 DOUBLE"))
        .containsExactly("col1 INT", "col2 STRING", "col3 DOUBLE");

    assertThat(ResponseParser.parseSelectHeadersString("name STRING, age INT, active BOOLEAN"))
        .containsExactly("name STRING", "age INT", "active BOOLEAN");
  }

  @Test
  void parsesSelectHeaderWithDifferentStructures() {
    assertThat(ResponseParser.parseSelectHeadersString(
        "`struct1` STRUCT<`nested1` STRING, `nested2` INT>, `struct2` STRUCT<`nested3` DOUBLE>"))
        .containsExactly(
            "`struct1` STRUCT<`nested1` STRING, `nested2` INT>",
            "`struct2` STRUCT<`nested3` DOUBLE>"
        );

    assertThat(ResponseParser.parseSelectHeadersString(
        "`parent` STRUCT<`child1` INT, `child2` STRING>, `nested` STRUCT<`inner1` DOUBLE>"))
        .containsExactly(
            "`parent` STRUCT<`child1` INT, `child2` STRING>",
            "`nested` STRUCT<`inner1` DOUBLE>"
        );
  }

  @Test
  void parseSelectResponseWithSchemaHeader() {
    // Prepare JSON node with "header" field for schema
    ObjectNode jsonNode = JsonNodeFactory.instance.objectNode();
    jsonNode.set("header", JsonNodeFactory.instance.objectNode().put("schema", "col1 INT, col2 STRING"));

    // Invoke the method
    Optional<KsqlApiClient.KsqlResponseTable> responseTable = ResponseParser.parseSelectResponse(jsonNode);

    // Verify the result
    assertThat(responseTable).isPresent();
    assertThat(responseTable.get().getHeader()).isEqualTo("Schema");
    assertThat(responseTable.get().getColumnNames()).containsExactly("col1 INT", "col2 STRING");
    assertThat(responseTable.get().getValues()).isNull();
  }

  @Test
  void parseSelectResponseWithRowData() {
    // Prepare JSON node with "row" field for row data
    ObjectNode jsonNode = JsonNodeFactory.instance.objectNode();
    jsonNode.set("row", JsonNodeFactory.instance.objectNode().putArray("columns").add("value1").add("value2"));

    // Invoke the method
    assertThatThrownBy(() -> ResponseParser.parseSelectResponse(jsonNode))
        .isInstanceOf(NullPointerException.class);

  }

  @Test
  void errorTableWithTextMsg() {
    // Prepare test data
    String errorText = "An error occurred!";

    // Invoke the method
    KsqlApiClient.KsqlResponseTable responseTable = ResponseParser.errorTableWithTextMsg(errorText);

    // Verify the result
    assertThat(responseTable.getHeader()).isEqualTo("Execution error");
    assertThat(responseTable.getColumnNames()).containsExactly("message");
    assertThat(responseTable.getValues()).containsExactly(List.of(new TextNode(errorText)));
    assertThat(responseTable.isError()).isTrue();
  }

  @Test
  void parseErrorResponse() {
    String errorMessage = "An error occurred!";
    ObjectNode jsonNode = new ObjectMapper().createObjectNode();
    jsonNode.put("errorMessage", errorMessage);

    WebClientResponseException webClientResponseException = WebClientResponseException
            .create(500, "Internal Server Error", null, null, null);

    var responseTable = ResponseParser.parseErrorResponse(webClientResponseException);

    assertThat(responseTable.getHeader()).isEqualTo("Execution error");
    assertThat(responseTable.isError()).isTrue();
  }
}
