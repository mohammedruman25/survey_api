package com.survey.api.surveymgmt.dto;

import java.io.Serializable;
import java.util.List;

import com.survey.api.lookup.dto.DesignationTypeDTO;
import com.survey.api.lookup.dto.RegionDataDTO;
import com.survey.api.lookup.dto.RegionTypeDTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class SurveyReleaseData implements Serializable {

	/**
	* 
	*/
	private static final long serialVersionUID = 1585408067360738212L;
	
	DesignationTypeDTO designationTypeDto;
	RegionTypeDTO regionTypeDto;
	List<RegionDataDTO> regionDataDtoList;

}
