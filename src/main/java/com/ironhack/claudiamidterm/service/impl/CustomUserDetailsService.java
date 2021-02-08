package com.ironhack.claudiamidterm.service.impl;

import com.ironhack.claudiamidterm.model.User;
import com.ironhack.claudiamidterm.repository.*;
import com.ironhack.claudiamidterm.security.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.*;

import java.util.*;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Optional<User> user=userRepository.findByUsername(username);

        if(user.isEmpty()){
            throw new UsernameNotFoundException("User does not exist");
        }

        CustomUserDetails customUserDetails=new CustomUserDetails(user.get());

        return customUserDetails;
    }
}
