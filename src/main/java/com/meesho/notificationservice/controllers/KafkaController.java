package com.meesho.notificationservice.controllers;

import com.meesho.notificationservice.services.kafka.Producer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/kafka")
public class KafkaController {
    private final Producer producer;
    @Autowired
    KafkaController(Producer producer) {
        this.producer=producer;
    }
    @PostMapping(value = "/publish")
    public void sendMessages(@RequestParam("message") Long messageId) {
        this.producer.sendMessage(messageId);
    }
}
