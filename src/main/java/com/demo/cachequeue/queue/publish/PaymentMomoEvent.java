package com.demo.cachequeue.queue.publish;

import com.demo.cachequeue.dto.PaymentMomoDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentMomoEvent extends AbstractEvent{
    private PaymentMomoDTO paymentMomoDTO;
}
