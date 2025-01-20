package com.shadowshiftstudio.aniwayauth.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name="password_reset_tokens")
public class PasswordResetToken {
    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "token")
    private String token;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @Column(name="expiry_date")
    private Date expiryDate;

    @Column(name = "created_at", updatable = false)
    private Date createdAt;

    public boolean isExpired() {
        return new Date().after(this.expiryDate);
    }
}
