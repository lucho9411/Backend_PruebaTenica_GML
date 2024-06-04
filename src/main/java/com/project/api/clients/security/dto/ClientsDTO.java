package com.project.api.clients.security.dto;

import java.io.Serializable;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@NoArgsConstructor
@ToString
public class ClientsDTO implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	@Nullable
	@JsonProperty(required = false)
	private long id;
	@Nullable
	@JsonProperty(required = false)
	private String sharedKey;
	@NotNull
	@NotBlank
	@NotEmpty
	@Size(min = 1, max = 100)
	@JsonProperty(required = true)
	private String bussinessId;
	@NotNull
	@NotBlank
	@NotEmpty
	@Size(min = 6, max = 255)
	@Pattern(regexp = "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\\\.[A-Z]{2,6}$")
	@Email(regexp = "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\\\.[A-Z]{2,6}$")
	@JsonProperty(required = true)
	private String email;
	@NotNull
	@NotBlank
	@NotEmpty
	@Size(min = 10, max = 10)
	@Pattern(regexp = "[0-9]")
	@JsonProperty(required = true)
	private String phone;
	@Nullable
	@JsonProperty(required = false)
	private java.time.LocalDateTime dataAdded;

}
