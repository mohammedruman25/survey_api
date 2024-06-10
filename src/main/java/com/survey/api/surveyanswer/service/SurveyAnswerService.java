package com.survey.api.surveyanswer.service;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.survey.api.common.dto.SurveyAppFileDto;
import com.survey.api.common.entity.SurveyAppFile;
import com.survey.api.common.exception.BadRequestException;
import com.survey.api.common.exception.ObjectNotFoundException;
import com.survey.api.common.repo.SurveyAppFileRepo;
import com.survey.api.ruleengine.service.SurveyReleaseRuleEngineService;
import com.survey.api.surveyanswer.dto.SurveyAnswerDto;
import com.survey.api.surveyanswer.entity.Answer;
import com.survey.api.surveyanswer.entity.AnswerContent;
import com.survey.api.surveyanswer.entity.SurveyAttempt;
import com.survey.api.surveyanswer.repo.AnswerRepo;
import com.survey.api.surveyanswer.repo.SurveyAttemptRepo;
import com.survey.api.surveymgmt.dto.SurveyTypeEnum;
import com.survey.api.surveymgmt.entity.Survey;
import com.survey.api.surveymgmt.repo.SurveyRepo;
import com.survey.api.user.entity.User;

@Service
public class SurveyAnswerService {
	
	@Autowired
	SurveyAttemptRepo surAttemptRepo;
	
	@Autowired
	AnswerRepo ansRepo;
	
	@Autowired
	SurveyAppFileRepo ansFileRepo;
	

	@Autowired
	private SurveyRepo surveyRepo;
	

	@Autowired
	private SurveyReleaseRuleEngineService releaseRuleServ;
	
	
	

	@Autowired
    private ModelMapper modelMapper;
	
	@Transactional
	public SurveyAnswerDto addNewSurveyAnswer(SurveyAnswerDto dto, Long surveyId, Long userId, Long designationId, Long regionId) {
		
		if(dto.getId() !=null) {
			throw new BadRequestException("ID must be null to insert new Survey Answer");
		}
		if(!(dto.getIsSurveyAnsweredComplete() || dto.getIsSurveyInProgress())) {
			throw new BadRequestException("Survey can be either in Progress or completely answered !");
		}
		
		SurveyAttempt existSurAttempt = surAttemptRepo.findOneBySurvey_idAndUser_idAndTargetEntityDataId(surveyId, userId, dto.getTargetEntityDataId());
		if(existSurAttempt != null) {
			if(existSurAttempt.getIsSurveyAnsweredComplete()) {
				throw new BadRequestException("Already answered this survey completely");
			}else {
				surAttemptRepo.delete(existSurAttempt);
			}
		}
		
		SurveyAttempt surAttempt = getEntityFromDto(dto, surveyId, userId);
		

		SurveyAttempt surAtmpEnt = surAttemptRepo.save(surAttempt);
		
		SurveyAnswerDto returnDto = getDtoFromEntity(surAtmpEnt);
		
		Survey sur = surveyRepo.getOne(surveyId);
		if(sur.getSurveyType().equals(SurveyTypeEnum.ENTITY)) {
			releaseRuleServ.updateSurveyAnsweredENTITY(surveyId, designationId, dto.getTargetEntityDataId(),dto.getIsSurveyAnsweredComplete(),dto.getIsSurveyInProgress());
		}else if(sur.getSurveyType().equals(SurveyTypeEnum.PROFILE)){
			releaseRuleServ.updateSurveyAnsweredPROFILE(surveyId, designationId, regionId,dto.getIsSurveyAnsweredComplete(),dto.getIsSurveyInProgress());
		}else if(sur.getSurveyType().equals(SurveyTypeEnum.OPEN)){
			releaseRuleServ.updateSurveyAnsweredOPEN(surveyId, designationId, regionId,dto.getIsSurveyAnsweredComplete(),dto.getIsSurveyInProgress());

		}
		
		
		
		return returnDto;
	}
	
	
	@Transactional
	public String uploadAnswerFiles(List<byte[]> files, List<String> originalFileNames, Long answerId) {
		List<String> fileUuids = new ArrayList<String>();
				
		int i = 0;
		for(byte[] f : files) {

			SurveyAppFile appFile = new SurveyAppFile();
			appFile.setFileData(f);
			appFile.setOriginalFileName(originalFileNames.get(i));
			
			SurveyAppFile savedAppFile = ansFileRepo.save(appFile);
			fileUuids.add(savedAppFile.getFileUuid().toString());
			i++;
		}
		
		Answer one = ansRepo.getOne(answerId);
		AnswerContent answerContent = one.getAnswerContent();
		if(answerContent==null) {
			answerContent = new AnswerContent();
		}
		
		answerContent.setFilePaths(fileUuids);
		one.setAnswerContent(answerContent);
		
		return "files uploaded successfully with UUID : "+ fileUuids;
	}
	
	@Transactional
	public String deleteAnswerFiles(Long answerId) {
		
		Answer one = ansRepo.getOne(answerId);
		AnswerContent answerContent = one.getAnswerContent();
		if(answerContent!=null && answerContent.getFilePaths() != null && ! answerContent.getFilePaths().isEmpty()) {
			List<String> fileUuids = answerContent.getFilePaths();
			
			for(String uuid: fileUuids) {
				ansFileRepo.deleteByFileUuid(UUID.fromString(uuid));
			}
			
			answerContent.setFilePaths(null);
			one.setAnswerContent(answerContent);

			return "Files deleted for this answer " + answerId;
		}else {
			return "no files present for this answer " + answerId;
		}
		
	}
	
	public SurveyAppFileDto getFile(String uuid) {
		SurveyAppFile file = ansFileRepo.findOneByFileUuid(UUID.fromString(uuid));
		SurveyAppFileDto dto = modelMapper.map(file, SurveyAppFileDto.class);
		return dto;
	}

	private SurveyAnswerDto getDtoFromEntity(SurveyAttempt surAtmpEnt) {
		SurveyAnswerDto returnDto = modelMapper.map(surAtmpEnt, SurveyAnswerDto.class);
		returnDto.setSurveyId(surAtmpEnt.getSurvey().getId());
		return returnDto;
	}
	
	public List<SurveyAnswerDto> getSurveyAnswers(Long surveyId){
		List<SurveyAttempt> surAttmtLst = surAttemptRepo.findBySurvey_id(surveyId);
		List<SurveyAnswerDto> lstDto = surAttmtLst.stream().map(this::getDtoFromEntity).collect(Collectors.toList());
		return lstDto;
	}
	
	public SurveyAnswerDto getSurveyAnswer(Long surveyId, Long userId, Long targetDataEntityId) {
		SurveyAttempt surAttempt = surAttemptRepo.findOneBySurvey_idAndUser_idAndTargetEntityDataId(surveyId, userId, targetDataEntityId);
		if(surAttempt == null) {
			throw new ObjectNotFoundException("No Survey attempt exist");
		}
		return getDtoFromEntity(surAttempt);
	}
	

	private SurveyAttempt getEntityFromDto(SurveyAnswerDto dto, Long surveyId, Long userId) {
		SurveyAttempt surAttempt = modelMapper.map(dto, SurveyAttempt.class);
		
		surAttempt.setAttemptDateTime(OffsetDateTime.now());
		
		User u = new User();
		u.setId(userId);
		surAttempt.setUser(u);
		
		Survey sur = new Survey();
		sur.setId(surveyId);
		
		surAttempt.setSurvey(sur);
		
		surAttempt.getAnswers().forEach(a -> a.setSurveyAttempt(surAttempt));
		return surAttempt;
	}
	
}
