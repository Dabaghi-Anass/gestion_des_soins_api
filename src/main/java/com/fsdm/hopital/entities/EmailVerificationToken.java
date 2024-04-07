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
public class EmailVerificationToken extends BaseEntity {
    private String token;
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "uid" ,
    nullable = false ,
    foreignKey = @ForeignKey(name = "FK_EMAIL_VERIFICATION_USER"))
    private User user;
}
