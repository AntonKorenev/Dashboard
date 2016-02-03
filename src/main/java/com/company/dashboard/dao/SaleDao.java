package com.company.dashboard.dao;

import com.company.dashboard.domain.Sale;
import org.hibernate.LockMode;
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

    public Sale incrementAndGet(Sale addSale) {
        Session session = getSessionFactory().openSession();
        Transaction t = session.beginTransaction();

        Sale saleSum = (Sale) session.get(Sale.class, addSale.getId(), LockMode.PESSIMISTIC_WRITE);
        if (saleSum == null) {
            saleSum = new Sale(0);
        }

        saleSum.setPrice(saleSum.getPrice() + addSale.getPrice());
        session.saveOrUpdate(saleSum);

        t.commit();
        session.refresh(saleSum);
        session.close();

        return saleSum;
    }

    public Sale getSale(int id) {
        Session session = getSessionFactory().openSession();
        Transaction t = session.beginTransaction();
        Sale sale = (Sale) session.createCriteria(Sale.class)
                .add(Restrictions.eq("id", id))
                .uniqueResult();
        t.commit();
        session.close();
        return sale;
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
