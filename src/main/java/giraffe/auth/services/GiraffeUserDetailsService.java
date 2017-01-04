package giraffe.auth.services;


import giraffe.auth.domain.GiraffeUserDetails;
import giraffe.auth.repository.UserRepository;
import giraffe.auth.domain.GiraffeEntity;
import giraffe.auth.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * @author Guschcyna Olga
 * @version 1.0.0
 */
@Service
public class GiraffeUserDetailsService implements UserDetailsService {

    @Autowired
    UserRepository userRepository;


    @Override
    public GiraffeUserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User account = userRepository.findByLoginAndStatus(username,  GiraffeEntity.Status.ACTIVE);

        if (account == null)
            throw new UsernameNotFoundException("Account with login: " + username + " not found");

        return new GiraffeUserDetails(account.getLogin(), account.getPasswordHash(), account.getAuthorities(), account.getUuid(), false, false, false, true);
    }

}
