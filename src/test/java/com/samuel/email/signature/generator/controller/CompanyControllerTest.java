package com.samuel.email.signature.generator.controller;

import com.samuel.email.signature.generator.models.Company;
import com.samuel.email.signature.generator.service.CompanyService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.ArgumentMatchers.any;

@WebMvcTest(CompanyController.class)
@ExtendWith(MockitoExtension.class)
public class CompanyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CompanyService companyService;

    private Company company;

    @BeforeEach
    void setUp() {
        company = Company.builder()
                .id(1L)
                .name("TechCorp")
                .address("123 Tech Street")
                .build();
    }

    @Test
    void testUpdateCompany_NoAdminRole() throws Exception {
        when(companyService.saveCompany(any(Company.class))).thenReturn(company);
        mockMvc.perform(put("/api/company")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\": 1, \"name\": \"TechCorp\", \"address\": \"123 Tech Street\"}"))
                .andExpect(status().isForbidden());
    }
}
