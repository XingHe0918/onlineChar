package com.example.loginservice;

import com.xfvape.uid.impl.CachedUidGenerator;
import com.xfvape.uid.worker.DisposableWorkerIdAssigner;
import com.xfvape.uid.worker.WorkerIdAssigner;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableCaching
@MapperScan(basePackages = {"com.xfvape.uid.worker.dao"})
public class LoginServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(LoginServiceApplication.class, args);
    }

    @Bean
    public CachedUidGenerator cachedUidGenerator(WorkerIdAssigner disposableWorkerIdAssigner) {
        CachedUidGenerator cachedUidGenerator = new CachedUidGenerator();
        cachedUidGenerator.setWorkerIdAssigner(disposableWorkerIdAssigner);
        cachedUidGenerator.setTimeBits(29);
        cachedUidGenerator.setWorkerBits(21);
        cachedUidGenerator.setSeqBits(13);
        //从2020-08-06起， 可以使用8.7年
        cachedUidGenerator.setEpochStr("2024-07-01");
        //扩容比如设置成2，  当RingBuffer不够用默认是扩大三倍，
        cachedUidGenerator.setBoostPower(2);
        //当环上的缓存的uid少于25的时候， 进行新生成填充
        cachedUidGenerator.setPaddingFactor(25);
        //开启一个线程， 定时检查填充uid。 不采用这个方式。
        //cachedUidGenerator.setScheduleInterval(60);

        //两个拒绝策略使用默认的足够
        //拒绝策略: 当环已满, 无法继续填充时, 默认无需指定, 将丢弃Put操作, 仅日志记录.
        //cachedUidGenerator.setRejectedPutBufferHandler();
        //拒绝策略: 当环已空, 无法继续获取时,默认无需指定, 将记录日志, 并抛出UidGenerateException异常.
        //cachedUidGenerator.setRejectedTakeBufferHandler();

        return cachedUidGenerator;
    }


    @Bean
    public DisposableWorkerIdAssigner disposableWorkerIdAssigner() {
        DisposableWorkerIdAssigner disposableWorkerIdAssigner = new DisposableWorkerIdAssigner();
        return disposableWorkerIdAssigner;
    }



}
