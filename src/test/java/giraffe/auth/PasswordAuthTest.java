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
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.DefaultOAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.test.OAuth2ContextConfiguration;
import org.springframework.security.oauth2.client.test.OAuth2ContextSetup;
import org.springframework.security.oauth2.client.test.RestTemplateHolder;
import org.springframework.security.oauth2.client.token.AccessTokenRequest;
import org.springframework.security.oauth2.client.token.DefaultAccessTokenRequest;
import org.springframework.security.oauth2.client.token.grant.password.ResourceOwnerPasswordResourceDetails;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
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
                .setUserType(User.UserType.REGISTERED)
                .setPasswordHash("testPassword");

        GiraffeAuthority giraffeAuthority = new GiraffeAuthority();
        giraffeAuthority.setRole(GiraffeAuthority.Role.USER);
        authorityRepository.save(giraffeAuthority);

        user.addAuthority(authorityRepository.findByUuidAndStatus(giraffeAuthority.getUuid(), GiraffeEntity.Status.ACTIVE));
        giraffeAuthority.addUser(user);

        userRepository.save(user);
    }

    /**
     * Testing with {@link OAuth2RestTemplate}
     * */
    @Test
    public void shouldReceiveTokenWithPasswordGrant() {
        OAuth2AccessToken accessToken = getAccessToken();

        assertNotNull(accessToken.getValue());
        assertEquals(accessToken.getAdditionalInformation().get("username"), "testUser");
    }



    /**
     * Testing with {@link RestTemplate}
     * */
    @Test
    public void shouldObtainRefreshToken() {
        OAuth2AccessToken accessToken = getAccessToken();

        String oldAccessToken = accessToken.getValue();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("grant_type", "refresh_token");
        map.add("client_id", "trustedClientId");
        map.add("client_secret", "trustedClientSecret");
        map.add("refresh_token", accessToken.getRefreshToken().getValue());

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);

        ResponseEntity<String> response = getRestTemplate().postForEntity(host + "/oauth/token", request, String.class);

        assertTrue(response.getStatusCode().is2xxSuccessful());
    }

    private OAuth2AccessToken getAccessToken() {
        ResourceOwnerPasswordResourceDetails resourceDetails = new ResourceOwnerPasswordResourceDetails();

        resourceDetails.setAccessTokenUri(host + "/oauth/token");
        resourceDetails.setClientId("trustedClientId");
        resourceDetails.setClientSecret("trustedClientSecret");
        resourceDetails.setUsername("testUser");
        resourceDetails.setPassword("testPassword");
        resourceDetails.setScope(asList("read", "write"));
        resourceDetails.setGrantType("password");

        AccessTokenRequest atr = new DefaultAccessTokenRequest();

        OAuth2RestTemplate restTemplate = new OAuth2RestTemplate(resourceDetails, new DefaultOAuth2ClientContext(atr));
        return restTemplate.getAccessToken();
    }

    /**
     * Testing with {@link RestTemplate}
     * */
    @Test(expected = HttpClientErrorException.class)
    public void shouldGrantTokenBasedOnCustomSocialGrant() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("grant_type", "facebook_social");
        map.add("client_id", "trustedClientId");
        map.add("facebook_social_token", "WRONG_TOKEN");

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);

        getRestTemplate().postForEntity(host + "/oauth/token", request, String.class);
    }


    /**
     * Testing with predefined client context
     * */
    @Test
    @OAuth2ContextConfiguration(value = ClientContextPasswordGrant.class, initialize = false)
    public void shouldReceiveProtectedResource() {

        ResponseEntity<String> response = getRestTemplate().getForEntity(host + "/home", String.class);
        assertTrue(response.getStatusCode().is2xxSuccessful());
    }

}

