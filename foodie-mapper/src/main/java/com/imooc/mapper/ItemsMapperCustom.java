package com.imooc.mapper;

import com.imooc.pojo.vo.ItemCommentVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface ItemsMapperCustom  {

    public  List<ItemCommentVO> queryItemComments(@Param("paramsMap") Map<String, Object> map);
}