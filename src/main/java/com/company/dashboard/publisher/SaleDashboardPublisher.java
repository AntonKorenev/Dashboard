package com.company.dashboard.publisher;

import com.company.dashboard.domain.Sale;

public class SaleDashboardPublisher {
    public static void publish(Sale sale){
        System.out.print(sale.toString());
    }
}
