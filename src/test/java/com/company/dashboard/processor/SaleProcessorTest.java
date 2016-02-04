package com.company.dashboard.processor;

import com.company.dashboard.dao.SaleDao;
import com.company.dashboard.domain.Sale;
import com.company.dashboard.producer.SaleProducer;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:spring_config.xml")
public class SaleProcessorTest {

    @Autowired
    SaleProcessor saleProcessor;

    @Autowired
    SaleProducer saleProducer;

    @Autowired
    SaleDao saleDao;

    @Test
    public void worksCorrectly() {
        ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(10);

        Sale beforeSale = saleDao.getSale(1);
        if (beforeSale == null) beforeSale = new Sale(0);

        for (int i = 0; i < 5; i++) {
            executor.execute(saleProducer);
        }

        Thread processorThread = new Thread(saleProcessor);
        processorThread.start();

        long duration = 15;
        try {
            TimeUnit.SECONDS.sleep(duration);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        processorThread.interrupt();

        Sale afterSale = saleDao.getSale(1);

        Assert.assertNotNull(afterSale);
        Assert.assertFalse(afterSale.getPrice() < beforeSale.getPrice());
    }

    @Test
    public void properValueInDatabaseTest() {
        //getting current value
        saleDao.deleteSale(1);
        int beforeSale = saleDao.incrementAndGet(new Sale(0)).getPrice();

        //filling sale queue
        LinkedBlockingQueue<Sale> testSaleQueue = new LinkedBlockingQueue<>();
        ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(10);
        for (int i = 0; i < 10000; i++) {
            SaleProducer producer = new SaleProducer(testSaleQueue);
            executor.execute(producer);
        }
        executor.shutdown();

        final Lock lock = new ReentrantLock();
        final Condition isFull = lock.newCondition();
        final Condition isDone = lock.newCondition();
        int cycle_count = 0;

        //getting sum from sales in queue
        int sum = 0;
        try {
            lock.lock();
            while(testSaleQueue.size() != 10000 || cycle_count == 2) {
                cycle_count++;
                isFull.await(2, TimeUnit.SECONDS);
            }
            sum = testSaleQueue.stream().mapToInt((sale) -> sale.getPrice()).sum();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }

        Thread processorThread = new Thread(new SaleProcessor(saleDao, testSaleQueue));
        processorThread.start();

        //waiting for final value in DB
        int afterSale = -1;
        cycle_count = 0;
        try {
            lock.lock();
            while (afterSale != beforeSale + sum || cycle_count == 2) {
                cycle_count++;
                afterSale = saleDao.getSale(1).getPrice();
                isDone.await(5, TimeUnit.SECONDS);
            }
            processorThread.interrupt();
            Assert.assertEquals(afterSale, beforeSale + sum);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }

        System.out.println("before=" + beforeSale);
        System.out.println("after=" + afterSale);
        System.out.println("sum=" + sum);
    }
}