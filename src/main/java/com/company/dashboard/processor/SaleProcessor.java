package com.company.dashboard.processor;

import com.company.dashboard.dao.SaleDao;
import com.company.dashboard.domain.Sale;
import com.company.dashboard.publisher.SaleDashboardPublisher;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class SaleProcessor implements Runnable {
    private final SaleDao saleDao;
    private LinkedBlockingQueue<Sale> sales;
    private ThreadPoolExecutor executor;
    private AtomicInteger cache;
    ScheduledExecutorService scheduler;

    @Autowired
    public SaleProcessor(SaleDao saleDao, LinkedBlockingQueue<Sale> sales) {
        this.saleDao = saleDao;
        this.sales = sales;
        executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(8);
        cache = new AtomicInteger(0);
        scheduler = Executors.newScheduledThreadPool(1);
    }

    protected void process(Sale sale) {
        Runnable task = () -> cache.addAndGet(sale.getPrice());
        executor.execute(task);
    }

    private void updateDatabase() {
        final Runnable updater = () -> {
            Sale newSale = saleDao.incrementAndGet(new Sale(cache.get()));
            cache.set(0);
            SaleDashboardPublisher.publish(newSale);
        };

        final ScheduledFuture<?> databaseUpdaterHandle = scheduler.scheduleAtFixedRate(updater, 2, 1, TimeUnit.SECONDS);
    }

    @Override
    public void run() {
        updateDatabase();

        while (!Thread.currentThread().isInterrupted()) {
            try {
                process(sales.take());
            } catch (InterruptedException e) {
                executor.shutdown();
                scheduler.shutdown();
                Thread.currentThread().interrupt();
            }
        }

    }
}
