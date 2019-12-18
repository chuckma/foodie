package com.imooc.service.impl;

import com.imooc.mapper.TestOrderMapper;
import com.imooc.pojo.TestOrder;
import com.imooc.service.TestOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author mcg
 * @Date 2019/12/18 20:42
 **/
@Service
public class TestOrderServiceImpl implements TestOrderService {

    @Autowired
    private TestOrderMapper orderMapper;

    @Override
    public TestOrder selectOneOrder(Integer id) {
        TestOrder order = new TestOrder();
        order.setId(id);
        TestOrder result = orderMapper.selectOne(order);
        return result;
    }

    @Override
    public List<TestOrder> queryAll() {
        List<TestOrder> testOrders = orderMapper.selectAll();
        return testOrders;
    }

    @Override
    public void insertOrder(TestOrder order) {
        orderMapper.insert(order);
    }
}
