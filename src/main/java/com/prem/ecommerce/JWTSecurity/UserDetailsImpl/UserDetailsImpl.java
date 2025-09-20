package com.prem.ecommerce.JWTSecurity.UserDetailsImpl;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.prem.ecommerce.Model.User;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class UserDetailsImpl implements UserDetails {

    private static final long serialVersionUID = 1L;

    private Long userId;

    private String userName;

    @JsonIgnore
    private String password;

    private String email;

    private Collection<? extends GrantedAuthority> grantedAuthorities;

    public UserDetailsImpl(Long userId, String userName, String password, String email,
            Collection<? extends GrantedAuthority> grantedAuthorities) {
        this.userId = userId;
        this.userName = userName;
        this.password = password;
        this.email = email;
        this.grantedAuthorities = grantedAuthorities;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return grantedAuthorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return userName;
    }

    public static UserDetailsImpl build(User user) {
        List<GrantedAuthority> grantedAuthorities = user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getRoleName().name()))
                .collect(Collectors.toList());

        return new UserDetailsImpl(user.getUserid(), user.getUserName(), user.getPassword(), user.getEmail(),
                grantedAuthorities);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) // if both are the same object
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        UserDetailsImpl user = (UserDetailsImpl) o;
        return userId.equals(user.userId); // compare based on unique userId
    }

}
