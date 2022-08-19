package com.prashant.payload;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JWTAuthResponse {
	
	private String accessToken;
	
	private String tokenType = "Bearer";

	public JWTAuthResponse(String accessToken) {
		super();
		this.accessToken = accessToken;
	}
	

}
