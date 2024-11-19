package com.samuel.email.signature.generator.listeners;

import com.samuel.email.signature.generator.events.CompanyUpdatedEvent;
import com.samuel.email.signature.generator.models.Company;
import com.samuel.email.signature.generator.models.User;
import com.samuel.email.signature.generator.service.EmailSignatureService;
import com.samuel.email.signature.generator.service.UsersService;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.List;

@Component
public class CompanyUpdatedListener {

    private final UsersService usersService;
    private final EmailSignatureService emailSignatureService;

    private final String imageDirectory = "images";

    public CompanyUpdatedListener(UsersService usersService, EmailSignatureService emailSignatureService) {
        this.usersService = usersService;
        this.emailSignatureService = emailSignatureService;
    }

    @Async // Optional: Makes the processing asynchronous
    @EventListener
    public void handleCompanyUpdatedEvent(CompanyUpdatedEvent event) {
        Company company = event.getCompany();

        try {
            List<User> users = usersService.findAll();

            for (User user : users) {
                String htmlContent = emailSignatureService.generateHtmlSignature(user, company);
                File generatedImageFile = emailSignatureService.generateImageFromHtml(htmlContent, user.getEmail(), imageDirectory);

                if (!generatedImageFile.exists()) {
                    throw new RuntimeException("Failed to generate signature image for user: " + user.getEmail());
                }
            }

            System.out.println("Signature images updated for all users after company update.");
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error updating signature image for user: " + e.getMessage());
        }
    }
}
