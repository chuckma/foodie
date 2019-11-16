package com.imooc.enums;

/**
 * @author mcg
 * @create 2019-11-15-11:59
 *
 *  @Desc: 商品评价等级，枚举
 */
public enum CommentLevel {
    GOOD(1, "好评"),
    NORMAL(2, "中评"),
    BAD(3, "差评");

    public final Integer type;
    public final String value;

    CommentLevel(Integer type, String value) {
        this.type = type;
        this.value = value;
    }
}
