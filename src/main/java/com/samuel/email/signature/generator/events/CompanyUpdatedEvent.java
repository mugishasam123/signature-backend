package com.samuel.email.signature.generator.events;

import com.samuel.email.signature.generator.models.Company;
public class CompanyUpdatedEvent {
    private final Company company;

    public CompanyUpdatedEvent(Company company) {
        this.company = company;
    }

    public Company getCompany() {
        return company;
    }
}
