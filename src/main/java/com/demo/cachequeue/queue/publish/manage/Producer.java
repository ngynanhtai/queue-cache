package com.demo.cachequeue.queue.publish.manage;

import com.demo.cachequeue.dto.PaymentMomoDTO;
import com.demo.cachequeue.queue.QueueName;
import com.demo.cachequeue.queue.publish.PaymentMomoEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
@Lazy
public class Producer {

    @Autowired
    private QueueDispatcher queueDispatcher;

    public void sendPaymentMomo(PaymentMomoDTO paymentMomoDTO) {
        queueDispatcher.enqueue(
                QueueName.PAYMENT_MOMO,
                new PaymentMomoEvent(paymentMomoDTO)
        );
    }
}
