package com.fsdm.hopital.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmailVerificationToken {
    @Id
    @SequenceGenerator(name = "email_verification_token_seq" , sequenceName = "email_verification_token_seq" , allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "email_verification_token_seq")
    private Long id;
    private String token;
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "uid" ,
    nullable = false ,
    foreignKey = @ForeignKey(name = "FK_EMAIL_VERIFICATION_USER"))
    private User user;
}
