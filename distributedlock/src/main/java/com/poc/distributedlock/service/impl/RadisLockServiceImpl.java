package com.poc.distributedlock.service.impl;

import com.poc.distributedlock.service.LockService;
import org.springframework.integration.support.locks.LockRegistry;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;

@Service
public class RadisLockServiceImpl implements LockService {

    private static final String MY_LOCK_KEY = "someLockKey";

    private final LockRegistry lockRegistry;

    public RadisLockServiceImpl(LockRegistry redisLockRegistry) {
        this.lockRegistry = redisLockRegistry;
    }





    @Override
    public String lock() {
        var lock = lockRegistry.obtain(MY_LOCK_KEY);
        String returnVal = null;
        if(lock.tryLock()){
            returnVal = "Redis lock successful";
        }
        else{
            returnVal = "Redis lock unsuccessful";
        }
        lock.unlock();
        return returnVal;
    }

    @Override
    public void failLock() {
        var executor = Executors.newFixedThreadPool(2);
        Runnable lockThreadOne = () -> {
            UUID uuid = UUID.randomUUID();
            StringBuilder sb = new StringBuilder();
            var lock = lockRegistry.obtain(MY_LOCK_KEY);
            try {
                System.out.println("Attempting to lock with thread: " + uuid);
                if(lock.tryLock(1, TimeUnit.SECONDS)){
                    System.out.println("Locked with thread: " + uuid);
                    Thread.sleep(5000);
                }
                else{
                    System.out.println("failed to lock with thread: " + uuid);
                }
            } catch(Exception e0){
                System.out.println("exception thrown with thread: " + uuid);
            } finally {
                lock.unlock();
                System.out.println("unlocked with thread: " + uuid);
            }
        };

        Runnable lockThreadTwo = () -> {
            UUID uuid = UUID.randomUUID();
            StringBuilder sb = new StringBuilder();
            var lock = lockRegistry.obtain(MY_LOCK_KEY);
            try {
                System.out.println("Attempting to lock with thread: " + uuid);
                if(lock.tryLock(1, TimeUnit.SECONDS)){
                    System.out.println("Locked with thread: " + uuid);
                    Thread.sleep(5000);
                }
                else{
                    System.out.println("failed to lock with thread: " + uuid);
                }
            } catch(Exception e0){
                System.out.println("exception thrown with thread: " + uuid);
            } finally {
                lock.unlock();
                System.out.println("unlocked with thread: " + uuid);
            }
        };
        executor.submit(lockThreadOne);
        executor.submit(lockThreadTwo);
        executor.shutdown();
    }

    @Override
    public String properLock() {
        Lock lock = null;
        try {
            lock = lockRegistry.obtain(MY_LOCK_KEY);
        } catch (Exception e) {
            System.out.println(String.format("Unable to obtain lock: %s", MY_LOCK_KEY));
        }
        String returnVal = null;
        try {
            if (lock.tryLock()) {
                returnVal =  "Redis lock successful";
            }
            else{
                returnVal = "Redis lock unsuccessful";
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }

        return returnVal;
    }
}
