package com.example.demo.model.requests;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@Builder
public class CreateUserRequest {

	@JsonProperty
	private String username;

	@JsonProperty
	@NotEmpty
	@NotNull
	@NotBlank(message = "password required")
	private String password;

	@JsonProperty
	@NotEmpty
	@NotNull
	@NotBlank(message = "password required")
	private String confirmPassword;
}
