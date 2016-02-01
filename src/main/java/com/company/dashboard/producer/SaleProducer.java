package com.company.dashboard.producer;

import com.company.dashboard.domain.Sale;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class SaleProducer implements Runnable{
    private LinkedBlockingQueue<Sale> sales;

    @Autowired
    SaleProducer(LinkedBlockingQueue<Sale> sales) {
        this.sales = sales;
    }

    protected Sale produce() {
        Sale sale = new Sale((int) (Math.random() * 10000) + 1);
        return sale;
    }

    @Override
    public void run() {
        try {
            Long duration = (long) (Math.random()*3);
            TimeUnit.SECONDS.sleep(duration);
            sales.add(produce());
            synchronized (sales) {
                sales.notify();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
