package be.kpse.ohlc.security;

import be.kpse.security.AccessTokenFilter;
import be.kpse.security.JwtAuthenticationProvider;
import be.kpse.security.TokenManager;

import java.util.List;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true, jsr250Enabled = true)
public class WebSecurityConfig {

	@Value("${jwt.secret}")
	private String secret;

	@Value("${jwt.expiration}")
	private String expirationTime;

	private AuthenticationManager authenticationManager;
	private final UserDetailsService userDetailsService;

	public WebSecurityConfig(UserDetailsService userDetailsService) {
		this.userDetailsService = userDetailsService;
	}

	private static final String[] AUTH_WHITELIST = {
			"/v2/api-docs", "/v3/api-docs", "/swagger-resources/**", "/swagger-ui/**", "/webjars/**", "/actuator/**",
			"/api/v1/public/**", "/error"
	};

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		final AuthenticationManager authenticationManager = http.getSharedObject(AuthenticationManager.class);

		return http
				.csrf(AbstractHttpConfigurer::disable)
				.cors(AbstractHttpConfigurer::disable)
//				.exceptionHandling(exception -> exception.authenticationEntryPoint(unauthorizedHandler))
				.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.authorizeHttpRequests(request -> {
					request.requestMatchers("/register").permitAll();
					request.requestMatchers("/users")
							.hasAnyAuthority("USER", "ADMIN");
				})
				.authenticationProvider(authenticationProvider())
				.addFilterBefore(accessTokenFilter(authenticationManager), UsernamePasswordAuthenticationFilter.class)
				.build();

	}

	public TokenManager tokenManager() {
		return new TokenManager(secret, expirationTime);

	}

	public AuthenticationProvider authenticationProvider() {
		return new JwtAuthenticationProvider(tokenManager());
	}

	public AccessTokenFilter accessTokenFilter(AuthenticationManager authenticationManager) throws Exception {
		return new AccessTokenFilter(tokenManager(), userDetailsService, authenticationManager);
	}

	//	@Override
	//	public void configure(WebSecurity web) throws Exception {
	//		web
	//				.ignoring()
	//				.antMatchers(AUTH_WHITELIST);
	//	}

	@Bean
	public CorsFilter corsFilter() {
		UrlBasedCorsConfigurationSource source =
				new UrlBasedCorsConfigurationSource();
		CorsConfiguration config = new CorsConfiguration();
		config.setAllowCredentials(true);
		config.addAllowedOrigin("*");
		config.addAllowedHeader("*");
		config.addAllowedMethod("*");
		config.setExposedHeaders(List.of("Location"));
		source.registerCorsConfiguration("/**", config);
		return new CorsFilter(source);
	}

}
