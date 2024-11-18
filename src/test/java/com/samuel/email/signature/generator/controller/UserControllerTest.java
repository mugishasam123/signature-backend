package com.samuel.email.signature.generator.controller;

import com.samuel.email.signature.generator.models.ERole;
import com.samuel.email.signature.generator.models.Role;
import com.samuel.email.signature.generator.payload.request.UpdateUserRequest;
import com.samuel.email.signature.generator.payload.response.MessageResponse;
import com.samuel.email.signature.generator.security.services.UserDetailsImpl;
import com.samuel.email.signature.generator.service.UsersService;
import com.samuel.email.signature.generator.models.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {
    private MockMvc mockMvc;
    @Mock
    private UsersService usersService;

    @InjectMocks
    private UserController userController;

    private static final String EMAIL = "testuser@example.com";
    private static final String PHONE = "123-456-7890";
    private static final String USER_TITLE = "Mr.";
    private static final String PASSWORD = "password123";

    private User user;

    @BeforeEach
    public void setUp() {
        user = User.builder()
                .id(1L)
                .email(EMAIL)
                .username("testuser")
                .password(PASSWORD)
                .phoneNumber(PHONE)
                .userTitle(USER_TITLE)
                .build();

        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    @Test
    void testGetCompany_AdminAccess() throws Exception {
        when(usersService.findAll()).thenReturn(Arrays.asList(user));
        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].email").value(EMAIL))
                .andExpect(jsonPath("$[0].username").value("testuser"));
    }

    @Test
    void testWhoAmI() throws Exception {
        Authentication authentication = Mockito.mock(Authentication.class);
        User user = User.builder()
                .email(EMAIL)
                .username("testuser")
                .roles(Collections.singleton(Role.builder().name(ERole.ROLE_USER).build()))
                .build();

        UserDetailsImpl userDetails = UserDetailsImpl.build(user);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        when(usersService.findByEmail(EMAIL)).thenReturn(user);
        mockMvc.perform(get("/api/users/me"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value(EMAIL))
                .andExpect(jsonPath("$.username").value("testuser"));
    }

    @Test
    void testUpdatePhoneNumber() throws Exception {
        UpdateUserRequest updateUserRequest = new UpdateUserRequest();
        updateUserRequest.setPhoneNumber("987-654-3210");
        MessageResponse messageResponse = new MessageResponse("Phone number updated successfully");
        when(usersService.updatePhoneNumber(any(UpdateUserRequest.class))).thenReturn(messageResponse);
        mockMvc.perform(put("/api/users/phone")
                        .contentType("application/json")
                        .content("{ \"phoneNumber\": \"987-654-3210\" }"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Phone number updated successfully"));
    }
    @Test
    void testUpdateUserTitle_AdminAccess() throws Exception {
        UpdateUserRequest updateUserRequest = new UpdateUserRequest();
        updateUserRequest.setUserTitle("Dr.");
        MessageResponse messageResponse = new MessageResponse("User title updated successfully");
        when(usersService.updateUserTitle(any(UpdateUserRequest.class))).thenReturn(messageResponse);
        mockMvc.perform(put("/api/users/userTitle")
                        .contentType("application/json")
                        .content("{ \"userTitle\": \"Dr.\" }"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("User title updated successfully"));
    }

}
