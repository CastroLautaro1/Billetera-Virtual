package com.billetera_virtual.account.infra.data.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "accounts")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AccountEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "account_id")
    private Long id;

    @Column(name = "user_id", nullable = false, unique = true)
    private Long userId;

    @Column(length = 23, unique = true)
    private String cvu;

    @Column(length = 21, unique = true)
    private String alias;

    @Column(nullable = false, columnDefinition = "double precision default 0.0")
    private double balance;

    @Column(nullable = false)
    private boolean status;

}
