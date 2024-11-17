package com.samuel.email.signature.generator.controller;

import com.samuel.email.signature.generator.models.Company;
import com.samuel.email.signature.generator.service.CompanyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/company")
@RequiredArgsConstructor
public class CompanyController {

    private final CompanyService companyService;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<Optional<Company>> getCompany() {
        try {
            Optional<Company> company = companyService.getCompany();
            return ResponseEntity.ok(company);
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }

    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping
    public ResponseEntity<Company> updateCompany(@RequestBody Company company) {
        try {
            Company updatedCompany = companyService.saveCompany(company);
            return ResponseEntity.ok(updatedCompany);
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }

    }
}
