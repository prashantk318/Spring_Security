package com.prashant.config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
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

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class SecurityConfiguration {
	
	@Autowired
	private CustomUserDetailsService customUserDetailsService;
	  @Bean
	  BCryptPasswordEncoder encode() { 
	 return new BCryptPasswordEncoder();
		  }
	 
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    	http.cors().and().csrf()
        .disable()
        .authorizeRequests().antMatchers(HttpMethod.GET,"/api/**").permitAll()
        .antMatchers("/api/auth/**").permitAll()
         .anyRequest()
        .authenticated()
        .and()
        .httpBasic()
        .and()
        .sessionManagement()
        .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    	http.headers().frameOptions().disable();
    	http.authenticationProvider(authenticationProvider());
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