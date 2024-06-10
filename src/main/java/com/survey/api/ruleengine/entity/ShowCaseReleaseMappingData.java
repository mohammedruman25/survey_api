package com.survey.api.ruleengine.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Index;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "SHOWCASE_RELEASE_MAPPING_DATA", indexes = {@Index(name = "showcase_rep_mapping_index1",  columnList="showcase_id", unique = false),
      @Index(name = "showcase_rep_mapping_index2", columnList="region_data_id",     unique = false)})
@NoArgsConstructor
@Getter
@Setter
@ToString
public class ShowCaseReleaseMappingData {

	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "showcase_id")
	private Long showcaseId;
	
	@Column(name = "region_data_id")
	private Long regionDataId;
	

	@Column(name = "is_expired" , columnDefinition = "boolean default false")
	private Boolean isExpired = false;

	
}
