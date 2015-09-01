package uk.co.foyst.smalldata.cep.api.dto;

public class KafkaEventConsumerConfigDto extends EventConsumerConfigDto {

    private String zookeeperUrl;
    private String topic;
    private String groupId;

    public KafkaEventConsumerConfigDto() {}

    public KafkaEventConsumerConfigDto(final String eventConsumerId, final String streamId, final String zookeeperUrl, final String groupId, final String topic) {
        super(eventConsumerId, streamId);

        this.groupId = groupId;
        this.topic = topic;
        this.zookeeperUrl = zookeeperUrl;
    }

    public String getZookeeperUrl() {
        return zookeeperUrl;
    }

    public void setZookeeperUrl(String zookeeperUrl) {
        this.zookeeperUrl = zookeeperUrl;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }
}
