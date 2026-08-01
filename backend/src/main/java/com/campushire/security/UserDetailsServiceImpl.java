package com.campushire.security;

import com.campushire.exception.ResourceNotFoundException;
import com.campushire.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email)
                .map(UserPrincipal::new)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + email));
    }

    public UserPrincipal loadPrincipalByEmail(String email) {
        return userRepository.findByEmail(email)
                .map(UserPrincipal::new)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));
    }
}
