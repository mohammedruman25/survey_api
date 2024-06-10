package com.survey.api.lookup.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DesignationTypeDTO implements Serializable  {
	
	Long id;
	String name;
	Boolean isEntityManager;
	
	

}
