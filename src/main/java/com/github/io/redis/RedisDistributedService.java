package com.github.io.redis;

import com.github.io.dto.ProductStock;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class RedisDistributedService implements IRedisDistributedService {
    @Resource
    private RedissonClient redissonClient;

    @Autowired
    private JedisPool jedisPool;

    @Override
    public void initStock(ProductStock request) {
        String skuStockCacheKey = "sku_stock" + request.getSku();
        Jedis jedis = jedisPool.getResource();
        jedis.set(skuStockCacheKey, String.valueOf(request.getQty()));
        jedis.close();
        log.info("缓存设置成功,sku[{}]数量[{}]", request.getSku(), request.getQty());
    }

    @Override
    public String reduceStock(ProductStock request) {
        log.info("========================================================");
        String key = "RedisDistributedService_reducePrice" + request.getSku();
        String skuStockCacheKey = "sku_stock" + request.getSku();
        RLock distributedLock = redissonClient.getLock(key);

        Jedis jedis = jedisPool.getResource();
        String val = jedis.get(skuStockCacheKey);
        jedis.close();

        Long qty = Long.valueOf(val);
        ProductStock stock = new ProductStock();
        stock.setSku(request.getSku());
        stock.setQty(qty);

        try {
            Boolean isLock = distributedLock.tryLock(5, TimeUnit.SECONDS);
            if (isLock) {
                log.info("扣减前-当前库存：" + stock.getQty());
                //Thread.sleep(1000);
                if (stock.getQty() == 0L || stock.getQty() - request.getQty() < 0) {
                    String message = "sku[" + request.getSku() + "]的库存扣除失败,无库存";
                    log.info(message);
                    return message;
                }
                Jedis jedis1 = jedisPool.getResource();
                jedis1.set(skuStockCacheKey, String.valueOf(stock.getQty() - request.getQty()));
                jedis1.close();
            }
        } catch (Exception exception) {
            log.info(exception.getMessage());
        } finally {
            distributedLock.unlock();
        }
        String result = "sku[" + request.getSku() + "]的库存扣除成功.当前库存：" + stock.getQty();
        log.info(result);
        return result;
    }
}
