package com.userlogin.registration.security.config;

import com.userlogin.registration.appuser.AppUserService;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@AllArgsConstructor
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    // burayı custom oluşturmak için WebSecurityConfigurerAdapter'ı extends ettik

    private final AppUserService appUserService; // Injection..
    private final BCryptPasswordEncoder bCryptPasswordEncoder; // Injection..

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeRequests()
                .antMatchers("/api/v*/registration/**")
                .permitAll()
                //antMatchers'a endpoint'ine girdiğimiz adrese gelen kullanıcı her işlemi gerçekleştirebilir buna izin verdik, yani kullanıcı login authenticate olmak zorunda değildir
                .anyRequest().authenticated()
                // bu satır diğer tüm istekler için kullanıcıdan login olmasını ister,login olmadan hiçbir isteği karşılamaz
                .and()
                .formLogin();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(daoAuthenticationProvider());
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(bCryptPasswordEncoder);
        provider.setUserDetailsService(appUserService);
        return provider;
    }
}
