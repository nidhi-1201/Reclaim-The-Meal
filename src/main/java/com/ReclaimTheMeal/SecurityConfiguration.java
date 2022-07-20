package com.ReclaimTheMeal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;


@Configuration
public class SecurityConfiguration {
	
	private static final String LOGIN_PAGE_URL = "/custom_login_url";
	private static final String LOGIN_PROCESSING_URL = "/perform_login";
	
	@Autowired
	AuthenticationFailureHandler authFailureHandler;
	
   
    @Bean
    public UserDetailsService userDetailsService() {
        return new CustomUserDetailsService();
    }
     
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
     
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
         
        return authProvider;
    }
 
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    	
		http.authorizeRequests()
        .antMatchers("/list_users").hasAnyAuthority("ADMIN")
        .antMatchers("/posts").hasAnyAuthority("USER","ADMIN")
        .antMatchers("/add").hasAnyAuthority("USER","ADMIN")
        .anyRequest().permitAll()
        .and()
        .formLogin()
        .loginPage(LOGIN_PAGE_URL)
        .loginProcessingUrl(LOGIN_PROCESSING_URL)
        .usernameParameter("email")
        .passwordParameter("password")
        .defaultSuccessUrl("/posts")
        .failureHandler(authFailureHandler)
        .permitAll()
        .and()
        .csrf().disable()
        .logout()
        .clearAuthentication(true)
        .invalidateHttpSession(true)
        .logoutSuccessUrl("/").permitAll();
        
        //.and().exceptionHandling().accessDeniedHandler(accessDeniedHandler());
    	return http.build();
    }
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().antMatchers("/images/**", "/js/**", "/webjars/**");
    }
    
    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
    	return new CustomAccessDeniedHandler();
    	
    }
    
}
	 
