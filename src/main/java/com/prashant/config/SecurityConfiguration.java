package com.prashant.config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.prashant.security.CustomUserDetailsService;
import com.prashant.security.JwtAuthenticationEntryPoint;
import com.prashant.security.JwtAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfiguration {
	
	@Autowired
	private CustomUserDetailsService customUserDetailsService;
	
	@Autowired
	private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
	
	@Bean
	public JwtAuthenticationFilter jwtAuthenticationFilter() {
		return new JwtAuthenticationFilter();
	}
	
	  @Bean
	  BCryptPasswordEncoder encode() { 
	 return new BCryptPasswordEncoder();
		  }
	 
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    	http.cors().and().csrf()
        .disable().exceptionHandling().authenticationEntryPoint(jwtAuthenticationEntryPoint).and()
        .sessionManagement()
        .sessionCreationPolicy(SessionCreationPolicy.STATELESS).and().authenticationProvider(authenticationProvider())
        .authorizeRequests().antMatchers(HttpMethod.GET,"/api/**").permitAll().antMatchers(HttpMethod.POST,"/api/**").permitAll()
        .antMatchers(HttpMethod.PUT,"/api/**").permitAll()
        .antMatchers("/api/auth/**").permitAll()
         .anyRequest()
        .authenticated()
        .and().
    	authenticationProvider(authenticationProvider());
		return http.build();
     
    }
     
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
		return null;
         
    }
	/*
	 * @Bean public UserDetailsService userDetailsService(BCryptPasswordEncoder
	 * encode) { UserDetails prashant =
	 * User.builder().username("prashant").password(encode.encode("prashant")).roles
	 * ("USER").build(); UserDetails ramesh =
	 * User.builder().username("ramesh").password(encode.encode("admin")).roles(
	 * "ADMIN").build(); return new InMemoryUserDetailsManager(prashant,ramesh); }
	 */
    
    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
         
        authProvider.setUserDetailsService(customUserDetailsService);
        authProvider.setPasswordEncoder(encode());
     
        return authProvider;
    }
   
         
}