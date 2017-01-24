package giraffe.auth.config.server;

import giraffe.services.GiraffeUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.CompositeTokenGranter;
import org.springframework.security.oauth2.provider.TokenGranter;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Guschcyna Olga
 * @version 1.0.0
 */
@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

    private TokenStore tokenStore;

    private AuthenticationManager authenticationManager;

    private TokenEnhancer tokenEnhancer;

    private JwtAccessTokenConverter jwtAccessTokenConverter;

    private GiraffeUserDetailsService giraffeUserDetailsService;

    private PasswordEncoder bCryptPasswordEncoder;


    @Autowired
    public AuthorizationServerConfig(TokenStore tokenStore,
                                     AuthenticationManager authenticationManager,
                                     TokenEnhancer tokenEnhancer,
                                     JwtAccessTokenConverter jwtAccessTokenConverter,
                                     GiraffeUserDetailsService giraffeUserDetailsService,
                                     PasswordEncoder bCryptPasswordEncoder) {
        this.tokenStore = tokenStore;
        this.authenticationManager = authenticationManager;
        this.tokenEnhancer = tokenEnhancer;
        this.jwtAccessTokenConverter = jwtAccessTokenConverter;
        this.giraffeUserDetailsService = giraffeUserDetailsService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    public void configure(AuthorizationServerSecurityConfigurer oauthServer) throws Exception {
        oauthServer
                .allowFormAuthenticationForClients()
                .tokenKeyAccess("permitAll()")
                .checkTokenAccess("isAuthenticated()")
                .passwordEncoder(bCryptPasswordEncoder);

    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
        tokenEnhancerChain.setTokenEnhancers(Arrays.asList(tokenEnhancer, jwtAccessTokenConverter));

        endpoints.tokenStore(tokenStore)
                .tokenEnhancer(tokenEnhancerChain)
                .authenticationManager(authenticationManager)
                .userDetailsService(giraffeUserDetailsService)
                .tokenGranter(tokenGranter(endpoints));
    }

    private TokenGranter tokenGranter(AuthorizationServerEndpointsConfigurer endpoints) {
        List<TokenGranter> granters = new ArrayList<>(Arrays.asList(endpoints.getTokenGranter()));
        granters.add(new SocialTokenGranter(giraffeUserDetailsService, endpoints.getTokenServices(), endpoints.getOAuth2RequestFactory(), endpoints.getClientDetailsService()));
        return new CompositeTokenGranter(granters);
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients
                .inMemory()
                .withClient("trustedClientId")
                .authorizedGrantTypes("password", "authorization_code", "refresh_token", "facebook_social")
                .authorities("ROLE_USER", "ROLE_ADMIN")
                .secret(bCryptPasswordEncoder.encode("trustedClientSecret"))
                .scopes("java", "read", "write") // define fixed scopes for client. Client doesn't have to provide scopes with authorization attempt
                .accessTokenValiditySeconds(3600) // 1 hour
                .refreshTokenValiditySeconds(604800); // 1 week
        }

}
