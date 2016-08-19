package uk.co.foyst.smalldata.cep.consumer;

import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector;
import uk.co.foyst.smalldata.cep.Stream;
import uk.co.foyst.smalldata.cep.adapter.CEPAdapter;
import uk.co.foyst.smalldata.cep.adapter.CEPAdapterException;
import uk.co.foyst.smalldata.cep.consumer.transformer.InboundEventTransformer;
import uk.co.foyst.smalldata.cep.consumer.transformer.UnescapedStringArrayInboundEventTransformer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class KafkaEventConsumer implements EventConsumer {

    private final EventConsumerId eventConsumerId;
    private final ConsumerConnector consumer;
    private final CEPAdapter cepAdapter;
    private final String topic;
    private final int numThreads;
    private final Stream inputStream;
    private final InboundEventTransformer eventTransformer;

    private ExecutorService executor;
    private boolean started;

    public KafkaEventConsumer(KafkaEventConsumerConfig config, ConsumerConnector consumer, final InboundEventTransformer inboundEventTransformer, CEPAdapter cepAdapter) {

        this.eventConsumerId = config.getEventConsumerId();
        this.consumer = consumer;
        this.eventTransformer = inboundEventTransformer;
        this.cepAdapter = cepAdapter;
        // TODO: Push stripping out of config class into builder pattern
        this.topic = config.getTopic();
        this.numThreads = config.getPoolSize();
        this.inputStream = config.getInputStream();
    }

    @Override
    public void start() {

        Map<String, Integer> topicCountMap = new HashMap<>();
        topicCountMap.put(topic, numThreads);
        Map<String, List<KafkaStream<byte[], byte[]>>> consumerMap = consumer.createMessageStreams(topicCountMap);
        List<KafkaStream<byte[], byte[]>> streams = consumerMap.get(topic);

        // now launch all the threads
        //
        executor = Executors.newFixedThreadPool(numThreads);

        // now add an object to consume the messages
        //
        int threadNumber = 0;
        for (final KafkaStream stream : streams) {
            executor.submit(new KafkaConsumer(stream, threadNumber));
            threadNumber++;
        }

        started = true;
    }

    @Override
    public void stop() {

        if (consumer != null) consumer.shutdown();
        if (executor != null) {
            executor.shutdown();
            try {
                if (!executor.awaitTermination(5000, TimeUnit.MILLISECONDS)) {
                    System.out.println("Timed out waiting for consumer threads to shut down, exiting uncleanly");
                }
            } catch (InterruptedException e) {
                System.out.println("Interrupted during shutdown, exiting uncleanly");
            }
        }

        started = false;
    }

    @Override
    public boolean isStarted() {
        return started;
    }

    private class KafkaConsumer implements Runnable {

        private final KafkaStream kafkaStream;
        private final int threadNumber;

        public KafkaConsumer(KafkaStream kafkaStream, int threadNumber) {
            this.kafkaStream = kafkaStream;
            this.threadNumber = threadNumber;
        }

        @Override
        public void run() {
            ConsumerIterator<byte[], byte[]> it = kafkaStream.iterator();
            while (it.hasNext())
                try {
                    cepAdapter.sendEvent(inputStream, eventTransformer.convertToObjectArray(it.next().message()));
                } catch (CEPAdapterException e) {
                    e.printStackTrace();
                }
            System.out.println("Shutting down Thread: " + threadNumber);
        }

    }
}
