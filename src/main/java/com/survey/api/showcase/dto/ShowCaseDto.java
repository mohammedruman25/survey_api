package com.survey.api.showcase.dto;

import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.List;

import com.survey.api.showcase.entity.ShowcaseCategory;
import com.survey.api.user.dto.UserDTO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class ShowCaseDto implements Serializable {
		



	/**
	 * 
	 */
	private static final long serialVersionUID = -7511352041344926416L;


    @ApiModelProperty(notes = "Generated Id - Mandatory for edit Requests")
	private Long id;

	
	private String title;	

    private String description;
	
	private List<ShowCaseFilesDto> files;
	
	private UserDTO createdBy;

	private ShowcaseCategoryDto category;
	

	private Long currentApproverId;
	
	private OffsetDateTime createdDateTime;
	private OffsetDateTime expireDateTime;

	private Boolean isUpVoted;


}

