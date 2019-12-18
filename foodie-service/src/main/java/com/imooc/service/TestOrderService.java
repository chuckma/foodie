package com.imooc.service;

import com.imooc.pojo.TestOrder;

import java.util.List;

/**
 * @Author mcg
 * @Date 2019/12/18 20:40
 **/
public interface TestOrderService {

    public TestOrder selectOneOrder(Integer id);
    public List<TestOrder> queryAll();

    public void insertOrder(TestOrder order);
}
