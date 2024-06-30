package com.demo.cachequeue.queue.publish.manage;

import com.demo.cachequeue.queue.QueueModeEnum;
import com.demo.cachequeue.queue.QueueName;
import com.demo.cachequeue.queue.QueuePriorityEnum;
import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

import javax.annotation.PostConstruct;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

@Component
@Log4j2
public class QueueDispatcher {

    @Autowired
    private ApplicationEventPublisher localMsgPublisher;

    @Autowired
    private Environment env;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private AmqpAdmin amqpAdmin;

    @Value("${queue.mode}")
    protected QueueModeEnum queueMode;

    @Value("${queue.exchange}")
    protected String exchange;

    private final String ROUTING_KEY = "routing.key";

    @PostConstruct
    public void init() {
        DirectExchange directExchange = new DirectExchange(exchange);
        amqpAdmin.declareExchange(directExchange);

        Map<String, Object> args = new HashMap<>();
        args.put("x-max-priority", QueuePriorityEnum.HIGH.getValue());

        Class<?> clazz = QueueName.class;
        Field[] fields = clazz.getFields();
        for (Field field : fields) {
            try {
                String queueName = field.get(field.getName()).toString();
                String routingKey = queueName.concat(ROUTING_KEY);
                Queue newQueue = new Queue(queueName, true, false, false, args);
                amqpAdmin.declareQueue(newQueue);
                amqpAdmin.declareBinding(BindingBuilder.bind(newQueue).to(directExchange).with(routingKey));
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void enqueueToRabbitMQ(String queueName, Object msg) {

        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        String routingKey = queueName.concat(ROUTING_KEY);
        rabbitTemplate.convertAndSend(exchange, routingKey, msg);

        stopWatch.stop();
        log.info(String.format("enqueue to RabbitMQ success ->: %s -> %s IN %s ms",
                queueName,
                msg,
                stopWatch.getTotalTimeMillis()));
    }

    public void enqueueToRabbitMQ(String queueName, Object msg, QueuePriorityEnum priority) {

        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        String routingKey = queueName.concat(ROUTING_KEY);
        rabbitTemplate.convertAndSend(exchange, routingKey, msg, new MessagePostProcessor() {
            @Override
            public Message postProcessMessage(Message message) throws AmqpException {
                message.getMessageProperties().setPriority(priority.getValue());
                return message;
            }
        });

        stopWatch.stop();
        log.info(String.format("enqueue to RabbitMQ success ->: %s -> %s IN %s ms",
                queueName,
                msg,
                stopWatch.getTotalTimeMillis()));
    }

    private void enqueueToLocal(String queueName, Object msg) {
        localMsgPublisher.publishEvent(msg);
        log.info(String.format("send msg to Local Queue success -> queue: %s -> %s", queueName, msg));
    }

    public void enqueue(String queueName, Object msg) {
        try {
            QueueModeEnum sendMode = queueMode;
            switch (sendMode) {
                case LOCAL:
                    enqueueToLocal(queueName, msg);
                    break;
                case AMQP:
                    enqueueToRabbitMQ(queueName, msg);
                    break;
            }
        } catch (Exception e) {
            log.error(
                    String.format("Enqueue error -> queueName: %s, msg: %s", queueName, msg),
                    e
            );
            throw e;
        }

    }
}
