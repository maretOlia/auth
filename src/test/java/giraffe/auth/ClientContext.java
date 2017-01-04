package giraffe.auth;

import org.springframework.security.oauth2.client.token.grant.password.ResourceOwnerPasswordResourceDetails;

import static java.util.Arrays.asList;

/**
 * @author Guschcyna Olga
 * @version 1.0.0
 */
public class ClientContext extends ResourceOwnerPasswordResourceDetails {

    public ClientContext(Object obj) {
        PasswordAuthTest test = (PasswordAuthTest) obj;
        setAccessTokenUri(test.getHost() + "/oauth/token");
        setClientId("trustedClientId");
        setScope(asList("read", "write"));
        setUsername("testUser");
        setPassword("testPassword");
    }

}