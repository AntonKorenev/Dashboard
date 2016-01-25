package com.company.dashboard.processor;

import com.company.dashboard.dao.SaleDao;
import com.company.dashboard.domain.Sale;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:spring_config.xml")
public class SaleProcessorTest {

    @Autowired
    SaleProcessor saleProcessor;

    SaleDao saleDaoMock;

    @Before
    public void setUp() {
        saleDaoMock = mock(SaleDao.class);
    }

    @Test
    public void daoReceivesSale() {
        Sale testSale = new Sale(1);

        saleProcessor.process(testSale);

        verify(saleDaoMock).getSale(testSale.getId());
        verify(saleDaoMock).saveOrUpdateSale(testSale);
    }
}