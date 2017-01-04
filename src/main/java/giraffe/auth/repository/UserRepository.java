package giraffe.auth.repository;


import giraffe.auth.domain.GiraffeEntity;
import giraffe.auth.domain.User;

/**
 * @author Guschcyna Olga
 * @version 1.0.0
 */
public interface UserRepository extends GiraffeRepository<User> {

    User findByLoginAndStatus(String login, GiraffeEntity.Status status);

}
