package giraffe.auth.domain;

import com.google.common.collect.Sets;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

/**
 * @author Guschcyna Olga
 * @version 1.0.0
 */
@Entity
public class User extends GiraffeEntity<User> implements Serializable {

    @Column(nullable = false)
    private String login;

    @Column(nullable = false, name = "pass_hash")    //TODO: ignore in JSON
    private String passwordHash;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_authorities",
            joinColumns = @JoinColumn(name = "user_uuid", referencedColumnName = "uuid"),
            inverseJoinColumns = @JoinColumn(name = "authority_uuid", referencedColumnName = "uuid"))
    private Set<GiraffeAuthority> authorities = Sets.newHashSet();


    public User() {
    }

    @Override
    public User self() {
        return this;
    }


    public String getLogin() {
        return login;
    }

    public User setLogin(String login) {
        this.login = login;
        return this;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public User setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
        return this;
    }

    public Set<GiraffeAuthority> getAuthorities() {
        return authorities;
    }

    public User addAuthority(GiraffeAuthority authority) {
        if (!authorities.contains(authority)) {
            authorities.add(authority);
        }
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        if (!super.equals(o)) return false;

        User user = (User) o;

        if (!login.equals(user.login)) return false;
        return passwordHash.equals(user.passwordHash);

    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + login.hashCode();
        result = 31 * result + passwordHash.hashCode();
        return result;
    }
}
