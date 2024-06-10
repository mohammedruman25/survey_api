package com.survey.api.showcase.service;

import java.time.OffsetDateTime;
import java.time.temporal.TemporalUnit;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.survey.api.common.dto.SurveyAppFileDto;
import com.survey.api.common.entity.SurveyAppFile;
import com.survey.api.common.exception.BadRequestException;
import com.survey.api.common.repo.SurveyAppFileRepo;
import com.survey.api.ruleengine.service.ShowCaseVisibilityRuleEngine;
import com.survey.api.showcase.dto.ShowCaseDto;
import com.survey.api.showcase.entity.ShowCase;
import com.survey.api.showcase.entity.ShowCaseFiles;
import com.survey.api.showcase.repo.ShowCaseRepo;
import com.survey.api.user.entity.User;

@Service
public class ShowCaseService {
	
	@Autowired
	ShowCaseRepo repo;
	
	@Autowired
	SurveyAppFileRepo fileRepo;
	
	@Autowired
	ShowCaseVisibilityRuleEngine shRule;
	


	@Autowired
    private ModelMapper modelMapper;
	
	@Transactional
	public ShowCaseDto addNewShowCase(ShowCaseDto dto , Long userId, Long regionDataId) {
		
		
		ShowCase shCase = getEntityFromDto(dto, userId);
		shCase.setCurrentApproverId(shRule.getApproverId(userId, regionDataId));
		ShowCase save = repo.save(shCase);
		ShowCaseDto savedDto = getDtoFromEntity(save);

		shRule.createShowcaseMapping(savedDto.getId(), regionDataId);
		return savedDto;
	
		
	}
	
	public Page<ShowCaseDto> getAllShowCase(Integer pageNo, Integer pageSize, String searchStr, String sortBy, Direction sortDirection, Long regionDataId){
		PageRequest page = PageRequest.of(pageNo, pageSize, Sort.by(sortDirection, sortBy));
		List<Long> showcaseList = shRule.getShowcaseList(regionDataId);
		Page<ShowCase> findAll = repo.findByTitleIgnoreCaseContainingAndIdIn(searchStr, showcaseList,page);
		return findAll.map(this::getDtoFromEntity);
	}
	
	public List<ShowCaseDto> getAllShowCaseWaitingForUpVote(Long userId){
		
		List<ShowCase> findAll = repo.findByCurrentApproverId(userId);
		return findAll.stream().map(this::getDtoFromEntity).collect(Collectors.toList());
	}
	
	public Page<ShowCaseDto> getOwnShowCase(Integer pageNo, Integer pageSize,Long userId,  String sortBy, Direction sortDirection){
		User u = new User();
		u.setId(userId);
		PageRequest page = PageRequest.of(pageNo, pageSize, Sort.by(sortDirection, sortBy));
		Page<ShowCase> findAll = repo.findByCreatedByAndIsUpVoted(u, false, page);
		return findAll.map(this::getDtoFromEntity);
	}
	
	@Transactional
	public ShowCaseDto updateShowCase(Long showCaseId, ShowCaseDto dto , Long userId) {
		
		ShowCase shCase = repo.getOne(showCaseId);
		if(shCase.getIsUpVoted()) {
			throw new BadRequestException("Can not edit show case, its already upVoted");
		}else if(shCase.getCreatedBy().getId() != userId) {
			throw new BadRequestException("Showcase can only by edited by the cretor of the showcase");
		}
		BeanUtils.copyProperties(dto, shCase, "id", "currentApproverId", "isUpVoted", "isExpired", "files" );
		setAuditData(userId, shCase);
		ShowCase save = repo.save(shCase);
		ShowCaseDto savedDto = getDtoFromEntity(save);
		return savedDto;
	
		
	}
	
