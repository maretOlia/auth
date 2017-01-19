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

    private String login;

    @Column(name = "pass_hash")    //TODO: ignore in JSON
    private String passwordHash;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_authorities",
            joinColumns = @JoinColumn(name = "user_uuid", referencedColumnName = "uuid"),
            inverseJoinColumns = @JoinColumn(name = "authority_uuid", referencedColumnName = "uuid"))
    private Set<GiraffeAuthority> authorities = Sets.newHashSet();

    @Column(name = "user_type", nullable = false)
    @Enumerated
    private UserType userType;

    // Social users
    @Column(name = "soc_id")
    private String socialId;

    // Social users
    @Column(name = "soc_provider")
    @Enumerated
    private SocialProvider socialProvider;


    public enum UserType {
        REGISTERED(1), SOCIAL(2);

        private int value;

        UserType(final int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    public enum SocialProvider {
        FACEBOOK(1);

        private int value;

        SocialProvider(final int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }


    public User() { }


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

    public UserType getUserType() {
        return userType;
    }

    public User setUserType(UserType userType) {
        this.userType = userType;
        return this;
    }

    public String getSocialId() {
        return socialId;
    }

    public User setSocialId(String socialId) {
        this.socialId = socialId;
        return this;
    }

    public SocialProvider getSocialProvider() {
        return socialProvider;
    }

    public User setSocialProvider(SocialProvider socialProvider) {
        this.socialProvider = socialProvider;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        if (!super.equals(o)) return false;

        User user = (User) o;

        if (login != null ? !login.equals(user.login) : user.login != null) return false;
        if (passwordHash != null ? !passwordHash.equals(user.passwordHash) : user.passwordHash != null) return false;
        if (userType != user.userType) return false;
        if (socialId != null ? !socialId.equals(user.socialId) : user.socialId != null) return false;
        return socialProvider == user.socialProvider;

    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (login != null ? login.hashCode() : 0);
        result = 31 * result + (passwordHash != null ? passwordHash.hashCode() : 0);
        result = 31 * result + userType.hashCode();
        result = 31 * result + (socialId != null ? socialId.hashCode() : 0);
        result = 31 * result + (socialProvider != null ? socialProvider.hashCode() : 0);
        return result;
    }
}
