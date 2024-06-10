package com.survey.api.auth.domain;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.survey.api.config.entity.DesignationTypeMaster;
import com.survey.api.config.entity.RegionData;
import com.survey.api.user.entity.User;


public class UserAuthDetails implements UserDetails {
	private static final long serialVersionUID = 1L;

	private Long id;
	private String phoneNo;

	private String userUuid;

	private String username;
	
	private String firstName;
	
	private String lastName;	
	
	List<String> roles;
	
	private DesignationTypeMaster designationType;
	
	private RegionData regionData;
	
	@JsonIgnore
	private String password;
	
	private Collection<? extends GrantedAuthority> authorities;

	public UserAuthDetails(Long id, String userUuid, String password,
			Collection<? extends GrantedAuthority> authorities) {
		this.id = id;
		this.userUuid = userUuid;
		this.password = password;
		this.authorities = authorities;
	}

	public UserAuthDetails(Long id, String phoneNo, String userUuid, String username, String firstName, String lastName,
			List<String> roles, String password,DesignationTypeMaster designationType, RegionData regionData, Collection<? extends GrantedAuthority> authorities) {
		super();
		this.id = id;
		this.phoneNo = phoneNo;
		this.userUuid = userUuid;
		this.username = username;
		this.firstName = firstName;
		this.lastName = lastName;
		this.roles = roles;
		this.password = password;
		this.designationType = designationType;
		this.regionData =regionData;
		this.authorities = authorities;
	}

	public String getUsername() {
		return username;
	}

	public String getUserUuid() {
		return userUuid;
	}

	public static UserAuthDetails build(User user) {
		List<GrantedAuthority> authorities = user.getRoles().stream()
				.map(role -> new SimpleGrantedAuthority(role.getName().name()))
				.collect(Collectors.toList());
		List<String> roles= user.getRoles().stream().map(e -> e.getName().toString()).collect(Collectors.toList());
		return new UserAuthDetails(
				user.getId(), 
				user.getPhoneNumber(),
				user.getUserName(),
				user.getUserUuid(),
				user.getFirstName(),
				user.getLastName(),
				roles,
				user.getOtp(),
				user.getDesignation(),
				user.getRegionData(),
				authorities);
	}


	public List<String> getRoles() {
		return roles;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}

	public Long getId() {
		return id;
	}
	public String getPhoneNo() {
		return phoneNo;
	}

	@Override
	public String getPassword() {
		return password;
	}

	
	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		UserAuthDetails user = (UserAuthDetails) o;
		return Objects.equals(id, user.id);
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setUserUuid(String userUuid) {
		this.userUuid = userUuid;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}

	public void setRoles(List<String> roles) {
		this.roles = roles;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setAuthorities(Collection<? extends GrantedAuthority> authorities) {
		this.authorities = authorities;
	}

	public DesignationTypeMaster getDesignationType() {
		return designationType;
	}

	public void setDesignationType(DesignationTypeMaster designationType) {
		this.designationType = designationType;
	}

	public RegionData getRegionData() {
		return regionData;
	}

	public void setRegionData(RegionData regionData) {
		this.regionData = regionData;
	}


}
