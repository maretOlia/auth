package giraffe.auth.config.server;

import giraffe.domain.GiraffeException;
import giraffe.domain.GiraffeUserDetails;
import giraffe.domain.User;
import giraffe.services.GiraffeUserDetailsService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.provider.*;
import org.springframework.security.oauth2.provider.token.AbstractTokenGranter;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

/**
 * @author Guschcyna Olga
 * @version 1.0.0
 */
public class SocialTokenGranter extends AbstractTokenGranter {

    private static final String GRANT_TYPE = "facebook_social";

    private GiraffeUserDetailsService giraffeUserDetailsService;

    SocialTokenGranter(
            GiraffeUserDetailsService giraffeUserDetailsService,
            AuthorizationServerTokenServices tokenServices,
            OAuth2RequestFactory defaultOauth2RequestFactory,
            ClientDetailsService clientDetailsService) {
        super(tokenServices, clientDetailsService, defaultOauth2RequestFactory, GRANT_TYPE);
        this.giraffeUserDetailsService = giraffeUserDetailsService;
    }

    @Override
    protected OAuth2Authentication getOAuth2Authentication(ClientDetails clientDetails, TokenRequest request) {

        // retrieve social token sent by the client
        Map<String, String> parameters = request.getRequestParameters();
        String socialToken = parameters.get("social_token");

        //validate social token and receive user information from external authentication server
        String url = "https://graph.facebook.com/me?access_token=" + socialToken;

        Authentication userAuth = null;
        try {

            ResponseEntity<FacebookUserInformation> response = new RestTemplate().getForEntity(url, FacebookUserInformation.class);

            if (response.getStatusCode().is4xxClientError()) throw new InvalidOrExpiredSocialToken();

            FacebookUserInformation userInformation = response.getBody();
            GiraffeUserDetails giraffeSocialUserDetails = giraffeUserDetailsService.loadOrCreateSocialUser(userInformation.getId(), userInformation.getEmail(), User.SocialProvider.FACEBOOK);

            userAuth = new UsernamePasswordAuthenticationToken(giraffeSocialUserDetails, "N/A", giraffeSocialUserDetails.getAuthorities());
        } catch (InvalidOrExpiredSocialToken | GiraffeException.UnableToValidateSocialUserInformation e) {
            e.printStackTrace();
            //TODO log the stacktrace
        }
        return new OAuth2Authentication(request.createOAuth2Request(clientDetails), userAuth);
    }

    private static class FacebookUserInformation {

        private String id;

        private String email;

        public FacebookUserInformation() {
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }
    }

}