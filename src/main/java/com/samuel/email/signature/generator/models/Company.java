package com.samuel.email.signature.generator.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Getter
@Setter
@Entity
@Table(name = "company")
@Builder
@NoArgsConstructor
@AllArgsConstructor
//@EntityListeners(CompanyEntityListener.class)

public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String missionStatement;

    private String address;

    private String website;

}