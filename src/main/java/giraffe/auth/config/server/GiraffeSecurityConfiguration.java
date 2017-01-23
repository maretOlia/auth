package giraffe.auth.config.server;

import giraffe.services.GiraffeUserDetailsService;
import org.h2.server.web.WebServlet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * @author Guschcyna Olga
 * @version 1.0.0
 */
@Configuration
public class GiraffeSecurityConfiguration extends WebSecurityConfigurerAdapter {

    private GiraffeUserDetailsService giraffeUserDetailsService;


    @Autowired
    GiraffeSecurityConfiguration(GiraffeUserDetailsService giraffeUserDetailsService) {
        this.giraffeUserDetailsService = giraffeUserDetailsService;
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .headers().frameOptions().disable()
                .and()
                .authorizeRequests()
                .antMatchers(HttpMethod.GET, "/console").permitAll() // allow access H2 browser console
                //.anyRequest().authenticated()
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }

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

    @Bean
    ServletRegistrationBean h2servletRegistration() {
        ServletRegistrationBean registrationBean = new ServletRegistrationBean(new WebServlet());
        registrationBean.addUrlMappings("/console/*"); //default db url: "jdbc:h2:mem:testdb"
        return registrationBean;
    }

}