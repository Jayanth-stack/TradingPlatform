package com.jayanth.tradingplatform.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.jayanth.tradingplatform.domain.USER_ROLE;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String fullName;
    private String email;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;


    private USER_ROLE role = USER_ROLE.ROLE_CUSTOMER;

    @Embedded
    private TwoFactorAuth twoFactorAuth = new TwoFactorAuth();

}
