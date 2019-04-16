package am.avantgarde.avantgardeweb.config;

import am.avantgarde.avantgardeweb.security.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.csrf().disable()
                .formLogin()
                    .loginPage("/login")
                    .usernameParameter("email")
                    .passwordParameter("password")
                    .successForwardUrl("/successLogin")
                    .failureForwardUrl("/failureLogin")
                .and()
                .logout()
                    .logoutSuccessUrl("/")
                .and()
                .authorizeRequests()
                    .antMatchers("/").permitAll()
                    .antMatchers("/register").permitAll()
                    .antMatchers("/account").hasAnyAuthority("USER","ADMIN")
                    .antMatchers("/adminPage").hasAuthority("ADMIN");
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder);
    }
}
