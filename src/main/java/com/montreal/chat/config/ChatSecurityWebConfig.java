package com.montreal.chat.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.montreal.chat.aa.ChatUserAuthenticationFilter;
import com.montreal.chat.aa.ChatUserAuthenticationProvider;
import com.montreal.chat.aa.ChatUserLogoutSuccessHandler;

@EnableWebSecurity
public class ChatSecurityWebConfig extends WebSecurityConfigurerAdapter {

	private static final String LOGIN_PATH = "/login.xhtml";
	private static final String LOGIN_ERROR_PATH = LOGIN_PATH + "?error=true";
	private static final String JAVAX_FACES_RESOURCE = "/javax.faces.resource/**";
	private static final String H2_CONSOLE = "/h2-console/**";
	private static final String RESOURCES = "/resources/**";
	
	@Autowired
	private ChatUserAuthenticationProvider authProvider;
	@Autowired
	private ChatUserLogoutSuccessHandler logoutHandler;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.addFilterBefore(authenticationFilter(), UsernamePasswordAuthenticationFilter.class)
		.authorizeRequests()
		.antMatchers(JAVAX_FACES_RESOURCE, H2_CONSOLE, RESOURCES).permitAll().anyRequest().authenticated();

		http.formLogin().loginPage(LOGIN_PATH).permitAll().failureUrl(LOGIN_ERROR_PATH);
		http.logout().logoutSuccessHandler(logoutHandler);
		http.csrf().disable();
		http.httpBasic();
		http.headers().frameOptions().disable();

		// TODO
//	    http.rememberMe();
	}

	public ChatUserAuthenticationFilter authenticationFilter() throws Exception {
		ChatUserAuthenticationFilter filter = new ChatUserAuthenticationFilter();
		filter.setAuthenticationManager(authenticationManagerBean());
		filter.setAuthenticationFailureHandler(new SimpleUrlAuthenticationFailureHandler(LOGIN_ERROR_PATH));
		return filter;
	}

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(authProvider);
	}
}
