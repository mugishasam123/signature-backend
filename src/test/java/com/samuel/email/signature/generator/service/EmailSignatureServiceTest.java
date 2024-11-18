package com.samuel.email.signature.generator.service;

import com.samuel.email.signature.generator.models.Company;
import com.samuel.email.signature.generator.models.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import java.io.File;
import java.io.IOException;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EmailSignatureServiceTest {

    @InjectMocks
    private EmailSignatureService emailSignatureService;

    @Mock
    private User user;

    @Mock
    private Company company;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGenerateHtmlSignature() {
        String username = "john_doe";
        String userTitle = "Software Engineer";
        String phoneNumber = "123-456-789";
        String companyName = "IST Africa";
        String companyAddress = "Kigali • KG 28 Ave, 57 • Rwanda";
        String website = "www.ist.com";
        String missionStatement = "Empowering learning, every day and everywhere.";

        when(user.getUsername()).thenReturn(username);
        when(user.getUserTitle()).thenReturn(userTitle);
        when(user.getPhoneNumber()).thenReturn(phoneNumber);
        when(company.getName()).thenReturn(companyName);
        when(company.getAddress()).thenReturn(companyAddress);
        when(company.getWebsite()).thenReturn(website);
        when(company.getMissionStatement()).thenReturn(missionStatement);

        String htmlSignature = emailSignatureService.generateHtmlSignature(user, company);

        assertTrue(htmlSignature.contains(username));
        assertTrue(htmlSignature.contains(userTitle));
        assertTrue(htmlSignature.contains(phoneNumber));
        assertTrue(htmlSignature.contains(companyName));
        assertTrue(htmlSignature.contains(companyAddress));
        assertTrue(htmlSignature.contains(website));
        assertTrue(htmlSignature.contains(missionStatement));
    }

    @Test
    void testGenerateHtmlSignatureWithMissingData() {
        when(user.getUsername()).thenReturn(null);
        when(user.getUserTitle()).thenReturn(null);
        when(user.getPhoneNumber()).thenReturn(null);
        when(company.getName()).thenReturn(null);
        when(company.getAddress()).thenReturn(null);
        when(company.getWebsite()).thenReturn(null);
        when(company.getMissionStatement()).thenReturn(null);

        String htmlSignature = emailSignatureService.generateHtmlSignature(user, company);
        assertTrue(htmlSignature.contains("N/A"));
        assertTrue(htmlSignature.contains("IST Africa"));
        assertTrue(htmlSignature.contains("www.ist.com"));
        assertTrue(htmlSignature.contains("Empowering learning, every day and everywhere."));
    }

    @Test
    void testGenerateImageFromHtml() throws IOException {
        String htmlContent = "<html><body><h1>Test Signature</h1></body></html>";
        String userEmail = "user@example.com";
        String imageDirectory = "/tmp";

        File mockedFile = mock(File.class);
        when(mockedFile.exists()).thenReturn(false);
        when(mockedFile.mkdirs()).thenReturn(true);

        File generatedImage = emailSignatureService.generateImageFromHtml(htmlContent, userEmail, imageDirectory);
        assertNotNull(generatedImage);
        assertTrue(generatedImage.getName().endsWith(".png"));
    }

    @Test
    void testGenerateImageFromHtmlIOException() throws IOException {
        String htmlContent = "<html><body><h1>Test Signature</h1></body></html>";
        String userEmail = "user@example.com";
        String imageDirectory = "/tmp";

        EmailSignatureService spyService = spy(emailSignatureService);
        doThrow(IOException.class).when(spyService).generateImageFromHtml(htmlContent, userEmail, imageDirectory);

        assertThrows(IOException.class, () -> {
            spyService.generateImageFromHtml(htmlContent, userEmail, imageDirectory);
        });
    }

}
