package br.com.ronan.springboot.service;

import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import br.com.ronan.springboot.repository.SpringbootUserRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SpringbootUserDetailsService implements UserDetailsService {
    private final SpringbootUserRepository springbootUserRepository;

    

    @Override
    public UserDetails loadUserByUsername(String username) {
        return Optional.ofNullable(springbootUserRepository.findByUserName(username)).orElseThrow(() -> new UsernameNotFoundException("Springboot user not found"));
    }
}
