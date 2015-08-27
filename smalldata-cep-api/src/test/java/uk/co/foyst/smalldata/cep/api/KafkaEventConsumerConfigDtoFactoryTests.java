package uk.co.foyst.smalldata.cep.api;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import uk.co.foyst.smalldata.cep.Stream;
import uk.co.foyst.smalldata.cep.StreamId;
import uk.co.foyst.smalldata.cep.consumer.EventConsumerId;
import uk.co.foyst.smalldata.cep.consumer.KafkaEventConsumerConfig;
import uk.co.foyst.smalldata.cep.service.StreamService;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class KafkaEventConsumerConfigDtoFactoryTests {

    @Mock
    private StreamService streamService;

    @InjectMocks
    private KafkaEventConsumerConfigDtoFactory dtoFactory;

    @Test
    public void shouldConvertKafkaEventConsumerConfigToDto() {

        // Arrange
        final String eventConsumerIdString = "9594df94-7c70-4b1b-a3f3-5b83dada1d13";
        final EventConsumerId eventConsumerId = EventConsumerId.fromString(eventConsumerIdString);
        final Stream inputStream = buildTestStream();
        final String zookeeperUrl = "http://localhost:6667/zk";
        final String groupId = "TestGroup";
        final String topic = "warningEventTopic";
        final KafkaEventConsumerConfig eventConsumerConfig = new KafkaEventConsumerConfig(eventConsumerId, inputStream, zookeeperUrl, groupId, topic);

        // Act
        final KafkaEventConsumerConfigDto eventConsumerConfigDto = (KafkaEventConsumerConfigDto) dtoFactory.build(eventConsumerConfig);

        // Assert
        assertEquals(eventConsumerConfig.getEventConsumerId().toString(), eventConsumerConfigDto.getEventConsumerId());
        assertEquals(inputStream.getStreamId().toString(), eventConsumerConfigDto.getStreamId());
        assertEquals(groupId, eventConsumerConfigDto.getGroupId());
        assertEquals(topic, eventConsumerConfigDto.getTopic());
        assertEquals(zookeeperUrl, eventConsumerConfigDto.getZookeeperUrl());
    }

    private Stream buildTestStream() {

        final String streamIdString = "8a747214-280e-4f58-9b81-633d5df4107a";
        final StreamId streamId = StreamId.fromString(streamIdString);
        final String streamName = "Error Stream";
        final String streamDefinition = "define stream ErrorStream(incidentTime long, severityLevel int, actionable bool);";
        final String streamDescription = "";
        return new Stream(streamId, streamName, streamDefinition, streamDescription);
    }

    @Test
    public void ShouldConvertDtoToKafkaEventConsumerConfig() {

        // Arrange
        final String eventConsumerIdString = "9594df94-7c70-4b1b-a3f3-5b83dada1d13";
        final String streamIdString = "8a747214-280e-4f58-9b81-633d5df4107a";
        final String zookeeperUrl = "http://localhost:6667/zk";
        final String groupId = "TestGroup";
        final String topic = "warningEventTopic";
        final KafkaEventConsumerConfigDto eventConsumerConfigDto = new KafkaEventConsumerConfigDto(eventConsumerIdString, streamIdString, zookeeperUrl, groupId, topic);
        final Stream stream = buildTestStream();
        when(streamService.read(stream.getStreamId())).thenReturn(stream);

        // Act
        final KafkaEventConsumerConfig eventConsumerConfig = (KafkaEventConsumerConfig)dtoFactory.convertToEventConsumerConfig(eventConsumerConfigDto);

        // Assert
        assertEquals(eventConsumerConfigDto.getEventConsumerId(), eventConsumerConfig.getEventConsumerId().toString());
        assertEquals(stream, eventConsumerConfig.getInputStream());
        assertEquals(eventConsumerConfigDto.getZookeeperUrl(), eventConsumerConfig.getZookeeperUrl());
        assertEquals(eventConsumerConfigDto.getTopic(), eventConsumerConfig.getTopic());
        assertEquals(eventConsumerConfigDto.getGroupId(), eventConsumerConfig.getGroupId());
    }
}