package com.company.dashboard;

import com.company.dashboard.processor.SaleProcessor;
import com.company.dashboard.producer.SaleProducer;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class Main {

    public static void main(String [] args) {
        AbstractApplicationContext context =
                new ClassPathXmlApplicationContext("spring_config.xml");

        ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(10);
        for (int i = 0; i < 20; i++) {
            SaleProducer producer = (SaleProducer) context.getBean("saleProducer");
            executor.execute(producer);
        }


        SaleProcessor saleProcessor = (SaleProcessor) context.getBean("saleProcessor");
        Thread processorThread = new Thread(saleProcessor);
        processorThread.start();

        long d = 20;
        try {
            TimeUnit.SECONDS.sleep(d);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < 20; i++) {
            SaleProducer producer = (SaleProducer) context.getBean("saleProducer");
            executor.execute(producer);
        }
        executor.shutdown();

        long duration = 30;
        try {
            TimeUnit.SECONDS.sleep(duration);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        processorThread.interrupt();
        System.out.println("Processor thread was interrupted");
    }
}
