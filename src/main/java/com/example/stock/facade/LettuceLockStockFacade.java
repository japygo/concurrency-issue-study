package com.example.stock.facade;

import org.springframework.stereotype.Component;

import com.example.stock.repository.RedisLockRepository;
import com.example.stock.service.LettuceLockStockService;

@Component
public class LettuceLockStockFacade {

    private final RedisLockRepository redisLockRepository;
    private final LettuceLockStockService stockService;

    public LettuceLockStockFacade(RedisLockRepository redisLockRepository, LettuceLockStockService stockService) {
        this.redisLockRepository = redisLockRepository;
        this.stockService = stockService;
    }

    public void decrease(Long id, Long quantity) throws InterruptedException {
        while (!redisLockRepository.lock(id)) {
            Thread.sleep(100);
        }

        try {
            stockService.decrease(id, quantity);
        } finally {
            redisLockRepository.unlock(id);
        }
    }

}
