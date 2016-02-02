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
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

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
        if(beforeSale == null) beforeSale = new Sale(0);

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
}