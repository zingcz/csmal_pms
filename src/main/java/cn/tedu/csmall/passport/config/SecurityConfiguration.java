package cn.tedu.csmall.passport.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    String[] url = new String[]{
            "/doc.html",
            "/**/*.js",
            "/**/*.css",
            "/swagger-resources",
            "/v2/api-docs",
    };
    @Override
    protected void configure(HttpSecurity http) throws Exception{
        http.authorizeRequests().
                mvcMatchers(url).
                permitAll().
                anyRequest().authenticated();
        //http.formLogin();
        http.csrf().disable();
    }
}
