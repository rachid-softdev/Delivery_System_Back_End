package fr.univrouen.deliverysystem.application.configuration;

import java.io.Serializable;
import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import jakarta.servlet.http.HttpServletRequest;

/**
 * Configuration par défaut : Désactivation de l'authentification
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@ConditionalOnProperty(name = "keycloak.enabled", havingValue = "false", matchIfMissing = false)
public class DisableSecurityConfiguration implements Serializable {

	private static final long serialVersionUID = 1L;

	private final JwtAuthenticationConverter jwtAuthenticationConverter;

	public DisableSecurityConfiguration(
			@Autowired JwtAuthenticationConverter jwtAuthenticationConverter) {
		this.jwtAuthenticationConverter = jwtAuthenticationConverter;
	}

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http)
			throws Exception {
		http
				// CORS :
				// https://docs.spring.io/spring-security/reference/servlet/integrations/cors.html
				.cors(corsCustomizer -> corsCustomizer.configurationSource(new CorsConfigurationSource() {
					@Override
					public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
						final CorsConfiguration corsConfiguration = new CorsConfiguration();
						corsConfiguration.setAllowCredentials(true);
						corsConfiguration.setAllowedOrigins(Arrays.asList("http://localhost:4200"));
						corsConfiguration.setAllowedHeaders(
								Arrays.asList(
										HttpHeaders.ORIGIN,
										HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN,
										HttpHeaders.CONTENT_TYPE,
										HttpHeaders.ACCEPT,
										HttpHeaders.AUTHORIZATION,
										HttpHeaders.ACCESS_CONTROL_ALLOW_CREDENTIALS,
										"X-Requested-With",
										HttpHeaders.ACCESS_CONTROL_REQUEST_METHOD,
										HttpHeaders.ACCESS_CONTROL_REQUEST_HEADERS));
						corsConfiguration.setExposedHeaders(
								Arrays.asList(
										HttpHeaders.ORIGIN,
										HttpHeaders.CONTENT_TYPE,
										HttpHeaders.ACCEPT,
										HttpHeaders.AUTHORIZATION,
										HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN,
										HttpHeaders.ACCESS_CONTROL_ALLOW_CREDENTIALS));
						corsConfiguration.setAllowedMethods(
								Arrays.asList(
										HttpMethod.GET.name(),
										HttpMethod.POST.name(),
										HttpMethod.PUT.name(),
										HttpMethod.PATCH.name(),
										HttpMethod.DELETE.name(),
										HttpMethod.OPTIONS.name(),
										HttpMethod.HEAD.name(),
										HttpMethod.TRACE.name()));
						corsConfiguration.setMaxAge(3600L);
						return corsConfiguration;
					}
				}))
				.csrf(AbstractHttpConfigurer::disable)
				.authorizeHttpRequests(req -> req.anyRequest().anonymous());
		// XSS :
		// https://stackoverflow.com/questions/76516612/headers-configuration-in-spring-security-6-for-automated-testing
		/*
		 * http.headers(headers ->
		 * headers.xssProtection(
		 * xss ->
		 * xss.headerValue(XXssProtectionHeaderWriter.HeaderValue.ENABLED_MODE_BLOCK)
		 * ).contentSecurityPolicy(
		 * cps -> cps.policyDirectives("script-src")
		 * ));
		 */
		return http.build();
	}

}
