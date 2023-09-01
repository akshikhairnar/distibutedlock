package com.poc.distributedlock.service;

import org.springframework.stereotype.Service;

@Service
public interface LockService {
    String lock();
    void failLock();
    String properLock();
}
