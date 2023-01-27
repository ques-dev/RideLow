package rs.ac.uns.ftn.transport.config;

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
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import rs.ac.uns.ftn.transport.auth.RestAuthenticationEntryPoint;
import rs.ac.uns.ftn.transport.auth.TokenAuthenticationFilter;
import rs.ac.uns.ftn.transport.service.interfaces.IUserService;
import rs.ac.uns.ftn.transport.util.TokenUtils;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
public class WebSecurityConfig {

	private final IUserService userService;
	private final RestAuthenticationEntryPoint restAuthenticationEntryPoint;
	private final TokenUtils tokenUtils;

	public WebSecurityConfig(IUserService userService, RestAuthenticationEntryPoint restAuthenticationEntryPoint,
							 TokenUtils tokenUtils){
		this.tokenUtils = tokenUtils;
		this.restAuthenticationEntryPoint = restAuthenticationEntryPoint;
		this.userService = userService;
	}

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

 	@Bean
 	public DaoAuthenticationProvider authenticationProvider() {
 	    DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
 	    authProvider.setUserDetailsService(userService);
 	    authProvider.setPasswordEncoder(passwordEncoder());
 	 
 	    return authProvider;
 	}
 
 	
    // Registrujemo authentication manager koji ce da uradi autentifikaciju korisnika za nas
 	@Bean
 	public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
 	    return authConfig.getAuthenticationManager();
 	}
	
	// Definisemo prava pristupa za zahteve ka odredjenim URL-ovima/rutama
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    	// svim korisnicima dopusti da pristupe sledecim putanjama:
    	// komunikacija izmedju klijenta i servera je stateless posto je u pitanju REST aplikacija
        // ovo znaci da server ne pamti nikakvo stanje, tokeni se ne cuvaju na serveru 
		// ovo nije slucaj kao sa sesijama koje se cuvaju na serverskoj strani - STATEFULL aplikacija
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        
        // sve neautentifikovane zahteve obradi uniformno i posalji 401 gresku	
        http.exceptionHandling().authenticationEntryPoint(restAuthenticationEntryPoint);
    	http.authorizeRequests()
				.antMatchers("/h2-console/**").permitAll()
				.antMatchers("/api/user/login").permitAll()
				.antMatchers("/api/driver/**").hasRole("DRIVER")
				.antMatchers("/api/user/*/ride", "/api/user/*/message").hasRole("PASSENGER")
				.antMatchers("/api/**").hasRole("ADMIN")
				.anyRequest().authenticated().and()
				.cors().and()
				.addFilterBefore(new TokenAuthenticationFilter(tokenUtils,  userService), BasicAuthenticationFilter.class);
		http.csrf().disable();
		http.headers().frameOptions().disable();

        http.authenticationProvider(authenticationProvider());
       
        return http.build();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
    	return (web) -> web.ignoring().antMatchers(HttpMethod.POST, "/auth/login")

    			.antMatchers(HttpMethod.GET, "/", "/webjars/**", "/*.html", "favicon.ico",
    			"/**/*.html", "/**/*.css", "/**/*.js");	 

    }

}
