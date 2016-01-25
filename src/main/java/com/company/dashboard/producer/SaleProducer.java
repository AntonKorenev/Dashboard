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
        return null;
    }

    @Override
    public void run() {

    }
}
