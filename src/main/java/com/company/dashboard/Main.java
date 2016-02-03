package com.company.dashboard;

import com.company.dashboard.processor.SaleProcessor;
import com.company.dashboard.producer.SaleProducer;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.concurrent.*;

public class Main {

    public static void main(String[] args) {
        AbstractApplicationContext context =
                new ClassPathXmlApplicationContext("spring_config.xml");

        ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(10);

        for (int i = 0; i < 10000; i++) {
            SaleProducer producer = (SaleProducer) context.getBean("saleProducer");
            executor.execute(producer);
        }
        executor.shutdown();

        ScheduledExecutorService scheduler =
                Executors.newScheduledThreadPool(2);

        SaleProcessor saleProcessor = (SaleProcessor) context.getBean("saleProcessor");
        final ScheduledFuture<?> saleProcessorHandler = scheduler.schedule(saleProcessor, 0, TimeUnit.SECONDS);

        scheduler.schedule((Runnable) () -> {
            saleProcessorHandler.cancel(true);
            System.out.println("Processor thread was interrupted");
            scheduler.shutdown();
        }, 10, TimeUnit.SECONDS);
    }
}
