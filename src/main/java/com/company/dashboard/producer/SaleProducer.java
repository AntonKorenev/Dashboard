package com.company.dashboard.producer;

import com.company.dashboard.domain.Sale;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.LinkedBlockingQueue;

public class SaleProducer implements Runnable {
    private LinkedBlockingQueue<Sale> sales;

    @Autowired
    public SaleProducer(LinkedBlockingQueue<Sale> sales) {
        this.sales = sales;
    }

    protected Sale produce() {
        Sale sale = new Sale((int) (Math.random() * 100) + 1);
        return sale;
    }

    @Override
    public void run() {
        sales.add(produce());
    }
}
