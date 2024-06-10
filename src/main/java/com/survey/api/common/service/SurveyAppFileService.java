package com.survey.api.common.service;

import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.survey.api.common.dto.SurveyAppFileDto;
import com.survey.api.common.entity.SurveyAppFile;
import com.survey.api.common.repo.SurveyAppFileRepo;

@Service
public class SurveyAppFileService {


	@Autowired
	SurveyAppFileRepo ansFileRepo;
	


	@Autowired
    private ModelMapper modelMapper;
	
	public SurveyAppFileDto getFile(String uuid) {
		SurveyAppFile file = ansFileRepo.findOneByFileUuid(UUID.fromString(uuid));
		SurveyAppFileDto dto = modelMapper.map(file, SurveyAppFileDto.class);
		return dto;
	}
}
