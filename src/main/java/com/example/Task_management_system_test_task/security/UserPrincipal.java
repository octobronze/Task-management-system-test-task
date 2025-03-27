package com.example.Task_management_system_test_task.security;

import com.example.Task_management_system_test_task.tables.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@NoArgsConstructor
@Setter
@Getter
public class UserPrincipal implements UserDetails {
    private Integer id;
    private String password;
    private String username;
    private List<? extends GrantedAuthority> authorities;

    private static final String ROLE_PREFIX = "ROLE_";

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
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

    public boolean isAdmin() {
        return this.authorities.iterator().next().getAuthority().equals(ROLE_PREFIX + "Admin");
    }

    public void setAuthorities(String roleName) {
        this.authorities = List.of(new SimpleGrantedAuthority(ROLE_PREFIX + roleName));
    }

    public UserPrincipal(User user) {
        this.id = user.getId();
        this.password = user.getPassword();
        this.username = user.getEmail();
        this.authorities = List.of(new SimpleGrantedAuthority(ROLE_PREFIX + user.getRole().getName()));
    }
}
