package com.company.dashboard.dao;

import com.company.dashboard.domain.Sale;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:spring_config.xml")
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class SaleDaoIntegrationTest {
    private static final int id = 1;

    @Autowired
    SaleDao saleDao;

    @Autowired
    ApplicationContext applicationContext;

    @Test
    public void bDaoIncrementAndReturnSale() {
        Sale testSale = new Sale(9000);
        Sale fromDb = saleDao.incrementAndGet(testSale);
        Assert.assertNotNull(fromDb);
        Assert.assertFalse(fromDb.getPrice() - testSale.getPrice() < 0);
        Assert.assertEquals(saleDao.getSale(id), fromDb);
    }

    @Test
    public void dDaoDeleteSale() {
        saleDao.deleteSale(id);
        Assert.assertNull(saleDao.getSale(id));
    }
}
