package uk.co.foyst.smalldata.cep.consumer.factory;

import kafka.consumer.Consumer;
import kafka.consumer.ConsumerConfig;
import kafka.javaapi.consumer.ConsumerConnector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.co.foyst.smalldata.cep.adapter.CEPAdapter;
import uk.co.foyst.smalldata.cep.consumer.EventConsumer;
import uk.co.foyst.smalldata.cep.consumer.EventConsumerConfig;
import uk.co.foyst.smalldata.cep.consumer.KafkaEventConsumer;
import uk.co.foyst.smalldata.cep.consumer.KafkaEventConsumerConfig;

import java.util.Properties;

@Component
public class KafkaEventConsumerFactory implements EventConsumerFactory {

    private final CEPAdapter cepAdapter;

    @Autowired
    public KafkaEventConsumerFactory(CEPAdapter cepAdapter) {
        this.cepAdapter = cepAdapter;
    }

    @Override
    public EventConsumer build(final EventConsumerConfig eventConsumerConfig) {

        EventConsumer eventConsumer = null;
        if (eventConsumerConfig instanceof KafkaEventConsumerConfig) {

            //TODO: Initialisation is spread between this Factory and the KafkaEventConsumer Constructor: consolidate
            final KafkaEventConsumerConfig kafkaConfig = (KafkaEventConsumerConfig) eventConsumerConfig;
            Properties props = new Properties();
            props.put("zookeeper.connect", kafkaConfig.getZookeeperUrl());
            props.put("group.id", kafkaConfig.getGroupId());
            props.put("zookeeper.session.timeout.ms", "400");
            props.put("zookeeper.sync.time.ms", "200");
            props.put("auto.commit.interval.ms", "1000");

            final ConsumerConfig consumerConfig = new ConsumerConfig(props);
            final ConsumerConnector consumer = Consumer.createJavaConsumerConnector(consumerConfig);
            eventConsumer = new KafkaEventConsumer(kafkaConfig, consumer, cepAdapter);
        }

        return eventConsumer;
    }
}
