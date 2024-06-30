package com.demo.cachequeue.queue;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum QueuePriorityEnum {
    LOW(1),
    MEDIUM(5),
    HIGH(10);
    private final int value;
}
