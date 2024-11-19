package com.samuel.email.signature.generator.service;

import com.samuel.email.signature.generator.events.CompanyUpdatedEvent;
import com.samuel.email.signature.generator.models.Company;
import com.samuel.email.signature.generator.repository.CompanyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CompanyService {

    private final CompanyRepository companyRepository;
    private final ApplicationEventPublisher eventPublisher;

    // Method to get the company (if needed)
    public Optional<Company> getCompany() {
        return companyRepository.findById(1L); // Assuming you have a single company
    }

    // Method to save or update the company
    @Transactional
    public Company saveCompany(Company company) {
        Optional<Company> existingCompany = companyRepository.findById(1L);

        if (existingCompany.isPresent()) {
            Company existing = existingCompany.get();
            if (company.getName() != null) {
                existing.setName(company.getName());
            }
            if (company.getAddress() != null) {
                existing.setAddress(company.getAddress());
            }
            if (company.getWebsite() != null) {
                existing.setWebsite(company.getWebsite());
            }
            if (company.getMissionStatement() != null) {
                existing.setMissionStatement(company.getMissionStatement());
            }

            companyRepository.save(existing);

            // Publish the event after the company is saved
            eventPublisher.publishEvent(new CompanyUpdatedEvent(existing));
            return existing;
        } else {
            company.setId(1L);
            Company savedCompany = companyRepository.save(company);

            // Publish the event after the company is saved
            eventPublisher.publishEvent(new CompanyUpdatedEvent(savedCompany));
            return savedCompany;
        }
    }

    // New method to update company and publish event separately
    @Transactional
    public void updateCompany(Company company) {
        companyRepository.save(company); // Save the company update
        // Publish the event
        eventPublisher.publishEvent(new CompanyUpdatedEvent(company));
    }
}
