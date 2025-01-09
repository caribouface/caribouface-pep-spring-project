// MessageService.java

package com.example.service;

import com.example.entity.Message;
import com.example.repository.AccountRepository;
import com.example.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class MessageService {

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private AccountRepository accountRepository;

    public Message createMessage(Message message) {
        if (message.getMessageText() == null || message.getMessageText().isBlank() ||
            message.getMessageText().length() > 255 || 
            !accountRepository.existsById(message.getPostedBy())) { 
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid message or user");
        }
        return messageRepository.save(message);
    }

    public List<Message> getAllMessages() {
        return messageRepository.findAll();
    }

    public Message getMessageById(Integer messageId) {
        Optional<Message> message = messageRepository.findById(messageId);
        return message.orElse(null); 
    }

    public void deleteMessage(Integer messageId) {
        try {
            messageRepository.deleteById(messageId);
        } catch (Exception e) {
            throw new EmptyResultDataAccessException("Message not found with ID: " + messageId, 1);
        }
    }

    public Message updateMessage(Integer messageId, String messageText) {
        Message existingMessage = messageRepository.findById(messageId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Message not found"));

        if (messageText == null || messageText.isBlank() || messageText.length() > 255) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid message text");
        }

        existingMessage.setMessageText(messageText);
        return messageRepository.save(existingMessage);
    }

    public List<Message> getMessagesByAccountId(Integer accountId) {
        if (!accountRepository.existsById(accountId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found");
        }
        return messageRepository.findByPostedBy(accountId);
    }
}