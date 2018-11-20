package library.config

import library.auth.PermissionBasedUserDetailsService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder


@Configuration
@EnableWebSecurity
class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private PermissionBasedUserDetailsService permissionBasedUserDetailsService

    @Autowired
    void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth
            .userDetailsService(permissionBasedUserDetailsService)

            // Override the default password encoder
            .passwordEncoder(new BCryptPasswordEncoder())
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.httpBasic().and().csrf().disable()
            .authorizeRequests()
            .antMatchers("/books*").permitAll()

    }
}
