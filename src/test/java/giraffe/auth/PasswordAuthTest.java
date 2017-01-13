package giraffe.auth;

import giraffe.auth.domain.GiraffeAuthority;
import giraffe.auth.domain.GiraffeEntity;
import giraffe.auth.domain.User;
import giraffe.auth.repository.AuthorityRepository;
import giraffe.auth.repository.UserRepository;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
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
import static org.junit.Assert.*;

/**
 * @author Guschcyna Olga
 * @version 1.0.0
 */
public class PasswordAuthTest extends GiraffeAuthServerApplicationTestsCase implements RestTemplateHolder {

    @Rule
    public OAuth2ContextSetup context = OAuth2ContextSetup.standard(this);

    RestOperations restTemplate = new RestTemplate();

    @Autowired
    UserRepository userRepository;

    @Autowired
    AuthorityRepository authorityRepository;


    @Override
    public RestOperations getRestTemplate() {
        return restTemplate;
    }

    @Override
    public void setRestTemplate(RestOperations restTemplate) {
        this.restTemplate = restTemplate;
    }


    @Before
    public void createAccount() {
        User user = new User()
                .setLogin("testUser")
                .setPasswordHash("testPassword");

        GiraffeAuthority giraffeAuthority = new GiraffeAuthority();
        giraffeAuthority.setRole(GiraffeAuthority.Role.USER);
        authorityRepository.save(giraffeAuthority);

        user.addAuthority(authorityRepository.findByUuidAndStatus(giraffeAuthority.getUuid(), GiraffeEntity.Status.ACTIVE));
        giraffeAuthority.addUser(user);

        userRepository.save(user);
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

