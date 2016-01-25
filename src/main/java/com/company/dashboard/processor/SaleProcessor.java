package com.company.dashboard.processor;

import com.company.dashboard.dao.SaleDao;
import com.company.dashboard.domain.Sale;
import com.company.dashboard.publisher.SaleDashboardPublisher;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.LinkedBlockingQueue;

public class SaleProcessor implements Runnable{
    private final SaleDao saleDao;
    private LinkedBlockingQueue<Sale> sales;

    @Autowired
    SaleProcessor(SaleDao saleDao, LinkedBlockingQueue<Sale> sales){
        this.saleDao = saleDao;
        this.sales = sales;
    }

    protected void process(Sale sale) {

    }

    @Override
    public void run() {

    }
}
