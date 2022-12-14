package ru.kata.spring.boot_security.demo.configs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import ru.kata.spring.boot_security.demo.service.UserDetailsServiceImpl;

@Configuration       // не нужна, т.к она есть у @EnableWebSecurity.
@EnableWebSecurity   //(debug = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    final
    SuccessUserHandler successUserHandler;          // зависимости.

    final
    UserDetailsServiceImpl userDetailsServiceImpl;

    final
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public WebSecurityConfig(SuccessUserHandler successUserHandler, BCryptPasswordEncoder bCryptPasswordEncoder, UserDetailsServiceImpl userDetailsServiceImpl) {
        this.successUserHandler = successUserHandler;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.userDetailsServiceImpl = userDetailsServiceImpl;
    }


// пример вместо отдельного класса BCryptPasswordEncoderConfig.
//    @Bean
//    public BCryptPasswordEncoder bCryptPasswordEncoder(){
//        return new BCryptPasswordEncoder();
//    }


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(userDetailsServiceImpl)
                .passwordEncoder(bCryptPasswordEncoder);
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/admin/**").hasRole("ADMIN")                               //.permitAll() вместо .hasRole("ADMIN"), чтобы выключить секъюрность!
                .antMatchers("/user/**").hasAnyRole("USER", "ADMIN")               //.permitAll() вместо .hasAnyRole("USER","ADMIN"), чтобы выключить секъюрность!
                .antMatchers("/").permitAll()
                //все остальные страницы требуют аутентификации
                .anyRequest().authenticated()                                                       //.permitAll() вместо .authenticated(), чтобы выключить секъюрность!
                .and()
                //настройка для входа в систему
                .formLogin()
                .successHandler(successUserHandler) // наш обработчик успешной аутентификации!
                .permitAll()
                .and()
                .logout()
//              .logoutUrl("/perform_logout")
                .permitAll();
    }


    // пример аутентификации inMemory.

//    @Bean
//    @Override
//    public UserDetailsService userDetailsService() {
//        User.UserBuilder users = User.withDefaultPasswordEncoder();
//        UserDetails user = users
//                        .username("user")
//                        .password("user")
//                        .roles("USER")
//                        .build();
//
//        UserDetails admin =  users
//                        .username("admin")
//                        .password("admin")
//                        .roles("USER", "ADMIN")
//                        .build();
//
//        return new InMemoryUserDetailsManager(user, admin);
//    }


    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setPasswordEncoder(bCryptPasswordEncoder);
        authenticationProvider.setUserDetailsService(userDetailsServiceImpl);
        return authenticationProvider;
    }
}