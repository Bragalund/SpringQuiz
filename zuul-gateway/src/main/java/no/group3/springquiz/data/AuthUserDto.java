package no.group3.springquiz.data;

import java.util.Set;

/**
 * Created by josoder on 07.12.17.
 */
public class AuthUserDto {
    private String username;
    private Set<String> roles;

    public AuthUserDto(String username, Set<String> roles) {
        this.username = username;
        this.roles = roles;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Set<String> getRoles() {
        return roles;
    }

    public void setRoles(Set<String> roles) {
        this.roles = roles;
    }
}
