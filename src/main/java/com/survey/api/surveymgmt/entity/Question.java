package com.survey.api.surveymgmt.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Type;

import com.survey.api.common.entity.BaseEntity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "QUESTION")
@NoArgsConstructor
@Getter
@Setter
public class Question extends BaseEntity implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 836318903627721457L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(notes = "Generated Id - Mandatory for edit Requests")
	private Long id;
	
	@ManyToOne
	@JoinColumn(name =  "survey_id", nullable = false)
	private Survey survey;
	
	@Type(type = "json")
	@Column(columnDefinition = "json")
	private QuestionContent content;
	
	@OneToOne
	private QuestionType questionType;
	
	@Column(columnDefinition="TEXT")
    private String showCondition;
	
	@Column(name="isOptional", columnDefinition = "boolean default true")
    private Boolean isOptional = true;
	
	@Transient
	@Column(name = "parent_question_id", nullable = true)
    private Long parentQuestionId;
	
	@ManyToOne(fetch = FetchType.LAZY, optional=true, cascade = CascadeType.ALL)
    @JoinColumn(name="parent_question_id")
    private Question parentQuestion;
    
    @OneToMany(mappedBy="parentQuestion", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval=true)
    private List<Question> childQuestions = new ArrayList<Question>();

}
