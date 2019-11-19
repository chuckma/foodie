package com.imooc.config;

import com.imooc.service.OrderService;
import com.imooc.utils.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author mcg
 * @create 2019-11-19-12:37
 */
@Component
public class OrderJob {


    @Autowired
    private OrderService orderService;

    /**
     * 使用定时任务关闭订单有些不足
     * 1. 会有时间差
     *      10：40 下单，11；00 不足一小时，12:00 又超过一小时 40
     * 2. 不支持集群
     *       解决方式是 单独一台集群用来运行所有的定时任务
     * 3. 会全表搜索数据库，非常的影响性能
     *
     *
     * 定时任务只适用于小型轻量
     *
     * 较好的解决方式是 mq
     */
//    @Scheduled(cron = "0/3 * * * * ?")
    @Scheduled(cron = "0 0 0/1 * * ?")
    public void closeOrder() {
        orderService.closeOrder();
        System.out.println("执行定时任务，当前时间为："+ DateUtil.getCurrentDateString(DateUtil.DATE_PATTERN));
    }
}
