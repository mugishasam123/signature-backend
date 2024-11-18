package com.samuel.email.signature.generator.service;

import com.samuel.email.signature.generator.models.Company;
import com.samuel.email.signature.generator.repository.CompanyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CompanyServiceTest {

    @InjectMocks
    private CompanyService companyService;

    @Mock
    private CompanyRepository companyRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetCompany_WhenCompanyExists() {
        Company company = new Company();
        company.setId(1L);
        company.setName("Example Company");
        when(companyRepository.findById(1L)).thenReturn(Optional.of(company));
        Optional<Company> result = companyService.getCompany();
        verify(companyRepository, times(1)).findById(1L);
        assertTrue(result.isPresent());
        assertEquals("Example Company", result.get().getName());
    }

    @Test
    void testGetCompany_WhenCompanyDoesNotExist() {
        when(companyRepository.findById(1L)).thenReturn(Optional.empty());
        Optional<Company> result = companyService.getCompany();
        verify(companyRepository, times(1)).findById(1L);
        assertFalse(result.isPresent());
    }

    @Test
    void testSaveCompany_WhenCompanyExists() {
        Company existingCompany = new Company();
        existingCompany.setId(1L);
        existingCompany.setName("Old Company");
        existingCompany.setAddress("Old Address");
        Company newCompany = new Company();
        newCompany.setName("New Company");
        newCompany.setAddress("New Address");
        newCompany.setWebsite("www.newcompany.com");
        newCompany.setMissionStatement("New Mission");

        when(companyRepository.findById(1L)).thenReturn(Optional.of(existingCompany));
        when(companyRepository.save(any(Company.class))).thenAnswer(invocation -> invocation.getArgument(0));
        Company result = companyService.saveCompany(newCompany);

        verify(companyRepository, times(1)).findById(1L);
        verify(companyRepository, times(1)).save(existingCompany);

        assertEquals("New Company", result.getName());
        assertEquals("New Address", result.getAddress());
        assertEquals("www.newcompany.com", result.getWebsite());
        assertEquals("New Mission", result.getMissionStatement());
    }

    @Test
    void testSaveCompany_WhenCompanyDoesNotExist() {
        Company newCompany = new Company();
        newCompany.setName("New Company");
        newCompany.setAddress("New Address");
        newCompany.setWebsite("www.newcompany.com");
        newCompany.setMissionStatement("New Mission");
        when(companyRepository.findById(1L)).thenReturn(Optional.empty());
        when(companyRepository.save(any(Company.class))).thenAnswer(invocation -> invocation.getArgument(0));
        Company result = companyService.saveCompany(newCompany);

        verify(companyRepository, times(1)).findById(1L);
        verify(companyRepository, times(1)).save(newCompany);

        assertEquals(1L, result.getId());
        assertEquals("New Company", result.getName());
        assertEquals("New Address", result.getAddress());
        assertEquals("www.newcompany.com", result.getWebsite());
        assertEquals("New Mission", result.getMissionStatement());
    }
}
