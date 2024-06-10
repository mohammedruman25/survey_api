package com.survey.api.surveymgmt.service;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.survey.api.auth.domain.UserAuthDetails;
import com.survey.api.common.exception.BadRequestException;
import com.survey.api.ruleengine.service.SurveyReleaseRuleEngineService;
import com.survey.api.surveymgmt.dto.QuestionDto;
import com.survey.api.surveymgmt.dto.SurveyDto;
import com.survey.api.surveymgmt.dto.SurveyReleaseData;
import com.survey.api.surveymgmt.dto.SurveySortByEnum;
import com.survey.api.surveymgmt.dto.SurveyTypeEnum;
import com.survey.api.surveymgmt.entity.Question;
import com.survey.api.surveymgmt.entity.QuestionType;
import com.survey.api.surveymgmt.entity.Survey;
import com.survey.api.surveymgmt.entity.SurveyStatus;
import com.survey.api.surveymgmt.repo.QuestionRepo;
import com.survey.api.surveymgmt.repo.QuestionTypeRepo;
import com.survey.api.surveymgmt.repo.SurveyRepo;
import com.survey.api.user.entity.User;

@Service
public class SurveyService {
	
	@Autowired
	private SurveyRepo surveyRepo;
	
	
	@Autowired
	private QuestionTypeRepo qtRepo;
	
	@Autowired
    private ModelMapper modelMapper;
	
	@Autowired
	private SurveyReleaseRuleEngineService releaseRuleServ;
	
	@Autowired
	private QuestionRepo questionRepo;
	
	@Transactional
	public SurveyDto saveNewSurvey(SurveyDto surveyDto, Long userId) {
		if(surveyDto.getId() !=null) {
			throw new BadRequestException("ID must be null to insert new Survey");
		}
		if(surveyDto.getSurveyType().equals(SurveyTypeEnum.PROFILE) && surveyDto.getExpireDateTime() != null) {
			throw new BadRequestException("PROFILE Survey must NOT have expired date time");
		}
		
		Survey sur = getEntityFromDto(surveyDto, userId);
		sur.setStatus(SurveyStatus.DRAFT);
		sur.setReleaseDateTime(null);
		sur.setSurveyReleaseData(null);	
		
		Survey saved = surveyRepo.save(sur);
		
		return getDtoFromEntity(saved);
		
	}	
	
	@Transactional
	public SurveyDto copySurvey(Long surveyId, Long userId) {
		User u  = new User();
		u.setId(userId);
		Survey surExist = surveyRepo.getOne(surveyId);
		
		if(surExist == null) {
			throw new BadRequestException("ID must exist in DB to update Survey");
		}
		SurveyDto surveyDto = getDtoFromEntity(surExist);
		surveyDto.getQuestions().forEach(q -> {
			q.setId(null);
			if(q.getChildQuestions() != null)
				q.getChildQuestions().forEach(ch -> ch.setId(null));
		});
		Survey sur = getEntityFromDto(surveyDto, userId);
		sur.setId(null);
		sur.setStatus(SurveyStatus.DRAFT);
		sur.setReleaseDateTime(null);
		sur.setSurveyReleaseData(null);	
		
		
		
		Survey saved = surveyRepo.save(sur);
		
		return getDtoFromEntity(saved);
		
	}	
	
	public SurveyDto getOneSurvey(Long surveyId) {
		
		Survey surExist = surveyRepo.getOne(surveyId);
		
		if(surExist == null) {
			throw new BadRequestException("ID must exist in DB to get Survey");
		}
		
		
		return getDtoFromEntity(surExist);
		
	}	
	
	
	@Transactional
	public SurveyDto updateSurvey(SurveyDto surveyDto, Long surveyId, Long userId) {
		if(surveyDto.getSurveyType().equals(SurveyTypeEnum.PROFILE) && surveyDto.getExpireDateTime() != null) {
			throw new BadRequestException("PROFILE Survey must NOT have expired date time");
		}
		User u  = new User();
		u.setId(userId);
		Survey surExist = surveyRepo.getOne(surveyId);
		
		if(surExist == null) {
			throw new BadRequestException("ID must exist in DB to update Survey");
		}else if(surExist.getStatus().equals(SurveyStatus.RELEASED)) {
			throw new BadRequestException("Survey is already released can not be updated");
		}else if(surExist.getStatus().equals(SurveyStatus.DELETED)) {
			throw new BadRequestException("Survey is already deleted can not be updated");
		}
		
		Survey sur = getEntityFromDto(surveyDto, userId);
		sur.setId(surveyId);
		sur.setStatus(SurveyStatus.DRAFT);
		sur.setReleaseDateTime(null);
		sur.setSurveyReleaseData(null);	
		Survey save = surveyRepo.save(sur);
		//saveQuestions(sur);
		
		return getDtoFromEntity(save);
		
	}
	
