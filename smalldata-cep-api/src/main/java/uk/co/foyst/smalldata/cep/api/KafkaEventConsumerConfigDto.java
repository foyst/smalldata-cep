package uk.co.foyst.smalldata.cep.api;

public class KafkaEventConsumerConfigDto extends EventConsumerConfigDto {

    private final String zookeeperUrl;
    private final String topic;
    private final String groupId;

    public KafkaEventConsumerConfigDto(final String eventConsumerId, final String streamId, final String zookeeperUrl, final String groupId, final String topic) {
        super(eventConsumerId, streamId);

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
}
