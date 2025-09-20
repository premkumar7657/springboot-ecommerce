package com.prem.ecommerce.JWTSecurity.UserDetailsImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.prem.ecommerce.Model.User;
import com.prem.ecommerce.Repository.UserRepository;


@Component
public class UserDetailsServiceImpl implements UserDetailsService {


    @Autowired
    UserRepository userRepository;

    @Override
    @Transactional
    public UserDetailsImpl loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userRepository.findByUserName(username)
        .orElseThrow(()-> new UsernameNotFoundException("User not found with this username : " + username));

        return UserDetailsImpl.build(user);
        
    }

}
