package com.example.receiver;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MessageController {

    private final RabbitTemplate rabbitTemplate;

    public MessageController(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @GetMapping("/notification")
    public ResponseEntity<Notification> receiveNotification (){
        Notification notification = rabbitTemplate.receiveAndConvert("Kurs", ParameterizedTypeReference.forType(Notification.class));
        if (notification != null){
            return  ResponseEntity.ok( notification);
        } return ResponseEntity.noContent().build(); // - nie ma żadnej wiadomości
    }

    @RabbitListener(queues = "Kurs")
    public void listenerMessage(Notification notification){
        System.out.println("Email: " + notification.getEmail() + " Title: "
                + notification.getTitle() + " Body: " + notification.getBody());
    }
}
