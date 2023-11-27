package com.anderscore;

import java.util.Properties;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.IntegerSerializer;
import org.apache.kafka.common.serialization.StringSerializer;

public class HelloProducer {

    private final Properties props = new Properties();
    private final KafkaProducer<Integer, String> producer;

    public HelloProducer(){
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, IntegerSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.PARTITIONER_CLASS_CONFIG, MyPartitioner.class.getName());
        producer= new KafkaProducer<>(props);
    }

    public void produce() {
        for(int i=0; i<100; i++) {
            ProducerRecord<Integer, String> Msg = new ProducerRecord<>("MyPartitionerTopic", i, "This is message #" + Integer.toString(i));
            producer.send(Msg);
        }
        producer.close();
    }
}
