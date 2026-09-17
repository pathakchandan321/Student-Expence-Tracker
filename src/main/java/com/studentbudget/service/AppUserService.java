package com.studentbudget.service;

import com.studentbudget.model.AppUser;
import com.studentbudget.model.RegistrationForm;
import com.studentbudget.repository.AppUserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AppUserService implements UserDetailsService {
    private final AppUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    public AppUserService(AppUserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository; this.passwordEncoder = passwordEncoder;
    }
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AppUser user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return User.withUsername(user.getUsername()).password(user.getPassword()).roles("USER").build();
    }
    public String register(RegistrationForm form) {
        if (form.getUsername() == null || form.getUsername().trim().length() < 3) return "Username must have at least 3 characters.";
        if (form.getPassword() == null || form.getPassword().length() < 6) return "Password must have at least 6 characters.";
        if (!form.getPassword().equals(form.getConfirmPassword())) return "Passwords do not match.";
        if (userRepository.findByUsername(form.getUsername().trim()).isPresent()) return "This username is already taken.";
        AppUser user = new AppUser(); user.setUsername(form.getUsername().trim()); user.setPassword(passwordEncoder.encode(form.getPassword()));
        userRepository.save(user); return null;
    }
    public AppUser getLoggedInUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByUsername(username).orElseThrow();
    }
}
