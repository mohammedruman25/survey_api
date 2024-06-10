package com.survey.api.showcase.entity;

import java.io.Serializable;
import java.time.OffsetDateTime;
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

import com.survey.api.surveymgmt.entity.Program;
import com.survey.api.user.entity.User;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "SHOWCASE")
@NoArgsConstructor
@Getter
@Setter
public class ShowCase implements Serializable {
		



	/**
	 * 
	 */
	private static final long serialVersionUID = -7511352041344926416L;


	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(notes = "Generated Id - Mandatory for edit Requests")
	private Long id;

	
	private String title;
	

	@Column(columnDefinition="TEXT")
    private String description;
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "showCase", orphanRemoval = true)
	private List<ShowCaseFiles> files;
	
	@OneToOne
	User createdBy;
	
	@ManyToOne
	@JoinColumn(name =  "category_id", nullable = true)
	private ShowcaseCategory category;
	
	@Column(name = "current_approver_id")
	private Long currentApproverId;
	
	@Column(name = "created_date_time")
	private OffsetDateTime createdDateTime;
	

	@Column(name = "expire_date_time")
	private OffsetDateTime expireDateTime;
	
	
	@Column(columnDefinition = "boolean default false")
	private Boolean isUpVoted = false;
	
	@Column(columnDefinition = "boolean default false")
	private Boolean isExpired = false;


}

