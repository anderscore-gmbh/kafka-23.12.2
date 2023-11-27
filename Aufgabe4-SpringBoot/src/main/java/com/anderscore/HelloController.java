package com.anderscore;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
public class HelloController {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    private int messageCount = 0;

    @RequestMapping("/")
    public String index() {
        messageCount++;
        kafkaTemplate.send("HelloTopic", UUID.randomUUID().toString(),"Hello from spring #" + messageCount);
        return "Sending Kafka message...#"+messageCount;
    }

}
