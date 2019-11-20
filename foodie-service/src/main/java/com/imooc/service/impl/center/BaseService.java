package com.imooc.service.impl.center;

import com.github.pagehelper.PageInfo;
import com.imooc.utils.PagedGridResult;

import java.util.List;

/**
 * @Author mcg
 * @Date 2019/11/20 20:33
 **/

public class BaseService {


    public PagedGridResult setterPageGrid(List<?> list, Integer page) {
        PageInfo<?> pageList = new PageInfo<>(list);
        PagedGridResult grid = new PagedGridResult();
        grid.setPage(page);
        grid.setRows(list);
        grid.setTotal(pageList.getPages()); // 总页数
        grid.setRecords(pageList.getTotal()); // 总记录
        return grid;
    }
}
