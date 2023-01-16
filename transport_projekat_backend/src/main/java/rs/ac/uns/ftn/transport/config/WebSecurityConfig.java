package rs.ac.uns.ftn.transport.config;

import org.aspectj.weaver.ast.Or;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import rs.ac.uns.ftn.transport.auth.RestAuthenticationEntryPoint;
import rs.ac.uns.ftn.transport.auth.TokenAuthenticationFilter;
import rs.ac.uns.ftn.transport.service.interfaces.IUserService;
import rs.ac.uns.ftn.transport.utils.TokenUtils;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    private final IUserService userService;
    @Autowired
    private TokenUtils tokenUtils;

    public WebSecurityConfig(IUserService userService){
        this.userService = userService;
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return userService;
    }

    @Autowired
    private RestAuthenticationEntryPoint restAuthenticationEntryPoint;

    @Bean
    @Order(1)
    public SecurityFilterChain setupFilterChain(HttpSecurity http) throws Exception {

        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.exceptionHandling().authenticationEntryPoint(restAuthenticationEntryPoint);
        return http.build();
    }

    @Bean
    @Order(2)
    public SecurityFilterChain PermitAllFilterChain(HttpSecurity http) throws Exception {
        http
                .securityMatcher("/api/user/login")
                .authorizeHttpRequests(authorize -> authorize
                        .anyRequest().permitAll()
                );
        return http.build();
    }

    @Bean
    @Order(3)
    public SecurityFilterChain DefaultFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeRequests(authorize -> {
                    try {
                        authorize
                                .anyRequest().authenticated().and().cors().and()
                                .addFilterBefore(new TokenAuthenticationFilter(tokenUtils,  userDetailsService()), BasicAuthenticationFilter.class);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
                );
        return http.build();
    }
}
