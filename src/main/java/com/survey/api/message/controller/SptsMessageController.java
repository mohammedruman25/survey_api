package com.survey.api.message.controller;

import com.survey.api.message.dto.SptsMessageDTO;
import com.survey.api.message.entity.SptsMessage;
import com.survey.api.message.service.SptsMessageService;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/messages")
@Api(tags = "Message Controller", description = "Operations related to messages")
public class SptsMessageController {

    private static final Logger logger = LoggerFactory.getLogger(SptsMessageController.class);

    @Autowired
    SptsMessageService messageService;

    @ApiOperation(value = "Create a new message", response = SptsMessage.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Message created successfully"),
            @ApiResponse(code = 400, message = "Invalid request body"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @PostMapping("/")
    public SptsMessage createMessage(@ApiParam(value = "Message data", required = true) @RequestBody SptsMessageDTO messageDTO) {
        logger.info("Creating a new message with title: {}", messageDTO.getTitle());
        SptsMessage createdMessage = messageService.createMessage(messageDTO);
        logger.info("Message created with ID: {}", createdMessage.getId());
        return createdMessage;
    }

    @ApiOperation(value = "Get all messages", response = List.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Messages retrieved successfully")
    })
    @GetMapping("/")
    public List<SptsMessage> getAllMessages() {
        logger.info("Retrieving all messages");
        return messageService.getAllMessages();
    }

    @ApiOperation(value = "Get a message by ID", response = SptsMessage.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Message found"),
            @ApiResponse(code = 404, message = "Message not found")
    })
    @GetMapping("/{id}")
    public Optional<SptsMessage> getMessageById(@PathVariable Long id) {
        logger.info("Retrieving message with ID: {}", id);
        return messageService.getMessageById(id);
    }

    @ApiOperation(value = "Update a message", response = SptsMessage.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Message updated successfully"),
            @ApiResponse(code = 400, message = "Invalid request body"),
            @ApiResponse(code = 404, message = "Message not found"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @PutMapping("/{id}")
    public SptsMessage updateMessage(@PathVariable Long id, @ApiParam(value = "Updated message data", required = true) @RequestBody SptsMessageDTO messageDTO) {
        logger.info("Updating message with ID: {}", id);
        SptsMessage updatedMessage = messageService.updateMessage(id, messageDTO);
        if (updatedMessage != null) {
            logger.info("Message updated with ID: {}", id);
        } else {
            logger.warn("Message with ID: {} not found", id);
        }
        return updatedMessage;
    }

    @ApiOperation(value = "Delete a message by ID")
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "Message deleted successfully"),
            @ApiResponse(code = 404, message = "Message not found")
    })
    @DeleteMapping("/{id}")
    public void deleteMessage(@PathVariable Long id) {
        logger.info("Deleting message with ID: {}", id);
        messageService.deleteMessage(id);
        logger.info("Message with ID: {} deleted", id);
    }

    @ApiOperation(value = "Get messages by group ID", response = List.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Messages retrieved successfully"),
            @ApiResponse(code = 404, message = "Messages not found")
    })
    @GetMapping("/group/{groupId}")
    public List<SptsMessage> getMessagesByGroupId(@PathVariable Long groupId) {
        logger.info("Retrieving messages for group ID: {}", groupId);
        List<SptsMessage> messages = messageService.getMessagesByGroupId(groupId);
        if (messages.isEmpty()) {
            logger.warn("No messages found for group ID: {}", groupId);
        }
        return messages;
    }
}
