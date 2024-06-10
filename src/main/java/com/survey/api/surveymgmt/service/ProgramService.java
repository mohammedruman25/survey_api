package com.survey.api.surveymgmt.service;

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
import com.survey.api.showcase.dto.ShowCaseDto;
import com.survey.api.surveymgmt.dto.ProgramDtoSmall;
import com.survey.api.surveymgmt.entity.Program;
import com.survey.api.surveymgmt.repo.ProgramRepo;
import com.survey.api.user.entity.User;

@Service
public class ProgramService {

	

	@Autowired
	private ProgramRepo pgmRepo;
	


	@Autowired
    private ModelMapper modelMapper;
	

	@Transactional
	public ProgramDtoSmall addNewProgram(ProgramDtoSmall dto, Long userId) {
		Program ent = new Program();
		BeanUtils.copyProperties(dto, ent, "id", "surveys");
		
		User u  = new User();
		u.setId(userId);
		ent.setUpdatedBy(u);
		ent.setUpdatedDateTime(OffsetDateTime.now());
		
		Program savedEnt = pgmRepo.save(ent);
		ProgramDtoSmall savedDto = new ProgramDtoSmall();
		BeanUtils.copyProperties(savedEnt, savedDto);
		
		return savedDto;
		
	}
	
	@Transactional
	public ProgramDtoSmall updateProgram(Long programId, ProgramDtoSmall dto, Long userId) {
		Program ent = pgmRepo.getOne(programId);
		if(ent == null) {
			throw new BadRequestException("No Program exists for ProramID : "+programId);
		}
		BeanUtils.copyProperties(dto, ent, "id", "surveys");
		
		User u  = new User();
		u.setId(userId);
		ent.setUpdatedBy(u);
		ent.setUpdatedDateTime(OffsetDateTime.now());
		
		Program savedEnt = pgmRepo.save(ent);
		ProgramDtoSmall savedDto = new ProgramDtoSmall();
		BeanUtils.copyProperties(savedEnt, savedDto);
		
		return savedDto;
		
	}
	
	public Page<ProgramDtoSmall> getAllProgram(Integer pageNo, Integer pageSize, String searchStr, String sortBy, Direction sortDirection){
		PageRequest page = PageRequest.of(pageNo, pageSize, Sort.by(sortDirection, sortBy));
		Page<Program> findAll = pgmRepo.findByTitleIgnoreCaseContaining(searchStr,page);
		return findAll.map(this::getDtoFromEntity);
	}
	
	private ProgramDtoSmall getDtoFromEntity(Program source) {
		ProgramDtoSmall dto = modelMapper.map(source, ProgramDtoSmall.class);
		return dto;
	}
	
}
