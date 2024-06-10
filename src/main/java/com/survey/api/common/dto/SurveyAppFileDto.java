package com.survey.api.common.dto;

import java.io.Serializable;
import java.util.UUID;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class SurveyAppFileDto  implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 324140107205654622L;
	
	
	private UUID fileUuid;
	
	
	
	
    private byte[] fileData;
	
	
	private String originalFileName;

}
