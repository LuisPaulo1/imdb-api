package com.imdb.core.configsecurity;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Component
@ConfigurationProperties("imdb.jwt")
public class JWTProperties {
	
	private String secret;
	private Long expiration;

}
