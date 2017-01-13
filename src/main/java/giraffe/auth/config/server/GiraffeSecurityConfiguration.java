package giraffe.auth.config.server;

import giraffe.auth.services.GiraffeUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * @author Guschcyna Olga
 * @version 1.0.0
 */
@Configuration
public class GiraffeSecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    GiraffeUserDetailsService giraffeUserDetailsService;

    @Bean
    @Qualifier("authenticationManagerBean")
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(giraffeUserDetailsService);
    }

    @Override
    @Qualifier("giraffeUserDetailsService")
    protected UserDetailsService userDetailsService() {
        return giraffeUserDetailsService;
    }

}