package com.demo.cachequeue.queue.subscribe.manage;

import com.demo.cachequeue.queue.QueueName;
import com.demo.cachequeue.queue.publish.PaymentMomoEvent;
import com.demo.cachequeue.queue.subscribe.PaymentMomoListener;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Consumer {
    @Autowired
    private PaymentMomoListener paymentMomoListener;

    @RabbitListener(queues = QueueName.PAYMENT_MOMO)
    public void receivePaymentMomo(PaymentMomoEvent event) {
        paymentMomoListener.invoke(event);
    }
}
