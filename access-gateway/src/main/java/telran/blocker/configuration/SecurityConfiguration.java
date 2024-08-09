package telran.blocker.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.client.RestTemplate;

@Configuration
public class SecurityConfiguration {
	@Value("${app.gateway.role.su}")
	String userSU;
	@Value("${app.gateway.role.admin}")
	String userADMIN;
	@Value("${app.gateway.role.user}")
	String userUser;
	
	@Value("${app.back.office.ip.url}")
	String boIPUrl;
	@Value("${app.back.office.srv.url}")
	String boServiceUrl;
	
	@Value("${app.blocking.data.exist.url}")
	String ipExistUrl;
	@Value("${app.blocking.data.list.url}")
	String ipGetAllUrl;
	@Value("${app.blocking.data.list.url}")
	String ipUrl;
	
	@Value("${app.notification.provider.exist.url}")	
	String serviceExistUrl;	
	@Value("${app.notification.provider.list.url}")	
	String serviceGetAllUrl;	
	@Value("${app.notification.provider.get.url}")
	String serviceUrl;	

	@Value("${app.account.provider.url}")
	String userUrl;
	
	@Bean
	RestTemplate getRestTemplate() {
		return new RestTemplate();
	}

	@Bean
	PasswordEncoder getPasswordEncoder() {
		return new BCryptPasswordEncoder();	}

	@Bean
	public SecurityFilterChain configure(HttpSecurity http) throws Exception {
		http.cors(custom -> custom.disable());
		http.csrf(custom -> custom.disable());
		http.authorizeHttpRequests(requests -> requests
				.requestMatchers(userUrl + "/**").hasRole(userSU)
				
				.requestMatchers(boIPUrl).hasRole(userADMIN)
				.requestMatchers(boIPUrl + "/**").hasRole(userADMIN)
				.requestMatchers(boServiceUrl).hasRole(userADMIN)
				.requestMatchers(boServiceUrl + "/**").hasRole(userADMIN)
				
				.requestMatchers(ipExistUrl + "/**").hasRole(userUser)
				.requestMatchers(ipGetAllUrl).hasRole(userUser)
				.requestMatchers(ipUrl + "/**").hasRole(userUser)
				
				.requestMatchers(serviceExistUrl + "/**").hasRole(userUser)
				.requestMatchers(serviceGetAllUrl).hasRole(userUser)
				.requestMatchers(serviceUrl + "/**").hasRole(userUser)	
				
				.anyRequest().authenticated());
		http.httpBasic(Customizer.withDefaults());
		http.sessionManagement(custom -> custom.sessionCreationPolicy(SessionCreationPolicy.ALWAYS));
		return http.build();
	}
}
