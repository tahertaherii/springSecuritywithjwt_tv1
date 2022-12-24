package com.example.springsecuritywithjwt;

import com.example.springsecuritywithjwt.filter.OncePerRequestFilterImplToAunthenticateJwt;
import com.example.springsecuritywithjwt.service.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
    public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {


    @Autowired
    OncePerRequestFilterImplToAunthenticateJwt oncePerRequestFilterImplToAunthenticateJwt;
        @Autowired
        UserDetailsServiceImpl userDetailsService;

        @Override
        protected void configure(AuthenticationManagerBuilder auth) throws Exception {
            // Set your configuration on the auth object
            auth.userDetailsService(userDetailsService);
            //here we can add authentication using different method such as inMemory and db
        }

        @Bean
        public PasswordEncoder getPasswordEncoder() {
            return NoOpPasswordEncoder.getInstance();
        }

        @Override
        protected void configure(HttpSecurity http) throws Exception {

            http.csrf().disable()
                    .authorizeRequests().antMatchers("/Authenticate").permitAll().
                    anyRequest().authenticated().and().
                    exceptionHandling().and().sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
           http.addFilterBefore(oncePerRequestFilterImplToAunthenticateJwt, UsernamePasswordAuthenticationFilter.class);
            //it is used for authorisation of a service.
        }

        @Autowired
        @Bean
        public AuthenticationManager getAuthenticateManager()
        {
            try {
                return super.authenticationManagerBean();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
