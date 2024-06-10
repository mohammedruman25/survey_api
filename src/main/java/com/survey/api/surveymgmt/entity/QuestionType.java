package com.survey.api.surveymgmt.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "QUESTIONTYPE")
@NoArgsConstructor
@Getter
@Setter
public class QuestionType {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(notes = "Generated Id - Mandatory for edit Requests")
	private Long id;
	
	@Column(length = 30)
	private String type;

}
