package com.project.api.clients.security.dto;

import java.io.Serializable;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@NoArgsConstructor
@ToString
public class LoginDTO implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	@NotNull
	@NotBlank
	@NotEmpty
	@Size(min = 6, max = 255)
	@JsonProperty(required = true)
	private String email;
	@NotNull
	@NotBlank
	@NotEmpty
	@Size(min = 1, max = 255)
	@JsonProperty(required = true)
	private String password;

}
