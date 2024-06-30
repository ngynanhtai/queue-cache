package com.demo.cachequeue.queue.subscribe;

import com.demo.cachequeue.dto.PaymentMomoDTO;
import com.demo.cachequeue.queue.publish.PaymentMomoEvent;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@Lazy
@Log4j2
public class PaymentMomoListener {
    @EventListener
    public void invoke(PaymentMomoEvent event) {
        PaymentMomoDTO paymentMomoDTO = event.getPaymentMomoDTO();
        log.info(paymentMomoDTO.getTransactionId());
    }
}
