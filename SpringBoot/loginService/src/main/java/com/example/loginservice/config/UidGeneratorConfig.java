package com.example.loginservice.config;


import com.xfvape.uid.impl.CachedUidGenerator;
import com.xfvape.uid.worker.DisposableWorkerIdAssigner;
import com.xfvape.uid.worker.WorkerIdAssigner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

@Configuration
public class UidGeneratorConfig {
    @Autowired
    CachedUidGenerator cachedUidGenerator;

    public Long nextId() {
        return cachedUidGenerator.getUID();
    }


}
