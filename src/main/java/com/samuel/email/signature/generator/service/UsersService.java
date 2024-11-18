package com.samuel.email.signature.generator.service;

import com.samuel.email.signature.generator.models.ERole;
import com.samuel.email.signature.generator.models.User;
import com.samuel.email.signature.generator.payload.request.UpdateUserRequest;
import com.samuel.email.signature.generator.payload.response.MessageResponse;
import com.samuel.email.signature.generator.repository.UserRepository;
import com.samuel.email.signature.generator.security.services.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UsersService {
    private final UserRepository userRepository;

    public List<User> findAll() {
        return userRepository.findAll()
                .stream()
                .filter(user -> user.getRoles().stream()
                        .noneMatch(role -> role.getName() == ERole.ROLE_ADMIN))
                .collect(Collectors.toList());
    }



    public User findByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found with email: " + email));
        return user;
    }

    public MessageResponse updatePhoneNumber(UpdateUserRequest updateUserRequest) {
        if (!StringUtils.hasText(updateUserRequest.getPhoneNumber())) {
            return MessageResponse.builder().message("Error: Please provide phone number!").build();
        }
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        User foundUser = findByEmail(userDetails.getEmail());
        foundUser.setPhoneNumber(updateUserRequest.getPhoneNumber());
        userRepository.save(foundUser);

        return MessageResponse.builder().message("Phone number updated successfully!").build();
    }

    public MessageResponse updateUserTitle(UpdateUserRequest updateUserRequest) {
        if (!StringUtils.hasText(updateUserRequest.getUserTitle())
                || !StringUtils.hasText(updateUserRequest.getEmail())) {
            return MessageResponse.builder().message("Error: Please provide all fields").build();
        }
        User foundUser = findByEmail(updateUserRequest.getEmail());
        foundUser.setUserTitle(updateUserRequest.getUserTitle());
        userRepository.save(foundUser);

        return MessageResponse.builder().message("User Title updated successfully!").build();
    }
}