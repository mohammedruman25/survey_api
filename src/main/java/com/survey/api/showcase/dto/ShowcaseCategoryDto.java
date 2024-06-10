package com.survey.api.showcase.dto;

import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.survey.api.user.dto.UserDTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class ShowcaseCategoryDto implements Serializable{
	
	



	/**
	 * 
	 */
	private static final long serialVersionUID = -4183294820273992221L;


	private Long id;

	
	private String title;
	

}
