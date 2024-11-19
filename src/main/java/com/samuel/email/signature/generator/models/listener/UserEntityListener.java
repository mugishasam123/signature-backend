package com.samuel.email.signature.generator.models.listener;

import com.samuel.email.signature.generator.config.SpringContext;
import com.samuel.email.signature.generator.models.Company;
import com.samuel.email.signature.generator.models.User;
import com.samuel.email.signature.generator.service.CompanyService;
import com.samuel.email.signature.generator.service.EmailSignatureService;
import jakarta.persistence.PostUpdate;

import java.io.File;
import java.util.Optional;

public class UserEntityListener {

    private final String imageDirectory = "images";

    @PostUpdate
    public void afterUserUpdate(User user) {
        try {
            EmailSignatureService emailSignatureService = SpringContext.getBean(EmailSignatureService.class);
            CompanyService companyService = SpringContext.getBean(CompanyService.class);
            Optional<Company> companyOptional = companyService.getCompany();
            Company company = companyOptional.orElse(null);
            String htmlContent = emailSignatureService.generateHtmlSignature(user, company);

            File generatedImageFile = emailSignatureService.generateImageFromHtml(htmlContent, user.getEmail(), imageDirectory);

            if (!generatedImageFile.exists()) {
                throw new RuntimeException("Failed to generate signature image for user: " + user.getEmail());
            }

            System.out.println("Signature image updated for user: " + user.getEmail());
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error updating signature image for user: " + e.getMessage());
        }
    }
}
