package uk.co.foyst.smalldata.cep.api.dto;

public class KafkaEventConsumerConfigDto extends EventConsumerConfigDto {

    private String zookeeperUrl;
    private String topic;
    private String groupId;
    private String messageTransformer;

    public KafkaEventConsumerConfigDto() {}

    public KafkaEventConsumerConfigDto(final String eventConsumerId, final String streamId, final String zookeeperUrl, final String groupId,
                                       final String topic, final String messageTransformer) {
        super(eventConsumerId, streamId);

        this.messageTransformer = messageTransformer;
        this.groupId = groupId;
        this.topic = topic;
        this.zookeeperUrl = zookeeperUrl;
    }

    public String getZookeeperUrl() {
        return zookeeperUrl;
    }

    public String getTopic() {
        return topic;
    }

    public String getGroupId() {
        return groupId;
    }

    public String getMessageTransformer() {
        return messageTransformer;
    }
}
