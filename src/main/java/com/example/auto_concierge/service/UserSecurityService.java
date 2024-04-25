package com.example.auto_concierge.service;

import com.example.auto_concierge.repository.UserRepository;
import com.example.auto_concierge.security.UserWithCredentials;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
public class UserSecurityService implements UserDetailsService {

    private final UserRepository userRepository;

    @Autowired
    public UserSecurityService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return new UserWithCredentials(userRepository.findByUsername(username).orElseThrow(
                () -> new NoSuchElementException("User with username="+username+" not found")
        ));
    }
}