	private void saveQuestions(Survey sur) {
		if(sur.getQuestions() != null) {

			sur.getQuestions().forEach(q -> {
				questionRepo.save(q);
				if(q.getChildQuestions() != null) {
					q.getChildQuestions().forEach(chq -> questionRepo.save(chq));
				}
				
			});
		}
		
	}

	@Transactional
	public SurveyDto updateSurveyExpiry(SurveyDto surveyDto, Long surveyId, Long userId) {
		User u  = new User();
		u.setId(userId);
		Survey surExist = surveyRepo.getOne(surveyId);
		
		if(surExist == null) {
			throw new BadRequestException("ID must exist in DB to update Survey");
		}else if(surExist.getStatus().equals(SurveyStatus.DELETED)) {
			throw new BadRequestException("Survey is already deleted can not be updated");
		}else if(surExist.getSurveyType().equals(SurveyTypeEnum.PROFILE)) {
			throw new BadRequestException("PROFILE survey can not have expiry date");
		}
		
		//only expiry date
		surExist.setExpireDateTime(surveyDto.getExpireDateTime());
		
		
		return getDtoFromEntity(surExist);
		
	}
	
	@Transactional
	public SurveyDto releaseSurvey(Long surveyId, List<SurveyReleaseData> surRelDto, Long userId) {
		User u  = new User();
		u.setId(userId);
		Survey sur = surveyRepo.getOne(surveyId);
		
		if(sur == null) {
			throw new BadRequestException("No survey exists with ID "+surveyId);
		}else if(sur.getStatus().equals(SurveyStatus.RELEASED)) {
			throw new BadRequestException("Survey is already released");
		}else if(sur.getStatus().equals(SurveyStatus.DELETED)) {
			throw new BadRequestException("Survey is already deleted, cannot be released");
		}
		
		//save survey release mapping
		releaseRuleServ.generateSurvetReleasemapping(surveyId, surRelDto,sur.getSurveyType());
		
		sur.setStatus(SurveyStatus.RELEASED);
		sur.setUpdatedBy(u);
		sur.setUpdatedDateTime(OffsetDateTime.now());
		sur.setReleaseDateTime(OffsetDateTime.now());
		sur.setSurveyReleaseData(surRelDto);
		
		Survey saved = surveyRepo.save(sur);
		
		return getDtoFromEntity(saved);
	}
	
	@Transactional
	public String deleteSurvey(Long surveyId, Long userId) {
		User u  = new User();
		u.setId(userId);
		Survey sur = surveyRepo.getOne(surveyId);
		
		if(sur == null) {
			throw new BadRequestException("No survey exists with ID "+surveyId);
		}else if(sur.getStatus().equals(SurveyStatus.RELEASED)) {
			throw new BadRequestException("Survey is already released, cannot be deleted");
		}else if(sur.getStatus().equals(SurveyStatus.DELETED)) {
			throw new BadRequestException("Survey is already deleted");
		}
		
		surveyRepo.delete(sur);
		// dont have to delete the rows in survey release mapping data tables because that only contains info aout released surveys, and released surveys cannot be deleted
		
		return "Survey Deleted";
	}
	
	
	
