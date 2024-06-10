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
@Table(name = "config_master_designation_type")
@Getter
@Setter
@NoArgsConstructor
public class DesignationTypeMaster implements Serializable  {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3798700264995871544L;
	@Id
	Long id;
	
	String name;
	
	@Column(name = "import_code")
	String importCode;
	
	@ManyToOne(fetch=FetchType.LAZY,targetEntity=RegionTypeMaster.class)
	@JoinColumn(name="manager_regiontype_id")
	RegionTypeMaster managerOfRegionType;

	
}
