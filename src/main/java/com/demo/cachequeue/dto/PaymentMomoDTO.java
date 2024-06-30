package com.demo.cachequeue.dto;

import lombok.*;

import java.math.BigInteger;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentMomoDTO {
    private String transactionId;
    private BigInteger balance;
    private String account;
}
