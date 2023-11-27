package com.anderscore;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.IntegerDeserializer;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.time.Duration;
import java.util.Properties;

import static java.util.Collections.singletonList;

public class HelloConsumerB extends Thread{

    private final Properties props = new Properties();
    private final KafkaConsumer<Integer, String> consumer;

    public HelloConsumerB(){
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "testgroup");
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "true");
        props.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, "5000");
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        props.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, "30000");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, IntegerDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);

        consumer = new KafkaConsumer<>(props);
    }


    public void run() {
        consumer.subscribe(singletonList("MyPartitionerTopicB"));
        while(true) {
            ConsumerRecords<Integer, String> records = consumer.poll(Duration.ofMillis(500));
            records.forEach(record -> System.out.printf("offset = %d, key = %s, value = %s, partition = %s\n", record.offset(), record.key(), record.value(), record.partition()));
        }
    }
}
