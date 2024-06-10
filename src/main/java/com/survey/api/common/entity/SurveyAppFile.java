package com.survey.api.common.entity;

import java.io.Serializable;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "SURVEY_APP_FILES")
@NoArgsConstructor
@Getter
@Setter
public class SurveyAppFile extends BaseEntity implements Serializable  {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1648963421463021496L;

	@Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
        name = "UUID",
        strategy = "org.hibernate.id.UUIDGenerator"
    )
    @ApiModelProperty(notes = "Generated Id - Mandatory for edit Requests")
	private UUID fileUuid;
	
	
	
	
	@Type(type="org.hibernate.type.BinaryType")
    @Column(name = "file_data", columnDefinition="BYTEA")
    private byte[] fileData;
	
	
	private String originalFileName;
}
