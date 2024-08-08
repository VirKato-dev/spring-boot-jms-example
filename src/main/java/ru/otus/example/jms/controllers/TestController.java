package ru.otus.example.jms.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.example.jms.dto.MessageDto;
import ru.otus.example.jms.services.ActiveMqProducer;

import java.util.UUID;

@RestController
@RequestMapping("/messages")
public class TestController {

    private final ActiveMqProducer producer;

    public TestController(ActiveMqProducer producer) {
        this.producer = producer;
    }

    @GetMapping
    public ResponseEntity<String> sendMessage(@RequestParam("text") String text) {
        MessageDto dto = MessageDto.builder()
                .uuid(UUID.randomUUID())
                .text(text)
                .build();
        producer.sendMessage(dto);
        return ResponseEntity.ok("Sent " + text);
    }
}
