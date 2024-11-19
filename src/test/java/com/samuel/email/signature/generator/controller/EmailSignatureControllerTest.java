// package com.samuel.email.signature.generator.controller;

// import com.samuel.email.signature.generator.security.services.UserDetailsImpl;
// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.Test;
// import org.mockito.*;
// import org.springframework.core.io.FileSystemResource;
// import org.springframework.core.io.ResourceLoader;
// import org.springframework.security.core.Authentication;
// import org.springframework.security.core.context.SecurityContextHolder;
// import org.springframework.test.web.servlet.MockMvc;
// import org.springframework.test.web.servlet.setup.MockMvcBuilders;
// import static org.mockito.Mockito.*;
// import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
// import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

// class EmailSignatureControllerTest {

//     private MockMvc mockMvc;

//     @Mock private ResourceLoader resourceLoader;

//     @InjectMocks private EmailSignatureController emailSignatureController;

//     @BeforeEach
//     void setUp() {
//         MockitoAnnotations.openMocks(this);
//         mockMvc = MockMvcBuilders.standaloneSetup(emailSignatureController).build();
//     }

//     private void mockAuthentication(String email) {
//         Authentication authentication = mock(Authentication.class);
//         UserDetailsImpl userDetails = mock(UserDetailsImpl.class);
//         when(authentication.getPrincipal()).thenReturn(userDetails);
//         when(userDetails.getEmail()).thenReturn(email);
//         SecurityContextHolder.getContext().setAuthentication(authentication);
//     }

//     @Test
//     void testGetSignatureImage() throws Exception {
//         String email = "testuser@example.com";
//         mockAuthentication(email);
//         String signatureFilePath = "images/testuser@example.com_signature.png";
//         when(resourceLoader.getResource("file:" + signatureFilePath)).thenReturn(new FileSystemResource(signatureFilePath));
//         mockMvc.perform(get("/api/signature/image/{email}", email))
//                 .andExpect(status().isNotFound())
//                 .andExpect(content().contentType("text/plain;charset=ISO-8859-1"));
//     }


// }
