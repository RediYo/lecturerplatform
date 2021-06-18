package com.nchu.lecturerplatform.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.StandardPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.annotation.Resource;
import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig  extends WebSecurityConfigurerAdapter {

    @Autowired
    DataSource dataSource;
    @Resource
    private MyAccessDeniedHandler myAccessDeniedHandler;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception{
         auth.jdbcAuthentication()
                 .dataSource(dataSource)
                 .usersByUsernameQuery("select username, password, enabled from student " +
                         "where username=?")
                 .authoritiesByUsernameQuery("select username, authority from role,user_role " +
                         "where username=? and user_role.role_id=role.id")
                 .passwordEncoder(encoder());
        auth.jdbcAuthentication()
                .dataSource(dataSource)
                .usersByUsernameQuery("select username, password, enabled from lecturer " +
                        "where username=?")
                .authoritiesByUsernameQuery("select username, authority from role,user_role " +
                        "where username=? and user_role.role_id=role.id")
                .passwordEncoder(encoder());
        auth.jdbcAuthentication()
                .dataSource(dataSource)
                .usersByUsernameQuery("select username, password, enabled from admin " +
                        "where username=?")
                .authoritiesByUsernameQuery("select username, authority from role,user_role " +
                        "where username=? and user_role.role_id=role.id")
                .passwordEncoder(encoder());
    }

    @Bean
    public PasswordEncoder encoder() {
        return new StandardPasswordEncoder("53cr3t");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception{
        http.authorizeRequests()
                .antMatchers("/student/**").hasRole("STUDENT")
                .antMatchers("/teacher/**").hasRole("LECTURER")
                .antMatchers("/admin/**").hasRole("ADMIN")
                .antMatchers("/","/**").permitAll()
                .anyRequest().authenticated()
                /*.antMatchers("/learn","/","/**").permitAll()*/
                .and()
                .formLogin().loginPage("/login")
                .defaultSuccessUrl("/authenticate")
                .usernameParameter("username")
                .passwordParameter("password")
                .and()
                .exceptionHandling().accessDeniedHandler(myAccessDeniedHandler)//权限不知，访问拒绝
                .and()
                .csrf().disable()
                .logout().logoutSuccessUrl("/").logoutRequestMatcher(new AntPathRequestMatcher("/logout","GET"));

    }
}