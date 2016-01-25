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
        Session session = getSessionFactory().openSession();
        Transaction t = session.beginTransaction();
        session.saveOrUpdate(saving);
        t.commit();
        session.close();
    }

    public Sale getSale(int id) {
        return (Sale) getSessionFactory().openSession()
                .createCriteria(Sale.class)
                .add(Restrictions.eq("id", id))
                .uniqueResult();
    }

    public void deleteSale(int id) {
        Sale deleting = getSale(id);
        Session session = sessionFactory.openSession();
        Transaction t = session.beginTransaction();
        session.delete(deleting);
        t.commit();
        session.close();
    }
}
