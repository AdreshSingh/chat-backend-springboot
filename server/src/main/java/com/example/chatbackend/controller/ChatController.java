package com.example.chatbackend.controller;

import com.example.chatbackend.model.Message;
import com.example.chatbackend.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/messages")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;
    private final com.example.chatbackend.repository.UserRepository userRepository;

    @GetMapping
    public List<Message> getMessages() {
        return chatService.getAllMessages();
    }

    @PostMapping
    public Message sendMessage(@RequestBody Message message, java.security.Principal principal) {
        message.setId(UUID.randomUUID().toString());
        message.setTimestamp(LocalDateTime.now());

        // Locate user by email (from token)
        var user = userRepository.findByEmail(principal.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        message.setSender(user.getName()); // Set sender name from DB

        return chatService.saveMessage(message);
    }
}
