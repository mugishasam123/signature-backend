package com.samuel.email.signature.generator.service;

import com.samuel.email.signature.generator.models.Company;
import com.samuel.email.signature.generator.repository.CompanyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CompanyService {

    private final CompanyRepository companyRepository;

    public Optional<Company> getCompany() {
        Optional<Company>company = companyRepository.findById(1L);
        return company;
    }

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

            return companyRepository.save(existing);
        } else {
            company.setId(1L);
            return companyRepository.save(company);
        }
    }
}