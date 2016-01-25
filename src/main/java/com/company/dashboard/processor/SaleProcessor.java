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
        Sale saleFromDb = saleDao.getSale(sale.getId());
        if(saleFromDb != null) sale.setPrice(sale.getPrice() + saleFromDb.getPrice());
        saleDao.saveOrUpdateSale(sale);
        System.out.println(sales.size() + " objects for processing remained");
        SaleDashboardPublisher.publish(sale);
    }

    @Override
    public void run() {
        while(!Thread.currentThread().isInterrupted()){
            if(sales.peek() != null) {
                process(sales.poll());
            }
        }
    }
}
