package com.social.application.Controller;

import com.social.application.DTOs.MessageDTO;
import com.social.application.Models.Message;
import com.social.application.Services.MessageService;
import com.social.application.Utility.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/message")
public class MessageController {
    private final MessageService messageService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<Message>>> fetchAllMsg() {
        List<Message> messages = messageService.fetchAllMessage();
        return ResponseEntity.ok(ApiResponse.success("Messages fetched", messages));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Message>> createMessage(@RequestBody Message message) {
        Message result = messageService.insertMessage(message);
        return ResponseEntity.status(201).body(ApiResponse.success("Message created", result));
    }

    // get mapping for the fetching message by specific userid //
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Message>> fetchByMsgId(@PathVariable long id){

        Message message = messageService.fetchMessageById(id);

        return ResponseEntity.ok(
                ApiResponse.success("Message fetched", message)
        );
    }

    @GetMapping("/chat/{user1}/{user2}")
    public ResponseEntity<ApiResponse<List<MessageDTO>>> getChat(
            @PathVariable long user1,
            @PathVariable long user2) {

        List<MessageDTO> messages = messageService.getChat(user1, user2);

        if(messages == null || messages.isEmpty()){
            return ResponseEntity.status(404)
                    .body(ApiResponse.error("No chat found", "CHAT_NOT_FOUND"));
        }

        return ResponseEntity.ok(
                ApiResponse.success("Chat fetched", messages)
        );
    }
}
