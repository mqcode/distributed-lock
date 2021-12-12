package com.github.io.redis;

import com.github.io.dto.ProductStock;
import lombok.extern.slf4j.Slf4j;
import org.databene.contiperf.PerfTest;
import org.databene.contiperf.junit.ContiPerfRule;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class RedisDistributedServiceTest {
    @Rule
    public ContiPerfRule contiPerfRule = new ContiPerfRule();

    @Autowired
    private IRedisDistributedService iRedisDistributedService;

    private Integer cnt = 0;

    /**
     * 100个线程 执行30000次
     */
    @Test
    @PerfTest(invocations = 30000, threads = 100)
    public void reduceStock() {

        ProductStock stock = new ProductStock();
        stock.setSku("AXIOS01");
        stock.setQty(3L);
        iRedisDistributedService.reduceStock(stock);
        log.info(String.valueOf(cnt++));

    }

    @Before
    public void initStock() {
        ProductStock stock = new ProductStock();
        stock.setSku("AXIOS01");
        stock.setQty(110000L);
        iRedisDistributedService.initStock(stock);
    }
}