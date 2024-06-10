package com.survey.api.surveymgmt.entity;

import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.survey.api.config.entity.DesignationTypeMaster;
import com.survey.api.config.entity.RegionData;
import com.survey.api.user.entity.User;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "PROGRAM")
@NoArgsConstructor
@Getter
@Setter
public class Program implements Serializable {
		
	/**
	 * 
	 */
	private static final long serialVersionUID = 3551002620370518444L;


	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(notes = "Generated Id - Mandatory for edit Requests")
	@Column(name = "program_id", nullable = false)
	private Long id;

	
	private String title;
	

	@Column(columnDefinition="TEXT")
    private String description;
	
//	@OneToMany(cascade = CascadeType.DETACH, fetch = FetchType.LAZY, mappedBy = "program")
//	@JsonIgnore
//	private List<Survey> surveys;
	
	
	@OneToOne
	User updatedBy;
	
	@Column(name = "updated_date_time")
	private OffsetDateTime updatedDateTime;
	
	


}

