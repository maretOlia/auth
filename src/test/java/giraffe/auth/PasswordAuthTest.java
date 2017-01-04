package giraffe.auth;

import org.junit.Rule;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.DefaultOAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.test.OAuth2ContextConfiguration;
import org.springframework.security.oauth2.client.test.OAuth2ContextSetup;
import org.springframework.security.oauth2.client.test.RestTemplateHolder;
import org.springframework.security.oauth2.client.token.AccessTokenRequest;
import org.springframework.security.oauth2.client.token.DefaultAccessTokenRequest;
import org.springframework.security.oauth2.client.token.grant.password.ResourceOwnerPasswordResourceDetails;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * @author Guschcyna Olga
 * @version 1.0.0
 */
public class PasswordAuthTest extends GiraffeAuthServerApplicationTests implements RestTemplateHolder {

    @Value("http://localhost:${server.port}")
    String host;

    @Rule
    public OAuth2ContextSetup context = OAuth2ContextSetup.standard(this);

    RestOperations restTemplate = new RestTemplate();

    @Override
    public RestOperations getRestTemplate() {
        return restTemplate;
    }

    @Override
    public void setRestTemplate(RestOperations restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String getHost() {
        return host;
    }


    @Test
    public void shouldReceiveTokenWithPasswordGrant() {

        ResourceOwnerPasswordResourceDetails resourceDetails = new ResourceOwnerPasswordResourceDetails();

        resourceDetails.setAccessTokenUri(host + "/oauth/token");
        resourceDetails.setClientId("trustedClientId");
        resourceDetails.setUsername("testUser");
        resourceDetails.setPassword("testPassword");
        resourceDetails.setScope(asList("read", "write"));
        resourceDetails.setGrantType("password");

        AccessTokenRequest atr = new DefaultAccessTokenRequest();

        OAuth2RestTemplate restTemplate = new OAuth2RestTemplate(resourceDetails, new DefaultOAuth2ClientContext(atr));

        assertNotNull(restTemplate.getAccessToken().getValue());
        assertEquals(restTemplate.getAccessToken().getAdditionalInformation().get("username"), "testUser");
    }


    @Test
    @OAuth2ContextConfiguration(value = ClientContext.class, initialize = false)
    public void shouldReceiveProtectedResource() {

        ResponseEntity<String> entity = getRestTemplate().getForEntity(host + "/home", String.class);
        assertTrue(entity.getStatusCode().is2xxSuccessful());
    }

}

