package com.test;

import com.imooc.Application;
import com.imooc.pojo.TestOrder;
import com.imooc.service.StuService;
import com.imooc.service.TestOrderService;
import com.imooc.service.TestTransService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author mcg
 * @create 2019-11-15-9:06
 */

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class TransTest {

    @Autowired
    private StuService stuService;

    @Autowired
    private TestTransService testTransService;

    @Autowired
    private TestOrderService testOrderService;

//    @Test
    public void myTest() {
//        stuService.testPropagationTrans();
        testTransService.testPropagationTrans();
    }

    @Test
    public void test1() {
        List<TestOrder> testOrders = testOrderService.queryAll();
        System.out.println(testOrders);
    }

    @Test
    public void insert() {
        TestOrder order = new TestOrder();
//        order.setCost(new BigDecimal(243));
        order.setDescription("ces ");
        testOrderService.insertOrder(order);
    }

}
