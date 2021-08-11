package com.zwallet.zwalletapi.Config;

import org.jasypt.util.numeric.BasicIntegerNumberEncryptor;
import org.jasypt.util.numeric.IntegerNumberEncryptor;
import org.jasypt.util.text.BasicTextEncryptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.zwallet.zwalletapi.Service.UserServiceImpl;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private UserServiceImpl userServiceImpl;

    @Autowired
    private AuthEntryPoint authEntryPoint;

    @Bean
    public CorsConfig authTokenFilter() {
        return new CorsConfig();
    }

    @Bean
    @Override
    protected AuthenticationManager authenticationManager() throws Exception {
        // TODO Auto-generated method stub
        return super.authenticationManager();
    }

    // UNTUK MEMBUAT AUTENTIKASI USER
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        // TODO Auto-generated method stub
        auth.userDetailsService(userServiceImpl);
    }

    // END POINT YANG DIIZINKAN SETELAH AUTENTIKASI
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // TODO Auto-generated method stub
        http.cors().and().csrf().disable().exceptionHandling().authenticationEntryPoint(authEntryPoint).and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and().authorizeRequests()
                .antMatchers("/**").permitAll().antMatchers(HttpMethod.POST, "/user/signup").permitAll()
                .antMatchers(HttpMethod.POST, "/user/signin").permitAll().anyRequest().fullyAuthenticated();

        http.addFilterBefore(authTokenFilter(), UsernamePasswordAuthenticationFilter.class);
    }

    // PASSWORD ENCODER
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // ENCRYPTOR TEXT
    @Bean
    public BasicTextEncryptor encryptorText() {

        BasicTextEncryptor ency = new BasicTextEncryptor();
        ency.setPassword("password");
        return ency;

    }

    @Bean
    public BasicIntegerNumberEncryptor encryptorNumber() {
        BasicIntegerNumberEncryptor ency = new BasicIntegerNumberEncryptor();
        ency.setPassword("password");
        return ency;

    }

}
