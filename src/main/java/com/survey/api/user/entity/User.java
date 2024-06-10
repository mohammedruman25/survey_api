package com.survey.api.user.entity;


import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.survey.api.config.entity.DesignationTypeMaster;
import com.survey.api.config.entity.RegionData;
import com.survey.api.config.entity.RegionTypeMaster;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "USER_ALL", indexes = {@Index(name = "emp_index1",  columnList="phone_number", unique = true),
        @Index(name = "emp_index2", columnList="user_uuid",     unique = true),
        @Index(name = "emp_index3", columnList="email",     unique = true)}
)
public class User  implements Serializable {


	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "user_uuid")
	private String userUuid;
	
	@Column(name = "user_name")
	private String userName;

	@Column(name = "phone_number")
	private String phoneNumber;
	
	@Column(name = "country_code")
	private String countryCode;

	@Column(name = "first_name")
	private String firstName;


	@Column(name = "last_name")
	private String lastName;
	
	@Column(name = "email")
	private String email;

	private String otp;
	
	private int otpIncorrectAttempt;
	
//    @Column(columnDefinition = "boolean default false")
	private Boolean accountLocked;
    

	@Column(name = "password")
	private String password;
	
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
	private Set<Role> roles = new HashSet<>();
	
	@ManyToOne(fetch=FetchType.EAGER,targetEntity=DesignationTypeMaster.class,optional=true)
	@JoinColumn(name="designation_id")
	private DesignationTypeMaster designation;

	@ManyToOne(fetch=FetchType.EAGER,targetEntity=RegionData.class,optional=true)
	@JoinColumn(name="region_data_id")
	private RegionData regionData;


}