	private Survey getEntityFromDto(SurveyDto surveyDto, Long userId) {
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.addMappings(new PropertyMap<SurveyDto, Survey>() {
			@Override
			protected void configure() {
				skip(destination.getId());
				skip(destination.getUpdatedBy());
				skip(destination.getUpdatedDateTime());
			}
		});
		Survey sur = modelMapper.map(surveyDto, Survey.class);
		
		User u  = new User();
		u.setId(userId);
		sur.setUpdatedBy(u);
		sur.setUpdatedDateTime(OffsetDateTime.now());

		for (Question q : sur.getQuestions()) {
			q.setSurvey(sur);
			if (q.getChildQuestions() != null)
				q.getChildQuestions().forEach(ch -> {
					ch.setParentQuestion(q);
					ch.setSurvey(sur);
				});
		}

		return sur;
	}
	
	
	private SurveyDto getDtoFromEntity(Survey survey) {
		List<Question> questions = survey.getQuestions();
		List<QuestionDto> qDtoLst = questions.stream().map(q -> {
			QuestionDto qd = modelMapper.map(q, QuestionDto.class);
			return qd;
		}).collect(Collectors.toList());
		
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.addMappings(new PropertyMap<Survey, SurveyDto>() {
			@Override
			protected void configure() {
				skip(destination.getQuestions());
			}
		});
		
		List<Long> childQuestions = new ArrayList<Long>();
		qDtoLst.forEach(q -> {
			if(q.getChildQuestions() != null) {
				q.getChildQuestions().forEach(ch ->childQuestions.add(ch.getId()));
			}
		});
		List<QuestionDto> filtered = qDtoLst.stream().filter(q -> !childQuestions.contains(q.getId())).collect(Collectors.toList());
		
		SurveyDto sur = modelMapper.map(survey, SurveyDto.class);
		sur.setQuestions(filtered);
		return sur;
	}
	

	public Page<SurveyDto> getAllSurvey(Integer pageNo, Integer pageSize, String searchStr, SurveySortByEnum sortBy, Direction sortDirection) {
		PageRequest page = PageRequest.of(pageNo, pageSize, Sort.by(sortDirection, sortBy.name()));
		Page<Survey> allSur = surveyRepo.findByTitleIgnoreCaseContainingAndStatusNot(searchStr,SurveyStatus.DELETED, page);
		Page<SurveyDto> allSurDto = allSur.map(this::getDtoFromEntity);
		return allSurDto;
	}
	
	@Transactional
	public void setSurveyExpired() {
		List<Survey> allExpiredSurvey = surveyRepo.getAllExpiredSurvey(SurveyStatus.RELEASED);
		allExpiredSurvey.forEach(s -> {
			releaseRuleServ.updateSurveyExpired(s.getId(), s.getSurveyType());
		});
		surveyRepo.setSurveyExpired(SurveyStatus.RELEASED.name(),SurveyStatus.EXPIRED.name());
		
		
	}
	
	
	
//	public Page<SurveyDto> getAllActivity(Integer pageNo, Integer pageSize, String searchStr, SurveySortByEnum sortBy, Direction sortDirection, UserAuthDetails userPrincipal) {
//		
//		List<Long> surveyIdForActivity = releaseRuleServ.getSurveyIdForActivity(userPrincipal.getDesignationType().getId(), userPrincipal.getRegionData().getId(),false);
//		
//		PageRequest page = PageRequest.of(pageNo, pageSize, Sort.by(sortDirection, sortBy.name()));
//		Page<Survey> allSur = surveyRepo.findByTitleIgnoreCaseContainingAndStatusAndIdIn(searchStr,SurveyStatus.RELEASED, surveyIdForActivity, page);
//		Page<SurveyDto> allSurDto = allSur.map(this::getDtoFromEntity);
//		return allSurDto;
//	}
	
	public List<QuestionType> getAllQuestionType(){
		return qtRepo.findAll();
	}

	public Page<SurveyDto> getAllActivityProfileType(Integer pageNo, Integer pageSize, String searchStr,
			SurveySortByEnum sortBy, Direction sortDirection, UserAuthDetails userPrincipal) {
		// TODO Auto-generated method stub
		return null;
	}
	

}
