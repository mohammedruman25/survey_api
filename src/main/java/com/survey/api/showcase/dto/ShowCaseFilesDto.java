package com.survey.api.showcase.dto;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class ShowCaseFilesDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1027359302561342821L;

	
    @ApiModelProperty(notes = "Generated Id - Mandatory for edit Requests")
	private Long id;

    private String fileUUID;
	
	


}

