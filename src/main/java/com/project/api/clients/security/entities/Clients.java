package com.project.api.clients.security.entities;

import java.io.Serializable;
import org.springframework.data.annotation.Id;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import jakarta.annotation.Nullable;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Data
@Builder
@Table(name = "clients")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Clients implements Serializable, Persistable<Long>{

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
	@Nullable
	@Column(value = "shared_key")
	private String sharedKey;
	@NotNull
	@Size(min = 1, max = 100)
	@Column(value = "bussiness_id")
	private String bussinessId;
	@NotNull
	@Size(min = 6, max = 255)
	@Pattern(regexp = "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\\\\\\\.[A-Z]{2,6}$")
	@Email(regexp = "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\\\\\\\.[A-Z]{2,6}$")
	@Column(value = "email")
	private String email;
	@NotNull
	@Size(min = 10, max = 10)
	@Pattern(regexp = "[0-9]")
	@Column(value = "phone")
	private String phone;
	@Nullable
	@Column(value = "data_added")
	private java.time.LocalDateTime dataAdded;
	
	@Override
	public Long getId() {
		return this.id;
	}
	
	@Override
	public boolean isNew() {
		return id == 0;
	}
}
