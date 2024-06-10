package com.survey.api.showcase.service;

import java.time.OffsetDateTime;

import javax.transaction.Transactional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.survey.api.common.exception.BadRequestException;
import com.survey.api.showcase.dto.ShowcaseCategoryDto;
import com.survey.api.showcase.entity.ShowcaseCategory;
import com.survey.api.showcase.repo.ShowcaseCategoryRepo;
import com.survey.api.user.entity.User;

@Service
public class ShowcaseCategoryService {

	

	@Autowired
	private ShowcaseCategoryRepo catRepo;
	


	@Autowired
    private ModelMapper modelMapper;
	

	@Transactional
	public ShowcaseCategoryDto addNewShowcaseCategory(ShowcaseCategoryDto dto, Long userId) {
		ShowcaseCategory ent = new ShowcaseCategory();
		BeanUtils.copyProperties(dto, ent, "id", "surveys");
		
		User u  = new User();
		u.setId(userId);
		ent.setUpdatedBy(u);
		ent.setUpdatedDateTime(OffsetDateTime.now());
		
		ShowcaseCategory savedEnt =catRepo.save(ent);
		ShowcaseCategoryDto savedDto = new ShowcaseCategoryDto();
		BeanUtils.copyProperties(savedEnt, savedDto);
		
		return savedDto;
		
	}
	
	@Transactional
	public ShowcaseCategoryDto updateShowcaseCategory(Long catId, ShowcaseCategoryDto dto, Long userId) {
		ShowcaseCategory ent = catRepo.getOne(catId);
		if(ent == null) {
			throw new BadRequestException("No ShowcaseCategory exists for ProramID : "+catId);
		}
		BeanUtils.copyProperties(dto, ent, "id", "surveys");
		
		User u  = new User();
		u.setId(userId);
		ent.setUpdatedBy(u);
		ent.setUpdatedDateTime(OffsetDateTime.now());
		
		ShowcaseCategory savedEnt = catRepo.save(ent);
		ShowcaseCategoryDto savedDto = new ShowcaseCategoryDto();
		BeanUtils.copyProperties(savedEnt, savedDto);
		
		return savedDto;
		
	}
	
	public Page<ShowcaseCategoryDto> getAllShowcaseCategory(Integer pageNo, Integer pageSize, String searchStr, String sortBy, Direction sortDirection){
		PageRequest page = PageRequest.of(pageNo, pageSize, Sort.by(sortDirection, sortBy));
		Page<ShowcaseCategory> findAll = catRepo.findByTitleIgnoreCaseContaining(searchStr,page);
		return findAll.map(this::getDtoFromEntity);
	}
	
	private ShowcaseCategoryDto getDtoFromEntity(ShowcaseCategory source) {
		ShowcaseCategoryDto dto = modelMapper.map(source, ShowcaseCategoryDto.class);
		return dto;
	}
	
}
