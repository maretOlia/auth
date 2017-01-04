package giraffe.auth.domain;

import org.springframework.security.core.userdetails.UserDetails;

import java.util.Objects;
import java.util.Set;


/**
 * @author Guschcyna Olga
 * @version 1.0.0
 */
public class GiraffeUserDetails implements UserDetails {

    private String username;

    private String password;

    private Set<GiraffeAuthority> authorities;

    private String uuid;

    private long expires;

    private boolean accountExpired;

    private boolean accountLocked;

    private boolean credentialsExpired;

    private boolean enabled;


    public GiraffeUserDetails() { }

    public GiraffeUserDetails(final String username,
                              final String password,
                              final Set<GiraffeAuthority> authorities,
                              final String uuid,
                              final boolean accountExpired,
                              final boolean accountLocked,
                              final boolean credentialsExpired,
                              final boolean enabled) {
        this.username = username;
        this.password = password;
        this.authorities = authorities;
        this.uuid = uuid;
        this.accountExpired = accountExpired;
        this.accountLocked = accountLocked;
        this.credentialsExpired = credentialsExpired;
        this.enabled = enabled;
    }

    @Override
    public Set<GiraffeAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return !accountExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !accountLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return !credentialsExpired;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(final boolean enabled) {
        this.enabled = enabled;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(final String uuid) {
        this.uuid = uuid;
    }

    public long getExpires() {
        return expires;
    }

    public void setExpires(final long expires) {
        this.expires = expires;
    }

    public void setAuthorities(Set<GiraffeAuthority> authorities) {
        this.authorities = authorities;
    }

    public boolean isAccountExpired() {
        return accountExpired;
    }

    public void setAccountExpired(final boolean accountExpired) {
        this.accountExpired = accountExpired;
    }

    public boolean isAccountLocked() {
        return accountLocked;
    }

    public void setAccountLocked(final boolean accountLocked) {
        this.accountLocked = accountLocked;
    }

    public boolean isCredentialsExpired() {
        return credentialsExpired;
    }

    public void setCredentialsExpired(boolean credentialsExpired) {
        this.credentialsExpired = credentialsExpired;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GiraffeUserDetails that = (GiraffeUserDetails) o;
        return expires == that.expires &&
                accountExpired == that.accountExpired &&
                accountLocked == that.accountLocked &&
                credentialsExpired == that.credentialsExpired &&
                enabled == that.enabled &&
                Objects.equals(username, that.username) &&
                Objects.equals(password, that.password) &&
                Objects.equals(authorities, that.authorities);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, password, authorities, expires, accountExpired, accountLocked, credentialsExpired, enabled);
    }

}
