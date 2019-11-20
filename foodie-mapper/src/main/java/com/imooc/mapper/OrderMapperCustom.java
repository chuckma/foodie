package com.imooc.mapper;

import com.imooc.pojo.vo.MyOrdersVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @author mcg
 * @create 2019-11-20-13:43
 */
public interface OrderMapperCustom {

    /**
     *
     * @param map
     * @return
     */
    public List<MyOrdersVO> queryMyOrders(@Param("paramsMap") Map<String, Object> map);
}
