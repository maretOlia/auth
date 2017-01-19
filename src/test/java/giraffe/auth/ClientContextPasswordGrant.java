package giraffe.auth;

import org.springframework.security.oauth2.client.token.grant.password.ResourceOwnerPasswordResourceDetails;

/**
 * @author Guschcyna Olga
 * @version 1.0.0
 */
public class ClientContextPasswordGrant extends ResourceOwnerPasswordResourceDetails {

    public ClientContextPasswordGrant(Object obj) {
        PasswordAuthTest test = (PasswordAuthTest) obj;
        setAccessTokenUri(test.getHost() + "/oauth/token");
        setClientId("trustedClientId");
        setClientSecret("trustedClientSecret");
        setUsername("testUser");
        setPassword("testPassword");
    }

}