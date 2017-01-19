package giraffe.auth.repository;


import giraffe.auth.domain.GiraffeEntity;
import giraffe.auth.domain.User;
import org.springframework.stereotype.Repository;

/**
 * @author Guschcyna Olga
 * @version 1.0.0
 */
@Repository
public interface UserRepository extends GiraffeRepository<User> {

    User findByLoginAndStatus(String login, GiraffeEntity.Status status);

    User findBySocialIdAndSocialProviderAndStatus(String socialId, User.SocialProvider socialProvider, GiraffeEntity.Status status);

}
