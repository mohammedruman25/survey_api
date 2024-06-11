package com.survey.api.message.service;

import com.survey.api.common.exception.BadRequestException;
import com.survey.api.message.dto.SptsMessageDTO;
import com.survey.api.message.entity.SptsMessage;
import com.survey.api.message.repo.SptsMessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class SptsMessageService {

    @Autowired
    private SptsMessageRepository messageRepository;

    // Create
    public SptsMessage createMessage(SptsMessageDTO messageDTO) {
        SptsMessage newMessage = new SptsMessage();
        newMessage.setTitle(messageDTO.getTitle());
        newMessage.setMessage(messageDTO.getMessage());
        newMessage.setGroupsId(messageDTO.getGroupsId());
        newMessage.setMsgSrc(messageDTO.getMsgSrc());
        newMessage.setStartDate(messageDTO.getStartDate());
        newMessage.setEndDate(messageDTO.getEndDate());
        return messageRepository.save(newMessage);
    }

    // Read
    public List<SptsMessage> getAllMessages() {
        return messageRepository.findAll();
    }

    public Optional<SptsMessage> getMessageById(Long id) {
        return messageRepository.findById(id);
    }

    // Update
    public SptsMessage updateMessage(Long id, SptsMessageDTO messageDTO) {
        Optional<SptsMessage> optionalMessage = messageRepository.findById(id);
        if (optionalMessage.isPresent()) {
            SptsMessage existingMessage = optionalMessage.get();
            existingMessage.setTitle(messageDTO.getTitle());
            existingMessage.setMessage(messageDTO.getMessage());
            existingMessage.setGroupsId(messageDTO.getGroupsId());
            existingMessage.setMsgSrc(messageDTO.getMsgSrc());
            existingMessage.setStartDate(messageDTO.getStartDate());
            existingMessage.setEndDate(messageDTO.getEndDate());
            return messageRepository.save(existingMessage);
        } else {
            throw new BadRequestException("Message not found with id: " + id);
        }
    }


    // Delete
    public void deleteMessage(Long id) {
        messageRepository.deleteById(id);
    }

    public List<SptsMessage> getMessagesByGroupId(Long groupId) {
        if(groupId >=1 && groupId <=6 ){
            Date currentDate = new Date();
            return messageRepository.findByGroupsIdAndStartDateLessThanEqualAndEndDateGreaterThan(groupId, currentDate, currentDate);

        }else{
            throw new BadRequestException("Invalid group id: " + groupId);
        }
    }
}

