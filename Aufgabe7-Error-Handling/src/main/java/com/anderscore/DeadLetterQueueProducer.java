package com.anderscore;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;
import java.util.concurrent.ExecutionException;

public class DeadLetterQueueProducer {
    private final Properties props = new Properties();
    private final KafkaProducer<String, String> producer;

    public DeadLetterQueueProducer(){
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "broker-1.k.anderscore.com:9092");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        producer = new KafkaProducer<>(props);
    }

    public void produce(String key, String data)  {
        ProducerRecord<String,String> helloMsg = new ProducerRecord<>("DLQ",key, data);
        try {
            RecordMetadata metadata = producer.send(helloMsg).get();
            System.out.println("Sent message to dlq (offset): "+metadata.offset());
        } catch (InterruptedException | ExecutionException e) {
            System.err.println("Unable to produce on DLQ");
            e.printStackTrace();
        }
    }
}
