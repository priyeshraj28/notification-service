package com.meesho.notificationservice.controllers;

import com.meesho.notificationservice.models.Message;
import com.meesho.notificationservice.models.SuccessResponse;
import com.meesho.notificationservice.services.MessageSenderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

import static com.meesho.notificationservice.constants.Constants.MESSAGE_SEND_INIT;

@RestController
@RequestMapping(path = "v1/sms")
@Validated
public class MessageController {
    private final MessageSenderService messageSenderService;

    @Autowired
    public MessageController(MessageSenderService messageSenderService) {
        this.messageSenderService = messageSenderService;
    }

    @PostMapping(path = "/send")
    public ResponseEntity<SuccessResponse> sendNewMessage(@Valid @RequestBody Message message) {
        message.setStatus(MESSAGE_SEND_INIT);
        LocalDateTime currentTime = LocalDateTime.now();
        message.setCreatedOn(currentTime);
        message.setLastUpdatedAt(currentTime);
        messageSenderService.sendNewMessage(message);
        return new ResponseEntity<>(new SuccessResponse(message,"success"), HttpStatus.CREATED);
    }

    @GetMapping(path = "/all")
    public ResponseEntity<SuccessResponse> getAllMessages() {
        List<Message> messageList=messageSenderService.getAllMessages();
        if (messageList.isEmpty())
            throw new NoSuchElementException("No messages found");
        return new ResponseEntity<>(new SuccessResponse(messageList,"success"), HttpStatus.OK);
    }

    @GetMapping(path = "/{messageId}")
    public ResponseEntity<SuccessResponse> getMessageById(@PathVariable("messageId") @Min(1) Long messageId) {
        Message message = messageSenderService.getMessageById(messageId)
                .orElseThrow(() -> new NoSuchElementException("No message with id: "+messageId));
        return new ResponseEntity<>(new SuccessResponse(message,"success"), HttpStatus.OK);
    }
}
