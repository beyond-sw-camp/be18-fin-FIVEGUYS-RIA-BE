package com.fiveguys.RIA.RIA_Backend.auth.service;


import com.fiveguys.RIA.RIA_Backend.user.model.entity.User;
import com.fiveguys.RIA.RIA_Backend.user.model.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String employeeNo) throws UsernameNotFoundException {
        User user = userRepository.findByEmployeeNoWithRole(employeeNo)
            .orElseThrow(() -> new UsernameNotFoundException("User not found: " + employeeNo));

        return new CustomUserDetails(user);
    }
}