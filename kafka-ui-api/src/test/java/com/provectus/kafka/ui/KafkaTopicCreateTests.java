package com.provectus.kafka.ui;

import com.provectus.kafka.ui.model.TopicCreationDTO;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.reactive.server.WebTestClient;

public class KafkaTopicCreateTests extends AbstractIntegrationTest {
  @Autowired
  private WebTestClient webTestClient;
  private TopicCreationDTO topicCreation;

  @BeforeEach
  public void setUpBefore() {
    this.topicCreation = new TopicCreationDTO()
        .replicationFactor(1)
        .partitions(3)
        .name(UUID.randomUUID().toString());
  }

  @Test
  void shouldCreateNewTopicSuccessfully() {
    webTestClient.post()
        .uri("/api/clusters/{clusterName}/topics", LOCAL)
        .bodyValue(topicCreation)
        .exchange()
        .expectStatus()
        .isOk();
  }

  @Test
  void shouldReturn400IfTopicAlreadyExists() {
    TopicCreationDTO topicCreation = new TopicCreationDTO()
        .replicationFactor(1)
        .partitions(3)
        .name(UUID.randomUUID().toString());

    webTestClient.post()
        .uri("/api/clusters/{clusterName}/topics", LOCAL)
        .bodyValue(topicCreation)
        .exchange()
        .expectStatus()
        .isOk();

    webTestClient.post()
        .uri("/api/clusters/{clusterName}/topics", LOCAL)
        .bodyValue(topicCreation)
        .exchange()
        .expectStatus()
        .isBadRequest();
  }

  @Test
  void shouldRecreateExistingTopicSuccessfully() {
    TopicCreationDTO topicCreation = new TopicCreationDTO()
            .replicationFactor(1)
            .partitions(3)
            .name(UUID.randomUUID().toString());

    webTestClient.post()
            .uri("/api/clusters/{clusterName}/topics", LOCAL)
            .bodyValue(topicCreation)
            .exchange()
            .expectStatus()
            .isOk();

    webTestClient.post()
            .uri("/api/clusters/{clusterName}/topics/" + topicCreation.getName(), LOCAL)
            .exchange()
            .expectStatus()
            .isCreated()
            .expectBody()
            .jsonPath("partitionCount").isEqualTo(topicCreation.getPartitions().toString())
            .jsonPath("replicationFactor").isEqualTo(topicCreation.getReplicationFactor().toString())
            .jsonPath("name").isEqualTo(topicCreation.getName());
  }

  @Test
  void shouldCloneExistingTopicSuccessfully() {
    TopicCreationDTO topicCreation = new TopicCreationDTO()
        .replicationFactor(1)
        .partitions(3)
        .name(UUID.randomUUID().toString());
    String clonedTopicName = UUID.randomUUID().toString();

    webTestClient.post()
        .uri("/api/clusters/{clusterName}/topics", LOCAL)
        .bodyValue(topicCreation)
        .exchange()
        .expectStatus()
        .isOk();

    webTestClient.post()
        .uri("/api/clusters/{clusterName}/topics/{topicName}/clone?newTopicName=" + clonedTopicName,
            LOCAL, topicCreation.getName())
        .exchange()
        .expectStatus()
        .isCreated()
        .expectBody()
        .jsonPath("partitionCount").isEqualTo(topicCreation.getPartitions().toString())
        .jsonPath("replicationFactor").isEqualTo(topicCreation.getReplicationFactor().toString())
        .jsonPath("name").isEqualTo(clonedTopicName);
  }

  @Test
  void shouldReturn400IfInvalidReplicationFactor() {
    TopicCreationDTO topicCreation = new TopicCreationDTO()
            .replicationFactor(0)  // Invalid replication factor
            .partitions(3)
            .name(UUID.randomUUID().toString());

    webTestClient.post()
            .uri("/api/clusters/{clusterName}/topics", LOCAL)
            .bodyValue(topicCreation)
            .exchange()
            .expectStatus()
            .isBadRequest();
  }

  @Test
  void shouldReturn400IfInvalidPartitions() {
    TopicCreationDTO topicCreation = new TopicCreationDTO()
            .replicationFactor(1)
            .partitions(0)  // Invalid number of partitions
            .name(UUID.randomUUID().toString());

    webTestClient.post()
            .uri("/api/clusters/{clusterName}/topics", LOCAL)
            .bodyValue(topicCreation)
            .exchange()
            .expectStatus()
            .isBadRequest();
  }
}
