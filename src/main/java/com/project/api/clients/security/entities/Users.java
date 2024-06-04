package com.project.api.clients.security.entities;

import java.io.Serializable;
import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.springframework.data.annotation.Id;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Data
@Builder
@Table(name = "users")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Users implements Serializable, Persistable<Long>, UserDetails{

	private static final long serialVersionUID = 1L;
	
	@Id
    private long id;
	@Column(value = "email")
	private String email;
	@JsonIgnore
	@Column(value = "password")
	private String password;
	@Column(value = "status")
	private int status;
	private String roles;
	 
	@Override
	public Long getId() {
		return this.id;
	}
	
	@Override
	public boolean isNew() {
		return id == 0;
	}
	
	 @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
		 return Stream.of(roles.split(", ")).map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
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
	 public String getUsername() {
		return email;
	 }
	

}
