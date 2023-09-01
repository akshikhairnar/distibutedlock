package com.poc.distributedlock.contoller;

import com.poc.distributedlock.service.LockService;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class RadisController {

    private final LockService lockService;

    public RadisController(LockService lockService) {
        this.lockService = lockService;
    }

    @PutMapping("lock")
    public String lock(){
        return lockService.lock();
    }

    @PutMapping("properLock")
    public String properLock(){
        return lockService.properLock();
    }

    @PutMapping("failLock")
    public String failLock(){
        lockService.failLock();
        return "fail lock called, output in logs";
    }

}
