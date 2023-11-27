package com.anderscore;

import java.util.Properties;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

public class HelloProducer {

    private final Properties props = new Properties();
    private final KafkaProducer<String, String> producer;

    public HelloProducer(){
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        producer= new KafkaProducer<>(props);
    }

    public void produce() {
        ProducerRecord<String,String> helloMsg = new ProducerRecord<>("HelloTopic","Hello Key", "Hello Value");
        producer.send(helloMsg);
        producer.close();
    }
}
