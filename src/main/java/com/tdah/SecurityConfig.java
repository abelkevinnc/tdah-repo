package com.tdah;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter{
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
		.authorizeRequests()
		.antMatchers("/assets/**","/css/**","/datatable/*","/img/**","/js/**","/").permitAll()
		.anyRequest()
		.permitAll()
		.and()
		.formLogin().loginPage("/autenticacion/login").defaultSuccessUrl("/").permitAll()
		.and()
		.logout().permitAll()
		.and().exceptionHandling().accessDeniedPage("/403")
		.and().csrf().disable();
		//.and()
		//.httpBasic();
	}
	
}
