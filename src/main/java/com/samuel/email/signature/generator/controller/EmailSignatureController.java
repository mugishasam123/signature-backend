package com.samuel.email.signature.generator.controller;

import com.samuel.email.signature.generator.models.Company;
import com.samuel.email.signature.generator.models.User;
import com.samuel.email.signature.generator.security.services.UserDetailsImpl;
import com.samuel.email.signature.generator.service.CompanyService;
import com.samuel.email.signature.generator.service.EmailSignatureService;
import com.samuel.email.signature.generator.service.UsersService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Optional;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/api/signature")
@RequiredArgsConstructor
public class EmailSignatureController {

    private final UsersService usersService;
    private final CompanyService companyService;
    private final EmailSignatureService emailSignatureService;

    private final String imageDirectory = "images";
    private final ResourceLoader resourceLoader;


    @GetMapping("/image/{email}")
    public ResponseEntity<?> getSignatureImage(@PathVariable("email") String email) {
        try {
            String sanitizedEmail = email.replaceAll("[^a-zA-Z0-9@.]", "");
            String outputPath = imageDirectory + File.separator + sanitizedEmail + "_signature.png";
            Path filePath = Paths.get(System.getProperty("user.dir")).resolve(outputPath);

            System.out.println("fullPath: " + filePath.toString());
            System.out.println("Resolved file path: " + outputPath);
            System.out.println("Current Working Directory: " + System.getProperty("user.dir"));

            if (!Files.exists(filePath)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Signature file not found for the given email.");
            }

            Resource resource = resourceLoader.getResource("file:" + filePath.toString());
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_TYPE, Files.probeContentType(filePath))
                    .body(resource);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while retrieving the signature file.");
        }
    }


    @GetMapping("/htmlSignature")
    public ResponseEntity<?> getHtmlSignature() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            User currentUser = usersService.findByEmail(userDetails.getEmail());
            Optional<Company> company = companyService.getCompany();
            String htmlContent = emailSignatureService.generateHtmlSignature(currentUser, company.orElse(null));
            return ResponseEntity.ok(htmlContent);
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }

    }
    @GetMapping("/generate")
    public ResponseEntity<?> generateImageSignature( ) throws Exception {

        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            User currentUser = usersService.findByEmail(userDetails.getEmail());
            Optional<Company> company = companyService.getCompany();
            String htmlContent = emailSignatureService.generateHtmlSignature(currentUser, company.orElse(null));
            File generatedImageFile = emailSignatureService.generateImageFromHtml(htmlContent, currentUser.getEmail(), imageDirectory);

            if (!generatedImageFile.exists()) {
                throw new FileNotFoundException("Generated image not found");
            }

            Resource resource = new FileSystemResource(generatedImageFile);
            return ResponseEntity.ok()
                    .header(HttpHeaders.CACHE_CONTROL, "no-cache, no-store, must-revalidate")
                    .header(HttpHeaders.PRAGMA, "no-cache")
                    .header(HttpHeaders.EXPIRES, "0")
                    .contentType(MediaType.IMAGE_PNG)
                    .body(resource);
        } catch (RuntimeException e) {
            e.printStackTrace();
        }

        return ResponseEntity.ok("testing image");
    }


}
