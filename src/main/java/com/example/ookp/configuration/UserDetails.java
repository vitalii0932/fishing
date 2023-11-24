package com.example.ookp.configuration;

import com.example.ookp.model.User;
import com.example.ookp.repository.UserRepository;
import com.example.ookp.service.UserService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Data
@RequiredArgsConstructor
public class UserDetails implements UserDetailsService {
    private final UserService userService;
    @Override
    public org.springframework.security.core.userdetails.UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println(username);
        User user = userService.findByEmail(username);
        System.out.println(user);
        if(user == null) {
            new UsernameNotFoundException("User not exists by " + username);
        }
        GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_"+user.getRole().getName());
        Set<GrantedAuthority> authorities = Collections.singleton(authority);
        System.out.println(authorities.toString());
        return new org.springframework.security.core.userdetails.User(username, user.getPassword(), authorities);
    }
}
