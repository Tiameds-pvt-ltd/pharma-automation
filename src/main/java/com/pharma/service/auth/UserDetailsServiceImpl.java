package com.pharma.service.auth;

import com.pharma.entity.Role;
import com.pharma.entity.User;
import com.pharma.repository.auth.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Autowired
    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String loginIdentifier) {

        User user = userRepository.findUserForLogin(loginIdentifier)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        log.info("User authenticated: {}", user.getUsername());

        // Map DB roles (e.g. SUPERADMIN) to ROLE_SUPERADMIN etc.
        String[] authorities = user.getRoles()
                .stream()
                .map(Role::getName)
                .map(name -> "ROLE_" + name)   // IMPORTANT: add ROLE_ prefix
                .toArray(String[]::new);

        return org.springframework.security.core.userdetails.User
                .withUsername(user.getUsername())
                .password(user.getPassword())
                .authorities(authorities)
                .accountExpired(false)
                .accountLocked(false)
                .credentialsExpired(false)
                .disabled(!user.isEnabled())
                .build();
    }
}


//package com.pharma.service.auth;
//
//import com.pharma.entity.Role;
//import com.pharma.entity.User;
//import com.pharma.repository.auth.UserRepository;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.stereotype.Service;
//
//@Slf4j
//@Service
//public class UserDetailsServiceImpl implements UserDetailsService {
//
//    private final UserRepository userRepository;
//
//    @Autowired
//    public UserDetailsServiceImpl(UserRepository userRepository) {
//        this.userRepository = userRepository;
//    }
//
//    @Override
//    public UserDetails loadUserByUsername(String loginIdentifier) {
//
//        User user = userRepository.findUserForLogin(loginIdentifier)
//                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
//
//        // DO NOT LOG THE FULL USER (LAZY FIELDS!!)
//        log.info("User authenticated: {}", user.getUsername());
//
//        return org.springframework.security.core.userdetails.User
//                .withUsername(user.getUsername())
//                .password(user.getPassword())
//                .authorities(
//                        user.getRoles()
//                                .stream()
//                                .map(Role::getName)
//                                .toArray(String[]::new)
//                )
//                .accountExpired(false)
//                .accountLocked(false)
//                .credentialsExpired(false)
//                .disabled(!user.isEnabled())
//                .build();
//    }
//}
