package com.survey.api.config.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "config_master_region_type")
@Getter
@Setter
@NoArgsConstructor
public class RegionTypeMaster implements Serializable  {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	Long id;
	String name;
	
	@ManyToOne(fetch=FetchType.LAZY,targetEntity=RegionTypeMaster.class,optional=true)
	@JoinColumn(name="parent_id")
	private RegionTypeMaster parentRegionType;
	
	@Column(name = "is_entity_type" , columnDefinition = "boolean default false")
	private Boolean isEnityType = false; 

}
