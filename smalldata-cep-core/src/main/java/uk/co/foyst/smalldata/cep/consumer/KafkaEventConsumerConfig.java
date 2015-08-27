package uk.co.foyst.smalldata.cep.consumer;

import uk.co.foyst.smalldata.cep.Stream;

public class KafkaEventConsumerConfig extends EventConsumerConfig {

    private final String topic;
    private final int poolSize;
    private final String zookeeperUrl;
    private final String groupId;

    public KafkaEventConsumerConfig(final EventConsumerId eventConsumerId, final Stream inputStream, final String zookeeperUrl, final String groupId,
                                    final String topic) {

        super(eventConsumerId, inputStream);
        this.zookeeperUrl = zookeeperUrl;
        this.groupId = groupId;
        this.topic = topic;
        this.poolSize = 5; //Fixed, for now
    }

    public String getTopic() {
        return topic;
    }

    public Integer getPoolSize() {
        return poolSize;
    }

    public String getZookeeperUrl() {
        return zookeeperUrl;
    }

    public String getGroupId() {
        return groupId;
    }
}
