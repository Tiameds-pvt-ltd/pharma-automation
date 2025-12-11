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

        // DO NOT LOG THE FULL USER (LAZY FIELDS!!)
        log.info("User authenticated: {}", user.getUsername());

        return org.springframework.security.core.userdetails.User
                .withUsername(user.getUsername())
                .password(user.getPassword())
                .authorities(
                        user.getRoles()
                                .stream()
                                .map(Role::getName)
                                .toArray(String[]::new)
                )
                .accountExpired(false)
                .accountLocked(false)
                .credentialsExpired(false)
                .disabled(!user.isEnabled())
                .build();
    }
}


//package com.pharma.service.auth;
//
//import com.pharma.repository.auth.UserRepository;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.stereotype.Service;
//import com.pharma.entity.User;
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
//    public UserDetails loadUserByUsername(String loginIdentifier) throws UsernameNotFoundException {
//        User user = userRepository.findByUsername(loginIdentifier)
//                .orElseGet(() -> userRepository.findByEmail(loginIdentifier).orElse(null)); // Use Optional
//
//        if (user == null) {
//            throw new UsernameNotFoundException("Could not find user with username or email: " + loginIdentifier);
//        }
//
//        //set the user details
//
//        log.info("User found: {}", user);
//        System.out.println("Roles for user: " + user.getRoles());
//        return new MyUserDetails(user);
//    }
//
//
//}
