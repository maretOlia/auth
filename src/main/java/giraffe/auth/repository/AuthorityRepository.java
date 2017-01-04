package giraffe.auth.repository;


import giraffe.auth.domain.GiraffeAuthority;
import org.springframework.stereotype.Repository;

/**
 * @author Guschcyna Olga
 * @version 1.0.0
 */
@Repository
public interface AuthorityRepository extends GiraffeRepository<GiraffeAuthority> {

    GiraffeAuthority findByRole(GiraffeAuthority.Role role);

}