package com.bookingManagement.util;

import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;

@Component
public class LockManager {
    private final ConcurrentHashMap<Long, Object> locks = new ConcurrentHashMap<>();

    public Object getLock(Long tupleId) {
        return locks.computeIfAbsent(tupleId, k -> new Object());
    }

    public void releaseLock(Long tupleId) {
        //rimuovere il lock se non è più necessario
        locks.remove(tupleId);
    }
}