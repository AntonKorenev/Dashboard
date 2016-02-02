package com.company.dashboard.processor;

import com.company.dashboard.dao.SaleDao;
import com.company.dashboard.domain.Sale;
import com.company.dashboard.publisher.SaleDashboardPublisher;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class SaleProcessor implements Runnable{
    private final SaleDao saleDao;
    private LinkedBlockingQueue<Sale> sales;
    private ThreadPoolExecutor executor;
    private AtomicInteger cache;

    @Autowired
    SaleProcessor(SaleDao saleDao, LinkedBlockingQueue<Sale> sales){
        this.saleDao = saleDao;
        this.sales = sales;
        executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(8);
        cache = new AtomicInteger(0);
    }

    protected void process(Sale sale) {
        Runnable task = () -> cache.addAndGet(sale.getPrice());
        executor.execute(task);
    }

    @Override
    public void run() {
        long duration = 5L;

        Thread databaseThread = new Thread(() -> {
            while(!Thread.currentThread().isInterrupted()) {
                try {
                    TimeUnit.SECONDS.sleep(duration);
                    Sale newSaleSum = saleDao.incrementAndGet(new Sale(cache.get()));
                    SaleDashboardPublisher.publish(newSaleSum);
                    cache.set(0);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        });
        databaseThread.start();

        while(!Thread.currentThread().isInterrupted()){
            try {
                process(sales.take());
            } catch (InterruptedException e) {
                executor.shutdown();
                databaseThread.interrupt();
                Thread.currentThread().interrupt();
            }
        }
    }
}
