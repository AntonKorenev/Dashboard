package com.company.dashboard.dao;

import com.company.dashboard.domain.Sale;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

public class SaleDao {
    private SessionFactory sessionFactory;

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public void saveOrUpdateSale(Sale saving) {

    }

    public Sale getSale(int id) {
        return null;
    }

    public void deleteSale(int id) {

    }
}
