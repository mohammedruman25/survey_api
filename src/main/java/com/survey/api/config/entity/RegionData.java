package com.survey.api.config.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Table(name = "config_data_region", indexes = {@Index(name = "config_data_region_index1",  columnList="code", unique = true)})
@Getter
@Setter
@NoArgsConstructor
public class RegionData implements Serializable  {
	

	private static final long serialVersionUID = 5340721702916281283L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	
	private Long id;
	
	private String name;
	
	private String code;
	
	private String type;
	
	@ManyToOne(fetch=FetchType.EAGER,targetEntity=RegionTypeMaster.class)
    @JoinColumn(name="region_type_id")
	private RegionTypeMaster regionType;
	
	@ManyToOne(fetch=FetchType.EAGER,targetEntity=RegionData.class)
    @JoinColumn(name="region_parent_id")
	private RegionData parentRegion;

}
