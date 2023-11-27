package com.anderscore;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicIdPartition;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import static java.util.Collections.singletonList;

public class HelloConsumer extends Thread{

    private Long offset = null;

    private final Properties props = new Properties();
    private final KafkaConsumer<String, String> consumer;

    private DeadLetterQueueProducer dlqProducer = new DeadLetterQueueProducer();

    public HelloConsumer(){
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "broker-1.k.anderscore.com:9092");
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "testgroup2");
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false"); // Kein Auto-Commit
        props.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, "5000");
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        props.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, "30000");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);


        consumer = new KafkaConsumer<>(props);
    }


    public void run() {
        var partitions = consumer.partitionsFor("HelloTopic");
        assert partitions.size() == 1; // Stimmt alles auf dem Server?
        var partition = new TopicPartition("HelloTopic",0);

        consumer.assign(List.of(partition));

        if(offset != null) {
            consumer.seek(partition,offset);
        }

        while(true) {
            ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(500));
            records.forEach(record -> {
                try {
                    process(record.value());
                } catch (Exception e) {
                    // Move to dead-letter queue
                    dlqProducer.produce(record.key(), record.value());
                }
                System.out.printf("Processed: offset = %d, key = %s, value = %s\n", record.offset(), record.key(), record.value());
            });
           consumer.commitSync();
        }
    }

    void process(String value) throws Exception {
        if(Math.random() < 0.1) {
            throw new Exception("Bad luck");
        }
        System.err.println("Message processed - value: " + value);
    }

    public void setOffset(Long offset) {
        this.offset = offset;
    }
}
