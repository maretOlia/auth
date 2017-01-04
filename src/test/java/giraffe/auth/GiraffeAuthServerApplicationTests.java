package giraffe.auth;

import giraffe.auth.domain.GiraffeAuthority;
import giraffe.auth.domain.GiraffeEntity;
import giraffe.auth.domain.User;
import giraffe.auth.repository.AuthorityRepository;
import giraffe.auth.repository.UserRepository;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class GiraffeAuthServerApplicationTests {

	@Autowired
	UserRepository userRepository;

	@Autowired
	AuthorityRepository authorityRepository;

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

}
