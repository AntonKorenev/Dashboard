package com.company.dashboard.producer;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.concurrent.LinkedBlockingQueue;

import static org.mockito.Mockito.mock;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:spring_config.xml")
public class SaleProducerTest {

    @Autowired
    SaleProducer saleProducer;

    LinkedBlockingQueue queueMock;

    @Before
    public void setUp() {
        queueMock = mock(LinkedBlockingQueue.class);
    }

    @Test
    public void produceNotEmptySale() {
        Assert.assertNotNull(saleProducer.produce());
        Assert.assertFalse(saleProducer.produce().getPrice() <= 0);
    }
}