package projet.micro.auth.security;

import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import projet.micro.auth.filter.CustomAuthenticationFilter;
import projet.micro.auth.filter.CustomAuthorizationFilter;


@Configuration @EnableWebSecurity @EnableGlobalMethodSecurity(prePostEnabled = true)
public class securityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private    UserDetailsService userDetailsService;
    @Autowired
    private final BCryptPasswordEncoder passwordEncoder;


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder);

    }

    public securityConfig(UserDetailsService userDetailsService ,BCryptPasswordEncoder passwordEncoder) {
        this.userDetailsService = userDetailsService;
         this.passwordEncoder = passwordEncoder;
    }



    @Override
    protected void configure(HttpSecurity http) throws Exception {
      
        http.csrf().disable();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.headers().frameOptions().disable();
        http.authorizeRequests().antMatchers("/login/**","/api/token/refresh/**").permitAll();
        http.authorizeRequests().anyRequest().authenticated();


        http.addFilter(new CustomAuthenticationFilter(authenticationManagerBean()));
        http.addFilterBefore(new CustomAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class);
    }
}