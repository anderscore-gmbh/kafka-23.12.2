package com.anderscore;

import java.util.Properties;

import gs.model.PersonValue;
import gs.model.PersonKey;
import io.confluent.kafka.serializers.KafkaAvroSerializer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;

public class HelloProducer {

    private final Properties props = new Properties();
    private final KafkaProducer<PersonKey, PersonValue> producer;

    public HelloProducer(){
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "broker-2.k.anderscore.com:9092");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, KafkaAvroSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, KafkaAvroSerializer.class);
        props.put("schema.registry.url", "http://broker-2.k.anderscore.com:8081");

        producer= new KafkaProducer<>(props);
    }

    public void produce() {
        PersonValue[] personen = {
            new PersonValue("Hans","Muster","123"),
            new PersonValue("Hanna","Musterin","456"),
            new PersonValue("Hasso","Musterhund","Hund-23"),
        };
        for(PersonValue p: personen) {
            PersonKey key = new PersonKey(p.getSteuerId());
            ProducerRecord<PersonKey,PersonValue> personMsg = new ProducerRecord<>("avro_personen",key, p);
            producer.send(personMsg);
        }
        producer.close();
    }

}
