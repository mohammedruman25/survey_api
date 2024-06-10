package com.survey.api.showcase.entity;

import java.io.Serializable;
import java.util.UUID;

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

@Entity
@Table(name = "SHOWCASE_FILES")
@NoArgsConstructor
@Getter
@Setter
public class ShowCaseFiles implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1027359302561342821L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(notes = "Generated Id - Mandatory for edit Requests")
	private Long id;

	@ManyToOne
	private ShowCase showCase;
	

	
    private UUID fileUUID;
	
	


}

