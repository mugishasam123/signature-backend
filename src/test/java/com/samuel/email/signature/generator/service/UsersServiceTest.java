package com.samuel.email.signature.generator.service;

import com.samuel.email.signature.generator.models.User;
import com.samuel.email.signature.generator.payload.request.UpdateUserRequest;
import com.samuel.email.signature.generator.payload.response.MessageResponse;
import com.samuel.email.signature.generator.repository.UserRepository;
import com.samuel.email.signature.generator.security.services.UserDetailsImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UsersServiceTest {

    @InjectMocks
    private UsersService usersService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindAll() {
        User user1 = new User();
        user1.setEmail("user1@example.com");
        User user2 = new User();
        user2.setEmail("user2@example.com");
        when(userRepository.findAll()).thenReturn(Arrays.asList(user1, user2));
        List<User> result = usersService.findAll();

        verify(userRepository, times(1)).findAll();
        assertEquals(2, result.size());
        assertEquals("user1@example.com", result.get(0).getEmail());
        assertEquals("user2@example.com", result.get(1).getEmail());
    }

    @Test
    void testFindByEmail_WhenUserExists() {
        User user = new User();
        user.setEmail("user@example.com");
        when(userRepository.findByEmail("user@example.com")).thenReturn(Optional.of(user));
        User result = usersService.findByEmail("user@example.com");

        verify(userRepository, times(1)).findByEmail("user@example.com");
        assertNotNull(result);
        assertEquals("user@example.com", result.getEmail());
    }

    @Test
    void testFindByEmail_WhenUserDoesNotExist() {
        when(userRepository.findByEmail("unknown@example.com")).thenReturn(Optional.empty());
        Exception exception = assertThrows(
                UsernameNotFoundException.class,
                () -> usersService.findByEmail("unknown@example.com")
        );

        assertEquals("User Not Found with email: unknown@example.com", exception.getMessage());
        verify(userRepository, times(1)).findByEmail("unknown@example.com");
    }

    @Test
    void testUpdatePhoneNumber_Success() {
        UserDetailsImpl userDetails = new UserDetailsImpl(
                1L,
                "user", // username
                "user@example.com",
                "password",
                Set.of() // Authorities
        );
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        User user = new User();
        user.setEmail("user@example.com");
        when(userRepository.findByEmail("user@example.com")).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);
        UpdateUserRequest request = new UpdateUserRequest();
        request.setPhoneNumber("123456789");

        MessageResponse response = usersService.updatePhoneNumber(request);
        verify(userRepository, times(1)).findByEmail("user@example.com");
        verify(userRepository, times(1)).save(user);
        assertEquals("Phone number updated successfully!", response.getMessage());
        assertEquals("123456789", user.getPhoneNumber());
    }

    @Test
    void testUpdatePhoneNumber_Error() {
        UpdateUserRequest request = new UpdateUserRequest();
        request.setPhoneNumber("");
        MessageResponse response = usersService.updatePhoneNumber(request);
        verify(userRepository, never()).findByEmail(anyString());
        assertEquals("Error: Please provide phone number!", response.getMessage());
    }

    @Test
    void testUpdateUserTitle_Success() {
        User user = new User();
        user.setEmail("user@example.com");
        when(userRepository.findByEmail("user@example.com")).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);
        UpdateUserRequest request = new UpdateUserRequest();
        request.setEmail("user@example.com");
        request.setUserTitle("New Title");
        MessageResponse response = usersService.updateUserTitle(request);

        verify(userRepository, times(1)).findByEmail("user@example.com");
        verify(userRepository, times(1)).save(user);
        assertEquals("User Title updated successfully!", response.getMessage());
        assertEquals("New Title", user.getUserTitle());
    }

    @Test
    void testUpdateUserTitle_Error() {
        UpdateUserRequest request = new UpdateUserRequest();
        request.setEmail("");
        request.setUserTitle("");
        MessageResponse response = usersService.updateUserTitle(request);

        verify(userRepository, never()).findByEmail(anyString());
        assertEquals("Error: Please provide all fields", response.getMessage());
    }
}
