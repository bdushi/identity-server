package al.bruno.identityserver.config;

import al.bruno.identityserver.repository.UserRepository;
import al.bruno.identityserver.security.FederatedIdentityConfigurer;
import al.bruno.identityserver.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
public class DefaultSecurityConfig {
	@Autowired
	UserRepository userRepository;
	@Bean
	public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
		http
			.authorizeRequests(authorizeRequests ->
				authorizeRequests
					.mvcMatchers("/assets/**", "/webjars/**", "/login").permitAll()
					.anyRequest().authenticated()
			)
			.cors(Customizer.withDefaults())
			.formLogin(Customizer.withDefaults())
			.apply(new FederatedIdentityConfigurer().oauth2UserHandler(userService()));
		return http.build();
	}
	@Bean
	public UserServiceImpl userService() {
		return new UserServiceImpl(userRepository);
	}
	@Bean
	public UserDetailsService userDetailsService() {
		return userService();
	}
}