	@Transactional
	public ShowCaseDto upVoteShowCase(Long showCaseId, Long userId, Long regionDataId) {
		
		ShowCase shCase = repo.getOne(showCaseId);
		if(shCase.getCurrentApproverId() != userId) {
			throw new BadRequestException("Permission denied");
		}
		shRule.upvoteShowcase(showCaseId, regionDataId);
		shCase.setCurrentApproverId(shRule.getApproverId(userId, regionDataId));
		
		shCase.setIsUpVoted(true);
		ShowCase save = repo.save(shCase);
		ShowCaseDto savedDto = getDtoFromEntity(save);
		return savedDto;
	
		
	}
	
	@Transactional
	public String deleteShowCase(Long showCaseId, Long userId) {
		ShowCase shCase = repo.getOne(showCaseId);
		if(shCase.getIsUpVoted() != null && shCase.getIsUpVoted()) {
			throw new BadRequestException("Can not delete show case, its already upVoted");
		}else if(shCase.getCreatedBy().getId() != userId) {
			throw new BadRequestException("Showcase can only by deleted by the cretor of the showcase");
		}

		shRule.deleteShowcaseReleaseMapping(showCaseId);
		List<UUID> fileUuids = shCase.getFiles().stream().map(f -> f.getFileUUID()).collect(Collectors.toList());
		repo.deleteById(showCaseId);
		
		fileUuids.forEach(u -> fileRepo.deleteByFileUuid(u));
		
		return "Showcase deleted";
	}
	
	@Transactional
	public void setShowCaseExpired() {
		repo.setShowCaseExpired();
	}
	

	@Transactional
	public ShowCaseDto addShhowCaseFiles(Long showCaseId, List<SurveyAppFileDto> fileDtos, Long userId) {
		ShowCase shCase = repo.getOne(showCaseId);
		fileDtos.forEach(fDto -> {
			SurveyAppFile file = modelMapper.map(fDto, SurveyAppFile.class);
			SurveyAppFile savedEnt = fileRepo.save(file);
			ShowCaseFiles shFile = new ShowCaseFiles();
			shFile.setShowCase(shCase);
			shFile.setFileUUID(savedEnt.getFileUuid());
			shCase.getFiles().add(shFile);
		});
		
		ShowCase save = repo.save(shCase);
		ShowCaseDto dto = getDtoFromEntity(save);
		return dto;
	}
	
	@Transactional
	public ShowCaseDto deleteShowCaseFile(Long showCaseId, String fileUUId, Long userId) {
		ShowCase shCase = repo.getOne(showCaseId);
		ShowCaseFiles file = shCase.getFiles().stream().filter(f -> f.getFileUUID().equals(UUID.fromString(fileUUId)))
		.findFirst()
		.orElseThrow(() -> new BadRequestException("No file exists -- "+fileUUId));
		
		shCase.getFiles().remove(file);
		fileRepo.deleteByFileUuid(UUID.fromString(fileUUId));
		
		ShowCase save = repo.save(shCase);
		ShowCaseDto dto = getDtoFromEntity(save);
		return dto;
	}
	
	private ShowCase getEntityFromDto(ShowCaseDto dto, Long userId) {
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.addMappings(new PropertyMap<ShowCaseDto, ShowCase>() {
			@Override
			protected void configure() {
				skip(destination.getIsUpVoted());
				skip(destination.getCreatedBy());
				skip(destination.getCreatedDateTime());
			}
		});
		ShowCase showCase = modelMapper.map(dto, ShowCase.class);
		setAuditData(userId, showCase);

		if(showCase.getFiles() != null) {
			showCase.getFiles().forEach(f -> f.setShowCase(showCase));			
		}
		return showCase;
	}

	private void setAuditData(Long userId, ShowCase showCase) {
		showCase.setCreatedDateTime(OffsetDateTime.now());
		showCase.setExpireDateTime(showCase.getCreatedDateTime().plusDays(60));

		User u = new User();
		u.setId(userId);
		showCase.setCreatedBy(u);
	}
	
	private ShowCaseDto getDtoFromEntity(ShowCase sh) {
		ShowCaseDto dto = modelMapper.map(sh, ShowCaseDto.class);
		return dto;
	}
}
