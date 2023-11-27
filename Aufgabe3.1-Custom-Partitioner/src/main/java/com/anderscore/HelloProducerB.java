package com.anderscore;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.IntegerSerializer;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;

public class HelloProducerB {

    private final Properties props = new Properties();
    private final KafkaProducer<Integer, String> producer;

    public HelloProducerB(){
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, IntegerSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.PARTITIONER_CLASS_CONFIG, MyPartitionerB.class.getName());
        producer= new KafkaProducer<>(props);
    }

    public void produce() {
        for(int i=0; i<100; i++) {
            ProducerRecord<Integer, String> Msg = new ProducerRecord<>("MyPartitionerTopicB", i, "This is message #" + Integer.toString(i));
            producer.send(Msg);
        }
        ProducerRecord<Integer, String> Msg = new ProducerRecord<>("MyPartitionerTopicB", 1111, "This is  a special message" );
        producer.send(Msg);
        producer.close();
    }
}
