package com.demo.cachequeue.queue.publish;

import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

@Getter
@Setter
public class AbstractEvent extends ApplicationEvent {
    protected Map<String, Object> extra = new LinkedHashMap<>();

    public AbstractEvent() {
        super(UUID.randomUUID().toString());
    }
}
